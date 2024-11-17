package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductFullRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.ProductVariantResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.ProductStatus;
import iuh.fit.se.techgalaxy.frontend.admin.services.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final TrademarkImpl trademarkService;
    private final ColorServiceImpl colorService;
    private final MemoryServiceImpl memoryService;
    private final UsageCategoryServiceImpl usageCategoryService;

    private final ProductServiceImpl productService;
    @Autowired
    public ProductController(TrademarkImpl trademarkService, ColorServiceImpl colorService, MemoryServiceImpl memoryService, UsageCategoryServiceImpl usageCategoryService, ProductServiceImpl productService) {
        this.trademarkService = trademarkService;
        this.colorService = colorService;
        this.memoryService = memoryService;
        this.usageCategoryService = usageCategoryService;
        this.productService = productService;
    }

    @GetMapping("/add")
    public ModelAndView showAddProductForm() {
        ModelAndView modelAndView = new ModelAndView("html/formPhone");
        modelAndView.addObject("productFullRequest", new ProductFullRequest());
        modelAndView.addObject("trademarks", trademarkService.getAllTrademarks().getData());
        modelAndView.addObject("colors", colorService.getAllColors().getData());
        modelAndView.addObject("memories", memoryService.getAllMemories().getData());
        modelAndView.addObject("usageCategories", usageCategoryService.getAllUsageCategories().getData());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView saveFullProduct(@Validated @ModelAttribute("productFullRequest") ProductFullRequest productFullRequest) {
        // In toàn bộ dữ liệu nhận được từ form
        System.out.println("Dữ liệu sản phẩm:");
        System.out.println(productFullRequest);

        ProductRequest productRequest = new ProductRequest();
        productRequest.setName(productFullRequest.getName());
        productRequest.setTrademarkId(productFullRequest.getTrademarkId());

        // Lưu thông tin sản phẩm
        DataResponse<ProductResponse> productResponseDataResponse= productService.createProduct(productRequest);
        System.out.println("Dữ liệu sản phẩm sau khi lưu:");
        List<ProductResponse> productResponses = ( List<ProductResponse>) productResponseDataResponse.getData();
        String productId = productResponses.get(0).getId();


        // Luu thong tin variant
        productFullRequest.getVariants().forEach(
                variantRequest -> {

                    ProductVariantRequest productVariantRequest = new ProductVariantRequest();
                    productVariantRequest.setName(variantRequest.getName());
                    productVariantRequest.setUsageCategoryId(variantRequest.getUsageCategoryId());
                    productVariantRequest.setDescription(variantRequest.getDescription());
                    productVariantRequest.setContent(variantRequest.getContent());
                    productVariantRequest.setAvatar(variantRequest.getAvatar());
                    productVariantRequest.setFeatured(true);
                    productVariantRequest.setStatus(ProductStatus.AVAILABLE);

                    System.out.println("Dữ liệu variant:");
                    System.out.println(productVariantRequest);

                    DataResponse<ProductVariantResponse> productVariantResponseDataResponse = productService.createVariant(productId, productVariantRequest);
                    System.out.println("Dữ liệu variant sau khi lưu:");
                    List<ProductVariantResponse> productVariantResponses = (List<ProductVariantResponse>) productVariantResponseDataResponse.getData();
                    String variantId = productVariantResponses.get(0).getId();
                    System.out.println("variantId: " + variantId);
                }
        );



        // Trả về form để tiếp tục nhập liệu hoặc thêm thông tin
        ModelAndView modelAndView = new ModelAndView("html/formPhone");
        modelAndView.addObject("trademarks", trademarkService.getAllTrademarks().getData());
        modelAndView.addObject("colors", colorService.getAllColors().getData());
        modelAndView.addObject("memories", memoryService.getAllMemories().getData());
        modelAndView.addObject("usageCategories", usageCategoryService.getAllUsageCategories().getData());
        return modelAndView;
    }
}
