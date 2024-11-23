package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.AccountUpdateRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.AccountResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.AccountUpdateResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.SystemUserResponseDTO;
import iuh.fit.se.techgalaxy.frontend.admin.services.AccountService;
import iuh.fit.se.techgalaxy.frontend.admin.services.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final SystemUserService systemUserService;

    @Autowired
    public AccountController(AccountService accountService, SystemUserService systemUserService) {
        this.accountService = accountService;
        this.systemUserService = systemUserService;
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
        DataResponse<AccountUpdateResponse> result = accountService.update(accountUpdateRequest);
        if (result == null) {
            model.setViewName("html/Account/formAccount");
            return model;
        }
        model.setViewName("redirect:/accounts");
        return model;
    }

    @GetMapping("/delete/{email}")
    public ModelAndView delete(@PathVariable String email, ModelAndView model) {
        SystemUserResponseDTO user = ((List<SystemUserResponseDTO>) systemUserService.findByEmail(email).getData()).get(0);
        systemUserService.delete(user.getId());
        model.setViewName("redirect:/accounts");
        return model;
    }
}
