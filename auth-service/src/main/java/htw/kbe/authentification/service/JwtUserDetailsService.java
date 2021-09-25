package htw.kbe.authentification.service;

import java.util.ArrayList;

import htw.kbe.authentification.model.User;
import htw.kbe.authentification.model.UserDTO;
import htw.kbe.authentification.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userDao;

    @Autowired
    private PasswordEncoder bcryptEncoder;



    public User save(UserDTO user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
//		newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
//		newUser.setPassword(user.getPassword());
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        return userDao.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }
}