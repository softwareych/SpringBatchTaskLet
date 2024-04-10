package com.batch.steps;

import com.batch.entities.Person;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ItemTwoReaderStep implements Tasklet {
    @Autowired
    private ResourceLoader resourceLoader; //importa archivos desde la carpeta resources, de nuetsro proyecto

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("----> Inicio del paso de LECTURA <----");

        Reader reader = new FileReader(resourceLoader.getResource("classpath:files/destination/persons.csv").getFile()); //classpath es la ruta resouces en este caso

        //Define el parser con el separador del doc
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')//comillas simple pq sino lo toma como caracter, separa por coma cada campo, segun el .csv
                .build();

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(parser)
                .withSkipLines(1) //cuantas lineas debe saltarse, el titulo de la 1ra linea no lo leas, salta a la 2da
                .build();

        List<Person> personList = new ArrayList<>();
        String[] actualLine;

        while ((actualLine = csvReader.readNext()) != null){
            Person person = new Person();
            person.setName(actualLine[0]);
            person.setLastName(actualLine[1]);
            person.setAge(Integer.parseInt(actualLine[2]));

            personList.add(person);
        }

        csvReader.close();
        reader.close();

        log.info("----> Fin del paso de LECTURA <----");

        //envío la list al proximo paso a través del contexto
        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("personList", personList); //funciona como un map

        return RepeatStatus.FINISHED; //indica que termino. el continued volveria a ejecutarlo
    }
}
