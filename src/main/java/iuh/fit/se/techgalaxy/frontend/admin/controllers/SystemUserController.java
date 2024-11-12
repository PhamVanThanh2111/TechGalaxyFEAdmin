package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/userSystem")
public class SystemUserController {
    @GetMapping
    public ModelAndView showUsersSystem(ModelAndView model) {
        model.setViewName("html/showUserSystem");
        return model;
    }
}
