package com.misis.praktikum.config;

import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultProcessEngineConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomCamundaConfig extends DefaultProcessEngineConfiguration {

    @Override
    public void preInit(SpringProcessEngineConfiguration configuration) {
        configuration.setHistory("full");
    }
}
