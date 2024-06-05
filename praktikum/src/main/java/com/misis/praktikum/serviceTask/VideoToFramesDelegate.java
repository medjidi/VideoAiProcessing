package com.misis.praktikum.serviceTask;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.bytedeco.opencv.global.opencv_videoio.CAP_FFMPEG;

@Component
public class VideoToFramesDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        byte[] videoBytes = (byte[]) execution.getVariable("video");
        InputStream videoInputStream = new ByteArrayInputStream(videoBytes);
        String processInstanceId = execution.getProcessInstanceId();
        String tempVideoPath = "temp_video_" + processInstanceId + ".mp4";
        Files.copy(videoInputStream, Path.of(tempVideoPath), StandardCopyOption.REPLACE_EXISTING);

        String outputDir = "C:\\videoS\\" + processInstanceId + "\\";
        new File(outputDir).mkdirs();

        VideoCapture videoCapture = new VideoCapture(tempVideoPath);
        if (!videoCapture.isOpened()) {
            throw new RuntimeException("Could not open video: " + tempVideoPath);
        }

        Mat frame = new Mat();
        int frameNumber = 0;
        while (videoCapture.read(frame)) {
            String frameFilePath = outputDir + "frame_" + frameNumber + ".png";
            opencv_imgcodecs.imwrite(frameFilePath, frame);
            frameNumber++;
        }
        videoCapture.release();
        Files.delete(Path.of(tempVideoPath));
        execution.setVariable("totalFrames", frameNumber);
        execution.setVariable("outputDir", outputDir);  // Сохраняем путь для дальнейшего использования
    }
}
