package htw.kbe.repository;


import java.util.List;

import htw.kbe.model.SongList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongListRepo extends JpaRepository<SongList, Integer> {


    List<SongList> findByOwner(String user);



}