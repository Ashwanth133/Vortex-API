package com.ashwanth.ApKeyManager.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
@Entity
@Table(name = "api_key")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class API {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_key", nullable = false)
    private String apiKey;

    @Column(name = "key_name")
    private String keyName;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "active")
    private boolean active;


    @Column(name = "fact_request_count")
    private int factRequestCount = 0;

    @Column(name = "qr_request_count")
    private int qrRequestCount = 0;

    @Column(name = "last_minute_reset")
    private LocalDateTime lastMinuteReset;

// getters and setters for all three

    public int getFactRequestCount() {
        return factRequestCount;
    }

    public void setFactRequestCount(int factRequestCount) {
        this.factRequestCount = factRequestCount;
    }

    public int getQrRequestCount() {
        return qrRequestCount;
    }

    public void setQrRequestCount(int qrRequestCount) {
        this.qrRequestCount = qrRequestCount;
    }

    public LocalDateTime getLastMinuteReset() {
        return lastMinuteReset;
    }

    public void setLastMinuteReset(LocalDateTime lastMinuteReset) {
        this.lastMinuteReset = lastMinuteReset;
    }

// + getters and setters for all three

    private int requestCount;
    private LocalDateTime lastReset;
    public int getRequestCount() { return requestCount; }
    public void setRequestCount(int requestCount) { this.requestCount = requestCount; }

    public LocalDateTime getLastReset() { return lastReset; }
    public void setLastReset(LocalDateTime lastReset) { this.lastReset = lastReset; }

    public API() {}

    @JsonProperty("id")
    public Long getId() { return id; }

    @JsonProperty("api_key")
    public String getApiKey() { return apiKey; }

    public void setApiKey(String apiKey) {   // ✅ FIXED
        this.apiKey = apiKey;
    }

    @JsonProperty("key_name")
    public String getKeyName() { return keyName; }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @JsonProperty("created_at")
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() { return active; }

    public void setActive(boolean active) {
        this.active = active;
    }
}