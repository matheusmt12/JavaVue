package com.example.thymeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.thymeleaf.Service.LocacaoService;
import com.example.thymeleaf.dto.FinalizarAluguelDTO;
import com.example.thymeleaf.entity.Locacoes;
import com.example.thymeleaf.repository.IRepositoryLocacao;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/locacao")
public class LocadocaoController {

    @Autowired
    private LocacaoService service;

    @PostMapping
    public ResponseEntity post(@RequestBody Locacoes locacao) {
        try {
            return new ResponseEntity<>(service.save(locacao),HttpStatus.valueOf(201));
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(),HttpStatus.valueOf(500));
        }
    }

    @GetMapping
    public ResponseEntity getAll( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5")
     int size, @RequestParam(defaultValue = "false") boolean finalizada) {
        try {
            return new ResponseEntity<>(service.fidAll(page,size,finalizada),HttpStatus.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.valueOf(500));
        }
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id){
        try {
            return new ResponseEntity<>(service.delete(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.valueOf(500));
        }
    }

    @PutMapping("/finalizar")
    public ResponseEntity finalizarAluguel(@RequestBody FinalizarAluguelDTO finalizar) {
        try {
            return new ResponseEntity<>(service.finalizarAluguel(finalizar),HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(),HttpStatus.valueOf(500));
        }
    }
}
