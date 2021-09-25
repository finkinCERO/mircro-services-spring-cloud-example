package htw.kbe.authentification.repository;
import htw.kbe.authentification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    //	Optional<DAOUser> findByUsername(String username);
    User findByUsername(String username);
}