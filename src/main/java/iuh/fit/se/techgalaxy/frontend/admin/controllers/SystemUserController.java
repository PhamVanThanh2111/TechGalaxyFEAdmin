package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.SystemUserRequestDTO;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.SystemUserResponseDTO;
import iuh.fit.se.techgalaxy.frontend.admin.services.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/userSystem")
public class SystemUserController {
    private final SystemUserService systemUserService;

    @Autowired
    public SystemUserController(SystemUserService systemUserService) {
        this.systemUserService = systemUserService;
    }

    @GetMapping
    public ModelAndView showUsersSystem(ModelAndView model) {
        DataResponse<SystemUserResponseDTO> response = systemUserService.findAll();
        List<SystemUserResponseDTO> list = null;
        if (response != null) {
            list = (List<SystemUserResponseDTO>) response.getData();
        }
        model.addObject("sys_users", list);
        model.setViewName("html/SystemUser/showSystemUser");
        return model;
    }

    @GetMapping("/add")
    public ModelAndView add(ModelAndView model) {
        return model;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("systemUserRequestDTO") SystemUserRequestDTO request, BindingResult bindingResult, ModelAndView model) {
        return model;
    }

    @GetMapping("/update/{id}")
    public ModelAndView formUpdate(ModelAndView model, @PathVariable String id) {
//        model.setViewName("html/SystemUser/formUpdate");
        return model;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(ModelAndView model, @PathVariable String id) {
//        systemUserService.delete(id);
//        model.setViewName("redirect:/userSystem");
        return model;
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(ModelAndView model, @PathVariable String id) {

        return model;
    }
}
