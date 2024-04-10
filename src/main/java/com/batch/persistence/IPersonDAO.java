package com.batch.persistence;

import com.batch.entities.Person;
import org.springframework.data.repository.CrudRepository;

public interface IPersonDAO extends CrudRepository<Person, Long> { //el Crudrepository ya lo interpreta, no es necesario ponerle anotación

}
