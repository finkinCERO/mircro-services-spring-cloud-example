package htw.kbe.service;


import htw.kbe.models.User;
import htw.kbe.repositories.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
//@Slf4j
public class UserService {

    private final UserRepository repository;
    private final RestTemplate restTemplate;

    @Autowired
    public UserService(UserRepository repository,
                       RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;

    }


    public User save(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pass = encoder.encode(user.getPassword());
        System.out.println("# pw: "+pass);

        System.out.println("# pw2: "+encoder.encode(user.getPassword()));
        user.setPassword(pass);
        return this.repository.save(user);
    }

    public boolean verify(User user) {
        try {
            //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
             return true;
        } catch (NullPointerException ex) {
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    public User getUser(String username) {
        return this.repository.findByUsername(username);
    }

}
