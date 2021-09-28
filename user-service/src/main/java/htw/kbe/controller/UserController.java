package htw.kbe.controller;
import htw.kbe.models.User;
import htw.kbe.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User register(@RequestBody User user) {
        System.out.println("### got username: "+user.getUsername());
        User existing = userService.getUser(user.getUsername());
        if(existing!=null) return null; //System.out.println("### user dosnt exists: "+user.getUsername());//return null;

        User u = userService.save(user);
        //u.setPassword("*****");
        return u;
    }

    @GetMapping(value = "/{username}", consumes = {"application/json", "application/xml"}, produces = {"application/json",
            "application/xml"})
    public User getUser(
            @PathVariable(value = "username") String username){
        User u = userService.getUser(username);
        u.setPassword("******");
        return userService.getUser(username);
    }


    @PostMapping(value = "/verify")
    public User verifyUser(@RequestBody User user) {
        System.out.println("Hello POST User verify");
        boolean b = userService.verify(user);

        if(b) {

            System.out.println("### user is okay");
            User u = userService.getUser(user.getUsername());
            u.setPassword("******");
            return u;
        }
        return null;


    }

    @GetMapping(value = "/secure")
    public String getSecure() {
        return "Secure endpoint available";
    }
}