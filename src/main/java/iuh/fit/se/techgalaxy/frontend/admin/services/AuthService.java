package iuh.fit.se.techgalaxy.frontend.admin.services;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthService {
    public ResponseEntity<Map> login(String username, String password, HttpSession session, HttpServletResponse response);

    void logout(HttpSession session, String accessToken, HttpServletResponse response);
}
