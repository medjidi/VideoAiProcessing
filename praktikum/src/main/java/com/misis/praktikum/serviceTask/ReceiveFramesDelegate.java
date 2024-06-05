package com.misis.praktikum.serviceTask;

import com.misis.praktikum.listener.FrameResponseService;
import com.misis.praktikum.messages.FrameResponse;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ReceiveFramesDelegate implements JavaDelegate {

    @Autowired
    private FrameResponseService frameResponseService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String processInstanceId = execution.getProcessInstanceId();
        Map<Integer, FrameResponse> responses = frameResponseService.getResponsesForProcess(processInstanceId);

        // Формируем таблицу объектов
        StringBuilder table = new StringBuilder("Frame Number | Objects Found\n");
        table.append("--------------------------\n");

        for (Map.Entry<Integer, FrameResponse> entry : responses.entrySet()) {
            int frameNumber = entry.getKey();
            List<String> objects = entry.getValue().getObjects();
            String objectsStr = String.join(", ", objects);

            table.append(frameNumber).append(" | ").append(objectsStr).append("\n");
        }

        // Сохраняем таблицу как переменную процесса
        execution.setVariable("objectTable", table.toString());
    }
}
