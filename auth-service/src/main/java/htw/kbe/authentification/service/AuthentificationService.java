package htw.kbe.authentification.service;

import java.util.ArrayList;

import htw.kbe.authentification.model.JwtRequest;
import htw.kbe.authentification.model.JwtResponse;
import htw.kbe.authentification.model.User;
import htw.kbe.authentification.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.Assert;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Service
public class AuthentificationService {

    private final RestTemplate restTemplate;
    private final JwtTokenUtil jwt;

    @Autowired
    public AuthentificationService(RestTemplate restTemplate,
                       final JwtTokenUtil jwt) {
        this.restTemplate = restTemplate;
        this.jwt = jwt;
    }

    public JwtResponse register(JwtRequest authRequest) {
        //do validation if user already exists
        try {
            authRequest.setPassword(BCrypt.hashpw(authRequest.getPassword(), BCrypt.gensalt()));
            System.out.println("###  ### ### ### hello World");

            System.out.println("###  ### ### ### hello World");
            User userVO = restTemplate.postForObject("http://user-service/users", authRequest, User.class);
            Assert.notNull(userVO, "Failed to register user. Please try again later");

            String accessToken = jwt.generate(userVO, "ACCESS");
            String refreshToken = jwt.generate(userVO, "REFRESH");

            return new JwtResponse(accessToken, refreshToken);
        } catch (Exception e) {

            return new JwtResponse("", "");
        }


    }
    public JwtResponse login(JwtRequest authRequest) {
        //do validation if user already exists
        //authRequest.setPassword(BCrypt.hashpw(authRequest.getPassword(), BCrypt.gensalt()));

        User user = restTemplate.postForObject("http://user-service/users/verify", authRequest, User.class);
        Assert.notNull(user, "Failed to register user. Please try again later");

        String accessToken = jwt.generate(user, "ACCESS");
        String refreshToken = jwt.generate(user, "REFRESH");

        return new JwtResponse(accessToken, refreshToken);

    }
}
