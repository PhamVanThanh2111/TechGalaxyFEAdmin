package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.AccountUpdateRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.*;
import iuh.fit.se.techgalaxy.frontend.admin.services.AccountService;
import iuh.fit.se.techgalaxy.frontend.admin.services.RoleService;
import iuh.fit.se.techgalaxy.frontend.admin.services.SystemUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final SystemUserService systemUserService;
    private final RoleService roleService;

    @Autowired
    public AccountController(AccountService accountService, SystemUserService systemUserService, RoleService roleService) {
        this.accountService = accountService;
        this.systemUserService = systemUserService;
        this.roleService = roleService;
    }

    @GetMapping
    public ModelAndView showAll(ModelAndView model, HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            model.addObject("accounts", accountService.findAll(accessToken).getData());
            model.setViewName("html/Account/showAccount");
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

    @GetMapping("/update/{id}")
    public ModelAndView showUpdateForm(ModelAndView model,
                                       @PathVariable String id,
                                       HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            AccountResponse account = ((List<AccountResponse>) accountService.findById(id, accessToken).getData()).get(0);
            List<RoleResponse> roles = (List<RoleResponse>) roleService.findAll(accessToken).getData();
            String accountRoles = "";
            if (!account.getRolesIds().isEmpty())
                accountRoles = String.join(",", account.getRolesIds());
            model.addObject("accountRoles", accountRoles);
            model.addObject("roles", roles);
            model.addObject("account", account);
            model.setViewName("html/Account/formAccount");
            return model;
        } catch (
                HttpClientErrorException.Unauthorized e) {
            model.setViewName("redirect:/home");
            return model;
        } catch (HttpClientErrorException.Forbidden e) {
            model.setViewName("redirect:/home");
            return model;
        } catch (Exception e) {
            model.setViewName("redirect:/home");
            e.printStackTrace();
            return model;
        }
    }

    @GetMapping("/delete/{email}")
    public ModelAndView delete(@PathVariable String email,
                               ModelAndView model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            SystemUserResponseDTO user = ((List<SystemUserResponseDTO>) systemUserService.findByEmail(email, accessToken).getData()).get(0);
            systemUserService.delete(user.getId(), accessToken);
            accountService.deleteAccount(user.getAccount().getId(), accessToken);

            redirectAttributes.addFlashAttribute("successMessage", "Account deleted successfully");
            model.setViewName("redirect:/accounts");
            return model;
        } catch (
                HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Account deleted failed");
            return model;
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Account deleted failed");
            return model;
        } catch (Exception e) {
            model.setViewName("redirect:/home");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Account deleted failed");
            return model;
        }
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(ModelAndView model,
                               @PathVariable String id,
                               HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            AccountResponse account = ((List<AccountResponse>) accountService.findById(id, accessToken).getData()).get(0);
            account.setPassword(null);
            SystemUserResponseDTO user = ((List<SystemUserResponseDTO>) systemUserService.findByEmail(account.getEmail(), accessToken).getData()).get(0);
            List<RoleResponse> roles = null;
            if (!account.getRolesIds().isEmpty()) {
                roles = (List<RoleResponse>) account.getRolesIds().stream().map(roleId -> roleService.findById(roleId, accessToken)).findFirst().get().getData();
            }

            model.addObject("account", account);
            model.addObject("user", user);
            model.addObject("roles", roles);
            model.setViewName("html/Account/detailAccount");
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
    public ModelAndView update(@ModelAttribute("account") AccountResponse account,
                               @RequestParam(name = "roles", required = false) List<String> rolesIds,
                               ModelAndView model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            if (rolesIds == null) {
                rolesIds = List.of();
            }
            account.setRolesIds(rolesIds);
            AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest();
            accountUpdateRequest.setId(account.getId());
            accountUpdateRequest.setRolesIds(account.getRolesIds());
            accountUpdateRequest.setEmail(account.getEmail());
            accountUpdateRequest.setPassword(account.getPassword());

            DataResponse<AccountUpdateResponse> result;
            if (account.getPassword().isEmpty()) {
                System.out.println("Update without password");
                result = accountService.updateWithoutPassword(accountUpdateRequest, accessToken);
            } else {
                System.out.println("Update with password");
                result = accountService.update(accountUpdateRequest, accessToken);
            }

            if (result == null) {
                model.setViewName("html/Account/formAccount");
                redirectAttributes.addFlashAttribute("errorMessage", "Account updated failed");
                return model;
            }
            else {
                redirectAttributes.addFlashAttribute("successMessage", "Account updated successfully");
            }
            model.setViewName("redirect:/accounts");
            return model;
        } catch (
                HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Account updated failed");
            return model;
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Account updated failed");
            return model;
        } catch (Exception e) {
            model.setViewName("redirect:/home");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Account updated failed");
            return model;
        }
    }
}
