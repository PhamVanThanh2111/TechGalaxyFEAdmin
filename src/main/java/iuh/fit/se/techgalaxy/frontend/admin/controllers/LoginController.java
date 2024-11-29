package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.RoleResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.SystemUserResponseDTO;
import iuh.fit.se.techgalaxy.frontend.admin.services.impl.AuthServiceImpl;
import iuh.fit.se.techgalaxy.frontend.admin.services.impl.RoleServiceImpl;
import iuh.fit.se.techgalaxy.frontend.admin.services.impl.SystemUserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class LoginController {
    private final RestClient restClient;

    private final AuthServiceImpl authService;
    private final RoleServiceImpl roleService;

    private final SystemUserServiceImpl systemUserService;

    @Autowired
    public LoginController(RestClient restClient, AuthServiceImpl authService, RoleServiceImpl roleService, SystemUserServiceImpl systemUserService) {
        this.restClient = restClient;
        this.authService = authService;
        this.roleService = roleService;
        this.systemUserService = systemUserService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model,HttpSession session) {
        if (session.getAttribute("accessToken") != null) {
            return "redirect:/home";
        }
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
        try {

            ResponseEntity<Map> loginResponse = authService.login(username, password, session, response);

            if (loginResponse.getStatusCode().is2xxSuccessful()) {
                DataResponse<RoleResponse> roleResponse = roleService.getRoleByEmail(username, (String) session.getAttribute("accessToken"));
                if (roleResponse.getStatus() == 200 && roleResponse.getData() != null) {
                    List<RoleResponse> roles = (List<RoleResponse>) roleResponse.getData();
                    for (RoleResponse role : roles) {
                        if (role.getName().equals("Admin")||role.getName().equals("Employee")) {
                            redirectAttributes.addFlashAttribute("successMessage", "Login successfully!");
                            DataResponse<SystemUserResponseDTO> systemUserResponse = systemUserService.findByEmail(username, (String) session.getAttribute("accessToken"));
//                        DataResponse<SystemUserResponseDTO> systemUserResponse = systemUserService.findByEmail(username);

                            if (systemUserResponse.getStatus() == 200 && systemUserResponse.getData() != null) {
                                List<SystemUserResponseDTO> systemUsers = (List<SystemUserResponseDTO>) systemUserResponse.getData();
                                SystemUserResponseDTO systemUser = systemUsers.get(0);
                                session.setAttribute("username", systemUser.getName());
                                session.setAttribute("profileImage", systemUser.getAvatar());
                            }
                            System.out.println("Login successfully!");
                            return "redirect:/home";
                        }
                    }
                    redirectAttributes.addFlashAttribute("errorMessage", "Login failed!");
                    model.addAttribute("errorMessage", "Login failed!");
                    authService.logout(session, (String) session.getAttribute("accessToken"), response);
                    return "redirect:/login?error=true";
                }
                else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Login failed!");
                    model.addAttribute("errorMessage", "Login failed!");
                    return "redirect:/login?error=true";
                }

            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Login failed!");
                model.addAttribute("errorMessage", "Login failed!");
                return "redirect:/login?error=true";
            }
        }
        catch (HttpClientErrorException e) {
            System.out.println("Client error: " + e.getMessage());
            authService.logout(session, (String) session.getAttribute("accessToken"), response);
            return "redirect:/login?error=true";
        }catch (RestClientException e) {
            System.out.println("Error calling external service: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Service is unavailable!");
            authService.logout(session, (String) session.getAttribute("accessToken"), response);
            return "redirect:/login?error=true";
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            authService.logout(session, (String) session.getAttribute("accessToken"), response);
            return "redirect:/login?error=true";
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