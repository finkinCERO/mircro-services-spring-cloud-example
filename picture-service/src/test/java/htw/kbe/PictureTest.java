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

    @BeforeEach
    public void setupMockMvc() throws Exception {
        mockPictureController = MockMvcBuilders.standaloneSetup(new PictureController(service, restTemplate)).build();
        this.gson = new Gson();
        pictureRepository.deleteAll();
    }


    @Test
    public void saveImageFromUrl() throws Exception {
        String payload = picture;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/", String.class))
                .thenReturn(myUser);
        MvcResult result = mockPictureController
                .perform(post("/pictures/")
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();
        System.out.println("### Return:" + result.getResponse().getContentAsString());

    }

    @Test
    public void saveImageFromBadUrl() throws Exception {
        String payload = "dfkljasdföja";
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/", String.class))
                .thenReturn(myUser);
        MvcResult result = mockPictureController
                .perform(post("/pictures/")
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(payload))
                .andExpect(status().isBadRequest()).andReturn();
        System.out.println("### Return:" + result.getResponse().getContentAsString());
    }

    @Test
    public void getImage() throws Exception {
        String payload = picture;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/", String.class))
                .thenReturn(myUser);
        MvcResult picResult = mockPictureController
                .perform(post("/pictures/")
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();
        String id = picResult.getResponse().getContentAsString();
        MvcResult result = mockPictureController
                .perform(get("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(""))
                .andExpect(status().isOk()).andReturn();
        System.out.println("### Return:" + result.getResponse().getContentAsString());
    }

    @Test
    public void getNonExistingImage() throws Exception {
        String id = "non-existing-image-id";
        MvcResult result = mockPictureController
                .perform(get("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(""))
                .andExpect(status().isNotFound()).andReturn();
        System.out.println("### Return:" + result.getResponse().getContentAsString());
    }

    @Test
    public void deleteImage() throws Exception {
        String payload = picture;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/", String.class))
                .thenReturn(myUser);
        MvcResult picResult = mockPictureController
                .perform(post("/pictures/")
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/", String.class))
                .thenReturn(myUser);
        String id = picResult.getResponse().getContentAsString();
        MvcResult result = mockPictureController
                .perform(delete("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(""))
                .andExpect(status().isAccepted()).andReturn();

        MvcResult picResult2 = mockPictureController
                .perform(get("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(""))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void updateOwnImage() throws Exception {


        String payload = picture;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/", String.class))
                .thenReturn(myUser);
        MvcResult picResult = mockPictureController
                .perform(post("/pictures/")
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();
        String id = picResult.getResponse().getContentAsString();
        MvcResult result = mockPictureController
                .perform(get("/pictures/" + id)
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(""))
                .andExpect(status().isOk()).andReturn();

        JsonObject pictureObj = new Gson().fromJson(result.getResponse().getContentAsString(), JsonObject.class);
        pictureObj.addProperty("title","new name");
        //pictureObj.setTitle("new title");

        /*Mockito.when(restTemplate.getForObject("http://auth-service/auth/", String.class))
                .thenReturn(myUser);
        MvcResult result2 = mockPictureController
                .perform(put("/pictures/")
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(gson.toJson(pictureObj)))
                .andExpect(status().isAccepted()).andReturn();
        System.out.println("### Return:" + result.getResponse().getContentAsString());

        PictureObject pictureUpdated= new Gson().fromJson(result2.getResponse().getContentAsString(), PictureObject.class);*/

        //assertEquals("new title",pictureUpdated.getTitle());

    }
}
