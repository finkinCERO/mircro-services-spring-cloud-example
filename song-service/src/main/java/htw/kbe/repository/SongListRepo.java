package htw.kbe.repository;


import java.util.List;

import htw.kbe.model.SongList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SongListRepo extends JpaRepository<SongList, Integer> {

    //	@Query("from DAOUser u inner join fetch songList where u.username = :owner")
    // (songList -> Owner --> SongListen
//	@Query("from SongList s inner join DAOUser u fetch s.owner where s.owner = :username")
    //@Query("select s from SongList where s.users = :owner")
    //Query("Select a from SongList a inner join Users b on owner=b.username")
//	@Query("from SongList s inner join fetch ")
    List<SongList> findByOwner(String user);



}