package com.misis.praktikum.serviceTask;

import com.misis.praktikum.listener.FrameResponseService;
import com.misis.praktikum.messages.FrameMessage;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class SendFramesDelegate implements JavaDelegate {

    @Autowired
    @Lazy
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FrameResponseService frameResponseService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String processInstanceId = execution.getProcessInstanceId();
        String outputDir = (String) execution.getVariable("outputDir");
        int totalFrames = (int) execution.getVariable("totalFrames");

        frameResponseService.setTotalFrames(processInstanceId, totalFrames); // Сначала установим количество кадров

        for (int frameNumber = 0; frameNumber < totalFrames; frameNumber++) {
            String frameFilePath = outputDir + "frame_" + frameNumber + ".png";

            FrameMessage message = new FrameMessage(processInstanceId, frameNumber, frameFilePath);
            rabbitTemplate.convertAndSend("video.kadr.in", message);
        }
    }
}
