package htw.kbe;

import com.google.gson.Gson;
import htw.kbe.controller.SongController;
import htw.kbe.controller.SongListController;
import htw.kbe.model.Song;
import htw.kbe.model.SongList;
import htw.kbe.repository.SongListRepo;
import htw.kbe.repository.SongRepo;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class ControllerTest {

    @Mock
    private RestTemplate restTemplate;

    private MockMvc mockSongListController;
    private MockMvc mockSongController;



//
//	@Autowired
//	WebSecurityConfig web;

    @Autowired
    private SongListRepo songListRepo;

    @Autowired
    private SongRepo songRepo;



    SongList songlist1;
    SongList songlist2;
    SongList songlistForeignPublic;
    SongList songlistForeignPrivate;
    SongList badSongList;

    SongList songlist;
    private Gson gson;
    private String myUser = "davis";
    private String anotherUser = "charly";

    String token;

    @BeforeEach
    public void setupMockMvc() throws Exception {
        this.gson = new Gson();
//        securityUser = getPrincipal();
//        loadData();
        mockSongController = MockMvcBuilders.standaloneSetup(new SongController(songRepo)).build();
        mockSongListController = MockMvcBuilders.standaloneSetup(new SongListController(songListRepo, restTemplate))
                .build();
        // mockMvc3 = MockMvcBuilders.standaloneSetup(new
        // SongController(sRepo)).build();


        songlist = new SongList("songlist default", false, myUser);
        Song song = new Song();
        song.setTitle("new song");
        song.setArtist("Davis D. Sky");
        song.setAlbum("Intellij");
        songlist.getSongList().add(song);
        songlist1 = new SongList("songlist no1", false, myUser);
        songlist2 = new SongList("songlist no2", true, myUser);

        songlistForeignPublic = new SongList("songlist no3", false, anotherUser);

        songlistForeignPrivate = new SongList("songlist no4", true, anotherUser);
        badSongList = new SongList("songlist no4", true, anotherUser);
        song.setTitle("");
        badSongList.getSongList().add(song);

        // ==========================================
//		uRepo.save(user1);

        // authenticate user 1
        String userToAuth = "{\"username\":\"mmuster\"," + "\"firstname\":\"Maxime\", " + "\"lastname\":"
                + "\"Muster\"," + "\"password\":\"pass1234\"}";

        //System.out.println(result.getResponse().getContentAsString());
        //JSONObject tokenObject = new JSONObject(result.getResponse().getContentAsString());
        //this.token = tokenObject.getString("token");
        //System.out.println(this.token);

    }
    @Test
    public void postSongList() throws Exception{
        String payload = gson.toJson(songlist);
        String username = myUser;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/",String.class))
          .thenReturn(username);


        MvcResult result = mockSongListController
                .perform(post("/songLists/")
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();

        //assertEquals(16, list.size());

    }
    @Test
    public void postBadSongList() throws Exception{
        String payload = gson.toJson(badSongList);
        String username = myUser;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/",String.class))
                .thenReturn(username);
        MvcResult result = mockSongListController
                .perform(post("/songLists/")
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(payload))
                .andExpect(status().isBadRequest()).andReturn();

    }
    @Test
    public void getOwnSongList() throws Exception{
        songListRepo.save(songlist);
        String payload = gson.toJson(songlist);
        String username = myUser;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/",String.class))
                .thenReturn(username);
        MvcResult result = mockSongListController
                .perform(get("/songLists/1")
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(""))
                .andExpect(status().isOk()).andReturn();

    }
    @Test
    public void getForeignPublicSongList() throws Exception{
        songListRepo.save(songlistForeignPublic);
        String payload = gson.toJson(songlist);
        String username = myUser;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/",String.class))
                .thenReturn(username);
        MvcResult result = mockSongListController
                .perform(get("/songLists/1")
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(""))
                .andExpect(status().isOk()).andReturn();

    }
    @Test
    public void getForeignPrivateSongList() throws Exception{
        songListRepo.save(songlistForeignPrivate);
        String payload = gson.toJson(songlist);
        String username = myUser;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/",String.class))
                .thenReturn(username);
        MvcResult result = mockSongListController
                .perform(get("/songLists/1")
                        .header("Content-Type", "application/json").header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg")
                        .content(""))
                .andExpect(status().isForbidden()).andReturn();

    }

}
