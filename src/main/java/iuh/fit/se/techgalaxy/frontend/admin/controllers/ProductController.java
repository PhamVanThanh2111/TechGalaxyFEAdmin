package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.*;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.*;
import iuh.fit.se.techgalaxy.frontend.admin.entities.*;
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
    public String saveFullProduct(@Validated @ModelAttribute("productFullRequest") ProductFullRequest productFullRequest, RedirectAttributes redirectAttributes) {
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
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving product.");
            return "redirect:/products";
        }

        if ( productResponseDataResponse.getData() == null) {
            System.out.println("Dữ liệu sản phẩm bị null.");
        } else {
            System.out.println("Dữ liệu sản phẩm sau khi lưu:");
            List<ProductResponse> productResponses = (List<ProductResponse>) productResponseDataResponse.getData();
            String productId = productResponses.get(0).getId();

            if (productFullRequest.getVariants() == null || productFullRequest.getVariants().isEmpty()) {
                System.out.println("Không có variant nào được tạo.");
            } else {
                // Luu thong tin variant
                List<ProductFullRequest.ProductVariantRequest> productVariants = productFullRequest.getVariants();
                for (ProductFullRequest.ProductVariantRequest variantRequest : productVariants) {
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
                        assert avatar != null;
                        DataResponse<UploadFileResponse> uploadFileResponseDataResponse = fileService.uploadFile(avatar, "products/" + variantRequest.getName().replace(" ", "_"));
                        System.out.println("Dữ liệu avatar sau khi lưu:");

                        if (uploadFileResponseDataResponse.getStatus() == 200 && uploadFileResponseDataResponse.getData() == null) {
                            System.out.println("Dữ liệu avatar bị null.");
                        } else {
                            List<UploadFileResponse> uploadFileResponses = (List<UploadFileResponse>) uploadFileResponseDataResponse.getData();
                            String avatarUrl = uploadFileResponses.get(0).getFileName();
                            productVariantRequest.setAvatar("products/" + variantRequest.getName().replace(" ", "_") + "/" + avatarUrl);
                        }
                    } catch (IOException | URISyntaxException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Dữ liệu variant:");
                    System.out.println(productVariantRequest);

                    DataResponse<ProductVariantResponse> productVariantResponseDataResponse = productService.createVariant(productId, productVariantRequest);
                    if (productVariantResponseDataResponse.getStatus() != 200) {
                        System.out.println("Lỗi khi lưu variant.");
                        redirectAttributes.addFlashAttribute("errorMessage", "Error saving variant.");
                        continue;
                    }
                    if (productVariantResponseDataResponse.getData() == null) {
                        System.out.println("Dữ liệu variant bị null.");
                    } else {
                        System.out.println("Dữ liệu variant sau khi lưu:");
                        List<ProductVariantResponse> productVariantResponses = (List<ProductVariantResponse>) productVariantResponseDataResponse.getData();
                        String variantId = productVariantResponses.get(0).getId();
                        System.out.println("variantId: " + variantId);

                        if (variantRequest.getDetails() == null || variantRequest.getDetails().isEmpty()) {
                            System.out.println("Không có chi tiết variant nào được tạo.");
                        } else {
                            List<ProductVariantDetailRequest> productVariantDetails = new ArrayList<>();
                            List<ProductFullRequest.ProductVariantRequest.ProductVariantDetailRequest> details = variantRequest.getDetails();
                            for (ProductFullRequest.ProductVariantRequest.ProductVariantDetailRequest detailRequest : details) {
                                ProductVariantDetailRequest productVariantDetailRequest = new ProductVariantDetailRequest();
                                productVariantDetailRequest.setMemid(detailRequest.getMemid());
                                productVariantDetailRequest.setPrice(detailRequest.getPrice());
                                productVariantDetailRequest.setSale(detailRequest.getSale());
                                List<ProductVariantDetailRequest.ColorRequest> colorRequests = new ArrayList<>();
                                if (detailRequest.getColors() == null || detailRequest.getColors().isEmpty()) {
                                    System.out.println("Không có màu nào được tạo.");
                                } else {
                                    for (ProductFullRequest.ProductVariantRequest.ProductVariantDetailRequest.ColorRequest colorRequest : detailRequest.getColors()) {
                                        ProductVariantDetailRequest.ColorRequest colorRequest1 = new ProductVariantDetailRequest.ColorRequest();
                                        colorRequest1.setColorId(colorRequest.getColorId());
                                        colorRequest1.setQuantity(colorRequest.getQuantity());
                                        colorRequests.add(colorRequest1);
                                        if (colorRequest.getImages() == null || colorRequest.getImages().length == 0) {
                                            System.out.println("Không có hình ảnh nào được tạo.");
                                        } else {
                                            // Luu hinh anh
                                            MultipartFile[] images = colorRequest.getImages();
                                            for (MultipartFile image : images) {
                                                if (image == null || image.isEmpty()) {
                                                    System.out.println("Hình ảnh bị null hoặc rỗng.");
                                                } else {
                                                    System.out.println("Hình ảnh nhận được: " + image.getOriginalFilename());
                                                }
                                            }
                                        }
                                    }
                                    productVariantDetailRequest.setColors(colorRequests);
                                    System.out.println("Dữ liệu chi tiết variant detail:");
                                    System.out.println(productVariantDetailRequest);
                                    productVariantDetails.add(productVariantDetailRequest);
                                }

                            }
                            System.out.println("Dữ liệu chi tiết variant trước khi lưu:");
                            productVariantDetails.forEach(
                                    System.out::println
                            );
                            DataResponse<ProductVariantDetailResponse> productVariantDetailResponseDataResponse = productService.createVariantDetail(variantId, productVariantDetails);
                            if (productVariantDetailResponseDataResponse.getStatus() != 200) {
                                System.out.println("Lỗi khi lưu chi tiết variant.");
                                redirectAttributes.addFlashAttribute("errorMessage", "Error saving variant detail.");
                            } else if (productVariantDetailResponseDataResponse.getData() == null) {
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
            DataResponse<ProductVariantResponse> productVariantResponseDataResponse = productService.getAllProductVariantsByProductId(id);
            if (productVariantResponseDataResponse == null || productVariantResponseDataResponse.getStatus() != 200 || productVariantResponseDataResponse.getData() == null) {
                System.out.println("Error fetching product variants.");
                redirectAttributes.addFlashAttribute("errorMessage", "Error fetching product variants.");
                return "redirect:/products";
            }
            List<ProductVariantResponse> variants = (List<ProductVariantResponse>) productVariantResponseDataResponse.getData();
            if (variants.isEmpty()) {
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
            } else {
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

        if (productResponseDataResponse == null || productResponseDataResponse.getStatus() != 200 || productResponseDataResponse.getData() == null) {
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
        DataResponse<ProductVariantResponse> productVariantResponseDataResponse = productService.getAllProductVariantsByProductId(productId);
        if (productVariantResponseDataResponse == null || productVariantResponseDataResponse.getStatus() != 200 || productVariantResponseDataResponse.getData() == null) {
            modelAndView.addObject("errorMessage", "No variants found for the product.");
            return modelAndView;
        }
        // Lấy danh sách biến thể của sản phẩm
        List<ProductVariantResponse> variants = (List<ProductVariantResponse>) productService.getAllProductVariantsByProductId(productId).getData();


        // Thêm dữ liệu vào ModelAndView
        modelAndView.addObject("productId", productId);
        modelAndView.addObject("productName", product.getName());
        modelAndView.addObject("variants", variants);

        return modelAndView;
    }


    @GetMapping("{producctID}/variants/{variantId}/details")
    public ModelAndView viewVariantDetails(@PathVariable String producctID, @PathVariable String variantId, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("html/showVariantDetail");
        System.out.println("Fetching variant details for variant: " + variantId);
        List<ProductVariantResponse> variants = (List<ProductVariantResponse>) productService.getVariantById(variantId).getData();
        ProductVariantResponse variant = variants.stream().filter(v -> v.getId().equals(variantId)).findFirst().orElse(null);
        System.out.println("Variant: " + variant);

        DataResponse<ProductVariantDetailResponse> productVariantDetailResponseDataResponse = productService.getAllVariantDetailsByVariantId(variantId);

        if (productVariantDetailResponseDataResponse.getStatus() != 200 || productVariantDetailResponseDataResponse.getData() == null || productVariantDetailResponseDataResponse.getData().isEmpty()) {
            modelAndView.addObject("errorMessage", "No variant details found for the variants.");
            modelAndView.addObject("productId", producctID);
            modelAndView.addObject("variantId", variantId);
            // Truyền dữ liệu sang ModelAndView
            return modelAndView;
        }

        List<ProductVariantDetailResponse> details = (List<ProductVariantDetailResponse>) productService
                .getAllVariantDetailsByVariantId(variantId)
                .getData();
        // Lấy danh sách colors và memories từ các service
        List<Color> colors = (List<Color>) colorService.getAllColors().getData();
        List<Memory> memories = (List<Memory>) memoryService.getAllMemories().getData();

        // Chuyển đổi colors và memories thành Map
        Map<String, String> colorMap = colors.stream()
                .collect(Collectors.toMap(Color::getId, Color::getName));
        Map<String, String> memoryMap = memories.stream()
                .collect(Collectors.toMap(Memory::getId, Memory::getName));

        // Truyền dữ liệu sang ModelAndView
        modelAndView.addObject("productId", producctID);
        modelAndView.addObject("detail", details);
        modelAndView.addObject("variantId", variantId);
        modelAndView.addObject("colorMap", colorMap);
        modelAndView.addObject("memoryMap", memoryMap);
        modelAndView.addObject("variant", variant);

        return modelAndView;
    }


    @GetMapping("/variants/edit/{variantId}")
    public ModelAndView editVariant(@PathVariable String variantId, Model model) {
        ModelAndView modelAndView = new ModelAndView("html/editVariant");

        List<ProductVariantResponse> variants = (List<ProductVariantResponse>) productService.getVariantById(variantId).getData();
        ProductVariantResponse variant = variants.get(0);
        if (variant == null) {
            return new ModelAndView("redirect:/products");
        }
        modelAndView.addObject("usageCategories", usageCategoryService.getAllUsageCategories().getData());
        model.addAttribute("variant", variant);
        return modelAndView;
		
    }

    @PostMapping("/variants/update/{variantId}")
    public String updateVariant(@PathVariable String variantId, @ModelAttribute ProductVariantRequest_FE request, RedirectAttributes redirectAttributes) {
        List<ProductVariantResponse> variants = (List<ProductVariantResponse>) productService.getVariantById(variantId).getData();
        ProductVariantResponse variantinDB = variants.get(0);
        ProductVariantRequest variantRequest = new ProductVariantRequest();
        variantRequest.setName(request.getName());
        variantRequest.setDescription(request.getDescription());
        variantRequest.setContent(request.getContent());
        variantRequest.setFeatured(request.getFeatured());
        variantRequest.setStatus(request.getStatus());
        variantRequest.setUsageCategoryId(request.getUsageCategoryId());
        try {
            if (request.getAvatar() == null || request.getAvatar().isEmpty()) {
                System.out.println("Avatar bị null hoặc rỗng.");
                variantRequest.setAvatar(variantinDB.getAvatar());
            } else {
                DataResponse<UploadFileResponse> uploadFileResponseDataResponse = fileService.uploadFile(request.getAvatar(), "products/" + request.getName().replace(" ", "_"));
                if (uploadFileResponseDataResponse.getStatus() == 200 && uploadFileResponseDataResponse.getData() == null) {
                    System.out.println("Dữ liệu avatar bị null.");
                } else {
                    List<UploadFileResponse> uploadFileResponses = (List<UploadFileResponse>) uploadFileResponseDataResponse.getData();
                    String avatarUrl = uploadFileResponses.get(0).getFileName();
                    variantRequest.setAvatar("products/" + variantRequest.getName().replace(" ", "_") + "/" + avatarUrl);
                }
            }

            productService.updateVariant(variantId, variantRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Variant updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating variant: " + e.getMessage());
        }
        return "redirect:/products";
    }


    @PostMapping("/variants/delete/{variantId}")
    public String deleteVariant(@PathVariable String variantId, RedirectAttributes redirectAttributes) {

        System.out.println("Deleting product variant: " + variantId);
        DataResponse<ProductVariantDetailResponse> variantDetailResponses = productService.getAllVariantDetailsByVariantId(variantId);


        if (variantDetailResponses == null || variantDetailResponses.getData() == null || variantDetailResponses.getData().isEmpty()) {
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

    @GetMapping("/{productId}/variants/add")
    public ModelAndView addVariant(@PathVariable String productId) {
        ModelAndView modelAndView = new ModelAndView("html/formVariants");

        modelAndView.addObject("productId", productId);

        modelAndView.addObject("usageCategories", usageCategoryService.getAllUsageCategories().getData());

        ProductVariantRequest_FE newVariant = new ProductVariantRequest_FE();
        modelAndView.addObject("variant", newVariant);

        return modelAndView;
    }

    @PostMapping("/{productId}/variants/add")
    public String saveVariant(@PathVariable String productId,
                              @ModelAttribute("variant") ProductVariantRequest_FE variantRequest,
                              RedirectAttributes redirectAttributes) {
        ProductVariantRequest request = new ProductVariantRequest();
        request.setName(variantRequest.getName());
        request.setDescription(variantRequest.getDescription());
        request.setContent(variantRequest.getContent());
        request.setFeatured(variantRequest.getFeatured());
        request.setStatus(variantRequest.getStatus());
        request.setUsageCategoryId(variantRequest.getUsageCategoryId());

        try {
            // Handle avatar upload
            if (variantRequest.getAvatar() != null && !variantRequest.getAvatar().isEmpty()) {
                DataResponse<UploadFileResponse> uploadFileResponseDataResponse = fileService.uploadFile(variantRequest.getAvatar(), "products/" + request.getName().replace(" ", "_"));
                if (uploadFileResponseDataResponse.getStatus() == 200 && uploadFileResponseDataResponse.getData() == null) {
                    System.out.println("Dữ liệu avatar bị null.");
                } else {
                    List<UploadFileResponse> uploadFileResponses = (List<UploadFileResponse>) uploadFileResponseDataResponse.getData();
                    String avatarUrl = uploadFileResponses.get(0).getFileName();
                    request.setAvatar("products/" + variantRequest.getName().replace(" ", "_") + "/" + avatarUrl);

                }
            } else {
                request.setAvatar(null);
            }

            variantRequest.setUsageCategoryId(productId);

            productService.createVariant(productId, request);
            redirectAttributes.addFlashAttribute("successMessage", "Variant saved successfully!");
            return "redirect:/products/" + productId + "/variants";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving variant: " + e.getMessage());
            return "redirect:/products/";
        }
    }

    @GetMapping("/{productId}/variants/{variantId}/details/add")
    public ModelAndView showAddVariantDetailForm(@PathVariable String productId,
                                                 @PathVariable String variantId,
                                                 Model model) {
        ModelAndView modelAndView = new ModelAndView("html/formVariantDetails");
        ProductVariantDetailRequest_FE request = new ProductVariantDetailRequest_FE();
        request.setColors(new ArrayList<>()); // Khởi tạo danh sách rỗng để tránh lỗi null

        model.addAttribute("productId", productId);
        model.addAttribute("variantId", variantId);
        colorService.getAllColors().getData().forEach(color -> {
            System.out.println("Color: " + color);
        });
        model.addAttribute("availableColors", colorService.getAllColors().getData());
        model.addAttribute("memories", memoryService.getAllMemories().getData());
        model.addAttribute("variantDetailRequest", request);
        return modelAndView;
    }


    @PostMapping("/{productId}/variants/{variantId}/details/add")
    public String saveVariantDetail(
            @PathVariable String productId,
            @PathVariable String variantId,
            @ModelAttribute("variantDetailRequest") ProductVariantDetailRequest_FE request,
            RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Received Request: " + request);

            if (request.getColors() == null || request.getColors().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "No colors found in the request.");
                return "redirect:/products/" + productId + "/variants/" + variantId + "/details";
            }

            List<ProductVariantDetailRequest.ColorRequest> serviceColors = request.getColors().stream().map(color -> {
                ProductVariantDetailRequest.ColorRequest serviceColor = new ProductVariantDetailRequest.ColorRequest();
                serviceColor.setColorId(color.getColorId());
                serviceColor.setQuantity(color.getQuantity());
                return serviceColor;
            }).collect(Collectors.toList());

            ProductVariantDetailRequest serviceRequest = new ProductVariantDetailRequest();
            serviceRequest.setMemid(request.getMemid());
            serviceRequest.setPrice(request.getPrice());
            serviceRequest.setSale(request.getSale());
            serviceRequest.setColors(serviceColors);

            DataResponse<ProductVariantDetailResponse> productVariantDetailResponseDataResponse = productService.createVariantDetail(variantId, Collections.singletonList(serviceRequest));
            if (productVariantDetailResponseDataResponse == null || productVariantDetailResponseDataResponse.getStatus() != 200) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error adding variant detail.");
                return "redirect:/products/" + productId + "/variants/" + variantId + "/details";
            }
            redirectAttributes.addFlashAttribute("successMessage", "Variant detail added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding variant detail: " + e.getMessage());
        }
        return "redirect:/products/" + productId + "/variants/" + variantId + "/details";
    }


}
