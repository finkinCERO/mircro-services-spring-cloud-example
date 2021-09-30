package htw.kbe.service;

import htw.kbe.model.PictureObject;
import htw.kbe.repository.PictureRepository;
import org.apache.commons.io.FileUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Service
public class PictureService {

    @Autowired
    private PictureRepository pictureRepo;

    public String addPicture(String title, String owner, File file) throws IOException {


        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        PictureObject pic = new PictureObject();
        pic.setTitle(title);
        pic.setImage(
                encodedString);

        //get username from token...
        pic.setOwner(owner);


        pic = pictureRepo.insert(pic);
        return pic.getId().toString();
    }

    public PictureObject getPicture(String id) {
        return pictureRepo.findById(id).get();
    }
    public String delete(PictureObject pic) {
        pictureRepo.delete(pic);
        return "ok";
    }
    public PictureObject update(PictureObject pic) {
        return pictureRepo.save(pic);

    }
}