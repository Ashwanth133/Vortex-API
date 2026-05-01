package com.ashwanth.ApKeyManager.Service;

import com.ashwanth.ApKeyManager.Model.API;
import com.ashwanth.ApKeyManager.Repository.APIRepository;
import com.ashwanth.ApKeyManager.Repository.UserRepository;
import com.ashwanth.ApKeyManager.Exception.LimitReachedException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
public class APIKeyService {

    private final APIRepository apiRepository;

    public APIKeyService(APIRepository apiRepository, UserRepository userRepository) {
        this.apiRepository = apiRepository;
    }


    

    public API generateKey(Long userId, String keyName) {

        if (userId == null) {
            throw new RuntimeException("User not logged in");
        }

        Long count = apiRepository.countByUserId(userId);

        // Enforce max 5 keys per user
        if (count >= 5) {
            throw new com.ashwanth.ApKeyManager.Exception.LimitReachedException(
                    "You can only create 5 API keys. Upgrade plan or delete existing keys.");
        }

        API api = new API();
        api.setKeyName(keyName);
        api.setUserId(userId);

        String key = "sk_" + UUID.randomUUID().toString().replace("-", "");
        while(apiRepository.findByApiKey(key) != null) {
            key = "sk_" + UUID.randomUUID().toString().replace("-", "");
        }
        api.setApiKey(key);

        api.setCreatedAt(LocalDateTime.now());
        api.setActive(true);
        api.setRequestCount(0);
        api.setLastReset(LocalDateTime.now());

        return apiRepository.save(api);
    }

    public List<API> getAllKeys(HttpSession session) {
        Long userId  = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("User not logged in");
        }

        return apiRepository.findAllByUserId(userId);
    }

    public void deleteKey(Long id, HttpSession session) {
        Long sessionUserId  = (Long) session.getAttribute("userId");
        if (sessionUserId == null) {
            throw new RuntimeException("User not logged in");
        }

        API api = apiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Key not found"));

        if (!api.getUserId().equals(sessionUserId)) {
            throw new RuntimeException("Unauthorized access");
        }

        apiRepository.delete(api);
    }
}