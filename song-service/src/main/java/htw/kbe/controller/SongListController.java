package htw.kbe.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import htw.kbe.model.Song;
import htw.kbe.model.SongList;
import htw.kbe.model.Token;
import htw.kbe.repository.SongListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping(value = "/songLists")
public class SongListController {

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private SongListRepo songListRepo;

    public SongListController(SongListRepo dao, RestTemplate rest) {
        this.songListRepo = dao;
        this.restTemplate = rest;
    }


    @GetMapping
    public ResponseEntity<List<SongList>> getSongListFromUserName(@RequestParam("username") String username, Principal principal) {
        //Optional<SongList> songlist = songListRepo.findByOwner(userId);
        try {
            System.out.println("### name: "+principal.getName());
            String _username = "davis";
            if (principal != null) _username = principal.getName();
            String u = "user";//userRepo.findByUsername(username);

            List<SongList> songs = songListRepo.findByOwner(u);
            List<SongList> result = new ArrayList<>();
            if (u == null) { 
                return new ResponseEntity<List<SongList>>(result,
                        HttpStatus.NOT_FOUND);
            }
            for (SongList s : songs) {
                System.out.println("###### -> " + s.getOwner());
                if (s.getOwner().equals(_username)) result.add(s);
                else if (!s.getIsPrivate()) result.add(s);
            }

            return new ResponseEntity<List<SongList>>(result,
                    HttpStatus.OK);
        } catch (NullPointerException e) {
            System.out.println("Null Pointer...");
            return new ResponseEntity<List<SongList>>(new ArrayList<SongList>(),
                    HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {

            System.out.println("Another Error...");
            return new ResponseEntity<List<SongList>>(new ArrayList<SongList>(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{listId}")
    public ResponseEntity<SongList> getSongList(@PathVariable("listId") String listId, @RequestHeader("Authorization")String token) {
        //Optional<SongList> songlist = songListRepo.findByOwner(userId);
        try {
            // for testing

            String user = restTemplate.getForObject("http://auth-service/auth/"+token, String.class);

            SongList sl = songListRepo.findById(Integer.parseInt(listId)).get();
            if (sl.getOwner().equals(user))
                return new ResponseEntity<SongList>(sl, HttpStatus.OK);
            else if (!sl.getIsPrivate()) return new ResponseEntity<SongList>(sl, HttpStatus.OK);
            else return new ResponseEntity<SongList>(new SongList(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<SongList>(new SongList(), HttpStatus.NOT_FOUND);
        }
    }


//    @GetMapping(value="/{username}")
//    public ResponseEntity<SongList> getSongList (@PathVariable(value="username") String username){
//
//    	// find by UserId and not by SongId
//    	List<SongList> songList = songListRepo.findByOwner(username);
//
//    	return new ResponseEntity<SongList>((SongList) songList, HttpStatus.ACCEPTED);
//    }

    //    @Transactional/?isername=mmuster
    @PostMapping(value = "/", consumes = {"application/json"}, produces = "application/json")
    public ResponseEntity<SongList> postSongList(@RequestBody SongList songlist, @RequestHeader("Authorization") String token) {
        System.out.println("Hello POST SongList");
        try {
            System.out.println("### trying creating SongList");
            String user = restTemplate.getForObject("http://auth-service/auth/"+token, String.class);
            System.out.println("### username: "+user);

            songlist.setOwner(user);
            //user.getSongLists().add(songlist);
            // if songs are bad
            for (Song s : songlist.getSongList()) {
                if (s.getTitle() == null || s.getTitle().replace(" ", "").equals(""))
                    return new ResponseEntity<SongList>(new SongList(),
                            HttpStatus.BAD_REQUEST);
            }
            // if songlist hasnt a name:
            if (songlist.getName() == null || songlist.getName().replace(" ", "").equals(""))
                return new ResponseEntity<SongList>(new SongList(),
                        HttpStatus.BAD_REQUEST);

            SongList list = songListRepo.save(songlist);
            songListRepo.flush();
            String path = "/songLists/" + list.getId();
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Location",
                    path);
            return new ResponseEntity<SongList>(list, responseHeaders,
                    HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println("Exception: "+ e.getStackTrace());
            return new ResponseEntity<SongList>(new SongList(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}", produces = {"text/plain"})
    public ResponseEntity<String> deleteSongList(@PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String token)
            throws IOException {
        try {
            String username = restTemplate.getForObject("http://auth-service/auth/"+token, String.class);
            Optional<SongList> sl = songListRepo.findById(id);
            if (!sl.isPresent()) {
                return new ResponseEntity<String>("song list doesn't exist", HttpStatus.NOT_FOUND);
            }
            if (sl.get().getOwner().equals(username)) {
                songListRepo.deleteById(id);
                return new ResponseEntity<String>("song list deleted", HttpStatus.NO_CONTENT);
            } else
                return new ResponseEntity<String>("forbidden", HttpStatus.FORBIDDEN);


        } catch (Exception e) {
            return new ResponseEntity<String>("resource not found", HttpStatus.NOT_FOUND);
        }

    }

    //    @Transactional
    //@JsonIgnore
    @GetMapping(value = "/all")
    public List<SongList> getAllSongLists() {
        return songListRepo.findAll();
    }

//    @PutMapping("/{username}/")
}