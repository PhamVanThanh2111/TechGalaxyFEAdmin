package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.SystemUserRequestDTO;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.SystemUserResponseDTO;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.UploadFileResponse;
import iuh.fit.se.techgalaxy.frontend.admin.services.FileService;
import iuh.fit.se.techgalaxy.frontend.admin.services.SystemUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/systemUsers")
public class SystemUserController {
    private final SystemUserService systemUserService;
    private final FileService fileService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("avatar"); // Bỏ qua field avatar
    }

    @Autowired
    public SystemUserController(SystemUserService systemUserService, FileService fileService) {
        this.systemUserService = systemUserService;
        this.fileService = fileService;
    }

    @GetMapping
    public ModelAndView showUsersSystem(ModelAndView model, HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            new ModelAndView("redirect:/login");
        }
        try {
            DataResponse<SystemUserResponseDTO> response = systemUserService.findAll(accessToken);
            List<SystemUserResponseDTO> list = null;
            if (response != null) {
                list = (List<SystemUserResponseDTO>) response.getData();
            }
            model.addObject("sys_users", list);
            model.setViewName("html/SystemUser/showSystemUser");
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

    @GetMapping("/add")
    public ModelAndView add(ModelAndView model) {
        SystemUserRequestDTO requestDTO = new SystemUserRequestDTO();
        model.setViewName("html/SystemUser/formSystemUser");
        model.addObject("systemUserRequestDTO", requestDTO);
        return model;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("systemUserRequestDTO") SystemUserRequestDTO request,
                             @RequestParam MultipartFile avatar,
                             BindingResult bindingResult,
                             ModelAndView model,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) throws IOException, URISyntaxException {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            new ModelAndView("redirect:/login");
        }
        try {
            if (bindingResult.hasErrors()) {
                model.setViewName("html/SystemUser/formSystemUser");
                return model;
            }
            if (!avatar.isEmpty()) {
                DataResponse<UploadFileResponse> response = fileService.uploadFile(avatar, "systemUser/avatar", accessToken);
                UploadFileResponse uploadFileResponse = ((List<UploadFileResponse>) response.getData()).get(0);
                System.out.println(uploadFileResponse.getFileName());
                request.setAvatar(uploadFileResponse.getFileName());
            }
            boolean isSaved = false;
            if (request.getId() == null || request.getId().isEmpty()) {
                if (systemUserService.create(request, accessToken).getData() != null)
                    isSaved = true;
            } else {
                if (request.getAvatar() == null || request.getAvatar().isEmpty()) {
                    SystemUserResponseDTO tmp = ((List<SystemUserResponseDTO>) systemUserService.findById(request.getId(), accessToken).getData()).get(0);
                    request.setAvatar(tmp.getAvatar());
                }
                if (systemUserService.update(request, accessToken).getData() != null)
                    isSaved = true;
            }
            if (isSaved) {
                redirectAttributes.addFlashAttribute("successMessage", "Save system user success");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Save system user failed");
            }
            model.setViewName("redirect:/systemUsers");
            return model;
        } catch (
                HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Save system user failed");
            return model;
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Save system user failed");
            return model;
        } catch (Exception e) {
            model.setViewName("redirect:/home");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Save system user failed");
            return model;
        }
    }

    @GetMapping("/update/{id}")
    public ModelAndView formUpdate(ModelAndView model,
                                   @PathVariable String id,
                                   HttpSession session) {
        try {
            String accessToken = (String) session.getAttribute("accessToken");
            if (accessToken == null) {
                new ModelAndView("redirect:/login");
            }
            List<SystemUserResponseDTO> list = (List<SystemUserResponseDTO>) systemUserService.findById(id, accessToken).getData();
            model.addObject("systemUserRequestDTO", list.get(0));
            model.setViewName("html/SystemUser/formSystemUser");
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

    @GetMapping("/delete/{id}")
    public ModelAndView delete(ModelAndView model,
                               @PathVariable String id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            new ModelAndView("redirect:/login");
        }
        try {
            systemUserService.delete(id, accessToken);
            redirectAttributes.addFlashAttribute("successMessage", "Delete system user success");
            model.setViewName("redirect:/systemUsers");
            return model;
        } catch (
                HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Delete system user failed");
            return model;
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Delete system user failed");
            return model;
        } catch (Exception e) {
            model.setViewName("redirect:/home");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Delete system user failed");
            return model;
        }
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(ModelAndView model,
                               @PathVariable String id,
                               HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            new ModelAndView("redirect:/login");
        }
        try {
            List<SystemUserResponseDTO> list = (List<SystemUserResponseDTO>) systemUserService.findById(id, accessToken).getData();
            model.addObject("systemUser", list.get(0));
            model.setViewName("html/SystemUser/detailSystemUser");
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
}
