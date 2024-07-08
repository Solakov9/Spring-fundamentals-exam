package com.paintingscollectors.controller;

import com.paintingscollectors.config.UserSession;
import com.paintingscollectors.model.dto.AddPaintingDTO;
import com.paintingscollectors.service.PaintingService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class PaintingController {
    private PaintingService paintingService;
    private UserSession userSession;

    public PaintingController(PaintingService paintingService, UserSession userSession) {
        this.paintingService = paintingService;
        this.userSession = userSession;
    }

    @ModelAttribute("paintingData")
    public AddPaintingDTO paintingDTO() {
        return new AddPaintingDTO();
    }
    @GetMapping("/add-painting")
    public String addPainting() {
        if (!userSession.isLoggedIn()){
            return "redirect:/";
        }
        return "/add-painting";
    }
    @PostMapping("/add-painting")
    public String doAddPainting(@Valid AddPaintingDTO data,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes
    ){if (!userSession.isLoggedIn()){
        return "redirect:/";
    }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("paintingData",data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.paintingData", bindingResult);
            return "redirect:/add-painting";
        }
        paintingService.addPainting(data);
        return "redirect:/home";
    }
    @PostMapping("/add-favourites/{paintingId}")
    public String addToFavourites(@PathVariable long paintingId){
        if (!userSession.isLoggedIn()){
            return "redirect:/";
        }
        paintingService.addToFavourite(userSession.byId(),paintingId);
        return"redirect:/home";
    }
    @DeleteMapping("/painting/{id}")
    public String deletePainting(@PathVariable long id){
        if (!userSession.isLoggedIn()){
            return "redirect:/";
        }
        paintingService.delete(id);
        return "redirect:/home";
    }

}
