package iuh.fit.se.techgalaxy.frontend.admin.services.impl;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.LoginResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.SystemUserResponseDTO;
import iuh.fit.se.techgalaxy.frontend.admin.services.AuthService;
import iuh.fit.se.techgalaxy.frontend.admin.services.SystemUserService;
import jakarta.servlet.http.Cookie;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    private final RestClient restClient;
    private final SystemUserService systemUserService;

    public AuthServiceImpl(RestClient restClient, SystemUserService systemUserService) {
        this.restClient = restClient;
        this.systemUserService = systemUserService;
    }

    @Override
    public ResponseEntity<Map> login(String username, String password, HttpSession session, HttpServletResponse response) {
        try {
            // Payload chứa thông tin username và password
            Map<String, String> payload = Map.of("username", username, "password", password);

            // Gửi yêu cầu tới BE để đăng nhập
            ResponseEntity<Map> loginResponse = restClient.post()
                    .uri("http://localhost:8081/api/accounts/auth/login")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toEntity(Map.class);

            // Kiểm tra nếu response trả về thành công
            if (loginResponse.getStatusCode() == HttpStatus.OK && loginResponse.getBody() != null) {
                Map<?, ?> responseBody = loginResponse.getBody();

                // Lấy accessToken từ body
                if (responseBody.containsKey("data")) {
                    Map<?, ?> data = (Map<?, ?>) ((List<?>) responseBody.get("data")).get(0);
                    String accessToken = (String) data.get("access_token");

                    // Lưu accessToken vào session
                    session.setAttribute("accessToken", accessToken);
                    System.out.println("Access Token in Service: " + accessToken);

                    // Kiểm tra cookie Set-Cookie trong header response
                    HttpHeaders headers = loginResponse.getHeaders();
                    List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);

                    if (cookies != null && !cookies.isEmpty()) {
                        // Gửi cookie từ BE đến FE
                        cookies.forEach(cookie -> {
                            System.out.println("Cookie từ BE: " + cookie);
                            response.addHeader(HttpHeaders.SET_COOKIE, cookie);
                        });
                    }
                }
                return loginResponse;
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }

    @Override
    public void logout(HttpSession session, String accessToken, HttpServletResponse response) {
        try {
            ResponseEntity<Void> restResponse = restClient.post()
                    .uri("http://localhost:8081/api/accounts/auth/logout")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .toBodilessEntity();

            if (restResponse.getStatusCode().is2xxSuccessful()) {
                System.out.println("Logout successful");
            } else {
                System.out.println("Logout failed with status: " + restResponse.getStatusCode());
            }

        } catch (Exception e) {
            System.out.println("Error during logout: " + e.getMessage());
        } finally {
            Cookie refreshTokenCookie = new Cookie("refresh_token", null);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(0);
            refreshTokenCookie.setHttpOnly(true);
            response.addCookie(refreshTokenCookie);

            // Invalidate session
            session.invalidate();
        }
    }

    @Override
    public SystemUserResponseDTO getSystemUserLogin(String accessToken) {
        DataResponse<LoginResponse.AccountGetAccount> loginResponse = restClient.get()
                .uri("http://localhost:8081/api/accounts/auth/account")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        LoginResponse.AccountGetAccount account = ((List<LoginResponse.AccountGetAccount>) loginResponse.getData()).get(0);
        return ((List<SystemUserResponseDTO>) systemUserService.findByEmail(account.getAccount().getEmail(), accessToken).getData()).get(0);
    }
}
