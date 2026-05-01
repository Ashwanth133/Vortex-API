package com.ashwanth.ApKeyManager.Exception;

public class LimitReachedException extends RuntimeException {
    public LimitReachedException(String message) {
        super(message);
    }
}
