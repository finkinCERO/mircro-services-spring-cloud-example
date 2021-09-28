package htw.kbe.authentification.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
public class UserDTO {
    private String username;
    private String password;

    private String firstname;
    private String lastname;

    public UserDTO(String username, String password, String firstname, String lastname){
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    public UserDTO(){}

}