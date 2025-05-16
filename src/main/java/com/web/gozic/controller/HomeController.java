package com.web.gozic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.web.gozic.repository.ClothesRepository;

@Controller
public class HomeController {

    @Autowired
    private ClothesRepository clothesRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("menClothes", clothesRepository.findByType("men"));
        model.addAttribute("womenClothes", clothesRepository.findByType("women"));
        model.addAttribute("boysClothes", clothesRepository.findByType("boys"));
        model.addAttribute("girlsClothes", clothesRepository.findByType("girls"));
        return "home";
    }
}