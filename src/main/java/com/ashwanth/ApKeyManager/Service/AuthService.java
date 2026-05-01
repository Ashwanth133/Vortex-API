package com.ashwanth.ApKeyManager.Service;

import com.ashwanth.ApKeyManager.DTO.LoginDTO;
import com.ashwanth.ApKeyManager.DTO.RegisterDTO;
import com.ashwanth.ApKeyManager.DTO.UserDTO;
import com.ashwanth.ApKeyManager.Model.User;
import com.ashwanth.ApKeyManager.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //if we use this above code it will act as autowired
    //it is dependency injection by constructor

    public User Login(LoginDTO loginDTO){
        User dbuser=userRepository.findByEmail(loginDTO.getEmail());

        //here the user.name is from frontend api
        //we are storing the user class i.e { 1.id,2.name,3.pass,4.age} by user.getname() like this
        //now user has all element f the user details if only  the user with username exists
        //user from api and db_user from db password matches then return true;

        if(dbuser != null &&
                dbuser.getPassword() != null &&
                loginDTO.getPassword().equals(dbuser.getPassword())){

            return dbuser;
        }
        else{
            return null;
        }
    }

    public UserDTO getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getAge()
        );
    }

    public User Register(RegisterDTO registerDTO){

        User user=new User();

        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        user.setAge(registerDTO.getAge());

        userRepository.save(user);

        return user;
    }

    public User setName(Long userId, String name) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setName(name);
        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        if (userId == null) {
            return null;
        }

        return userRepository.findById(userId).orElse(null);
    }
}
