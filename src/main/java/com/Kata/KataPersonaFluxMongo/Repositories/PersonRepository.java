package com.Kata.KataPersonaFluxMongo.Repositories;

import com.Kata.KataPersonaFluxMongo.Collection.Person;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface PersonRepository extends ReactiveMongoRepository<Person,String> {
     Mono<Person> findByName(String name);
}
