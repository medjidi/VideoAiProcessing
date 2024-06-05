package com.misis.praktikum.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FrameResponse {
    private String processInstanceId;
    private int frameNumber;
    private List<String> objects;
}
