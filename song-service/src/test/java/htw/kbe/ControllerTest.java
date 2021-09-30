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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


    String tokenString = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImNlcnNmbyIsInN1YiI6ImNlcnNmbyIsImlhdCI6MTYzMjg2OTg2NSwiZXhwIjoxNjMyOTU2MjY1fQ.GHwh75m8HvILtlPbg8zVMER4uJEBmirHivLiy9WPwNgnvoMByUKXbqR2NIx7_-S8N_HVUcrWmhzy06DHm-rtPg";
    SongList songlist1;
    SongList songlist2;
    SongList songlistForeignPublic;
    SongList songlistForeignPrivate;
    SongList badSongList;

    SongList songlist;
    private Gson gson;
    private String myUser = "davis";
    private String anotherUser = "charly";

    private Song s1_validSong;
    private Song s2_badSong;

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
        songListRepo.deleteAll();
        songRepo.deleteAll();

        songlist = new SongList("songlist default", false, myUser);
        Song song = new Song();
        song.setTitle("new song");
        song.setArtist("Davis D. Sky");
        song.setAlbum("Intellij");
        songlist.getSongList().add(song);

        s1_validSong = song;
        songlist1 = new SongList("songlist no1", false, myUser);
        songlist2 = new SongList("songlist no2", true, myUser);

        songlistForeignPublic = new SongList("songlist no3", false, anotherUser);

        songlistForeignPrivate = new SongList("songlist no4", true, anotherUser);
        badSongList = new SongList("", true, anotherUser);
        Song song2 = new Song();
        song2.setTitle("");
        song2.setArtist("Davis D. Sky");
        song2.setAlbum("Intellij");

        s2_badSong = song2;
        badSongList.getSongList().add(song);


    }


    @Test
    public void postSongList() throws Exception {
        String payload = gson.toJson(songlist);
        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);


        MvcResult result = mockSongListController
                .perform(post("/playlists/")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();

        //assertEquals(16, list.size());

    }

    @Test
    public void postBadSongList() throws Exception {
        String payload = gson.toJson(badSongList);
        String username = myUser;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongListController
                .perform(post("/playlists/")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    public void getOwnSongList() throws Exception {
        SongList s = songListRepo.save(songlist);
        String payload = gson.toJson(songlist);
        String username = myUser;


        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result2 = mockSongListController
                .perform(get("/playlists/" + s.getId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    public void getForeignPublicSongList() throws Exception {
        SongList s = songListRepo.save(songlistForeignPublic);
        String payload = gson.toJson(songlist);
        String username = myUser;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongListController
                .perform(get("/playlists/" + s.getId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    public void getForeignPrivateSongListTest() throws Exception {
        songListRepo.deleteAll();
        SongList s = songListRepo.save(songlistForeignPrivate);

        String username = myUser;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongListController
                .perform(get("/playlists/" + s.getId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isForbidden()).andReturn();

    }

    @Test
    public void getOwnLists() throws Exception {
        SongList s = songListRepo.save(songlist);
        String payload = gson.toJson(songlist);
        String username = myUser;


        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result2 = mockSongListController
                .perform(get("/playlists?username=" + myUser)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isOk()).andReturn();

    }
    @Test
    public void getNonExistingLists() throws Exception {
        SongList s = songListRepo.save(songlist);
        String payload = gson.toJson(songlist);
        String username = myUser;


        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result2 = mockSongListController
                .perform(get("/playlists?username=abcabc" + myUser)
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    public void getNonExistingPlayListTest() throws Exception {
        songListRepo.save(songlistForeignPrivate);
        String payload = gson.toJson(songlist);
        String username = myUser;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongListController
                .perform(get("/playlists/11111")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isNotFound()).andReturn();

    }

    @Test
    public void deleteOwmSongListTest() throws Exception {

        SongList s = songListRepo.save(songlist);

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(myUser);

        // Method not allowed?

        MvcResult result2 = mockSongListController
                .perform(delete("/playlists/" + s.getId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                )
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    public void deleteNotOwmSongListTest() throws Exception {

        SongList s = songListRepo.save(songlistForeignPrivate);

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(myUser);

        // Method not allowed?

        MvcResult result2 = mockSongListController
                .perform(delete("/playlists/" + s.getId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                )
                .andExpect(status().isForbidden()).andReturn();
    }

    @Test
    public void deleteNonExistingTest() throws Exception {

        SongList s = songListRepo.save(songlistForeignPrivate);
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(myUser);
        // Method not allowed?
        MvcResult result2 = mockSongListController
                .perform(delete("/playlists/123123123" + s.getId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                )
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void deleteBadArgSongListTest() throws Exception {
        SongList s = songListRepo.save(songlistForeignPrivate);
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(myUser);
        // id of songlist is required to be an integer
        MvcResult result2 = mockSongListController
                .perform(delete("/playlists/abc" + s.getId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                )
                .andExpect(status().isBadRequest()).andReturn();
        // delete without paramter should'nt been allowed
        mockSongListController
                .perform(delete("/playlists/")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                )
                .andExpect(status().isMethodNotAllowed()).andReturn();
    }

    // update songlist
    @Test
    public void updateOwnSongListTest() throws Exception {
        SongList s = songListRepo.save(songlist);
        String username = myUser;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);

        s.setName("my new playlist 999");

        String payload = gson.toJson(s);


        MvcResult result = mockSongListController
                .perform(put("/playlists/" + s.getId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();

        System.out.println("Updated: " + result.getResponse().getContentAsString());
        assertEquals(true, result.getResponse().getContentAsString().contains("my new playlist 999"));

        //assertEquals(16, list.size());

    }

    @Test
    public void updateNotOwnSongListTest() throws Exception {
        SongList s = songListRepo.save(songlistForeignPublic);
        String username = myUser;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);

        s.setName("my new playlist 999");

        String payload = gson.toJson(s);

        MvcResult result = mockSongListController
                .perform(put("/playlists/" + s.getId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isForbidden()).andReturn();

        assertEquals(false, result.getResponse().getContentAsString().contains("my new playlist 999"));

        //assertEquals(16, list.size());

    }

    @Test
    public void updateNonExistingSongListTest() throws Exception {
        SongList s = songListRepo.save(songlistForeignPublic);
        String username = myUser;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);

        s.setName("my new playlist 999");

        String payload = gson.toJson(s);

        MvcResult result = mockSongListController
                .perform(put("/playlists/100")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isNotFound()).andReturn();


    }

    @Test
    public void updateWithBadArgSongListTest() throws Exception {
        SongList s = songlistForeignPublic;
        String username = myUser;
        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);

        s.setName("my new playlist 999");

        String payload = gson.toJson(s);

        MvcResult result = mockSongListController
                .perform(put("/playlists/abc")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isBadRequest()).andReturn();

        mockSongListController
                .perform(put("/playlists/")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isMethodNotAllowed()).andReturn();

    }
    // ########## SONGLIST TEST END ###############
    // ########## SONG TEST START #################

    @Test
    public void getSongTest() throws Exception {
        Song so = songRepo.save(s1_validSong);
        String payload = gson.toJson(so);
        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongController
                .perform(get("/songs/" + so.getSongId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isOk()).andReturn();

        assertEquals(true, result.getResponse().getContentAsString().contains(so.getTitle()));

    }

    @Test
    public void getNonExistingSongTest() throws Exception {

        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongController
                .perform(get("/songs/99999999")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isNotFound()).andReturn();

    }

    @Test
    public void getBadArgsSongTest() throws Exception {

        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongController
                .perform(get("/songs/abc")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isBadRequest()).andReturn();

        MvcResult result2 = mockSongController
                .perform(get("/songs/")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(""))
                .andExpect(status().isMethodNotAllowed()).andReturn();

    }

    @Test
    public void postSongTest() throws Exception {
        String payload = gson.toJson(s1_validSong);
        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongController
                .perform(post("/songs/")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    public void postInvalidSongTest() throws Exception {
        s2_badSong.setTitle(null);
        String payload = gson.toJson(s2_badSong);
        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongController
                .perform(post("/songs/")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isBadRequest()).andReturn();

        // empty title string test
        Song s2 = new Song();
        s2.setTitle("");
        String payload2 = gson.toJson(s2);

        MvcResult result2 = mockSongController
                .perform(post("/songs/")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload2))
                .andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    public void updateSongTest() throws Exception {
        Song s = songRepo.save(s1_validSong);
        s.setAlbum("amazing album");
        s.setTitle("my title 0");
        s.setArtist("another artist");
        String payload = gson.toJson(s1_validSong);
        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongController
                .perform(put("/songs/" + s.getSongId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isAccepted()).andReturn();
    }

    @Test
    public void updateWithoutTitleSongTest() throws Exception {
        Song s = songRepo.save(s1_validSong);
        s.setAlbum("amazing album");
        s.setTitle("");
        s.setArtist("another artist");
        String payload = gson.toJson(s1_validSong);
        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongController
                .perform(put("/songs/" + s.getSongId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isConflict()).andReturn();
    }

    @Test
    public void updateNonExistingSongTest() throws Exception {
        Song s = songRepo.save(s1_validSong);
        s.setAlbum("amazing album");
        s.setTitle("");
        s.setArtist("another artist");
        String payload = gson.toJson(s1_validSong);
        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongController
                .perform(put("/songs/99999999" + s.getSongId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void updateBadExistingSongTest() throws Exception {
        Song s = songRepo.save(s1_validSong);
        String payload = gson.toJson(s1_validSong);
        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongController
                .perform(put("/songs/abc" + s.getSongId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void updateNoArgSongTest() throws Exception {
        Song s = songRepo.save(s1_validSong);
        String payload = gson.toJson(s1_validSong);
        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongController
                .perform(put("/songs/")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isMethodNotAllowed()).andReturn();
    }

    @Test
    public void deleteSongTest() throws Exception {
        Song s = songRepo.save(s1_validSong);
        String payload = gson.toJson(s1_validSong);
        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongController
                .perform(delete("/songs/" + s.getSongId())
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    public void deleteNonExistingSongTest() throws Exception {
        Song s = songRepo.save(s1_validSong);
        String payload = gson.toJson(s1_validSong);
        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongController
                .perform(delete("/songs/111191")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void deleteSongBadArgsTest() throws Exception {
        Song s = songRepo.save(s1_validSong);
        String payload = gson.toJson(s1_validSong);
        String username = myUser;

        Mockito.when(restTemplate.getForObject("http://auth-service/auth/" + tokenString, String.class))
                .thenReturn(username);
        MvcResult result = mockSongController
                .perform(delete("/songs/abc")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isBadRequest()).andReturn();

        mockSongController
                .perform(delete("/songs/")
                        .header("Content-Type", "application/json").header("Authorization", tokenString)
                        .content(payload))
                .andExpect(status().isMethodNotAllowed()).andReturn();
    }
}
