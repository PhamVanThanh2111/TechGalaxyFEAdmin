package iuh.fit.se.techgalaxy.frontend.admin.services;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.SystemUserResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthService {
    ResponseEntity<Map> login(String username, String password, HttpSession session, HttpServletResponse response);

    void logout(HttpSession session, String accessToken, HttpServletResponse response);

    SystemUserResponseDTO getSystemUserLogin(String accessToken);
}
