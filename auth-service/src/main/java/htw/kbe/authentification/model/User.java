package htw.kbe.authentification.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

@Entity
@Table(name="user")
public class User {
    @Id
    @Column(name = "username", length = 50, nullable = false)
    private String username;
    @Column(length = 50, nullable = true)
    @JsonIgnore
    private String firstname;
    @Column(length = 50, nullable = true)
    @JsonIgnore
    private String lastname;
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    public String getUsername(){ return this.username; }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}