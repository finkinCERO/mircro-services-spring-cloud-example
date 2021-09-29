package htw.kbe.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
    private String token;
    public Token(String token){this.token = token;}

    public String getToken(){
        return this.token;
    }
    public void setToken(String token){
        this.token = token;
    }
}
