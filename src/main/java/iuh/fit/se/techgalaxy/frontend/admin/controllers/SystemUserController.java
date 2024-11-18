package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.SystemUserRequestDTO;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.SystemUserResponseDTO;
import iuh.fit.se.techgalaxy.frontend.admin.services.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/systemUsers")
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
        SystemUserRequestDTO requestDTO = new SystemUserRequestDTO();
        model.setViewName("html/SystemUser/formSystemUser");
        model.addObject("systemUserRequestDTO", requestDTO);
        return model;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("systemUserRequestDTO") SystemUserRequestDTO request, BindingResult bindingResult, ModelAndView model) {
        if (bindingResult.hasErrors()) {
            model.setViewName("html/SystemUser/formSystemUser");
            return model;
        }
        if (request.getId() == null || request.getId().isEmpty()) {
            systemUserService.create(request);
        } else {
            systemUserService.update(request);
        }
        model.setViewName("redirect:/systemUsers");
        return model;
    }

    @GetMapping("/update/{id}")
    public ModelAndView formUpdate(ModelAndView model, @PathVariable String id) {
        List<SystemUserResponseDTO> list = (List<SystemUserResponseDTO>) systemUserService.findById(id).getData();
        model.addObject("systemUserRequestDTO", list.get(0));
//        System.out.println(id);
//        System.out.println( list.get(0).getId());
//        System.out.println( list.get(0).getName());
//        System.out.println( list.get(0).getPhone());
//        System.out.println( list.get(0).getAddress());
//        System.out.println( list.get(0).getSystemUserStatus());
//        System.out.println( list.get(0).getLevel());
//        System.out.println( list.get(0).getGender());
//        System.out.println( list.get(0).getAvatar());
//        System.out.println( list.get(0).getAccount());
//        System.out.println( list.get(0).getAccount().getEmail());
        model.setViewName("html/SystemUser/formSystemUser");
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
