package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.OrderResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ModelAndView showOrders(ModelAndView model) {
        DataResponse<OrderResponse> response = orderService.getAll();
        List<OrderResponse> orders = null;
        if (response != null) {
            orders = (List<OrderResponse>) response.getData();
        }
        System.out.println(orders);
        model.setViewName("html/showOrder");
        model.addObject("orders", orders);
        return model;
    }

    @GetMapping("/add")
    public ModelAndView showForm(ModelAndView model) {
        model.setViewName("html/formOrder");
        return model;
    }
}
