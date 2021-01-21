package com.softserve.teachua.controller;

import com.softserve.teachua.dto.controller.CenterEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CityController {


    @GetMapping("/center/{id}")
    public CenterEntity getCenter(@PathVariable long id){
        return new CenterEntity(id,"City", "Adress", "Coordinate");
    }

    @PostMapping("/center")
    public CenterEntity createCenter(@RequestBody CenterEntity centerEntity){
        CenterEntity newCenter = new CenterEntity();
        newCenter=centerEntity;
        return newCenter;
    }

    @DeleteMapping("/center/{id}")
    public String deleteCenter(@PathVariable long id){
        return "Sity " + id + " is deleted";
    }

    @PutMapping("/center/{id}")
    public String updateCenter(@PathVariable long id)
    {
        return "Sity " + id + " is updated";
    }

    @GetMapping("/centres")
    public List<CenterEntity> getCenteres(){
        List<CenterEntity> centres = new ArrayList<CenterEntity>();
        centres.add(new CenterEntity(1L,"City1","Adress1","Coordinate1"));
        centres.add(new CenterEntity(1L,"City1","Adress2","Coordinate2"));
        centres.add(new CenterEntity(1L,"City1","Adress3","Coordinate3"));
        return centres;
    }

    @PostMapping("/centres")
    public List<CenterEntity> createCenteres(){
        List<CenterEntity> centres = new ArrayList<CenterEntity>();
        centres.add(new CenterEntity(1L,"City1","Adress1","Coordinate1"));
        centres.add(new CenterEntity(1L,"City1","Adress2","Coordinate2"));
        centres.add(new CenterEntity(1L,"City1","Adress3","Coordinate3"));
        return centres;
    }

    @DeleteMapping("/centres")
    public String deleteCenteres(){
        return "Centeres was deleted";
    }

    @PutMapping("/centres")
    public String updateCenteres()
    {
        return "Centres was updated";
    }



}
