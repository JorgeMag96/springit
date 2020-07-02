package com.jorge.springit.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String home(Model model, HttpServletRequest httpServletRequest, Locale locale){
        model.addAttribute("message","Hello, Spring Boot 2 model attribute.");
        System.out.println(locale.toString());
        return "Hello, Spring Boot 2, your locale is = "+locale.toString()+" ?";
    }
}
