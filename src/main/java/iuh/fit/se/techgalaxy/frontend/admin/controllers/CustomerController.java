package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.CustomerRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.CustomerResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.UploadFileResponse;
import iuh.fit.se.techgalaxy.frontend.admin.mapper.CustomerMapper;
import iuh.fit.se.techgalaxy.frontend.admin.services.CustomerService;
import iuh.fit.se.techgalaxy.frontend.admin.services.FileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final FileService fileService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("avatar"); // Bỏ qua field avatar
    }

    @Autowired
    public CustomerController(CustomerService customerService, FileService fileService) {
        this.customerService = customerService;
        this.fileService = fileService;
    }

    @GetMapping
    public ModelAndView getList(ModelAndView model, HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            DataResponse<CustomerResponse> response = customerService.findAll(accessToken);

            List<CustomerResponse> customers = null;

            if (response != null) {
                customers = (List<CustomerResponse>) response.getData();
            }
            model.setViewName("html/Customer/showCustomer");
            model.addObject("customers", customers);
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
            return model;
        }
    }

    @GetMapping("/add")
    public ModelAndView showForm(ModelAndView model) {
        CustomerRequest customerRequest = new CustomerRequest();
        model.setViewName("html/Customer/formCustomer");
        model.addObject("customerRequest", customerRequest);
        return model;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("customerRequest") CustomerRequest customerRequest,
                             @RequestParam("avatar") MultipartFile avatar,
                             BindingResult bindingResult,
                             ModelAndView model,
                             HttpSession session) throws IOException, URISyntaxException {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            if (bindingResult.hasErrors()) {
                model.setViewName("html/Customer/formCustomer");
                return model;
            }
            if (!avatar.isEmpty()) {
                DataResponse<UploadFileResponse> response = fileService.uploadFile(avatar, "customer/avatar", accessToken);
                UploadFileResponse uploadFileResponse = ((List<UploadFileResponse>) response.getData()).get(0);
                System.out.println(uploadFileResponse.getFileName());
                customerRequest.setAvatar(uploadFileResponse.getFileName());
            }
            if (customerRequest.getId() == null || customerRequest.getId().isEmpty()) { // add new customer
                customerService.save(customerRequest, accessToken);
            } else { // update customer
                customerService.update(customerRequest, accessToken);
            }
            model.setViewName("redirect:/customers");
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
            return model;
        }
    }

    @GetMapping("/update/{id}")
    public ModelAndView showFormUpdate(@PathVariable String id,
                                       ModelAndView model,
                                       HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            List<CustomerResponse> list = (List<CustomerResponse>) customerService.findById(id, accessToken).getData();
            CustomerRequest customerRequest = CustomerMapper.INSTANCE.toCustomerRequest(CustomerMapper.INSTANCE.toCustomerFromResponse(list.get(0)));
            if (customerRequest.getDateOfBirth() != null)
                model.addObject("dateOfBirth", customerRequest.getDateOfBirth().toString());
            model.setViewName("html/Customer/formCustomer");
            model.addObject("customerRequest", customerRequest);
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
            return model;
        }
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detailCustomer(@PathVariable String id,
                                       ModelAndView model,
                                       HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            List<CustomerResponse> list = (List<CustomerResponse>) customerService.findById(id, accessToken).getData();
            CustomerRequest customerRequest = CustomerMapper.INSTANCE.toCustomerRequest(CustomerMapper.INSTANCE.toCustomerFromResponse(list.get(0)));
            model.addObject("customerRequest", customerRequest);
            model.setViewName("html/Customer/detailCustomer");

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
            return model;
        }
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteCustomer(@PathVariable String id,
                                       ModelAndView model,
                                       HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            customerService.delete(id, accessToken);
            model.setViewName("redirect:/customers");
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
            return model;
        }
    }
}
