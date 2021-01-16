package com.softserve.teachua.controller;

import com.softserve.teachua.liqpay.LiqPayObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProjectController {
    private final LiqPayObj liqPayObj;

    @Autowired
    public ProjectController(LiqPayObj liqPayObj) {
        this.liqPayObj = liqPayObj;
    }

    @GetMapping("/project-page")
    public String projectPage(Model model) {
        liqPayObj.liqPayParam(model);
        return "project-page";
    }

}
