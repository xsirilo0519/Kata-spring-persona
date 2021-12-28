package com.Kata.KataPersonaFluxMongo.Controller;

import com.Kata.KataPersonaFluxMongo.Collection.Person;
import com.Kata.KataPersonaFluxMongo.Repositories.PersonRepository;
import com.Kata.KataPersonaFluxMongo.Services.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = PersonController.class)
public class PersonControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @SpyBean
    private PersonService personService;

    @MockBean
    private PersonRepository repository;

    //captura los datos
    @Captor
    private ArgumentCaptor<Person> argumentCaptor;

    @ParameterizedTest
    @CsvSource({"Raul Alzate,0", "Raul Alzate,1"})
    void post(String name,Integer times){
        if(times == 0) {
            when(repository.findByName(name)).thenReturn(Mono.just(new Person()));
        }

        if(times == 1) {
            when(repository.findByName(name)).thenReturn(Mono.empty());
        }

        var request = Mono.just(new Person(name,true));

         when(repository.save(any())).thenReturn(Mono.empty());
//        when(repository.findByName(name)).thenReturn(Mono.just(new Person()));



        webTestClient.post()
                .uri("/person")
                .body(request, Person.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        verify(personService).insert(argumentCaptor.capture());
        verify(repository).findByName(name);
        verify(repository, Mockito.times(times)).save(any());

        var person = argumentCaptor.getValue();
        Assertions.assertEquals(name, person.getName());

    }

    @Test
    void get(){
        webTestClient.get()
                .uri("/person/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .consumeWith(personEntityExchangeResult -> {
                    var person = personEntityExchangeResult.getResponseBody();
                    assert person != null;
                });
    }

    @Test
    void update(){
        var request = Mono.just(new Person());
        webTestClient.put()
                .uri("/person")
                .body(request, Person.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }

    @Test
    void delete(){
        webTestClient.delete()
                .uri("/person/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }

    @Test
    void list() {
        var list = Flux.just(
                new Person("Raul Alzate",true),
                new Person("Pedro",false )
        );
        when(repository.findAll()).thenReturn(list);

        webTestClient.get()
                .uri("/person")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("Raul Alzate")
                .jsonPath("$[1].name").isEqualTo("Pedro");

        verify(personService).listAll();
        verify(repository).findAll();
    }


//    @Test
//    void list(){
//        webTestClient.get()
//                .uri("/person")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$[0].name").isEqualTo("Raul Alzate")
//                .jsonPath("$[1].name").isEqualTo("Pedro");
//    }

}
