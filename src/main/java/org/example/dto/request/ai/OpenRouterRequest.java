package org.example.dto.request.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OpenRouterRequest {
    
    @JsonProperty("model")
    private String model;
    
    @JsonProperty("messages")
    private List<Message> messages;
    
    @JsonProperty("temperature")
    private Double temperature;
    
    public OpenRouterRequest() {
    }
    
    public OpenRouterRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
        this.temperature = 0.7;
    }
    
    public OpenRouterRequest(String model, List<Message> messages, Double temperature) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    
    public Double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    
    public static class Message {
        @JsonProperty("role")
        private String role;
        
        @JsonProperty("content")
        private String content;
        
        public Message() {
        }
        
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
        
        public String getContent() {
            return content;
        }
        
        public void setContent(String content) {
            this.content = content;
        }
    }
}
