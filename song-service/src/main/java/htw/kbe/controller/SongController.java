package htw.kbe.controller;


import htw.kbe.model.Song;
import htw.kbe.repository.SongRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/songs")
public class SongController {
    private SongRepo songRepo;

    public SongController(SongRepo dao) {
        this.songRepo = dao;

    }

    // GET http://localhost:8080/authSpring/rest/songs/1
    @GetMapping(value = "/{id}", consumes = {"application/json", "application/xml"}, produces = {"application/json",
            "application/xml"})
    public ResponseEntity<Optional> getSong(@PathVariable(value = "id") Integer id) throws IOException {
        try {
            Optional<Song> song = songRepo.findById(id);
            //songRepo.getById(id);

            if (song.isPresent()) {
                System.out.println("###################### " + song);
                return new ResponseEntity<Optional>(song, HttpStatus.OK);
            }
            return new ResponseEntity<Optional>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<Optional>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * System.out.println("################### new post ######################");
     * String artist = request.getParameter("artist"); String label =
     * request.getParameter("label"); response.setContentType("application/json");
     * if (checkParamsPost(request)) { try { String title =
     * request.getParameter("title"); String released =
     * request.getParameter("released"); System.out.println("released -> " +
     * released); if (title != null && !title.equals("")) {
     *
     * create(response, title, artist, label, released); } else {
     * sendResponse("set title to create new song", response,
     * HttpServletResponse.SC_NOT_ACCEPTABLE); }
     *
     * } catch (NumberFormatException | NullPointerException e) { // return bad
     * request 400 or something with wrong format sendResponse("", response,
     * HttpServletResponse.SC_BAD_REQUEST); e.printStackTrace(); } } else {
     * sendResponse("", response, HttpServletResponse.SC_BAD_REQUEST); }
     */
    @PostMapping(value = "/", consumes = {"application/json"}, produces = "application/json")
    public ResponseEntity<Song> add(@RequestBody Song song) {
        System.out.println("Hello POST Song");
        try {
            if (song.getTitle().replaceAll(" ", "").equals(""))
                return new ResponseEntity<Song>(song, HttpStatus.BAD_REQUEST);
            songRepo.save(song);
            return new ResponseEntity<Song>(song, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Song>(song, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"}, produces = "application/json")
    public ResponseEntity<Song> updateSong(@RequestBody Song song, @PathVariable(value = "id") Integer id) {

        try {
            if (id == null) {
                return new ResponseEntity<Song>(song, HttpStatus.BAD_REQUEST);
            }
            Integer _id = id;
            Optional<Song> _song = songRepo.findById(_id);
            if (!_song.isPresent())
                return new ResponseEntity<Song>(song, HttpStatus.NOT_FOUND);
            else if (song.getTitle() == null || song.getTitle().replace(" ", "").equals(""))
                return new ResponseEntity<Song>(song, HttpStatus.CONFLICT);
            else {
                Song songToUpdate = songRepo.findById(id).get();
                songToUpdate.setTitle(song.getTitle());
                songToUpdate.setArtist(song.getArtist());
                songToUpdate.setAlbum(song.getAlbum());
                songToUpdate.setReleased(song.getReleased());
                songRepo.save(songToUpdate);
                songRepo.flush();
                return new ResponseEntity<Song>(songToUpdate, HttpStatus.ACCEPTED);
            }
            // check songId !=null -> bad request
            // check song.id != null -> bad request
            // check songId == song.id -> continue update
        } catch (NumberFormatException e) {
            return new ResponseEntity<Song>(song, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<Song>(song, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping(value = "/{id}", consumes = {"application/json", "application/xml"}, produces = {"text/plain"})
    public ResponseEntity<String> deleteSong(@PathVariable(value = "id") Integer id) throws IOException {
        try {
            Optional<Song> s = songRepo.findById(id);
            if (!s.isPresent())
                return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
            songRepo.deleteById(id);
            return new ResponseEntity<String>("", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
        }
    }

    // just for check
    @GetMapping(value = "/all", consumes = {"application/json", "application/xml"}, produces = {"application/json",
            "application/xml"})
    public Iterable<Song> getAll() {
        return songRepo.findAll();
    }
}
