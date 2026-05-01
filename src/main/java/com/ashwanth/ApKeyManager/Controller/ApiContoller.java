package com.ashwanth.ApKeyManager.Controller;
import com.ashwanth.ApKeyManager.Service.ApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fact")
public class ApiContoller {

    private ApiService apiService;
    public ApiContoller(ApiService apiService) {
        this.apiService = apiService;
    }
    @GetMapping("/random")
    public java.util.Map<String, String> randomfunfacts(){
        String f = apiService.randomfunfacts();
        return java.util.Map.of("fact", f);
    }

    @GetMapping("/{id}")
    public String givefunfacts(@PathVariable long id){
        return apiService.givefacts(id);
    }
}
