package com.misis.praktikum.config;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaConfig {

    @Bean
    public Deployment deployProcess(RepositoryService repositoryService) {
        return repositoryService.createDeployment()
                .addClasspathResource("processes/videoProcessing.bpmn")
                .name("Video Processing Deployment")
                .deploy();
    }

}