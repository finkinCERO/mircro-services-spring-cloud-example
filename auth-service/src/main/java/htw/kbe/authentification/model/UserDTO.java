package htw.kbe.authentification.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDTO {
    private String username;
    private String password;

    private String firstname;
    private String lastname;

}