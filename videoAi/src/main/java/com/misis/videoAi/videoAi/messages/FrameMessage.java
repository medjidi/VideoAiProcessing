package com.misis.videoAi.videoAi.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrameMessage {
    private String processInstanceId;
    private int frameNumber;
    private String framePath;
}
