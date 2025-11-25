package org.example.service;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.stereotype.Service;

@Service
public class ImageAIService {

    private final ImageModel imageModel;

    public ImageAIService(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public String generateImage(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be empty");
        }
        
        String enhancedPrompt = prompt + ", background light, professional style, high quality";

        try {
            return callDallE3(enhancedPrompt);
        } catch (Exception e) {
            return callDallE2(enhancedPrompt);
        }
    }

    private String callDallE3(String prompt) {
        ImagePrompt request = new ImagePrompt(prompt,
                ImageOptionsBuilder.builder()
                        .withModel("dall-e-3")
                        .withHeight(1024)
                        .withWidth(1024)
                        .build());
        return imageModel.call(request).getResult().getOutput().getUrl();
    }

    private String callDallE2(String prompt) {
        ImagePrompt request = new ImagePrompt(prompt,
                ImageOptionsBuilder.builder()
                        .withModel("dall-e-2")
                        .withHeight(512)
                        .withWidth(512)
                        .build());
        return imageModel.call(request).getResult().getOutput().getUrl();
    }
}
