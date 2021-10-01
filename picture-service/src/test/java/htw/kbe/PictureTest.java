package htw.kbe;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import htw.kbe.controller.PictureController;
import htw.kbe.model.PictureObject;
import htw.kbe.repository.PictureRepository;
import htw.kbe.service.PictureService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class PictureTest {

    @Mock
    private RestTemplate restTemplate;

    private MockMvc mockPictureController;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private PictureService service;

    private String picture = "https://davisfrank.de/image/bbb.jpg";

    private String myUser = "test-user";
    private String anotherUser = "charly";
    private Gson gson;
    String tokenString = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg";
    String defaultTitle = "picture for song";

    @BeforeEach
    public void setupMockMvc() throws Exception {
        mockPictureController = MockMvcBuilders.standaloneSetup(new PictureController(service, restTemplate)).build();
        this.gson = new Gson();
        pictureRepository.deleteAll();
    }


    @Test
    public void saveImageFromUrl() throws Exception {
        String payload = picture;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn(myUser);
        MvcResult result = mockPictureController
                .perform(post("/pictures/"+defaultTitle)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();
        System.out.println("### Return:" + result.getResponse().getContentAsString());

    }

    @Test
    public void saveImageFromBadUrl() throws Exception {
        String payload = "dfkljasdföja";
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn(myUser);
        MvcResult result = mockPictureController
                .perform(post("/pictures/"+defaultTitle)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isBadRequest()).andReturn();
        System.out.println("### Return:" + result.getResponse().getContentAsString());
    }

    @Test
    public void getImage() throws Exception {
        String payload = picture;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn(myUser);
        MvcResult picResult = mockPictureController
                .perform(post("/pictures/"+defaultTitle)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();
        String id = picResult.getResponse().getContentAsString();
        MvcResult result = mockPictureController
                .perform(get("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isOk()).andReturn();
        System.out.println("### Return:" + result.getResponse().getContentAsString());
    }

    @Test
    public void getNonExistingImage() throws Exception {
        String id = "non-existing-image-id";
        MvcResult result = mockPictureController
                .perform(get("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isNotFound()).andReturn();
        System.out.println("### Return:" + result.getResponse().getContentAsString());
    }

    @Test
    public void deleteOwnImage() throws Exception {
        String payload = picture;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn(myUser);
        MvcResult picResult = mockPictureController
                .perform(post("/pictures/"+defaultTitle)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn(myUser);
        String id = picResult.getResponse().getContentAsString();
        MvcResult result = mockPictureController
                .perform(delete("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isAccepted()).andReturn();

        MvcResult picResult2 = mockPictureController
                .perform(get("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isNotFound()).andReturn();
    }
    // try deleting not own
    @Test
    public void deleteNotOwnImage() throws Exception {
        String payload = picture;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn(myUser);
        MvcResult picResult = mockPictureController
                .perform(post("/pictures/"+defaultTitle)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();
        System.out.println("pass 1");



        String id = picResult.getResponse().getContentAsString();
        MvcResult result = mockPictureController
                .perform(get("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isOk()).andReturn();

        // return another user when auth-service is asked
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn("yolandi");

        System.out.println("pass 2");
        MvcResult picResult2 = mockPictureController
                .perform(delete("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void updateOwnImage() throws Exception {


        String payload = picture;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn(myUser);
        MvcResult picResult = mockPictureController
                .perform(post("/pictures/"+defaultTitle)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();
        String id = picResult.getResponse().getContentAsString();

        MvcResult result = mockPictureController
                .perform(get("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isOk()).andReturn();
        System.out.println("Got pic -> "+result.getResponse().getContentAsString());
        String json = result.getResponse().getContentAsString().replace(defaultTitle, "new song image");

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn(myUser);

        MvcResult result2 = mockPictureController
                .perform(put("/pictures/"+id)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(json))
                .andExpect(status().isAccepted()).andReturn();
        System.out.println("### Return:" + result2.getResponse().getContentAsString());

        assertEquals(true,result2.getResponse().getContentAsString().contains("new song image"));

    }
    @Test
    public void updateNotOwnImage() throws Exception {


        String payload = picture;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn(myUser);
        MvcResult picResult = mockPictureController
                .perform(post("/pictures/"+defaultTitle)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();
        String id = picResult.getResponse().getContentAsString();

        MvcResult result = mockPictureController
                .perform(get("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isOk()).andReturn();
        System.out.println("Got pic -> "+result.getResponse().getContentAsString());
        String json = result.getResponse().getContentAsString().replace(defaultTitle, "new song image");

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn("yolandi");
        MvcResult result2 = mockPictureController
                .perform(put("/pictures/"+id)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(json))
                .andExpect(status().isUnauthorized()).andReturn();
        System.out.println("### Return:" + result2.getResponse().getContentAsString());

        //assertEquals(false,result2.getResponse().getContentAsString().contains("new song image"));

    }
    @Test
    public void updateNonExisting() throws Exception {


        String payload = picture;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn(myUser);
        MvcResult picResult = mockPictureController
                .perform(post("/pictures/"+defaultTitle)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();
        String id = picResult.getResponse().getContentAsString();

        MvcResult result = mockPictureController
                .perform(get("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isOk()).andReturn();
        System.out.println("Got pic -> "+result.getResponse().getContentAsString());
        String json = result.getResponse().getContentAsString().replace(defaultTitle, "new song image");

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn(anotherUser);
        MvcResult result2 = mockPictureController
                .perform(put("/pictures/123123123"+id)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(json))
                .andExpect(status().isNotFound()).andReturn();
        System.out.println("### Return:" + result2.getResponse().getContentAsString());

        //assertEquals(false,result2.getResponse().getContentAsString().contains("new song image"));

    }
    /*
    @Test
    public void updateMethodNotAllowed() throws Exception {


        String payload = picture;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn(myUser);
        MvcResult picResult = mockPictureController
                .perform(post("/pictures/"+defaultTitle)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();
        String id = picResult.getResponse().getContentAsString();

        MvcResult result = mockPictureController
                .perform(get("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isOk()).andReturn();
        System.out.println("Got pic -> "+result.getResponse().getContentAsString());
        String json = result.getResponse().getContentAsString().replace(defaultTitle, "new song image");

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/"+tokenString, String.class))
                .thenReturn(myUser);
        MvcResult result2 = mockPictureController
                .perform(put("/pictures/")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(json))
                .andExpect(status().isNotFound()).andReturn();
        System.out.println("### Return:" + result2.getResponse().getContentAsString());

        //assertEquals(false,result2.getResponse().getContentAsString().contains("new song image"));

    }*/
}
