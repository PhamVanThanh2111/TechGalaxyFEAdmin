package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.LoginResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.impl.AuthServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Controller
public class LoginController {
    private final RestClient restClient;

    private final AuthServiceImpl authService;

    @Autowired
    public LoginController(RestClient restClient, AuthServiceImpl authService) {
        this.restClient = restClient;
        this.authService = authService;
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
                        HttpServletResponse response,
                        Model model,
                        RedirectAttributes redirectAttributes
                        ) {
        ResponseEntity<Map> loginResponse = authService.login(username, password, session, response);

        if (loginResponse.getStatusCode().is2xxSuccessful()) {
            redirectAttributes.addFlashAttribute("successMessage", "Login successfully!");

            return "redirect:/home";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Login failed!");
            model.addAttribute("errorMessage", "Login failed!");
            return "redirect:/login";
        }
    }



    @PostMapping("/logout")
    public ModelAndView logout(HttpSession session, HttpServletResponse response) {
        System.out.println(">>> LOGOUT");
        String accessToken = (String) session.getAttribute("accessToken");

        if (accessToken != null) {
            authService.logout(session, accessToken, response);
        }

        return new ModelAndView("redirect:/login");
    }

}
