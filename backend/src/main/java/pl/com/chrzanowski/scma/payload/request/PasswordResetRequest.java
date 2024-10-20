package pl.com.chrzanowski.scma.payload.request;


public class PasswordResetRequest {

    private String email;


    public PasswordResetRequest(String email) {
        this.email = email;
    }

    public PasswordResetRequest() {
    }

    public String getEmail() {
        return email;
    }

    public PasswordResetRequest setEmail(String email) {
        this.email = email;
        return this;
    }
}
