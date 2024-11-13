package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.CustomerRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.CustomerResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.Gender;
import iuh.fit.se.techgalaxy.frontend.admin.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ModelAndView getList(ModelAndView model) {
        DataResponse<CustomerResponse> response = customerService.findAll();

        List<CustomerResponse> customers = null;

        if (response != null) {
            customers = (List<CustomerResponse>) response.getData();
        }
        model.setViewName("html/showCustomer");
        model.addObject("customers", customers);
        return model;
    }

    @GetMapping("/add")
    public ModelAndView showForm(ModelAndView model) {
        CustomerRequest customerRequest = new CustomerRequest();
        model.setViewName("html/addCustomer");
        model.addObject("customerRequest", customerRequest);
        return model;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("customerRequest") CustomerRequest customerRequest, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        if (bindingResult.hasErrors()) {
            model.setViewName("html/addCustomer");
            return model;
        }

        customerService.save(customerRequest);
        model.setViewName("redirect:/customers");
        return model;
    }

    @GetMapping("/update/{id}")
    public ModelAndView showFormUpdate(@PathVariable String id, ModelAndView model) {
        CustomerResponse customerRequest = new CustomerResponse();
        model.setViewName("html/addCustomer");
        model.addObject("customerRequest", customerRequest);
        return model;
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detailCustomer(@PathVariable String id, ModelAndView model) {
        List<CustomerResponse> list = (List<CustomerResponse>) customerService.findById(id).getData();
        CustomerResponse customerRequest = list.get(0);
        model.setViewName("html/detailCustomer"); //
        model.addObject("customerRequest", customerRequest);
        return model;
    }
}
