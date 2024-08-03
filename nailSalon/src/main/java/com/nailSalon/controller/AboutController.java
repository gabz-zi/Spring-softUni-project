package com.nailSalon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/about-us")
    public String aboutUs() {

        return "about-us";
    }


}
