package com.ashwanth.ApKeyManager.Controller;

import com.ashwanth.ApKeyManager.DTO.LoginDTO;
import com.ashwanth.ApKeyManager.DTO.NameDTO;
import com.ashwanth.ApKeyManager.DTO.RegisterDTO;
import com.ashwanth.ApKeyManager.DTO.UserDTO;
import com.ashwanth.ApKeyManager.Model.User;
import com.ashwanth.ApKeyManager.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {

        this.authService=authService;

    }

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }




    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDTO user = authService.getCurrentUser(userId);

        return ResponseEntity.ok(new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getAge()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpSession session){

        User user=authService.Login(loginDTO);
        if(user!=null) {

            session.setAttribute("userId", user.getId());
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/register")
    public  ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO,HttpSession session){
        User user=authService.Register(registerDTO);
        session.setAttribute("userId", user.getId());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/set-name")
    public ResponseEntity<?> setname(@RequestBody NameDTO nameDTO,HttpSession session){

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(authService.setName(userId,nameDTO.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        request.getSession().invalidate();
        return ResponseEntity.ok("Logged out");
    }
}
