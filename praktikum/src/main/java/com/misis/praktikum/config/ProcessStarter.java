package com.misis.praktikum.config;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProcessStarter implements CommandLineRunner {

    private final RuntimeService runtimeService;

    @Autowired
    public ProcessStarter(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Override
    public void run(String... args) throws Exception {
        runtimeService.startProcessInstanceByKey("videoProcessing");
        System.out.println("Process started");
    }

}
