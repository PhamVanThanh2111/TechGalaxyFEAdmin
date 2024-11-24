package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/", "/home"})
public class HomeController {
    @GetMapping
    public ModelAndView showHome(HttpSession session, ModelAndView model) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return new ModelAndView("redirect:/login");
        }
        model.setViewName("index");
        return model;
    }
}
