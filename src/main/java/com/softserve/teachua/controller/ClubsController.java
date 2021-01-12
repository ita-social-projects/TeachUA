package com.softserve.teachua.controller;

import com.softserve.teachua.config.LiqPayParent;
import com.softserve.teachua.dto.CityIdModel;
import com.softserve.teachua.dto.SearchModel;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.service.CityService;
import com.softserve.teachua.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClubsController {

    private ClubRepository  clubRepository;
    private CityService cityService;
    private ClubService clubService;
    private LiqPayParent liqPayParent;

    @Autowired
    public ClubsController(ClubRepository clubRepository, CityService cityService,
                           ClubService clubService, LiqPayParent liqPayParent) {
        this.clubRepository = clubRepository;
        this.cityService = cityService;
        this.clubService = clubService;
        this.liqPayParent = liqPayParent;
    }
    @GetMapping("/clubs")
    public String getClubsPage(Model model){
        liqPayParent.liqPayParam(model);
        model.addAttribute("cities", cityService.getAll());
        model.addAttribute("currentCity", cityService.getCityById(1L));
        model.addAttribute("clubs", clubService.getByCityId(1L));
        model.addAttribute("seacrhModel", new SearchModel());
        model.addAttribute("cityModel", new CityIdModel());
        return "clubs";
    }
    @PostMapping("/clubs/select_city")
    public String getByCity(@ModelAttribute("cityModel") CityIdModel cityModel, Model model){
        model.addAttribute("cities", cityService.getAll());
        model.addAttribute("currentCity", cityService.getCityById(cityModel.getId()));
        model.addAttribute("clubs", clubService.getByCityId(cityModel.getId()));
        model.addAttribute("seacrhModel", new SearchModel());
        model.addAttribute("cityModel", new CityIdModel());
        liqPayParent.liqPayParam(model);
        return "clubs";
    }
    @PostMapping("/search/{id}")
    public String getBySearch(@PathVariable("id") Long id,@ModelAttribute("seacrhModel") SearchModel searchModel, Model model){
        model.addAttribute("cities", cityService.getAll());
        model.addAttribute("currentCity", cityService.getCityById(id));
        model.addAttribute("clubs", clubService.getByCityIdAndSearchParam(id,searchModel.getRequest()));
        model.addAttribute("seacrhModel", new SearchModel());
        model.addAttribute("cityModel", new CityIdModel());
        liqPayParent.liqPayParam(model);
        return "clubs";
    }
}
