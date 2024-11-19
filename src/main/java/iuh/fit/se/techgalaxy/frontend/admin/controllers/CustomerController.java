package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.CustomerRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.CustomerResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.mapper.CustomerMapper;
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
        model.setViewName("html/Customer/showCustomer");
        model.addObject("customers", customers);
        return model;
    }

    @GetMapping("/add")
    public ModelAndView showForm(ModelAndView model) {
        CustomerRequest customerRequest = new CustomerRequest();
        model.setViewName("html/Customer/formCustomer");
        model.addObject("customerRequest", customerRequest);
        return model;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("customerRequest") CustomerRequest customerRequest, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        if (bindingResult.hasErrors()) {
            model.setViewName("html/Customer/formCustomer");
            return model;
        }
        if (customerRequest.getId() == null || customerRequest.getId().isEmpty()) { // add new customer
            customerService.save(customerRequest);
        } else { // update customer
            customerService.update(customerRequest);
        }
        model.setViewName("redirect:/customers");
        return model;
    }

    @GetMapping("/update/{id}")
    public ModelAndView showFormUpdate(@PathVariable String id, ModelAndView model) {
        List<CustomerResponse> list = (List<CustomerResponse>) customerService.findById(id).getData();
        CustomerRequest customerRequest = CustomerMapper.INSTANCE.toCustomerRequest(CustomerMapper.INSTANCE.toCustomerFromResponse(list.get(0)));
        if (customerRequest.getDateOfBirth() != null)
            model.addObject("dateOfBirth", customerRequest.getDateOfBirth().toString());
        model.setViewName("html/Customer/formCustomer");
        model.addObject("customerRequest", customerRequest);
        return model;
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detailCustomer(@PathVariable String id, ModelAndView model) {
        List<CustomerResponse> list = (List<CustomerResponse>) customerService.findById(id).getData();
        CustomerRequest customerRequest = CustomerMapper.INSTANCE.toCustomerRequest(CustomerMapper.INSTANCE.toCustomerFromResponse(list.get(0)));
        model.addObject("customerRequest", customerRequest);
        model.setViewName("html/Customer/detailCustomer");

        return model;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteCustomer(@PathVariable String id, ModelAndView model) {
        customerService.delete(id);
        model.setViewName("redirect:/customers");
        return model;
    }
}
