package htw.kbe.repository;

import htw.kbe.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepo extends JpaRepository<Song, Integer> {

//	Song findBySongId(Integer songId);

}
