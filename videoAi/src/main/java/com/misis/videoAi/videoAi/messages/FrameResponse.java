package com.misis.videoAi.videoAi.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrameResponse {
    private String processInstanceId;
    private int frameNumber;
    private List<String> objects;
}
