package htw.kbe.repository;

import htw.kbe.model.PictureObject;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PictureRepository extends MongoRepository<PictureObject, String> { }
