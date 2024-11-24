package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.OrderDetailResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.OrderResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductVariantResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.ProductVariant;
import iuh.fit.se.techgalaxy.frontend.admin.services.OrderDetailService;
import iuh.fit.se.techgalaxy.frontend.admin.services.OrderService;
import iuh.fit.se.techgalaxy.frontend.admin.services.ProductService;
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
    private final OrderDetailService orderDetailService;
    private final ProductService productService;

    @Autowired
    public OrderController(OrderService orderService, OrderDetailService orderDetailService, ProductService productService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
    }

    @GetMapping
    public ModelAndView showOrders(ModelAndView model) {
        DataResponse<OrderResponse> response = orderService.getAll();
        List<OrderResponse> orders = null;
        if (response != null) {
            orders = (List<OrderResponse>) response.getData();
            orders.forEach(order -> {
                List<OrderDetailResponse> orderDetails = (List<OrderDetailResponse>) orderDetailService.getOrderDetail(order.getId()).getData();
                orderDetails.forEach(orderDetail -> {
                    String productVariantName = ((List<ProductVariantResponse>) productService.findProductVariantByProductVariantDetailId(orderDetail.getProductVariantDetail().getId()).getData()).get(0).getName();
                    orderDetail.setName(productVariantName);
                });
                order.setOrderDetails(orderDetails);
            });
        }
        model.setViewName("html/Order/showOrder");
        model.addObject("orders", orders);
        return model;
    }

    @GetMapping("/add")
    public ModelAndView showForm(ModelAndView model) {
        model.setViewName("html/Order/formOrder");
        return model;
    }
}
