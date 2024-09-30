package com.nacosfunaabpay.paymentplatform.controller;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.service.LevelService;
import com.nacosfunaabpay.paymentplatform.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private LevelService levelService;

    @Autowired ProgramService programService;

    @GetMapping("")
    public String renderHomePage(Model model) {
        model.addAttribute("paymentForm", new PaymentFormDTO());
        model.addAttribute("levels", levelService.getAllLevels());
        model.addAttribute("programs", programService.getAllPrograms());
        return "home";
    }

}
