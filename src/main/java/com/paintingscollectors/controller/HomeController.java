package com.paintingscollectors.controller;

import com.paintingscollectors.config.UserSession;
import com.paintingscollectors.model.entity.Painting;
import com.paintingscollectors.model.entity.User;
import com.paintingscollectors.repository.UserRepository;
import com.paintingscollectors.service.PaintingService;
import com.paintingscollectors.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class HomeController {

    private UserSession userSession;
    private UserService userService;
    private PaintingService paintingService;

    public HomeController(UserSession userSession, UserService userService, PaintingService paintingService) {
        this.userSession = userSession;
        this.userService = userService;
        this.paintingService = paintingService;
    }

    @GetMapping("/")
    public String nonLogged(){
        if (userSession.isLoggedIn()){
            return "redirect:/home";
        }
        return "index";
    }
    @GetMapping("/home")
    public String logged(Model model){
        if (!userSession.isLoggedIn()) {
            return "redirect:/";
        }
        Set<Painting> addedPaintings = userService.getAddedPaintings();
        model.addAttribute("userPaintings",addedPaintings);
        Set<Painting> otherUsersPaintings = userService.getOtherUsersPaintings();
        model.addAttribute("otherUserPaintings",otherUsersPaintings);
        Set<Painting> favouritePaintings = userService.getFavouritePaintings();
        model.addAttribute("favouritePaintings",favouritePaintings);
        List<Painting> topTwoPaintings = paintingService.getTopTwoPaintings();
        model.addAttribute("topTwoPaintings",topTwoPaintings);
        return "/home";
    }
}
