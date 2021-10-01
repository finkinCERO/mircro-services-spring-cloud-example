package htw.kbe.authentification.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private String accessToken;
    private String refreshToken;
    private String message;
    private boolean error;

    public JwtResponse(){

    }

    public JwtResponse(String accessToken, String refreshToken, String message, boolean error){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = message;
        this.error = error;
    }
    public JwtResponse(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = "success";
        this.error = false;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
