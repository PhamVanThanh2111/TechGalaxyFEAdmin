package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.LoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Controller
public class LoginController {
    private final RestClient restClient;

    public LoginController(RestClient restClient) {
        this.restClient = restClient;
    }
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("errorMessage", null);

        return "html/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Model model) {
        try {
            // Payload chứa thông tin username và password
            Map<String, String> payload = Map.of("username", username, "password", password);

            // Gửi yêu cầu tới BE để đăng nhập
            ResponseEntity<Map> loginResponse = restClient.post()
                    .uri("http://localhost:8081/api/accounts/auth/login")
                    .contentType(APPLICATION_JSON)
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
                    System.out.println("Access Token in Login Controller: " + accessToken);

                    // Kiểm tra cookie Set-Cookie trong header response
                    HttpHeaders headers = loginResponse.getHeaders();
                    List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);

                    if (cookies != null) {
                        cookies.forEach(cookie -> {
                            System.out.println("Cookie từ BE: " + cookie);
                        });
                        response.addHeader(HttpHeaders.SET_COOKIE, cookies.get(0));
                    } else {
                        System.out.println("Không có cookie `Set-Cookie` từ BE.");
                    }

                    return "redirect:/home";
                } else {
                    model.addAttribute("errorMessage", "Invalid username or password.");
                    return "html/login";
                }
            } else {
                model.addAttribute("errorMessage", "Login failed: Invalid response from server.");
                return "html/login";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Login failed: " + e.getMessage());
            return "html/login";
        }
    }




    @PostMapping("/logout")
    public String logout(HttpSession session, Model model) {
        try {
            restClient.post()
                    .uri("http://localhost:8081/api/accounts/auth/logout")
                    .retrieve()
                    .toBodilessEntity();

            session.invalidate();

            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Logout failed: " + e.getMessage());
            return "redirect:/login";
        }
    }
    @GetMapping("/refresh-token")
    public String refreshToken(HttpSession session, Model model) {
        try {
            // Gửi yêu cầu làm mới token
            Map<String, Object> response = restClient.post()
                    .uri("http://localhost:8081/api/accounts/auth/refresh-token")
                    .retrieve()
                    .body(Map.class);

            // Lấy access_token mới
            if (response.containsKey("data")) {
                Map<?, ?> data = (Map<?, ?>) ((List<?>) response.get("data")).get(0);
                String newAccessToken = (String) data.get("access_token");

                // Lưu accessToken mới vào session
                session.setAttribute("accessToken", newAccessToken);
            }
            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Session expired. Please login again.");
            return "redirect:/login";
        }
    }


}
