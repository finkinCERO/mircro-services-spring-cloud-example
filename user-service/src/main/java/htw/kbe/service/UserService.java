package htw.kbe.service;


import htw.kbe.models.User;
import htw.kbe.repositories.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
//@Slf4j
public class UserService {

    private final UserRepository repository;
    private final RestTemplate restTemplate;

    @Autowired
    private PasswordEncoder bcryptEncoder;



    @Autowired
    public UserService(UserRepository repository,
                       RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }


    public User save(User user) {

        String pass = bcryptEncoder.encode(user.getPassword());
        user.setPassword(pass);
        return this.repository.save(user);
    }
    public boolean verify(User user) {
        try {
            User existing = this.repository.findByUsername(user.getUsername());
            if(existing.getPassword().equals(bcryptEncoder.encode(user.getPassword()))) return true;
        } catch (NullPointerException ex){
            return false;
        }
        return false;
    }

    public User getUser(String username) {
        return this.repository.findByUsername(username);
    }

}
