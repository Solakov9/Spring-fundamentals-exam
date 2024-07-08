package com.paintingscollectors.controller;

import com.paintingscollectors.config.UserSession;
import com.paintingscollectors.model.dto.UserLoginDTO;
import com.paintingscollectors.model.dto.UserRegisterDTO;
import com.paintingscollectors.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {
    private final UserSession userSession;
    private UserService userService;

    public UserController(UserSession userSession, UserService userService) {
        this.userSession = userSession;
        this.userService = userService;
    }

    @ModelAttribute("registerData")
    public UserRegisterDTO registerDTO() {
        return new UserRegisterDTO();
    }
    @ModelAttribute("loginData")
    public UserLoginDTO loginDTO() {
        return new UserLoginDTO();
    }

    @GetMapping("/register")
    public String register() {
        if (userSession.isLoggedIn()){
            return "redirect:/home";
        }
        return "/register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid UserRegisterDTO data,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes
    ) {
        if (userSession.isLoggedIn()){
            return "redirect:/home";
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerData",data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            return "redirect:/register";
        }
        boolean success = userService.register(data);
        if (!success){
            return"redirect:/register";
        }
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String login(){
        if (userSession.isLoggedIn()){
            return "redirect:/home";
        }
        return"/login";
    }
    @PostMapping("/login")
    public String doLogin(@Valid UserLoginDTO data,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes
    ) {
        if (userSession.isLoggedIn()){
            return "redirect:/home";
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("loginData",data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.loginData", bindingResult);
            return "redirect:/login";
        }

        boolean success = userService.login(data);
        if (!success){
            redirectAttributes.addFlashAttribute("loginData",data);
            redirectAttributes.addFlashAttribute("userPassMismatch", true);
            return "redirect:/login";
        }
        return "redirect:/home";
    }
    @GetMapping("/logout")
    public String logout(){
        if (userSession.isLoggedIn()){
            userSession.logout();
        }
        return"redirect:/";
    }
    @PostMapping("/vote/{paintingId}")
    public String addToVoted(@PathVariable long paintingId){
        if (!userSession.isLoggedIn()){
            return "redirect:/";
        }

        userService.addToVoted(userSession.byId(),paintingId);
        return"redirect:/home";
    }
}
