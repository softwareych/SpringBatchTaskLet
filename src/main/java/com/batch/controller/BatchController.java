package com.batch.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1")
public class BatchController {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;
    @PostMapping("/uploadFile")
    public ResponseEntity<?> receiveFile(@RequestParam(name = "file") MultipartFile multipartFile){

        String fileName = multipartFile.getOriginalFilename(); //obtenemos el nombre del archivo

        try {
            //URL donde esta el archivo
            Path path = Paths.get("src" + File.separator + "main" + File.separator + "resources" + File.separator + "files" + File.separator + fileName);

            Files.createDirectories(path.getParent()); //si el directorio no existe lo crea
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING); //copiamos el archivo que llega en una secuencia de bytes a la ruta que queremos, lo reemplaza si existe

            log.info("----> Inicio del proceso batch <----");
            JobParameters jobParameter = new JobParametersBuilder() //se envian parametros iniciales al job que se ejecutara
                    .addDate("fecha", new Date()) //para diferenciar un proceso de otro (mas si el job se ejecuta al mismo tiempo ponemos la fecha
                    .addString("fileName", fileName) //estos objetos o parametros se envian al context del proceso batch
                    .toJobParameters();

            jobLauncher.run(job, jobParameter); //es el joblauncher quien se encarga de ejecutar el job

            //se arma la respuesta que se enviara con el nombre del archivo y el estado
            Map<String, String> response = new HashMap<>();
            response.put("archivo", fileName);
            response.put("estado", "recibido");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al iniciar el proceso batch, Error {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
