package com.Kata.KataPersonaFluxMongo.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Person {

   private String name;
   private boolean opcion;

    public Person(String name, boolean opcion) {
        this.name=name;
        this.opcion=opcion;
    }

    public Person() {

    }



    public void setName(String name) {
        this.name = name;
    }

    public void setOpcion(Boolean opcion) {
        this.opcion = opcion;
    }


    public String getName() {
        return name;
    }

    public boolean getOpcion() {
        return opcion;
    }
}
