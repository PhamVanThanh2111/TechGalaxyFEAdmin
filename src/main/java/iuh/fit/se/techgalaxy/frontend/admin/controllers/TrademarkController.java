package iuh.fit.se.techgalaxy.frontend.admin.controllers;

import iuh.fit.se.techgalaxy.frontend.admin.dto.request.TrademarkRequest;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
import iuh.fit.se.techgalaxy.frontend.admin.dto.response.TrademarkResponse;
import iuh.fit.se.techgalaxy.frontend.admin.entities.Trademark;
import iuh.fit.se.techgalaxy.frontend.admin.mapper.TrademarkMapper;
import iuh.fit.se.techgalaxy.frontend.admin.services.TrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
        DataResponse<TrademarkResponse> response = trademarkService.findAll();
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
    public ModelAndView save(@ModelAttribute("trademarkRequest") TrademarkRequest trademarkRequest, ModelAndView model, BindingResult binding) {
        if (trademarkRequest.getId() == null && trademarkRequest.getId().isEmpty()) {
            trademarkService.save(trademarkRequest.getName());
        } else {
            trademarkService.update(trademarkRequest);
        }
        model.setViewName("redirect:/trademarks");
        return model;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(ModelAndView model, @PathVariable String id) {
        trademarkService.delete(id);
        model.setViewName("redirect:/trademarks");
        return model;
    }
}
