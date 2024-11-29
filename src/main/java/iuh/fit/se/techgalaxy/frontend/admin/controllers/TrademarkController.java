package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.TrademarkRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.TrademarkResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Trademark;
import iuh.fit.se.techgalaxy.frontend.admin.mapper.TrademarkMapper;
import iuh.fit.se.techgalaxy.frontend.admin.services.TrademarkService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/trademarks")
public class TrademarkController {
    private final TrademarkService trademarkService;

    @Autowired
    public TrademarkController(TrademarkService trademarkService) {
        this.trademarkService = trademarkService;
    }

    @GetMapping
    public ModelAndView findAll(ModelAndView model) {
        DataResponse<TrademarkResponse> response = trademarkService.getAllTrademarks();
        List<TrademarkResponse> trademarks = null;
        if (response != null) {
            trademarks = (List<TrademarkResponse>) response.getData();
        }
        model.addObject("trademarks", trademarks);
        model.setViewName("html/Trademark/showTrademark");
        return model;
    }

    @GetMapping("/add")
    public ModelAndView showAdd(ModelAndView model) {
        TrademarkRequest trademarkRequest = new TrademarkRequest();
        model.setViewName("html/Trademark/formTrademark");
        model.addObject("trademarkRequest", trademarkRequest); //
        return model;
    }

    @GetMapping("/update/{id}")
    public ModelAndView showUpdate(ModelAndView model, @PathVariable String id) {
        DataResponse<TrademarkResponse> response = trademarkService.findById(id);
        TrademarkResponse trademarkResponse = ((List<TrademarkResponse>) response.getData()).get(0);
        Trademark trademark = TrademarkMapper.INSTANCE.toTrademarkFromResponse(trademarkResponse);
        TrademarkRequest trademarkRequest = TrademarkMapper.INSTANCE.toTrademarkRequest(trademark);
        model.setViewName("html/Trademark/formTrademark");
        model.addObject("trademarkRequest", trademarkRequest);
        return model;
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(ModelAndView model, @PathVariable String id) {
        DataResponse<TrademarkResponse> response = trademarkService.findById(id);
        TrademarkResponse trademarkResponse = ((List<TrademarkResponse>) response.getData()).get(0);
        model.setViewName("html/Trademark/detailTrademark");
        model.addObject("trademarkResponse", trademarkResponse);
        return model;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("trademarkRequest") TrademarkRequest trademarkRequest,
                             ModelAndView model,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            boolean isSaved = false;
            if (trademarkRequest.getId() == null || trademarkRequest.getId().isEmpty()) {
                if (trademarkService.save(trademarkRequest.getName(), accessToken).getData() != null)
                    isSaved = true;
            } else {
                if (trademarkService.update(trademarkRequest, accessToken).getData() != null)
                    isSaved = true;
            }
            if (isSaved) {
                redirectAttributes.addFlashAttribute("successMessage", "Save trademark success");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Save trademark failed");
            }
            model.setViewName("redirect:/trademarks");
            return model;
        } catch (
                HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Save trademark failed");
            return model;
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Save trademark failed");
            return model;
        } catch (Exception e) {
            model.setViewName("redirect:/home");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Save trademark failed");
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
            model.setViewName("redirect:/login");
            return model;
        }
        try {
            trademarkService.delete(id, accessToken);
            redirectAttributes.addFlashAttribute("successMessage", "Delete trademark success");
            model.setViewName("redirect:/trademarks");
            return model;
        } catch (
                HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Delete trademark failed");
            return model;
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden request: " + e.getMessage());
            model.setViewName("redirect:/home");
            redirectAttributes.addFlashAttribute("errorMessage", "Delete trademark failed");
            return model;
        } catch (Exception e) {
            model.setViewName("redirect:/home");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Delete trademark failed");
            return model;
        }
    }
}
