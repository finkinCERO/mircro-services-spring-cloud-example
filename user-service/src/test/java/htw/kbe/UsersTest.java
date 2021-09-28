
package htw.kbe;

import com.google.gson.Gson;
import htw.kbe.controller.UserController;
import htw.kbe.models.User;
import htw.kbe.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.json.JSONObject;
/*

import java.lang.reflect.Type;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@TestPropertySource(locations = "/test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsersTest {
    private MockMvc userController;
    @Autowired
    private UserRepository uRepo;

    private Gson gson;

    private User user1 = new User();
    private User user2 = new User();


    String token;
    @BeforeEach
    public void setupMockMvc() throws Exception {
        this.gson = new Gson();
        userController = MockMvcBuilders.standaloneSetup(new UserController()).build();
//        user1 = new UserDTO("mmuster","Bobby","Smith","pass1234");
        user1.setUsername("mmuster");
        user1.setFirstname("Maxime");
        user1.setLastname("Muster");
        user1.setPassword("pass1234");

        user2.setUsername("charly");
        user2.setFirstname("Bobby");
        user2.setLastname("Smith");
        user2.setPassword("pass1234");

        uRepo.save(user1);
        uRepo.save(user2);
        // ==========================================
//		uRepo.save(user1);

        // authenticate user 1


    }

    @Test
    @Order(1)
    public void getUser() throws Exception {

        MvcResult result = userController
                .perform(get("/users/mmuster")
                        .header("Content-Type", "application/json")
                        .content(""))
                .andExpect(status().isOk()).andReturn();

    }


}
*/