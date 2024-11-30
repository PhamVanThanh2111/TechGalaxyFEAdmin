package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.AccountUpdateRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.*;
import iuh.fit.se.techgalaxy.frontend.admin.services.AccountService;
import iuh.fit.se.techgalaxy.frontend.admin.services.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final AuthService authService;
    private final AccountService accountService;

    @Autowired
    public ProfileController(AuthService authService, AccountService accountService) {
        this.authService = authService;
        this.accountService = accountService;
    }

    @GetMapping
    public ModelAndView showProfile(ModelAndView model, HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            SystemUserResponseDTO systemUserLogin = authService.getSystemUserLogin(accessToken);
            AccountResponse accountResponse = ((List<AccountResponse>) accountService.findByEmail(systemUserLogin.getAccount().getEmail(), accessToken).getData()).get(0);
            AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest();
            accountUpdateRequest.setEmail(systemUserLogin.getAccount().getEmail());
            accountUpdateRequest.setId(accountResponse.getId());
            accountUpdateRequest.setRolesIds(systemUserLogin.getAccount().getRoles().stream().map(role -> role.getId()).toList());

            model.addObject("systemUser", systemUserLogin);
            model.addObject("account", accountUpdateRequest);
            model.setViewName("html/Profile/profile");
            return model;
        } catch (
                HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            model.setViewName("redirect:/home");
            return model;
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            model.setViewName("redirect:/home");
            return model;
        } catch (Exception e) {
            model.setViewName("redirect:/home");
            e.printStackTrace();
            return model;
        }
    }

    @PostMapping("/save")
    public ModelAndView savePassword(ModelAndView model,
                                     @ModelAttribute("account") AccountUpdateRequest account,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }

        DataResponse<AccountUpdateResponse> result = null;
        if (!account.getPassword().isEmpty()) {
            result = accountService.update(account, accessToken);
        }
        if (result != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Update password successfully");
        }
        else {
            redirectAttributes.addFlashAttribute("errorMessage", "Update password failed");
        }
        model.setViewName("redirect:/profile");
        return model;
    }
}
