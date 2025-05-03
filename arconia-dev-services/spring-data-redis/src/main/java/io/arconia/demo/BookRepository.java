package io.arconia.demo;

import org.springframework.data.repository.CrudRepository;

interface BookRepository extends CrudRepository<Book, String> {
}
