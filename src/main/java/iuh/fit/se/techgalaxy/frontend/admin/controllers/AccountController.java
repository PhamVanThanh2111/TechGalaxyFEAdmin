package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.AccountUpdateRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.*;
import iuh.fit.se.techgalaxy.frontend.admin.services.AccountService;
import iuh.fit.se.techgalaxy.frontend.admin.services.RoleService;
import iuh.fit.se.techgalaxy.frontend.admin.services.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

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
    public ModelAndView showAll(ModelAndView model) {
        model.addObject("accounts", accountService.findAll().getData());
        model.setViewName("html/Account/showAccount");
        return model;
    }

    @GetMapping("/update/{id}")
    public ModelAndView showUpdateForm(ModelAndView model, @PathVariable String id) {
        AccountResponse account = ((List<AccountResponse>) accountService.findById(id).getData()).get(0);
        model.addObject("account", account);
        model.setViewName("html/Account/formAccount");
        return model;
    }

    @GetMapping("/delete/{email}")
    public ModelAndView delete(@PathVariable String email, ModelAndView model) {
        SystemUserResponseDTO user = ((List<SystemUserResponseDTO>) systemUserService.findByEmail(email).getData()).get(0);
        systemUserService.delete(user.getId());
        model.setViewName("redirect:/accounts");
        return model;
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(ModelAndView model, @PathVariable String id) {
        AccountResponse account = ((List<AccountResponse>) accountService.findById(id).getData()).get(0);
        SystemUserResponseDTO user = ((List<SystemUserResponseDTO>) systemUserService.findByEmail(account.getEmail()).getData()).get(0);
        List<RoleResponse> roles = (List<RoleResponse>) account.getRolesIds().stream().map(roleService::findById).findFirst().get().getData();

        model.addObject("account", account);
        model.addObject("user", user);
        model.addObject("roles", roles);
        model.setViewName("html/Account/detailAccount");
        return model;
    }

    @PostMapping("/save")
    public ModelAndView update(@ModelAttribute("account") AccountResponse account, @RequestParam(name = "roles", required = false) List<String> rolesIds, ModelAndView model) {
        if (rolesIds == null) {
            rolesIds = List.of();
        }
        account.setRolesIds(rolesIds);
        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest();
        accountUpdateRequest.setId(account.getId());
        accountUpdateRequest.setRolesIds(account.getRolesIds());
        accountUpdateRequest.setEmail(account.getEmail());
        accountUpdateRequest.setPassword(account.getPassword());

        DataResponse<AccountUpdateResponse> result = null;
        if (account.getPassword().isEmpty()) {
            System.out.println("Update without password");
            result = accountService.updateWithoutPassword(accountUpdateRequest);
        } else {
            System.out.println("Update with password");
            result = accountService.update(accountUpdateRequest);
        }

        if (result == null) {
            model.setViewName("html/Account/formAccount");
            return model;
        }
        model.setViewName("redirect:/accounts");
        return model;
    }
}
