package com.ashwanth.ApKeyManager.DTO;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class APIDto {
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("api_key")
    private String apiKey;
    
    @JsonProperty("key_name")
    private String keyName;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}