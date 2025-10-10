package io.arconia.demo;

import org.springframework.data.mongodb.repository.MongoRepository;

interface BookRepository extends MongoRepository<Book, String> {
}
