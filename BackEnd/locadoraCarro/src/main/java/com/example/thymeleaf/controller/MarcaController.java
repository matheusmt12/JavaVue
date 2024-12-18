package com.example.thymeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.thymeleaf.Service.MarcaService;
import com.example.thymeleaf.dto.MarcaDTO;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequestMapping(value = "/marca")
@RestController
public class MarcaController {

    @Autowired
    public MarcaService marcaService;


    @PostMapping

    public ResponseEntity post( @RequestBody MarcaDTO marca){

        try {
            marcaService.save(marca);
            return new ResponseEntity<>("Nova Marca Adicionada :" + marca.getName(),HttpStatus.CREATED);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable long id){
    
        try {
            return new ResponseEntity<>(marcaService.getMarca(id), HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "0") int size){
        try{
            return new ResponseEntity<>(marcaService.findAll(page, size),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity put(@PathVariable long id, @RequestBody MarcaDTO marca){
        try {
            return new ResponseEntity<>(marcaService.put(id,marca),HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(),HttpStatus.valueOf(500));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id){
        try {
            return new ResponseEntity<>(marcaService.delete(id),HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(),HttpStatus.valueOf(500));
        }
    }

    @GetMapping("/index")
    public ResponseEntity<?> getAllMarcas(){
        try {
            return new ResponseEntity<>(marcaService.getAllMarcas(), HttpStatus.valueOf(200));
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(500));
        }
    }
}
