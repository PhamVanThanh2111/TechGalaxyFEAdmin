package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductFullRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantDetailRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.*;
import iuh.fit.se.techgalaxy.frontend.admin.entities.ProductVariantDetail;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.ProductStatus;
import iuh.fit.se.techgalaxy.frontend.admin.services.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
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

    private final FileServiceImpl fileService;

    private final ProductServiceImpl productService;

    @Autowired
    public ProductController(TrademarkImpl trademarkService, ColorServiceImpl colorService, MemoryServiceImpl memoryService, UsageCategoryServiceImpl usageCategoryService, ProductServiceImpl productService, FileServiceImpl fileService) {
        this.trademarkService = trademarkService;
        this.colorService = colorService;
        this.memoryService = memoryService;
        this.usageCategoryService = usageCategoryService;
        this.productService = productService;
        this.fileService = fileService;
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
        DataResponse<ProductResponse> productResponseDataResponse = productService.createProduct(productRequest);

        if (productResponseDataResponse.getStatus() != 200) {
            System.out.println("Lỗi khi lưu sản phẩm.");
        }

        if (productResponseDataResponse.getStatus() == 200 && productResponseDataResponse.getData() == null) {
            System.out.println("Dữ liệu sản phẩm bị null.");
        } else {
            System.out.println("Dữ liệu sản phẩm sau khi lưu:");
            List<ProductResponse> productResponses = (List<ProductResponse>) productResponseDataResponse.getData();
            String productId = productResponses.get(0).getId();

            if (productFullRequest.getVariants() == null || productFullRequest.getVariants().isEmpty()) {
                System.out.println("Không có variant nào được tạo.");
            } else {
                // Luu thong tin variant
                productFullRequest.getVariants().forEach(
                        variantRequest -> {

                            ProductVariantRequest productVariantRequest = new ProductVariantRequest();
                            productVariantRequest.setName(variantRequest.getName());
                            productVariantRequest.setUsageCategoryId(variantRequest.getUsageCategoryId());
                            productVariantRequest.setDescription(variantRequest.getDescription());
                            productVariantRequest.setContent(variantRequest.getContent());
                            productVariantRequest.setAvatar(null);
                            productVariantRequest.setFeatured(true);
                            productVariantRequest.setStatus(ProductStatus.AVAILABLE);

                            MultipartFile avatar = variantRequest.getAvatar();
                            if (avatar == null || avatar.isEmpty()) {
                                System.out.println("Avatar bị null hoặc rỗng.");
                            } else {
                                System.out.println("Avatar nhận được: " + avatar.getOriginalFilename());
                            }
                            try {
                                DataResponse<UploadFileResponse> uploadFileResponseDataResponse = fileService.uploadFile(avatar, variantRequest.getName().replace(" ", "_"));
                                System.out.println("Dữ liệu avatar sau khi lưu:");

                                if (uploadFileResponseDataResponse.getStatus() == 200 && uploadFileResponseDataResponse.getData() == null) {
                                    System.out.println("Dữ liệu avatar bị null.");
                                } else {
                                    List<UploadFileResponse> uploadFileResponses = (List<UploadFileResponse>) uploadFileResponseDataResponse.getData();
                                    String avatarUrl = uploadFileResponses.get(0).getFileName();
                                    productVariantRequest.setAvatar(variantRequest.getName() + "/" + avatarUrl);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (URISyntaxException e) {
                                throw new RuntimeException(e);
                            }

                            System.out.println("Dữ liệu variant:");
                            System.out.println(productVariantRequest);

                            DataResponse<ProductVariantResponse> productVariantResponseDataResponse = productService.createVariant(productId, productVariantRequest);
                            if (productVariantResponseDataResponse.getStatus() != 200) {
                                System.out.println("Lỗi khi lưu variant.");
                            }

                            if (productVariantResponseDataResponse.getStatus() == 200 && productVariantResponseDataResponse.getData() == null) {
                                System.out.println("Dữ liệu variant bị null.");
                            } else {
                                System.out.println("Dữ liệu variant sau khi lưu:");
                                List<ProductVariantResponse> productVariantResponses = (List<ProductVariantResponse>) productVariantResponseDataResponse.getData();
                                String variantId = productVariantResponses.get(0).getId();
                                System.out.println("variantId: " + variantId);

                                if (variantRequest.getDetails() == null || variantRequest.getDetails().isEmpty()) {
                                    System.out.println("Không có chi tiết variant nào được tạo.");
                                } else {
                                    // Luu thong tin chi tiet variant
                                    List<ProductVariantDetailRequest> productVariantDetails = new ArrayList<>();
                                    variantRequest.getDetails().forEach(
                                            detailRequest -> {
                                                ProductVariantDetailRequest productVariantDetailRequest = new ProductVariantDetailRequest();
                                                productVariantDetailRequest.setMemid(detailRequest.getMemid());
                                                productVariantDetailRequest.setPrice(detailRequest.getPrice());
                                                productVariantDetailRequest.setSale(detailRequest.getSale());
                                                List<ProductVariantDetailRequest.ColorRequest> colorRequests = new ArrayList<>();
                                                if (detailRequest.getColors() == null || detailRequest.getColors().isEmpty()) {
                                                    System.out.println("Không có màu nào được tạo.");
                                                } else {
                                                    detailRequest.getColors().forEach(
                                                            colorRequest -> {
                                                                ProductVariantDetailRequest.ColorRequest colorRequest1 = new ProductVariantDetailRequest.ColorRequest();
                                                                colorRequest1.setColorId(colorRequest.getColorId());
                                                                colorRequest1.setQuantity(colorRequest.getQuantity());
                                                                colorRequests.add(colorRequest1);
                                                                if (colorRequest.getImages() == null || colorRequest.getImages().length == 0) {
                                                                    System.out.println("Không có hình ảnh nào được tạo.");
                                                                } else {
                                                                    // Luu hinh anh
                                                                    MultipartFile[] images = colorRequest.getImages();


                                                                }
                                                            }
                                                    );
                                                    productVariantDetailRequest.setColors(colorRequests);
                                                    System.out.println("Dữ liệu chi tiết variant detail:");
                                                    System.out.println(productVariantDetailRequest);
                                                    productVariantDetails.add(productVariantDetailRequest);
                                                }

                                            }
                                    );
                                    System.out.println("Dữ liệu chi tiết variant trước khi lưu:");
                                    productVariantDetails.forEach(
                                            System.out::println
                                    );
                                    DataResponse<ProductVariantDetailResponse> productVariantDetailResponseDataResponse = productService.createVariantDetail(variantId, productVariantDetails);
                                    if (productVariantDetailResponseDataResponse.getStatus() != 200) {
                                        System.out.println("Lỗi khi lưu chi tiết variant.");
                                    }

                                    if (productVariantDetailResponseDataResponse.getStatus() == 200 && productVariantDetailResponseDataResponse.getData() == null) {
                                        System.out.println("Dữ liệu chi tiết variant bị null.");
                                    } else {
                                        System.out.println("Dữ liệu chi tiết variant sau khi lưu:");
                                        List<ProductVariantDetailResponse> productVariantDetailResponses = (List<ProductVariantDetailResponse>) productVariantDetailResponseDataResponse.getData();
                                        productVariantDetailResponses.forEach(
                                                System.out::println
                                        );
                                    }

                                }
                            }
                        }
                );


            }
        }

        ModelAndView modelAndView = new ModelAndView("html/formPhone");
        modelAndView.addObject("productFullRequest", new ProductFullRequest());
        modelAndView.addObject("trademarks", trademarkService.getAllTrademarks().getData());
        modelAndView.addObject("colors", colorService.getAllColors().getData());
        modelAndView.addObject("memories", memoryService.getAllMemories().getData());
        modelAndView.addObject("usageCategories", usageCategoryService.getAllUsageCategories().getData());
        return modelAndView;
    }
}
