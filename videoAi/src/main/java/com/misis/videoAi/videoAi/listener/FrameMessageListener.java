package com.misis.videoAi.videoAi.listener;

import com.misis.videoAi.videoAi.config.RabbitMQConfig;
import com.misis.videoAi.videoAi.messages.FrameMessage;
import com.misis.videoAi.videoAi.messages.FrameResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FrameMessageListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.IN_QUEUE)
    public void receiveMessage(FrameMessage frameMessage) {
        String framePath = frameMessage.getFramePath();
        List<String> detectedObjects = detectObjectsInFrame(framePath);

        FrameResponse response = new FrameResponse(
                frameMessage.getProcessInstanceId(),
                frameMessage.getFrameNumber(),
                detectedObjects
        );

        rabbitTemplate.convertAndSend(RabbitMQConfig.OUT_QUEUE, response);
    }

    private List<String> detectObjectsInFrame(String framePath) {
        List<String> detectedObjects = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource("scripts/yolo_detection.py");
            Path scriptPath = Paths.get(resource.getURI());
            ProcessBuilder pb = new ProcessBuilder("py -3 ", scriptPath.toString(), framePath);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                detectedObjects.add(line);
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detectedObjects;
    }
}
