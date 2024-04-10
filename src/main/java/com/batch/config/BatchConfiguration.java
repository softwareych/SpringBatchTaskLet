package com.batch.config;

import com.batch.steps.ItemForWriterStep;
import com.batch.steps.ItemOneDescompressStep;
import com.batch.steps.ItemThreeProcessorStep;
import com.batch.steps.ItemTwoReaderStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory; //deprecado JobBuilderFactory para spring +3.0.0

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    @JobScope //el objeto se crea cuando se usse spring batch sino no se crea el objeto
    public ItemOneDescompressStep itemOneDescompressStep() {
        return new ItemOneDescompressStep();
    }

    @Bean
    @JobScope
    public ItemTwoReaderStep itemTwoReaderStep(){
        return new ItemTwoReaderStep();
    }

    @Bean
    @JobScope
    public ItemThreeProcessorStep itemThreeProcessorStep() {
        return new ItemThreeProcessorStep();
    }

    @Bean
    @JobScope
    public ItemForWriterStep itemForWriterStep() {
        return new ItemForWriterStep();
    }

    @Bean
    public Step descompressFilestep() {
        return  stepBuilderFactory.get("itemOneDescompressStep")
                .tasklet(itemOneDescompressStep())
                .build();
    }

    @Bean
    public Step readFileStep(){
        return  stepBuilderFactory.get("itemTwoReaderStep")
                .tasklet(itemTwoReaderStep())
                .build();
    }

    @Bean
    public  Step processDataStep() {
        return stepBuilderFactory.get("itemProcessStep")
                .tasklet(itemThreeProcessorStep())
                .build();
    }

    @Bean
    public Step writerDataStep(){
        return  stepBuilderFactory.get("itemWriterStep")
                .tasklet(itemForWriterStep())
                .build();
    }

    @Bean
    public Job readCSVJob() {
        return  jobBuilderFactory.get("readCSVJob")
                .start(descompressFilestep())
                .next(readFileStep())
                .next(processDataStep())
                .next(writerDataStep())
                .build();
    }
}
