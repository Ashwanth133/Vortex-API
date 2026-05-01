package com.ashwanth.ApKeyManager.Service;

import com.ashwanth.ApKeyManager.Model.Funfacts;
import com.ashwanth.ApKeyManager.Repository.FunfactsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ApiService {
    private final FunfactsRepository funfactsRepository;

    public ApiService(FunfactsRepository funfactsRepository) {
        this.funfactsRepository = funfactsRepository;
    }

    public String givefacts(long id) {
        return funfactsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fact not found"))
                .getFacts();
    }

    public String randomfunfacts() {
        List<Funfacts> facts = funfactsRepository.findAll();

        if (facts.isEmpty()) {
            throw new RuntimeException("No facts available");
        }

        int randomIndex = new Random().nextInt(facts.size());

        return facts.get(randomIndex).getFacts();
    }

}
