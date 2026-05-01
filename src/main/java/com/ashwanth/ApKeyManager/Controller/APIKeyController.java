package com.ashwanth.ApKeyManager.Controller;

import com.ashwanth.ApKeyManager.DTO.APIDto;
import com.ashwanth.ApKeyManager.Model.API;
import com.ashwanth.ApKeyManager.Service.APIKeyService;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class APIKeyController {
    private final APIKeyService apiKeyService;

    public APIKeyController(APIKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping("/generate-key")
    public ResponseEntity<?> generateKey(HttpSession session, @RequestBody Map<String, String> body) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "unauthorized",
                            "message", "User not logged in"
                    ));
        }

        String keyName = body.get("keyName");

        API api = apiKeyService.generateKey(userId, keyName);

        return ResponseEntity.ok(api);
    }
    @GetMapping("/keys")
    public List<API> getAllApi(HttpSession session){
        return apiKeyService.getAllKeys(session);

    }
    @DeleteMapping("/keys/{id}")
    public ResponseEntity<String> deleteKey(@PathVariable Long id,HttpSession session){
        apiKeyService.deleteKey(id,session);
        return ResponseEntity.ok("Deletion Success");
    }

}
