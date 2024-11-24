package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import com.google.gson.*;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.*;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Color;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Memory;
import iuh.fit.se.techgalaxy.frontend.admin.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;
    private final MemoryService memoryService;
    private final ColorService colorService;

    @Autowired
    public OrderController(OrderService orderService, OrderDetailService orderDetailService, ProductService productService, MemoryService memoryService, ColorService colorService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
        this.memoryService = memoryService;
        this.colorService = colorService;
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
        List<ProductVariantResponse> productVariants = (List<ProductVariantResponse>) productService.getAllVariants().getData();
        productVariants.forEach(productVariant -> {
            List<ProductVariantDetailResponse> productVariantDetails = (List<ProductVariantDetailResponse>) productService.getAllVariantDetailsByVariantId(productVariant.getId()).getData();
            productVariant.setProductVariantDetails(productVariantDetails);
        });

        List<Memory> memories = (List<Memory>) memoryService.getAllMemories().getData();
        List<Color> colors = (List<Color>) colorService.getAllColors().getData();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        System.out.println(gson.toJson(productVariants));
        System.out.println(gson.toJson(memories));
        System.out.println(gson.toJson(colors));
        model.addObject("productVariants", gson.toJson(productVariants));
        model.addObject("memories", gson.toJson(memories));
        model.addObject("colors", gson.toJson(colors));
        model.setViewName("html/Order/formOrder");
        return model;
    }

    public static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.format(formatter));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), formatter);
        }
    }
}
