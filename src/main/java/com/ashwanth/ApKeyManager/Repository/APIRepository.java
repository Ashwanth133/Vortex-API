package com.ashwanth.ApKeyManager.Repository;

import com.ashwanth.ApKeyManager.Model.API;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface APIRepository extends JpaRepository<API, Long> {

    API findByApiKey(String apiKey);

    API findByApiKeyAndActiveTrue(String apiKey);

    API findByUserId(Long user_id);

    List<API> findAllByUserId(Long user_id);

    Long countByUserId(Long user_id);
}