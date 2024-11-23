package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductFullRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantDetailRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.request.ProductVariantRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.*;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Color;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Memory;
import iuh.fit.se.techgalaxy.frontend.admin.entities.ProductVariantDetail;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.ProductStatus;
import iuh.fit.se.techgalaxy.frontend.admin.services.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final TrademarkServiceImpl trademarkService;
    private final ColorServiceImpl colorService;
    private final MemoryServiceImpl memoryService;
    private final UsageCategoryServiceImpl usageCategoryService;

    private final FileServiceImpl fileService;

    private final ProductServiceImpl productService;

    @Autowired
    public ProductController(TrademarkServiceImpl trademarkService, ColorServiceImpl colorService, MemoryServiceImpl memoryService, UsageCategoryServiceImpl usageCategoryService, ProductServiceImpl productService, FileServiceImpl fileService) {
        this.trademarkService = trademarkService;
        this.colorService = colorService;
        this.memoryService = memoryService;
        this.usageCategoryService = usageCategoryService;
        this.productService = productService;
        this.fileService = fileService;
    }

    @GetMapping("/add")
    public ModelAndView showAddProductForm() {
        ModelAndView modelAndView = new ModelAndView("html/Phone/formPhone");
        modelAndView.addObject("productFullRequest", new ProductFullRequest());
        modelAndView.addObject("trademarks", trademarkService.getAllTrademarks().getData());
        modelAndView.addObject("colors", colorService.getAllColors().getData());
        modelAndView.addObject("memories", memoryService.getAllMemories().getData());
        modelAndView.addObject("usageCategories", usageCategoryService.getAllUsageCategories().getData());
        return modelAndView;
    }

    @PostMapping("/add")
    public String saveFullProduct(@Validated @ModelAttribute("productFullRequest") ProductFullRequest productFullRequest) {
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

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateProductForm(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("html/Phone/updateProduct");
        DataResponse<ProductResponse> productResponse = productService.getProductById(id);
        if (productResponse == null || productResponse.getStatus() != 200 || productResponse.getData() == null) {
            System.out.println("Error fetching product details.");
            return new ModelAndView("redirect:/products");
        }
        List<ProductResponse> products = (List<ProductResponse>) productResponse.getData();
        ProductResponse product = products.get(0);
        if (product.getId() == null || product.getId().isEmpty()) {
            System.out.println("Product ID is missing or invalid.");
            return new ModelAndView("redirect:/products");
        }
        modelAndView.addObject("trademarks", trademarkService.getAllTrademarks().getData());
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Deleting product: " + id);
            List<ProductVariantResponse> variants = (List<ProductVariantResponse>) productService.getAllProductVariantsByProductId(id);
            variants.forEach(
                    variant -> {
                        System.out.println("Deleting variant: " + variant.getId());
                    }
            );
            if (variants == null || variants.isEmpty()|| variants.size() == 0){
                DataResponse<Object> productResponseDataResponse = productService.deleteProduct(id);
                if (productResponseDataResponse == null || productResponseDataResponse.getStatus() != 200) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product: " + id);
                    return "redirect:/products";
                }
                redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully: " + id);
                return "redirect:/products";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Product has variants. Delete variants first.");
                return "redirect:/products";
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product: " + e.getMessage());
            return "redirect:/products";
        }
    }


    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable String id, @ModelAttribute("product") ProductRequest productRequest) {
        try {

            DataResponse<ProductResponse> productVariantDetailResponseDataResponse = productService.updateProduct(id, productRequest);
            if (productVariantDetailResponseDataResponse == null || productVariantDetailResponseDataResponse.getStatus() != 200 || productVariantDetailResponseDataResponse.getData() == null) {
                System.out.println("Error updating product.");
                return "redirect:/products/edit/" + id;
            }else {
                List<ProductResponse> productResponses = (List<ProductResponse>) productVariantDetailResponseDataResponse.getData();
                ProductResponse productResponse = productResponses.get(0);
                System.out.println("Product updated successfully: " + productResponse.getId());
                return "redirect:/products";
            }
        } catch (Exception e) {
            System.out.println("Error updating product: " + e.getMessage());
            return "redirect:/products/edit/" + id;
        }
    }

    @GetMapping
    public ModelAndView showProductList() {
        ModelAndView modelAndView = new ModelAndView("html/Phone/showPhone");
        System.out.println("Fetching product list...");
        DataResponse<ProductResponse> productResponseDataResponse = productService.getAllProducts();

        if (productResponseDataResponse == null || productResponseDataResponse.getStatus() != 200 || productResponseDataResponse.getData() == null){
            System.out.println("Error fetching product list.");
            modelAndView.addObject("errorMessage", "Unable to fetch product list. Please try again later.");
            return modelAndView;
        }

        List<ProductResponse> productResponses = (List<ProductResponse>) productResponseDataResponse.getData();
        modelAndView.addObject("products", productResponses);

        return modelAndView;
    }

    @GetMapping("/{productId}/variants")
    public ModelAndView viewVariants(@PathVariable String productId, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("html/Phone/showVariants");

        // Lấy thông tin sản phẩm
        List<ProductResponse> products = (List<ProductResponse>) productService.getProductById(productId).getData();
        if (products == null || products.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found.");
            return new ModelAndView("redirect:/products");
        }

        ProductResponse product = products.get(0);

        // Lấy danh sách biến thể của sản phẩm
        List<ProductVariantResponse> variants = (List<ProductVariantResponse>) productService.getAllProductVariantsByProductId(productId).getData();
        if (variants == null || variants.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No variants found for the product.");
            return new ModelAndView("redirect:/products");
        }

        // Thêm dữ liệu vào ModelAndView
        modelAndView.addObject("productName", product.getName());
        modelAndView.addObject("variants", variants);

        return modelAndView;
    }


    @GetMapping("/variants/{variantId}/details")
    public ModelAndView viewVariantDetails(@PathVariable String variantId, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("html/Phone/showVariantDetail");
        List<ProductVariantResponse> variants = (List<ProductVariantResponse>) productService.getVariantById(variantId).getData();
        ProductVariantResponse variant = variants.stream().filter(v -> v.getId().equals(variantId)).findFirst().orElse(null);
        // Lấy danh sách chi tiết variant
        List<ProductVariantDetailResponse> details = (List<ProductVariantDetailResponse>) productService
                .getAllVariantDetailsByVariantId(variantId)
                .getData();

        if (details == null || details.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No variant details found for the variants.");
            return new ModelAndView("redirect:/products");
        }

        // Lấy danh sách colors và memories từ các service
        List<Color> colors = (List<Color>) colorService.getAllColors().getData();
        List<Memory> memories = (List<Memory>) memoryService.getAllMemories().getData();

        // Chuyển đổi colors và memories thành Map
        Map<String, String> colorMap = colors.stream()
                .collect(Collectors.toMap(Color::getId, Color::getName));
        Map<String, String> memoryMap = memories.stream()
                .collect(Collectors.toMap(Memory::getId, Memory::getName));

        // Truyền dữ liệu sang ModelAndView
        modelAndView.addObject("detail", details);
        modelAndView.addObject("variantId", variantId);
        modelAndView.addObject("colorMap", colorMap);
        modelAndView.addObject("memoryMap", memoryMap);
        modelAndView.addObject("variant", variant);

        return modelAndView;
    }

    @GetMapping("/variants/edit/{variantId}")
    public String editVariant(@PathVariable String variantId, Model model) {
        List<ProductVariantResponse> variants = (List<ProductVariantResponse>) productService.getAllVariants().getData();
        ProductVariantResponse variant = variants.stream().filter(v -> v.getId().equals(variantId)).findFirst().orElse(null);
        if (variant == null) {
            return "redirect:/products";
        }
        model.addAttribute("variant", variant);
        return "html/Phone/edit-variant";
    }

    @PostMapping("/variants/delete/{variantId}")
    public String deleteVariant(@PathVariable String variantId, RedirectAttributes redirectAttributes) {

            System.out.println("Deleting product variant: " + variantId);
            DataResponse<ProductVariantDetailResponse> variantDetailResponses = productService.getAllVariantDetailsByVariantId(variantId);


            if (variantDetailResponses==null || variantDetailResponses.getData() == null || variantDetailResponses.getData().isEmpty()){
                DataResponse<Object> productResponseDataResponse = productService.deleteVariant(variantId);
                if (productResponseDataResponse == null || productResponseDataResponse.getStatus() != 200) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product variant: " + variantId);
                    return "redirect:/products";
                }
                redirectAttributes.addFlashAttribute("successMessage", "Product variant deleted successfully: " + variantId);
                return "redirect:/products";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Product variant has variants details. Delete variants first.");
                return "redirect:/products";
            }
    }


    @PostMapping("variants/details/delete/{variantDetailId}")
    public String deleteVariantDetail(@PathVariable String variantDetailId, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Deleting product variant detail: " + variantDetailId);
            DataResponse<Object> productResponseDataResponse = productService.deleteVariantDetail(variantDetailId);
            if (productResponseDataResponse == null || productResponseDataResponse.getStatus() != 200) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product variant detail: " + variantDetailId);
                return "redirect:/products";
            }
            redirectAttributes.addFlashAttribute("successMessage", "Product variant detail deleted successfully: " + variantDetailId);
            return "redirect:/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product variant detail: " + e.getMessage());
            return "redirect:/products";
        }
    }
}
