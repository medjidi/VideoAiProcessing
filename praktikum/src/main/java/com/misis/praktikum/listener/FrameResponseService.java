package com.misis.praktikum.listener;

import com.misis.praktikum.messages.FrameResponse;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class FrameResponseService {

    private final Map<String, Map<Integer, FrameResponse>> responses = new ConcurrentHashMap<>();
    private final Map<String, Integer> frameCounts = new ConcurrentHashMap<>();

    @Autowired
    private RuntimeService runtimeService;

    @RabbitListener(queues = "video.kadr.out")
    public void receiveFrameResponse(FrameResponse response) {
        String processInstanceId = response.getProcessInstanceId();
        int frameNumber = response.getFrameNumber();
        log.info("Received frame response for process instance {}, frame number {}", processInstanceId, frameNumber);

        responses.computeIfAbsent(processInstanceId, k -> new ConcurrentHashMap<>()).put(frameNumber, response);

        // Проверяем, все ли кадры обработаны
        int totalFrames = frameCounts.getOrDefault(processInstanceId, -1);
        log.info("Total frame count for process instance {} is {}", processInstanceId, totalFrames);
        if (totalFrames != -1 && responses.get(processInstanceId).size() == totalFrames) {
            log.info("All frames processed for process instance {}, sending FramesProcessedMessage", processInstanceId);
            // Все кадры обработаны, отправляем сообщение
            runtimeService.createMessageCorrelation("FramesProcessedMessage")
                    .processInstanceId(processInstanceId)
                    .correlate();
        }
    }

    public void setTotalFrames(String processInstanceId, int totalFrames) {
        log.info("Setting total frame count for process instance {} to {}", processInstanceId, totalFrames);
        frameCounts.put(processInstanceId, totalFrames);
    }

    public Map<Integer, FrameResponse> getResponsesForProcess(String processInstanceId) {
        return responses.getOrDefault(processInstanceId, new ConcurrentHashMap<>());
    }
}
