package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipValidation;
import com.space.repository.ShipRepository;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;



@RestController
@RequestMapping(path = "/rest")
public class ShipContoller {
    @Autowired
    ShipRepository shipRepository;
    @Autowired
    ShipService shipService;



    @GetMapping(path = "ships",produces = "application/json")
    ResponseEntity<List<Ship>> all(
            @RequestParam Map<String,String> allParams
   ){
        return  new ResponseEntity<>(shipService.find(allParams), HttpStatus.OK);

    }

    @GetMapping(path = "ships/count",produces = "application/json")
    ResponseEntity<Long> count(
            @RequestParam Map<String,String> allParams

    ) {

        long size =  shipService.countAll(allParams);
        return  new ResponseEntity<>(size, HttpStatus.OK);

    }

    @PostMapping(path = "ships/{id}")
    ResponseEntity<Ship> update(@PathVariable Optional<String> id, @RequestBody Map<String,String> map){
        if (id.isPresent()){
            long ID = 0;
            try{
                ID = Long.valueOf(id.get());
            }
            catch (Exception e){
                return    new ResponseEntity<>( HttpStatus.BAD_REQUEST);
            }
            if (ID<=0){
                return    new ResponseEntity<>( HttpStatus.BAD_REQUEST);
            }

            Optional<Ship>  shipSearch = shipService.findOne(ID);
            if (shipSearch.isPresent()){
                Ship old = shipSearch.get();
                if (map.size()==0) return new ResponseEntity<>(old, HttpStatus.OK);
                Ship ship = ShipValidation.validateUpdate(map,shipSearch.get());
                if (ship==null) return new ResponseEntity<>(old, HttpStatus.BAD_REQUEST);
                ship = shipService.save(ship);
                return    new ResponseEntity<>(ship, HttpStatus.OK);
            }
            else {
                return    new ResponseEntity<>( null,HttpStatus.NOT_FOUND);
            }
        }
        else return new ResponseEntity(null,HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "ships")
    ResponseEntity<Ship> create( @RequestBody Map<String, String> map){

        Ship ship = ShipValidation.validate(map);
        if (ship==null)  return new ResponseEntity(HttpStatus.BAD_REQUEST);
        ship = shipService.save(ship);
        if (ship==null ) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        return    new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @DeleteMapping(path = "ships/{id}")
    ResponseEntity    delete(@PathVariable Optional<String> id)
    {
        if (id.isPresent()){
            long ID = 0;
            try{
                ID = Long.valueOf(id.get());
            }
            catch (Exception e){
                return    new ResponseEntity<>( HttpStatus.BAD_REQUEST);
            }
            if (ID<=0){
                return    new ResponseEntity<>( HttpStatus.BAD_REQUEST);
            }
            Optional<Ship>  ship = shipService.findOne(ID);
            if (ship.isPresent()){
                shipService.delete(ship.get());
                return    new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return    new ResponseEntity<>( HttpStatus.NOT_FOUND);
            }
        }
         else return new ResponseEntity(HttpStatus.BAD_REQUEST);
//       return    new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(path = "ships/{id}",produces = "application/json")
    ResponseEntity<Ship> getById(@PathVariable Optional<String> id) {
        if (id.isPresent()){
            long ID = 0;
            try{
              ID = Long.valueOf(id.get());
            }
            catch (Exception e){
                return    new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            if (ID<=0){
                return    new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            Optional<Ship>  ship = shipService.findOne(ID);
            if (ship.isPresent()){

                return    new ResponseEntity<>(ship.get(), HttpStatus.OK);
            }
            else {
                return    new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

        }else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }



}

