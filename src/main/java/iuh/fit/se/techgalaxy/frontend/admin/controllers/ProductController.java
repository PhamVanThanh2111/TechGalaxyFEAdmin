package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.*;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.*;
import iuh.fit.se.techgalaxy.frontend.admin.entities.*;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.ProductStatus;
import iuh.fit.se.techgalaxy.frontend.admin.services.impl.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
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

    private final AttributeServiceImpl attributeService;

    private final ProductImageServiceImpl productImgService;

    @Autowired
    public ProductController(TrademarkServiceImpl trademarkService, ColorServiceImpl colorService, MemoryServiceImpl memoryService, UsageCategoryServiceImpl usageCategoryService, ProductServiceImpl productService, FileServiceImpl fileService, AttributeServiceImpl attributeService, ProductImageServiceImpl productImgService) {
        this.trademarkService = trademarkService;
        this.colorService = colorService;
        this.memoryService = memoryService;
        this.usageCategoryService = usageCategoryService;
        this.productService = productService;
        this.fileService = fileService;
        this.attributeService = attributeService;
        this.productImgService = productImgService;
    }

    @GetMapping("/add")
    public ModelAndView showAddProductForm(HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            new ModelAndView("redirect:/login");
        }
        try {
            ModelAndView modelAndView = new ModelAndView("html/Phone/formPhone");
            modelAndView.addObject("productFullRequest", new ProductFullRequest());
            modelAndView.addObject("trademarks", trademarkService.getAllTrademarks().getData());
            modelAndView.addObject("colors", colorService.getAllColors().getData());
            modelAndView.addObject("memories", memoryService.getAllMemories().getData());
            modelAndView.addObject("usageCategories", usageCategoryService.getAllUsageCategories().getData());
            return modelAndView;
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (Exception e) {
            return new ModelAndView("redirect:/home");
        }
    }

    @PostMapping("/add")
    public String saveFullProduct(@ModelAttribute("productFullRequest") @Valid ProductFullRequest productFullRequest, RedirectAttributes redirectAttributes, BindingResult result, HttpSession session, HttpServletResponse response) {

        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        // In toàn bộ dữ liệu nhận được từ form
        System.out.println("Dữ liệu sản phẩm:");
        System.out.println(productFullRequest);
        if (result.hasErrors()) {
            System.out.println("Validation failed: " + result.getAllErrors());
            result.getAllErrors().forEach(error -> System.out.println(error.toString()));
            redirectAttributes.addFlashAttribute("productFullRequest", productFullRequest);
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed: " + result.getAllErrors());
            return "redirect:/products/add";
        }
        try {
            ProductRequest productRequest = new ProductRequest();
            productRequest.setName(productFullRequest.getName());
            productRequest.setTrademarkId(productFullRequest.getTrademarkId());

            // Lưu thông tin sản phẩm
            DataResponse<ProductResponse> productResponseDataResponse = productService.createProduct(productRequest, accessToken);

            if (productResponseDataResponse.getStatus() != 200) {
                System.out.println("Lỗi khi lưu sản phẩm.");
                redirectAttributes.addFlashAttribute("errorMessage", "Error saving product.");
                return "redirect:/products";
            }

            if (productResponseDataResponse.getData() == null) {
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
                            try {
                                DataResponse<UploadFileResponse> uploadFileResponseDataResponse = fileService.uploadFile(avatar, "products/" + variantRequest.getName().replace(" ", "_"), accessToken);
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
                        }
                        System.out.println("Dữ liệu variant:");
                        System.out.println(productVariantRequest);

                        DataResponse<ProductVariantResponse> productVariantResponseDataResponse = productService.createVariant(productId, productVariantRequest, accessToken);
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
                                List<ProductFullRequest.ProductVariantRequest.ProductVariantDetailRequest> details = variantRequest.getDetails();
                                for (ProductFullRequest.ProductVariantRequest.ProductVariantDetailRequest detailRequest : details) {
                                    ProductVariantDetailRequest productVariantDetailRequest = new ProductVariantDetailRequest();
                                    productVariantDetailRequest.setMemid(detailRequest.getMemid());
                                    productVariantDetailRequest.setPrice(detailRequest.getPrice());
                                    productVariantDetailRequest.setSale(detailRequest.getSale());
                                    if (detailRequest.getColors() == null || detailRequest.getColors().isEmpty()) {
                                        System.out.println("Không có màu nào được tạo.");
                                    } else {
                                        List<ProductsImageRequest> imagesProductsImageRequests = new ArrayList<>();
                                        for (ProductFullRequest.ProductVariantRequest.ProductVariantDetailRequest.ColorRequest colorRequest : detailRequest.getColors()) {
                                            List<ProductVariantDetailRequest> productVariantDetails = new ArrayList<>();
                                            ProductVariantDetailRequest.ColorRequest colorRequest1 = new ProductVariantDetailRequest.ColorRequest();
                                            System.out.println("Dữ liệu màu:");
                                            System.out.println(colorRequest.getColorId());
                                            colorRequest1.setColorId(colorRequest.getColorId());
                                            colorRequest1.setQuantity(colorRequest.getQuantity());
                                            List<ProductVariantDetailRequest.ColorRequest> colorRequests1 = List.of(colorRequest1);
                                            productVariantDetailRequest.setColors(colorRequests1);
                                            System.out.println("Dữ liệu chi tiết variant detail:");
                                            System.out.println(productVariantDetailRequest);
                                            productVariantDetails.add(productVariantDetailRequest);
                                            System.out.println("Dữ liệu chi tiết variant trước khi lưu:");
                                            productVariantDetails.forEach(
                                                    System.out::println
                                            );
                                            DataResponse<String> productVariantDetailResponseDataResponse = productService.createVariantDetail(variantId, productVariantDetails, accessToken);
                                            if (colorRequest.getImages() == null || colorRequest.getImages().length == 0) {
                                                System.out.println("Không có hình ảnh nào được tạo.");
                                            } else {
                                                if (productVariantDetailResponseDataResponse.getStatus() != 200) {
                                                    System.out.println("Lỗi khi lưu chi tiết variant.");
                                                    redirectAttributes.addFlashAttribute("errorMessage", "Error saving variant detail.");
                                                } else if (productVariantDetailResponseDataResponse.getData() == null) {
                                                    System.out.println("Dữ liệu chi tiết variant bị null.");
                                                } else {
                                                    System.out.println("Dữ liệu chi tiết variant sau khi lưu:");
                                                    List<String> productVariantDetailResponses = (List<String>) productVariantDetailResponseDataResponse.getData();
                                                    String detailId = productVariantDetailResponses.get(0);

                                                    // Luu hinh anh
                                                    MultipartFile[] images = colorRequest.getImages();
                                                    for (MultipartFile image : images) {
                                                        if (image == null || image.isEmpty()) {
                                                            System.out.println("Hình ảnh bị null hoặc rỗng.");
                                                        } else {
                                                            System.out.println("Hình ảnh nhận được: " + image.getOriginalFilename());
                                                            DataResponse<UploadFileResponse> uploadFileResponseDataResponse = fileService.uploadFile(image, "products/" + variantRequest.getName().replace(" ", "_") + "/" + detailRequest.getMemid() + "/" + colorRequest.getColorId(), accessToken);
                                                            if (uploadFileResponseDataResponse.getStatus() == 200 && uploadFileResponseDataResponse.getData() == null) {
                                                                System.out.println("Dữ liệu hình ảnh bị null.");
                                                            } else {
                                                                List<UploadFileResponse> uploadFileResponses = (List<UploadFileResponse>) uploadFileResponseDataResponse.getData();
                                                                String imageUrl = uploadFileResponses.get(0).getFileName();
                                                                ProductsImageRequest productsImageRequest = new ProductsImageRequest("products/" + variantRequest.getName().replace(" ", "_") + "/" + detailRequest.getMemid() + "/" + colorRequest.getColorId() + "/" + imageUrl, false);
                                                                imagesProductsImageRequests.add(productsImageRequest);

                                                                DataResponse<ProductsImageResponse> productImageResponseDataResponse = productImgService.createProductImg(detailId, imagesProductsImageRequests, accessToken);
                                                                if (productImageResponseDataResponse.getStatus() != 200) {
                                                                    System.out.println("Lỗi khi lưu hình ảnh.");
                                                                    redirectAttributes.addFlashAttribute("errorMessage", "Error saving image.");
                                                                } else if (productImageResponseDataResponse.getData() == null) {
                                                                    System.out.println("Dữ liệu hình ảnh bị null.");
                                                                } else {
                                                                    System.out.println("Dữ liệu hình ảnh sau khi lưu:");
                                                                    List<ProductsImageResponse> productsImageResponses = (List<ProductsImageResponse>) productImageResponseDataResponse.getData();
                                                                    System.out.println(productsImageResponses);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return "redirect:/products";
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/home";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/home";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/home";
        }
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateProductForm(@PathVariable String id, HttpSession session, HttpServletResponse response) {

        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return new ModelAndView("redirect:/login");
        }
        try {
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
            ProductRequest productRequest = new ProductRequest();
            productRequest.setId(product.getId());
            productRequest.setName(product.getName());
            productRequest.setTrademarkId(product.getTrademark().getId());

            modelAndView.addObject("product", productRequest);
            modelAndView.addObject("trademarks", trademarkService.getAllTrademarks().getData());
            //        modelAndView.addObject("product", product);
            return modelAndView;
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (Exception e) {
            return new ModelAndView("redirect:/home");
        }
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateProduct(@PathVariable String id, @ModelAttribute("product") @Valid ProductRequest productRequest, BindingResult result, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return new ModelAndView("redirect:/login");
        }

        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("html/Phone/updateProduct");
            modelAndView.addObject("trademarks", trademarkService.getAllTrademarks().getData());
            return modelAndView;
        }
        try {
            DataResponse<ProductResponse> productVariantDetailResponseDataResponse = productService.updateProduct(id, productRequest, accessToken);
            if (productVariantDetailResponseDataResponse == null || productVariantDetailResponseDataResponse.getStatus() != 200 || productVariantDetailResponseDataResponse.getData() == null) {
                System.out.println("Error updating product.");
                return new ModelAndView("redirect:/products/edit/" + id);
            } else {
                List<ProductResponse> productResponses = (List<ProductResponse>) productVariantDetailResponseDataResponse.getData();
                ProductResponse productResponse = productResponses.get(0);
                System.out.println("Product updated successfully: " + productResponse.getId());
                return new ModelAndView("redirect:/products");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (Exception e) {
            System.out.println("Error updating product: " + e.getMessage());
            return new ModelAndView("redirect:/products/edit/" + id);
        }
    }


    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id, RedirectAttributes redirectAttributes, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }

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
                DataResponse<Object> productResponseDataResponse = productService.deleteProduct(id, accessToken);
                if (productResponseDataResponse == null || productResponseDataResponse.getStatus() != 200) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product: " + id);
                    return "redirect:/products";
                }
                redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully: " + id);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Product has variants. Delete variants first.");
            }
            return "redirect:/products";

        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/home";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product: " + e.getMessage());
            return "redirect:/products";
        }
    }


    @GetMapping
    public ModelAndView showProductList(HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return new ModelAndView("redirect:/login");
        }
        try {

            ModelAndView modelAndView = new ModelAndView("html/Phone/showPhone");
            System.out.println("Fetching product list...");
            DataResponse<ProductResponse> productResponseDataResponse = productService.getAllProducts(accessToken);

            if (productResponseDataResponse == null || productResponseDataResponse.getStatus() != 200 || productResponseDataResponse.getData() == null) {
                System.out.println("Error fetching product list.");
                modelAndView.addObject("errorMessage", "Unable to fetch product list. Please try again later.");
                return modelAndView;
            }

            List<ProductResponse> productResponses = (List<ProductResponse>) productResponseDataResponse.getData();
            modelAndView.addObject("products", productResponses);

            return modelAndView;
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (Exception e) {
            return new ModelAndView("redirect:/home");
        }
    }

    @GetMapping("/{productId}/variants")
    public ModelAndView viewVariants(@PathVariable String productId, RedirectAttributes redirectAttributes, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return new ModelAndView("redirect:/login");
        }
        try {
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
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (Exception e) {
            return new ModelAndView("redirect:/home");
        }
    }


    @GetMapping("{producctID}/variants/{variantId}/details")
    public ModelAndView viewVariantDetails(@PathVariable String producctID, @PathVariable String variantId, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return new ModelAndView("redirect:/login");
        }
        try {
            ModelAndView modelAndView = new ModelAndView("html/Phone/showVariantDetail");
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
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (Exception e) {
            return new ModelAndView("redirect:/home");
        }
    }


    @GetMapping("/variants/edit/{variantId}")
    public ModelAndView editVariant(@PathVariable String variantId, Model model, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return new ModelAndView("redirect:/login");
        }
        try {
            ModelAndView modelAndView = new ModelAndView("html/Phone/editVariant");

            List<ProductVariantResponse> variants = (List<ProductVariantResponse>) productService.getVariantById(variantId).getData();
            ProductVariantResponse variant = variants.get(0);
            if (variant == null) {
                return new ModelAndView("redirect:/products");
            }
            modelAndView.addObject("usageCategories", usageCategoryService.getAllUsageCategories().getData());
            model.addAttribute("variant", variant);
            return modelAndView;
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (Exception e) {
            return new ModelAndView("redirect:/home");
        }

    }

    @PostMapping("/variants/update/{variantId}")
    public String updateVariant(@PathVariable String variantId, @ModelAttribute ProductVariantRequest_FE request, RedirectAttributes redirectAttributes, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {
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
                    DataResponse<UploadFileResponse> uploadFileResponseDataResponse = fileService.uploadFile(request.getAvatar(), "products/" + request.getName().replace(" ", "_"), accessToken);
                    if (uploadFileResponseDataResponse.getStatus() == 200 && uploadFileResponseDataResponse.getData() == null) {
                        System.out.println("Dữ liệu avatar bị null.");
                    } else {
                        List<UploadFileResponse> uploadFileResponses = (List<UploadFileResponse>) uploadFileResponseDataResponse.getData();
                        String avatarUrl = uploadFileResponses.get(0).getFileName();
                        variantRequest.setAvatar("products/" + variantRequest.getName().replace(" ", "_") + "/" + avatarUrl);
                    }
                }

                DataResponse<ProductVariantResponse> productVariantResponseDataResponse = productService.updateVariant(variantId, variantRequest, accessToken);
                if (productVariantResponseDataResponse == null || productVariantResponseDataResponse.getStatus() != 200) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error updating variant.");
                    return "redirect:/products";
                } else {
                    System.out.println("Variant updated successfully: " + variantId);
                    redirectAttributes.addFlashAttribute("successMessage", "Variant updated successfully!");

                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error updating variant: " + e.getMessage());
            }
            return "redirect:/products";
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/home";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/home";
        } catch (Exception e) {
            return "redirect:/home";
        }
    }


    @PostMapping("/variants/delete/{variantId}")
    public String deleteVariant(@PathVariable String variantId, RedirectAttributes redirectAttributes, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {

            System.out.println("Deleting product variant: " + variantId);
            DataResponse<ProductVariantDetailResponse> variantDetailResponses = productService.getAllVariantDetailsByVariantId(variantId);


            if (variantDetailResponses == null || variantDetailResponses.getData() == null || variantDetailResponses.getData().isEmpty()) {
                DataResponse<Object> productResponseDataResponse = productService.deleteVariant(variantId, accessToken);
                if (productResponseDataResponse == null || productResponseDataResponse.getStatus() != 200) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product variant: " + variantId);
                    return "redirect:/products";
                }
                redirectAttributes.addFlashAttribute("successMessage", "Product variant deleted successfully: " + variantId);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Product variant has variants details. Delete variants first.");
            }
            return "redirect:/products";
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/home";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product variant: " + e.getMessage());
            return "redirect:/products";
        }
    }


    @PostMapping("variants/details/delete/{variantDetailId}")
    public String deleteVariantDetail(@PathVariable String variantDetailId, RedirectAttributes redirectAttributes, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {
            System.out.println("Deleting product variant detail: " + variantDetailId);
            DataResponse<Object> productResponseDataResponse = productService.deleteVariantDetail(variantDetailId, accessToken);
            if (productResponseDataResponse == null || productResponseDataResponse.getStatus() != 200) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product variant detail: " + variantDetailId);
                return "redirect:/products";
            }
            redirectAttributes.addFlashAttribute("successMessage", "Product variant detail deleted successfully: " + variantDetailId);
            return "redirect:/products";
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/home";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product variant detail: " + e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/{productId}/variants/add")
    public ModelAndView addVariant(@PathVariable String productId, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return new ModelAndView("redirect:/login");
        }
        try {
            ModelAndView modelAndView = new ModelAndView("html/Phone/formVariants");

            modelAndView.addObject("productId", productId);

            modelAndView.addObject("usageCategories", usageCategoryService.getAllUsageCategories().getData());

            ProductVariantRequest_FE newVariant = new ProductVariantRequest_FE();
            modelAndView.addObject("variant", newVariant);

            return modelAndView;
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (Exception e) {
            return new ModelAndView("redirect:/home");
        }
    }

    @PostMapping("/{productId}/variants/add")
    public String saveVariant(@PathVariable String productId,
                              @ModelAttribute("variant") ProductVariantRequest_FE variantRequest,
                              RedirectAttributes redirectAttributes,
                              HttpSession session,
                              HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {
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
                    DataResponse<UploadFileResponse> uploadFileResponseDataResponse = fileService.uploadFile(variantRequest.getAvatar(), "products/" + request.getName().replace(" ", "_"), accessToken);
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

                productService.createVariant(productId, request, accessToken);
                redirectAttributes.addFlashAttribute("successMessage", "Variant saved successfully!");
                return "redirect:/products/" + productId + "/variants";
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error saving variant: " + e.getMessage());
                return "redirect:/products/";
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/home";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/home";
        } catch (Exception e) {
            return "redirect:/home";
        }

    }

    @GetMapping("/{productId}/variants/{variantId}/details/add")
    public ModelAndView showAddVariantDetailForm(@PathVariable String productId,
                                                 @PathVariable String variantId,
                                                 Model model, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");

        if (accessToken == null) {
            return new ModelAndView("redirect:/login");
        }
        try {
            ModelAndView modelAndView = new ModelAndView("html/Phone/formVariantDetails");
            ProductVariantDetailRequest_FE request = new ProductVariantDetailRequest_FE();
            request.setColors(new ArrayList<>()); // Khởi tạo danh sách rỗng để tránh lỗi null

            model.addAttribute("productId", productId);
            model.addAttribute("variantId", variantId);
            colorService.getAllColors().getData().forEach(color ->
                    System.out.println("Color: " + color)
            );
            model.addAttribute("availableColors", colorService.getAllColors().getData());
            model.addAttribute("memories", memoryService.getAllMemories().getData());
            model.addAttribute("variantDetailRequest", request);
            return modelAndView;
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return new ModelAndView("redirect:/home");
        } catch (Exception e) {
            return new ModelAndView("redirect:/home");
        }
    }


    @PostMapping("/{productId}/variants/{variantId}/details/add")
    public String saveVariantDetail(
            @PathVariable String productId,
            @PathVariable String variantId,
            @ModelAttribute("variantDetailRequest") ProductVariantDetailRequest_FE request,
            RedirectAttributes redirectAttributes, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {
            System.out.println("Received Request: " + request);

            if (request.getColors() == null || request.getColors().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "No colors found in the request.");
                return "redirect:/products/" + productId + "/variants/" + variantId + "/details";
            }
            //Lay product theo id
            DataResponse<ProductResponse> productResponseDataResponse = productService.getProductById(productId);
            if (productResponseDataResponse == null || productResponseDataResponse.getStatus() != 200 || productResponseDataResponse.getData() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Product not found.");
                return "redirect:/products";
            }
            List<ProductResponse> products = (List<ProductResponse>) productResponseDataResponse.getData();
            ProductResponse product = products.get(0);

            //Lay variant theo id
            DataResponse<ProductVariantResponse> productVariantResponseDataResponse = productService.getVariantById(variantId);
            if (productVariantResponseDataResponse == null || productVariantResponseDataResponse.getStatus() != 200 || productVariantResponseDataResponse.getData() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Variant not found.");
                return "redirect:/products/" + productId + "/variants";
            }
            List<ProductVariantResponse> variants = (List<ProductVariantResponse>) productVariantResponseDataResponse.getData();
            ProductVariantResponse variant = variants.get(0);

//            List<ProductVariantDetailRequest.ColorRequest> serviceColors = request.getColors().stream().map(color -> {
//                ProductVariantDetailRequest.ColorRequest serviceColor = new ProductVariantDetailRequest.ColorRequest();
//                serviceColor.setColorId(color.getColorId());
//                serviceColor.setQuantity(color.getQuantity());
//                return serviceColor;
//            }).collect(Collectors.toList());
            for (ProductVariantDetailRequest_FE.ColorRequest color : request.getColors()) {
                List<ProductsImageRequest> imagesProductsImageRequests = new ArrayList<>();
                ProductVariantDetailRequest.ColorRequest serviceColor = new ProductVariantDetailRequest.ColorRequest();
                serviceColor.setColorId(color.getColorId());
                serviceColor.setQuantity(color.getQuantity());
                MultipartFile[] images = color.getImages();
                for (MultipartFile image : images) {
                    if (image == null || image.isEmpty()) {
                        System.out.println("Hình ảnh bị null hoặc rỗng.");
                    } else {
                        System.out.println("Hình ảnh nhận được: " + image.getOriginalFilename());
                        DataResponse<UploadFileResponse> uploadFileResponseDataResponse = fileService.uploadFile(image, "products/" + variant.getName().replace(" ", "_") + "/" + request.getMemid() + "/" + color.getColorId(), accessToken);
                        if (uploadFileResponseDataResponse == null) {
                            System.out.println("Error uploading file.");
                            continue;
                        }

                        if (uploadFileResponseDataResponse.getStatus() == 200 && uploadFileResponseDataResponse.getData() == null) {
                            System.out.println("Dữ liệu avatar bị null.");
                        } else {
                            List<UploadFileResponse> uploadFileResponses = (List<UploadFileResponse>) uploadFileResponseDataResponse.getData();
                            String imageURL = uploadFileResponses.get(0).getFileName();
                            ProductsImageRequest productsImageRequest = new ProductsImageRequest("products/" + variant.getName().replace(" ", "_") + "/" + request.getMemid() + "/" + color.getColorId() + "/" + imageURL, false);
                            imagesProductsImageRequests.add(productsImageRequest);
                        }
                    }
                }
                List<ProductVariantDetailRequest.ColorRequest> serviceColors = List.of(serviceColor);
                ProductVariantDetailRequest serviceRequest = new ProductVariantDetailRequest();
                serviceRequest.setMemid(request.getMemid());
                serviceRequest.setPrice(request.getPrice());
                serviceRequest.setSale(request.getSale());
                serviceRequest.setColors(serviceColors);
                DataResponse<String> productVariantDetailResponseDataResponse = productService.createVariantDetail(variantId, Collections.singletonList(serviceRequest), accessToken);
                if (productVariantDetailResponseDataResponse == null || productVariantDetailResponseDataResponse.getStatus() != 200) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error adding variant detail.");
                } else {
                    List<String> productVariantDetailResponses = (List<String>) productVariantDetailResponseDataResponse.getData();
                    String detailId = productVariantDetailResponses.get(0);

                    DataResponse<ProductsImageResponse> productImageResponseDataResponse = productImgService.createProductImg(detailId, imagesProductsImageRequests, accessToken);
                    if (productImageResponseDataResponse == null || productImageResponseDataResponse.getStatus() != 200) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Error saving image.");
                    } else if (productImageResponseDataResponse.getData() == null) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Error saving image.");
                    } else {
                        List<ProductsImageResponse> productsImageResponses = (List<ProductsImageResponse>) productImageResponseDataResponse.getData();
                        System.out.println("Dữ liệu hình ảnh sau khi lưu:");
                        System.out.println(productsImageResponses);
                    }

                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Variant detail added successfully!");
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/login";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding variant detail: " + e.getMessage());
        }
        return "redirect:/products/" + productId + "/variants/" + variantId + "/details";
    }


    @GetMapping("/{productId}/variants/{variantId}/details/{detailId}")
    public String viewDetail(
            @PathVariable String productId,
            @PathVariable String variantId,
            @PathVariable String detailId,
            Model model, HttpSession session, HttpServletResponse response) {

        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {

            DataResponse<ProductDetailResponse> detailResponseDataResponse = productService.getVariantDetailById(detailId);
            if (detailResponseDataResponse == null || detailResponseDataResponse.getStatus() != 200 || detailResponseDataResponse.getData() == null) {
                return "redirect:/products/" + productId + "/variants/" + variantId + "/details";
            }
            List<ProductDetailResponse> details = (List<ProductDetailResponse>) detailResponseDataResponse.getData();
            ProductDetailResponse detail = details.get(0);
            model.addAttribute("detail", detail);
            model.addAttribute("productId", productId);
            model.addAttribute("variantId", variantId);
            return "html/Phone/detailVariantsDetails";
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/login";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }


    @GetMapping("/{productId}/variants/{variantId}/details/update/{detailId}")
    public String showUpdateDetailForm(
            @PathVariable String productId,
            @PathVariable String variantId,
            @PathVariable String detailId,
            Model model, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {
            DataResponse<ProductDetailResponse> detailResponseDataResponse = productService.getVariantDetailById(detailId);

            if (detailResponseDataResponse == null || detailResponseDataResponse.getStatus() != 200 || detailResponseDataResponse.getData() == null) {
                return "redirect:/products/" + productId + "/variants/" + variantId + "/details";
            }

            List<ProductDetailResponse> details = (List<ProductDetailResponse>) detailResponseDataResponse.getData();
            ProductDetailResponse detail = details.get(0);

            model.addAttribute("detail", detail);
            model.addAttribute("productId", productId);
            model.addAttribute("variantId", variantId);
            model.addAttribute("ProductStatus", ProductStatus.values());
            return "html/Phone/updateDetail";
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/login";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @PostMapping("/{productId}/variants/{variantId}/details/update/{detailId}")
    public String updateDetail(
            @PathVariable String productId,
            @PathVariable String variantId,
            @PathVariable String detailId,
            @Valid @ModelAttribute ProductDetailUpdateRequest updateRequest,
            BindingResult result,
            RedirectAttributes redirectAttributes, HttpSession session, HttpServletResponse response1) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed: Sale is invalid or out of range (0.00-1.00).");
            return "redirect:/products/" + productId + "/variants/" + variantId + "/details/update/" + detailId;
        }
        try {
            ProductStatus status = ProductStatus.fromString(updateRequest.getStatus().toString());
            updateRequest.setStatus(status);
//            updateRequest.setSale(updateRequest.getSale() / 100);

            // Gửi yêu cầu cập nhật đến service
            DataResponse<Boolean> response = productService.updateVariantDetail(detailId, updateRequest, accessToken);

            // Kiểm tra kết quả và xử lý phản hồi
            if (response == null || response.getStatus() != 200) {
                String message = response == null ? "Error updating product detail." : response.getMessage();
                redirectAttributes.addFlashAttribute("errorMessage", "Error updating product detail." + message);
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Product detail updated successfully!");
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/login";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid product status: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unexpected error occurred: " + e.getMessage());
        }

        // Điều hướng về trang chi tiết
        return "redirect:/products/" + productId + "/variants/" + variantId + "/details";
    }

    @GetMapping("/{productId}/variants/{variantId}")
    public String showDetailVariant(@PathVariable String productId, @PathVariable String variantId, Model model, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {
            DataResponse<ProductVariantResponse> variantResponse = productService.getVariantById(variantId);
            if (variantResponse == null || variantResponse.getStatus() != 200 || variantResponse.getData() == null) {
                return "redirect:/products/" + productId + "/variants";
            }
            List<ProductVariantResponse> variants = (List<ProductVariantResponse>) variantResponse.getData();
            ProductVariantResponse variant = variants.get(0);
            model.addAttribute("variant", variant);
            model.addAttribute("productId", productId);
            model.addAttribute("variantId", variantId);

            return "html/Phone/detailVariants";
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/login";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/{productId}/variants/{variantId}/attributes")
    public String showAttributesByVariant(@PathVariable String productId, @PathVariable String variantId, Model model, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {
            DataResponse<ValueResponse> attributeResponse = attributeService.getAttributesByVariantId(variantId);
            if (attributeResponse == null || attributeResponse.getStatus() != 200 || attributeResponse.getData() == null) {
                return "redirect:/products/" + productId + "/variants/" + variantId + "/details";
            }
            List<ValueResponse> attributeValues = (List<ValueResponse>) attributeResponse.getData();
            attributeValues.forEach(System.out::println);
            model.addAttribute("attributes_values", attributeValues);
            model.addAttribute("productId", productId);
            model.addAttribute("variantId", variantId);

            return "html/Phone/showAttribute";
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/login";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/{productId}/variants/{variantId}/attributes/create")
    public String showCreateAttributeValueForm(@PathVariable String productId,
                                               @PathVariable String variantId,
                                               Model model, HttpSession session, HttpServletResponse response) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {
            // Get all attributes
            DataResponse<AttributeResponse> attributesResponse = attributeService.getAllAttribute();
            if (attributesResponse == null || attributesResponse.getStatus() != 200 || attributesResponse.getData() == null) {
                model.addAttribute("errorMessage", "Failed to load attributes.");
                return "redirect:/products/" + productId + "/variants/" + variantId + "/attributes";
            }

            List<AttributeResponse> attributes = (List<AttributeResponse>) attributesResponse.getData();
            model.addAttribute("attributes", attributes);
            model.addAttribute("productId", productId);
            model.addAttribute("variantId", variantId);
            return "html/Phone/createAttributeValue";
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/login";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @PostMapping("/{productId}/variants/{variantId}/attributes/create")
    public String createAttributeValueByVariantId(
            @PathVariable String productId,
            @PathVariable String variantId,
            @ModelAttribute AttributeValueRequest attributeValueRequest,
            RedirectAttributes redirectAttributes, HttpSession session, HttpServletResponse response1) {
        String accessToken = (String) session.getAttribute("accessToken");

        if (accessToken == null) {
            return "redirect:/login";
        }
        try {
            System.out.println("Received Request: " + attributeValueRequest);

            List<AttributeValueRequest> attributeValues = List.of(attributeValueRequest);
            DataResponse<Object> response = attributeService.createAttributeValueByVariantId(variantId, attributeValues, accessToken);

            if (response == null || response.getStatus() != 200) {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to create attribute values.");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Attribute values created successfully!");
            }

            // Redirect back to the attributes page
            return "redirect:/products/" + productId + "/variants/" + variantId + "/attributes";
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/login";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @PostMapping("/{productId}/variants/{variantId}/attributes/delete/{valueId}")
    public String deleteAttributeValue(
            @PathVariable String productId,
            @PathVariable String variantId,
            @PathVariable String valueId,
            RedirectAttributes redirectAttributes, HttpSession session, HttpServletResponse response1) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {
            DataResponse<ValueResponse> response = attributeService.deleteValue(valueId, accessToken);
            if (response == null || response.getStatus() != 200) {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete attribute value.");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Attribute value deleted successfully!");
            }

            // Redirect back to the attributes page
            return "redirect:/products/" + productId + "/variants/" + variantId + "/attributes";
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/login";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/{productId}/variants/{variantId}/attributes/update/{valueId}")
    public String showUpdateAttributeValueForm(
            @PathVariable String productId,
            @PathVariable String variantId,
            @PathVariable String valueId,
            Model model, HttpSession session, HttpServletResponse response1) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {

            // Get attribute value by ID
            DataResponse<ValueResponse> valueResponse = attributeService.getValueById(valueId);
            if (valueResponse == null || valueResponse.getStatus() != 200 || valueResponse.getData() == null) {
                model.addAttribute("errorMessage", "Failed to load attribute value.");
                return "redirect:/products/" + productId + "/variants/" + variantId + "/attributes";
            }


            List<ValueResponse> values = (List<ValueResponse>) valueResponse.getData();
            ValueResponse value = values.get(0);
            System.out.println("Value: " + value);

            // Get Attribute by Name
            DataResponse<AttributeResponse> attributeResponse = attributeService.getAttributeById(value.getAttributeId());
            if (attributeResponse == null || attributeResponse.getStatus() != 200 || attributeResponse.getData() == null) {
                model.addAttribute("errorMessage", "Failed to load attribute.");
                return "redirect:/products/" + productId + "/variants/" + variantId + "/attributes";
            }
            List<AttributeResponse> attributes = (List<AttributeResponse>) attributeResponse.getData();
            AttributeResponse attribute = attributes.get(0);

            model.addAttribute("attribute", attribute);
            model.addAttribute("value", value);
            model.addAttribute("productId", productId);
            model.addAttribute("variantId", variantId);

            return "html/Phone/updateValue";
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/login";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @PostMapping("/{productId}/variants/{variantId}/attributes/update/{valueId}")
    public String updateAttributeValue(
            @PathVariable String productId,
            @PathVariable String variantId,
            @ModelAttribute AttributeValueUpdateRequest updateRequest,
            RedirectAttributes redirectAttributes, HttpSession session, HttpServletResponse response1) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {

            System.out.println("Received Request: " + updateRequest);
            DataResponse<ValueResponse> response = attributeService.updateValueProductVariant(variantId, updateRequest, accessToken);
            if (response == null || response.getStatus() != 200) {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to update attribute value.");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Attribute value updated successfully!");
            }
            return "redirect:/products/" + productId + "/variants/" + variantId + "/attributes";

        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            return "redirect:/login";
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

}
