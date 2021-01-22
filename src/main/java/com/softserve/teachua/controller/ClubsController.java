package com.softserve.teachua.controller;

import com.softserve.teachua.dto.*;
import com.softserve.teachua.liqpay.LiqPayObj;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.service.CityService;
import com.softserve.teachua.service.ClubService;
import java.util.Iterator;
import java.util.List;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClubsController {

    private ClubRepository clubRepository;
    private CityService cityService;
    private ClubService clubService;
    private LiqPayObj liqPayObj;

    @Autowired
    public ClubsController(ClubRepository clubRepository, CityService cityService,
                           ClubService clubService, LiqPayObj liqPayObj) {
        this.clubRepository = clubRepository;
        this.cityService = cityService;
        this.clubService = clubService;
        this.liqPayObj = liqPayObj;
    }

    @GetMapping({"/teachua", "/"})
    public String redirect() {
        return "redirect:/clubs/1";
    }

    @GetMapping("/clubs/{id}")
    public String getClubsPage(@PathVariable("id") Long id, Model model,
                               @PageableDefault(value = 8) Pageable pageable,
                               @RequestParam(value = "search", required = false) String search){
        liqPayObj.liqPayParam(model);
        model.addAttribute("seacrhModel", new SearchModel());
        model.addAttribute("cityModel", new CityIdModel());
        model.addAttribute("cities", curentCityOnTopList(cityService.getAll(),id));
        model.addAttribute("currentCity", cityService.getCityById(id));
        if (search==null||search.equals("")){
            PageableAdvancedDto<ClubDto> allClubDto = clubService.getByCityId(pageable,id);
            model.addAttribute("pageable", allClubDto);
            model.addAttribute("search","");
        }else {
            PageableAdvancedDto<ClubDto> allClubDto  = clubService.getByCityIdAndSearchParam(id,search,pageable);
            model.addAttribute("search",search);
            model.addAttribute("pageable", allClubDto);
        }
        return "clubs";
    }

    @PostMapping("/clubs")
    public String getByCity(@ModelAttribute("cityModel") CityIdModel cityModel, Model model, @PageableDefault(value = 8)Pageable pageable){
        PageableAdvancedDto<ClubDto> allClubDto = clubService.getByCityId(pageable,cityModel.getId());
        model.addAttribute("cities", curentCityOnTopList(cityService.getAll(),cityModel.getId()));
        model.addAttribute("currentCity", cityService.getCityById(cityModel.getId()));
        model.addAttribute("pageable", allClubDto);
        model.addAttribute("seacrhModel", new SearchModel());
        model.addAttribute("cityModel", new CityIdModel());
        model.addAttribute("search","");
        liqPayObj.liqPayParam(model);
        return "redirect:/clubs/"+cityModel.getId();
    }

    private List<CityDto> curentCityOnTopList(List<CityDto> cityDtos, Long id){
        Iterator<CityDto> iterator = cityDtos.listIterator();
        CityDto dto;
        while (iterator.hasNext()){
            dto =  iterator.next();
            if(dto.getId().equals(id)){
                iterator.remove();
                cityDtos.add(0,dto);
                return cityDtos;
            }
        }
        return cityDtos;
    }
}
