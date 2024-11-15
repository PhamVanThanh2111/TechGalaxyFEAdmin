package iuh.fit.se.techgalaxy.frontend.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "USERNAME_NOT_EMPTY")
    private String username;

    @NotBlank(message = "PASSWORD_NOT_EMPTY")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
