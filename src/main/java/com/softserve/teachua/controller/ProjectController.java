package com.softserve.teachua.controller;

import com.softserve.teachua.config.LiqPayParent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ProjectController {
    private final LiqPayParent liqPayParent;

    @Autowired
    public ProjectController(LiqPayParent liqPayParent) {
        this.liqPayParent = liqPayParent;
    }

    @GetMapping("/project-page")
    public String projectPage(Model model) {
        liqPayParent.liqPayParam(model);
        return "projectpage";
    }

}
