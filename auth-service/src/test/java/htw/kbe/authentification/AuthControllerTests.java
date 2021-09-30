package htw.kbe.authentification;


import com.google.gson.Gson;
import htw.kbe.authentification.controller.AuthController;
import htw.kbe.authentification.service.AuthentificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AuthApp.class)
@TestPropertySource(locations = "/test.properties")
public class AuthControllerTests {

    private MockMvc mockAuthentificationController;

    private Gson gson;


//
//	@Autowired
//	WebSecurityConfig web;

    @Autowired
    private AuthentificationService userService;


    @BeforeEach
    public void setupMockMvc() throws Exception {
        this.gson = new Gson();
//        securityUser = getPrincipal();
//        loadData();
        mockAuthentificationController = MockMvcBuilders.standaloneSetup(new AuthController(userService)).build();
    }

    @Test
    public void registerUser() throws Exception {
        String userToAuth = "{\"username\":\"mmuster\"," + "\"firstname\":\"Maxime\", " + "\"lastname\":"
                + "\"Muster\"," + "\"password\":\"pass1234\"}";


        MvcResult result = mockAuthentificationController
                .perform(post("/auth/register")
                        .header("Content-Type", "application/json")
                        .content(userToAuth))
                .andExpect(status().isOk()).andReturn();
        System.out.println("result -> " + result.getResponse().getContentAsString());
        assertEquals(true, result.getResponse().getContentAsString().contains("success"));

    }

    @Test
    public void registerExistingUser() throws Exception {
        String userToAuth = "{\"username\":\"mmuster0\"," + "\"firstname\":\"Maxime\", " + "\"lastname\":"
                + "\"Muster\"," + "\"password\":\"pass1234\"}";

        MvcResult result = mockAuthentificationController
                .perform(post("/auth/register")
                        .header("Content-Type", "application/json")
                        .content(userToAuth))
                .andExpect(status().isOk()).andReturn();
        System.out.println("res ->" +result.getResponse().getContentAsString());
        assertEquals(true, result.getResponse().getContentAsString().contains("success"));

        MvcResult result2 = mockAuthentificationController
                .perform(post("/auth/register")
                        .header("Content-Type", "application/json")
                        .content(userToAuth))
                .andExpect(status().isOk()).andReturn();
        assertEquals(true, result2.getResponse().getContentAsString().contains("error:"));

    }

    @Test
    public void registerWithoutUsername() throws Exception {
        String userToAuth = "{\"username\":\"\"," + "\"firstname\":\"Maxime\", " + "\"lastname\":"
                + "\"Muster\"," + "\"password\":\"pass1234\"}";

        MvcResult result = mockAuthentificationController
                .perform(post("/auth/register")
                        .header("Content-Type", "application/json")
                        .content(userToAuth))
                .andExpect(status().isOk()).andReturn();
        assertEquals(true, result.getResponse().getContentAsString().contains("error:"));

    }

    @Test
    public void registerPasswordToShort() throws Exception {
        String userToAuth = "{\"username\":\"max4\"," + "\"firstname\":\"Maxime\", " + "\"lastname\":"
                + "\"Muster\"," + "\"password\":\"pass\"}";

        MvcResult result = mockAuthentificationController
                .perform(post("/auth/register")
                        .header("Content-Type", "application/json")
                        .content(userToAuth))
                .andExpect(status().isOk()).andReturn();
        assertEquals(true, result.getResponse().getContentAsString().contains("error:"));

    }

    @Test
    public void loginTest() throws Exception {
        String userToAuth = "{\"username\":\"max0\"," + "\"firstname\":\"Maxime\", " + "\"lastname\":"
                + "\"Muster\"," + "\"password\":\"pass1234\"}";

        MvcResult result = mockAuthentificationController
                .perform(post("/auth/register")
                        .header("Content-Type", "application/json")
                        .content(userToAuth))
                .andExpect(status().isOk()).andReturn();
        assertEquals(true, result.getResponse().getContentAsString().contains("success"));

        MvcResult result2 = mockAuthentificationController
                .perform(post("/auth/login")
                        .header("Content-Type", "application/json")
                        .content(userToAuth))
                .andExpect(status().isOk()).andReturn();
        assertEquals(true, result2.getResponse().getContentAsString().contains("success"));

    }
    @Test
    public void loginFailTest() throws Exception {
        String userToAuth = "{\"username\":\"max00\"," + "\"firstname\":\"Maxime\", " + "\"lastname\":"
                + "\"Muster\"," + "\"password\":\"pass1234\"}";

        MvcResult result = mockAuthentificationController
                .perform(post("/auth/register")
                        .header("Content-Type", "application/json")
                        .content(userToAuth))
                .andExpect(status().isOk()).andReturn();
        assertEquals(true, result.getResponse().getContentAsString().contains("success"));
        userToAuth = "{\"username\":\"max00\"," + "\"firstname\":\"Maxime\", " + "\"lastname\":"
                + "\"Muster\"," + "\"password\":\"pass1123234\"}";
        MvcResult result2 = mockAuthentificationController
                .perform(post("/auth/login")
                        .header("Content-Type", "application/json")
                        .content(userToAuth))
                .andExpect(status().isOk()).andReturn();
        System.out.println("res ->" +result2.getResponse().getContentAsString());
        assertEquals(true, result2.getResponse().getContentAsString().contains("error:"));

    }
}
