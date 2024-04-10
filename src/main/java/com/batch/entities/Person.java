package com.batch.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data //incluye get, set, toString, consstructor vacio... etc.. verlo en structure
@FieldDefaults(level = AccessLevel.PRIVATE) // coloca todos PRIVATE, otro atributo seria el makeFinal = true haría los atributos FINAL, y no deberian tener constructor vacio
@Entity
@Table(name = "persons") //buena práctica, tabla en plural y nombre entity en singular
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @Column(name = "last_name")
    String lastName;
    int age;

    @Column(name = "insertion_date")
    String insertionDate;
}
