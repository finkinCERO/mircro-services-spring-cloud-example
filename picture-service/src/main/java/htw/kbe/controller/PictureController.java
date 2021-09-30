package htw.kbe.controller;


import htw.kbe.model.PictureObject;
import htw.kbe.service.PictureService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.io.FileUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

@RestController
@RequestMapping(value = "/pictures")
public class PictureController {


    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    PictureService pictureService;


    @Autowired
    public PictureController(final PictureService pictureService, RestTemplate rest) {
        this.pictureService = pictureService;
        this.restTemplate = rest;
    }

    @GetMapping(value = "/", consumes = {"application/json", "application/xml"}, produces = {"application/json",
            "application/xml"})
    public String helloPictures(
            ) {
        try {
            System.out.println("Hello get picture...");
            return "hello world";
        } catch (Exception e) {
            return "exception hello";
        }
    }

    @GetMapping(value = "/{id}", consumes = {"application/json", "application/xml"}, produces = {"application/json",
            "application/xml"})
    public ResponseEntity<PictureObject> getPicture(
            @PathVariable(value = "id") String id) {
        try {
            System.out.println("Hello get picture...");
            PictureObject pic = pictureService.getPicture(id);
            return new ResponseEntity<PictureObject>(pic, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<PictureObject>(new PictureObject(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/{title}")
    public ResponseEntity<String> verifyUser(@PathVariable(value = "title") String title, @RequestBody String _url, @RequestHeader("Authorization") String token) {
        try {
            System.out.println("Hello POST User verify");
            String user = restTemplate.getForObject("http://auth-service/auth/" + token, String.class);
            URL url = new URL(_url);
            //String path = tDir + "tmp" + ".jpg";
            File file = new File("tmp" + new Date().getTime() + ".jpg");
            FileUtils.copyURLToFile(url, file);
            file.deleteOnExit();
            String id = pictureService.addPicture(title, user, file);
            return new ResponseEntity<String>(id, HttpStatus.ACCEPTED);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
        }
        //create File object
        // save
    }

    @DeleteMapping(value = "/{id}", consumes = {"application/json", "application/xml"}, produces = {"text/plain"})
    public ResponseEntity<String> deleteImage(@PathVariable(value = "id") String id, @RequestHeader("Authorization") String token) throws IOException {

        try {

            System.out.println("Hello Delete...");
            PictureObject pic = pictureService.getPicture(id);
            String user = restTemplate.getForObject("http://auth-service/auth/" + token, String.class);
            System.out.println("user: " + user + " owner:" + pic.getOwner());
            if (pic.getOwner().equals(user)) {
                pictureService.delete(pic);
                return new ResponseEntity<String>("", HttpStatus.ACCEPTED);
            } else return new ResponseEntity<String>("", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{name}", consumes = {"application/json", "application/xml"}, produces = {"application/json",
            "application/xml"})
    public ResponseEntity<PictureObject> getUser(@RequestBody PictureObject picture,
                                                 @PathVariable(value = "name") String name, @RequestHeader("Authorization") String token) {
        // get pictureobject by name...
        try {
            System.out.println("Hello Put... " + picture.getTitle());
            PictureObject pic = pictureService.getPicture(name);
            String user = restTemplate.getForObject("http://auth-service/auth/" + token, String.class);
            System.out.println("owner: " + pic.getOwner() + " user: " + user);
            if (pic.getOwner().equals(user)) {
                return new ResponseEntity<PictureObject>(pictureService.update(picture), HttpStatus.ACCEPTED);
            } else return new ResponseEntity<PictureObject>(pic, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<PictureObject>(new PictureObject(), HttpStatus.NOT_FOUND);
        }
    }

}