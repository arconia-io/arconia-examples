package io.arconia.demo;

import io.arconia.multitenancy.core.context.TenantContext;
import io.arconia.multitenancy.core.tenantdetails.Tenant;
import io.arconia.multitenancy.core.tenantdetails.TenantDetails;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;

@SpringBootApplication
public class TenantDetailsJdbcApplication {

	static void main(String[] args) {
		SpringApplication.run(TenantDetailsJdbcApplication.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> routerFunctions() {
		return RouterFunctions.route()
				.GET("/tenant", _ -> ServerResponse.ok()
						.body(TenantContext.getRequiredTenantIdentifier()))
				.build();
	}

}

@Component
class TenantDataLoader implements ApplicationRunner {

    private static final List<TenantDetails> TENANTS = List.of(
            Tenant.builder().identifier("dukes").build(),
            Tenant.builder().identifier("beans").build(),
            Tenant.builder().identifier("pixie").enabled(false).addAttribute("status", "onboarding").build());

    private static final String UPSERT_TENANT = """
            insert into tenant_details (identifier, enabled)
            values (:identifier, :enabled)
            on conflict (identifier) do update
                set enabled = excluded.enabled
            returning id
            """;

    private static final String UPSERT_ATTRIBUTE = """
            insert into tenant_details_attributes (tenant_id, attribute_name, attribute_value)
            values (:tenantId, :name, :value)
            on conflict (tenant_id, attribute_name) do update
                set attribute_value = excluded.attribute_value
            """;

    private final JdbcClient jdbcClient;

    TenantDataLoader(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        TENANTS.forEach(this::upsert);
    }

    private void upsert(TenantDetails tenant) {
        Integer tenantId = jdbcClient.sql(UPSERT_TENANT)
                .param("identifier", tenant.identifier())
                .param("enabled", tenant.enabled())
                .query(Integer.class)
                .single();

        tenant.attributes().forEach((name, value) ->
                jdbcClient.sql(UPSERT_ATTRIBUTE)
                        .param("tenantId", tenantId)
                        .param("name", name)
                        .param("value", value)
                        .update());
    }

}
