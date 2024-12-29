package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.CustomerRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.OrderDetailRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.OrderRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.*;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Color;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Memory;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Order;
import iuh.fit.se.techgalaxy.frontend.admin.entities.OrderDetail;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.CustomerStatus;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.DetailStatus;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.OrderStatus;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.PaymentStatus;
import iuh.fit.se.techgalaxy.frontend.admin.mapper.OrderDetailMapper;
import iuh.fit.se.techgalaxy.frontend.admin.mapper.OrderMapper;
import iuh.fit.se.techgalaxy.frontend.admin.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;
    private final MemoryService memoryService;
    private final ColorService colorService;
    private final AccountService accountService;
    private final CustomerService customerService;
    private final AuthService authService;

    @Autowired
    public OrderController(OrderService orderService, OrderDetailService orderDetailService, ProductService productService, MemoryService memoryService, ColorService colorService, AccountService accountService, CustomerService customerService, AuthService authService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
        this.memoryService = memoryService;
        this.colorService = colorService;
        this.accountService = accountService;
        this.customerService = customerService;
        this.authService = authService;
    }

    @GetMapping
    public ModelAndView showOrders(ModelAndView model, HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            DataResponse<OrderResponse> response = orderService.getAll(accessToken);
            List<OrderResponse> orders = getOrderResponses(response, accessToken);
            model.setViewName("html/Order/showOrder");
            model.addObject("orders", orders);
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
            e.printStackTrace();
            return model;
        }
    }

    private List<OrderResponse> getOrderResponses(DataResponse<OrderResponse> response, String accessToken) {
        List<OrderResponse> orders = null;
        if (response != null) {
            orders = (List<OrderResponse>) response.getData();
            orders.forEach(order -> {
                List<OrderDetailResponse> orderDetails = (List<OrderDetailResponse>) orderDetailService.getOrderDetail(order.getId(), accessToken).getData();
                orderDetails.forEach(orderDetail -> {
                    String productVariantName = ((List<ProductVariantResponse>) productService.findProductVariantByProductVariantDetailId(orderDetail.getProductVariantDetail().getId()).getData()).get(0).getName();
                    orderDetail.setName(productVariantName);
                });
                order.setOrderDetails(orderDetails);
            });
        }
        return orders;
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

        model.addObject("productVariants", gson.toJson(productVariants));
        model.addObject("memories", gson.toJson(memories));
        model.addObject("colors", gson.toJson(colors));
        model.addObject("order", new OrderRequest());
        model.setViewName("html/Order/addOrder");
        return model;
    }

    @PostMapping("/save")
    public ModelAndView saveOrder(@ModelAttribute("order") OrderRequest orderRequest,
                                  @RequestParam("productCount") int productCount,
                                  @RequestParam Map<String, String> params,
                                  @RequestParam("source") String source,
                                  ModelAndView model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            // Customer
            if (accountService.existsByEmail(orderRequest.getCustomer().getAccount().getEmail(), accessToken)) { // co customer trong dbs
                CustomerResponse customerFindByEmail = ((List<CustomerResponse>) customerService.findByEmail(orderRequest.getCustomer().getAccount().getEmail(), accessToken).getData()).get(0);
                orderRequest.setCustomer(customerFindByEmail);
            } else {
                CustomerRequest customerRequest = new CustomerRequest();
                customerRequest.setUserStatus(CustomerStatus.ACTIVE);
                customerRequest.setAccount(orderRequest.getCustomer().getAccount());
                customerRequest.setName("unknown");
                CustomerResponse customerResponse = ((List<CustomerResponse>) customerService.save(customerRequest, accessToken).getData()).get(0);
                orderRequest.setCustomer(customerResponse);
            }

            // SystemUser
            SystemUserResponse systemUser = new SystemUserResponse();
            SystemUserResponseDTO systemUserLogin = authService.getSystemUserLogin(accessToken);
            systemUser.setId(systemUserLogin.getId());
            orderRequest.setSystemUser(systemUser);

            // Save order
            OrderResponse orderResponse;
            if (source.equals("addOrder")) {
                // Address
                String address = orderRequest.getAddress();
                orderRequest.setAddress(address);

                // Payment status
                orderRequest.setPaymentStatus(PaymentStatus.PENDING);

                // Order status
                orderRequest.setOrderStatus(OrderStatus.NEW);
                orderResponse = ((List<OrderResponse>) orderService.create(orderRequest, accessToken).getData()).get(0);
            } else {
                orderResponse = ((List<OrderResponse>) orderService.update(orderRequest, accessToken).getData()).get(0);
            }

            // Save order detail
            OrderDetailRequest orderDetailRequest = new OrderDetailRequest();
            for (int i = 1; i <= productCount; i++) {
                String productVariantId = params.get("productName[" + i + "]");
                int quantity = Integer.parseInt(params.get("quantity[" + i + "]"));
                String memoryId = params.get("memory[" + i + "]");
                String colorId = params.get("color[" + i + "]");
                double price = Double.parseDouble(params.get("price[" + i + "]").replaceAll(",", ""));

                ProductVariantResponse productVariant = new ProductVariantResponse();
                productVariant.setId(productVariantId);

                ProductVariantDetailResponse productVariantDetail = new ProductVariantDetailResponse();
                productVariantDetail.setId(productVariantId);


                OrderDetailResponse orderDetailResponse;
                if (source.equals("addOrder")) {
                    ProductDetailResponse response = ((List<ProductDetailResponse>) productService.findProductVariantDetailByProductVariantAndColorAndMemory(productVariantId, colorId, memoryId).getData()).get(0);
                    productVariantDetail.setId(response.getId());

                    orderDetailRequest.setDetailStatus(DetailStatus.PENDING);
                    orderDetailRequest.setOrder(orderResponse);
                    orderDetailRequest.setProductVariantDetail(productVariantDetail);
                    orderDetailRequest.setQuantity(quantity);
                    orderDetailRequest.setPrice(price / quantity);
                    orderDetailResponse = ((List<OrderDetailResponse>) orderDetailService.createOrderDetail(orderDetailRequest, accessToken).getData()).get(0);
                } else {
                    orderDetailResponse = ((List<OrderDetailResponse>) orderDetailService.getOrderDetailByOrderIdAndProductVariantDetailId(orderResponse.getId(), productVariantDetail.getId(), accessToken).getData()).get(0);
                    orderDetailResponse.setQuantity(quantity);
                    orderDetailResponse.setPrice(price);

                    OrderDetail orderDetailUpdate = OrderDetailMapper.INSTANCE.toOrderDetailFromResponse(orderDetailResponse);
                    orderDetailService.updateOrderDetail(orderDetailResponse.getId(), OrderDetailMapper.INSTANCE.toOrderDetailRequest(orderDetailUpdate), accessToken);
                    System.out.println("update thanh cong");
                }
                if (orderDetailResponse == null) {
                    model.setViewName("html/Order/addOrder");
                    redirectAttributes.addFlashAttribute("errorMessage", "Order save failed");
                    return model;
                }
            }
            model.setViewName("redirect:/orders");
            redirectAttributes.addFlashAttribute("successMessage", "Order save successfully");
            return model;
        } catch (
                HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Order save failed");
            return model;
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Order save failed");
            return model;
        } catch (Exception e) {
            model.setViewName("redirect:/home");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Order save failed");
            return model;
        }
    }

    @GetMapping("/confirm/{id}/{orderStatus}")
    public ModelAndView confirm(@PathVariable String id,
                                @PathVariable String orderStatus,
                                ModelAndView model,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            OrderResponse order = ((List<OrderResponse>) orderService.getById(id, accessToken).getData()).get(0);

            switch (orderStatus) {
                case "NEW" -> order.setOrderStatus(OrderStatus.NEW);
                case "PROCESSING" -> order.setOrderStatus(OrderStatus.PROCESSING);
                case "SHIPPED" -> order.setOrderStatus(OrderStatus.SHIPPED);
                case "DELIVERED" -> order.setOrderStatus(OrderStatus.DELIVERED);
                case "CANCELLED" -> order.setOrderStatus(OrderStatus.CANCELLED);
                case "RETURNED" -> order.setOrderStatus(OrderStatus.RETURNED);
                case "COMPLETED" -> order.setOrderStatus(OrderStatus.COMPLETED);
                case "OUT_FOR_DELIVERY" -> order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
            }

            Order orderUpdate = OrderMapper.INSTANCE.toOrderFromResponse(order);
            orderService.update(OrderMapper.INSTANCE.toOrderRequest(orderUpdate), accessToken);
            model.setViewName("redirect:/orders");
            redirectAttributes.addFlashAttribute("successMessage", "Order updated status successfully");
            return model;
        } catch (
                HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Order updated status failed");
            return model;
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Order updated status failed");
            return model;
        } catch (Exception e) {
            model.setViewName("redirect:/home");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Order updated status failed");
            return model;
        }
    }

    @GetMapping("/update/{id}")
    public ModelAndView showFormUpdateOrder(ModelAndView model,
                                            @PathVariable String id,
                                            HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            List<ProductVariantResponse> productVariants = (List<ProductVariantResponse>) productService.getAllVariants().getData();
            productVariants.forEach(productVariant -> {
                List<ProductVariantDetailResponse> productVariantDetails = (List<ProductVariantDetailResponse>) productService.getAllVariantDetailsByVariantId(productVariant.getId()).getData();
                productVariant.setProductVariantDetails(productVariantDetails);
            });

            List<Memory> memories = (List<Memory>) memoryService.getAllMemories().getData();
            List<Color> colors = (List<Color>) colorService.getAllColors().getData();

            OrderResponse order = ((List<OrderResponse>) orderService.getById(id, accessToken).getData()).get(0);
            order.setId(id);
            List<OrderDetailResponse> orderDetails = (List<OrderDetailResponse>) orderDetailService.getOrderDetail(id, accessToken).getData();
            orderDetails.forEach(orderDetail -> {
                ProductDetailResponse productVariantDetail = ((List<ProductDetailResponse>) productService.getVariantDetailById(orderDetail.getProductVariantDetail().getId()).getData()).get(0);
                String productVariantName = ((List<ProductVariantResponse>) productService.findProductVariantByProductVariantDetailId(orderDetail.getProductVariantDetail().getId()).getData()).get(0).getName();
                orderDetail.setName(productVariantName);
                orderDetail.getProductVariantDetail().setColor(productVariantDetail.getColor());
                orderDetail.getProductVariantDetail().setMemory(productVariantDetail.getMemory());
            });


            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();

            model.addObject("productVariants", gson.toJson(productVariants));
            model.addObject("memories", gson.toJson(memories));
            model.addObject("colors", gson.toJson(colors));
            model.addObject("order", order);
            model.addObject("orderDetails", gson.toJson(orderDetails));
            System.out.println("Order details");
            System.out.println(order.getCustomer().getAccount().getEmail());
            System.out.println(gson.toJson(productVariants));
            System.out.println(gson.toJson(memories));
            System.out.println(gson.toJson(colors));
            System.out.println(gson.toJson(orderDetails));
            model.setViewName("html/Order/updateOrder");
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
            e.printStackTrace();
            return model;
        }
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

    public static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.format(formatter));
            }
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            return LocalDate.parse(in.nextString(), formatter);
        }
    }
}
