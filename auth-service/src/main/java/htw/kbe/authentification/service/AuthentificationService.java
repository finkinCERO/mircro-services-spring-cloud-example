package htw.kbe.authentification.service;

import java.util.ArrayList;

import htw.kbe.authentification.model.JwtRequest;
import htw.kbe.authentification.model.JwtResponse;
import htw.kbe.authentification.model.User;
import htw.kbe.authentification.model.UserDTO;
import htw.kbe.authentification.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.Assert;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Service
public class AuthentificationService implements UserDetailsService {

    private final RestTemplate restTemplate;
    private final JwtTokenUtil jwt;
    private final UserRepository userRepo;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public AuthentificationService(RestTemplate restTemplate,
                                   final JwtTokenUtil jwt, UserRepository repository, AuthenticationManager manager) {
        this.restTemplate = restTemplate;
        this.jwt = jwt;
        this.userRepo = repository;
        this.authenticationManager = manager;
    }

    // sign up with token generation
    public JwtResponse register(JwtRequest authRequest) {
        //do validation if user already exists
        try {
            //authRequest.setPassword(BCrypt.hashpw(authRequest.getPassword(), BCrypt.gensalt()));

            User userVO = userRepo.findByUsername(authRequest.getUsername());//restTemplate.postForObject("http://user-service/users", authRequest, User.class);

            //Assert.notNull(userVO, "Failed to register user. Please try again later");
            if (userVO != null)
                return new JwtResponse("", "", "user exists", true);
            System.out.println("### user: " + userVO);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User u = new User();
            u.setUsername(authRequest.getUsername());
            u.setPassword(encoder.encode(authRequest.getPassword()));
            userVO = userRepo.save(u);
            String accessToken = jwt.generate(userVO, "ACCESS");
            String refreshToken = jwt.generate(userVO, "REFRESH");

            return new JwtResponse(accessToken, refreshToken, "success", false);
        } catch (NullPointerException e){
            System.out.println("### Null Pointer error");
            return new JwtResponse("", "", "", true);
        } catch (Exception e) {
            System.out.println("### Type: "+e.getStackTrace());
            System.out.println("### Error register");
            return new JwtResponse("", "", "", true);
        }
    }

    // login to get token
    public JwtResponse login(JwtRequest authRequest) {
        //do validation if user already exists
        //authRequest.setPassword(BCrypt.hashpw(authRequest.getPassword(), BCrypt.gensalt()));
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            System.out.println("authentificated...");
            User user = userRepo.findByUsername(authRequest.getUsername());
            String accessToken = jwt.generate(user, "ACCESS");
            String refreshToken = jwt.generate(user, "REFRESH");
            return new JwtResponse(accessToken, refreshToken, "success", false);
        } catch (Exception e){
            return new JwtResponse("", "", "user or password incorrect", true);

        }

        //*
        //Assert.notNull(user, "Failed to register user. Please try again later");
        //if (user == null) {
        //    return new JwtResponse("", "", "username or password incorrect", true);
        //}


    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + s);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }
}
