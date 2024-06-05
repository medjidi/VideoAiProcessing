package com.misis.praktikum.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FrameMessage {
    private String processInstanceId;
    private int frameNumber;
    private String framePath;
}
