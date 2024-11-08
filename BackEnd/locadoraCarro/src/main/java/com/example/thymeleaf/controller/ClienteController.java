package com.example.thymeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.thymeleaf.Service.ClienteService;
import com.example.thymeleaf.dto.ClienteDTO;
import com.example.thymeleaf.dto.MudarStatusClienteDTO;
import com.example.thymeleaf.entity.Cliente;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Cliente cliente) {
        try {
            service.save(cliente);
            return new ResponseEntity<>("Cliente cadastrado com sucesso", HttpStatus.CREATED);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(500));
        }
    }

    @GetMapping
    public ResponseEntity<?> get() {
        try {
            return new ResponseEntity<>(service.findAll(), HttpStatus.valueOf(200));
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(500));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> put(@PathVariable long id, @RequestBody ClienteDTO cliente) {
        try {
            service.edit(id, cliente);
            return new ResponseEntity<>("Conclusão nas alteraçoes do cliente", HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(500));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        try {
            return new ResponseEntity<>(service.delete(id), HttpStatus.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(500));
        }
    }

    @GetMapping("/index")
    public ResponseEntity<?> getIndex(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "false") boolean active,
            @RequestParam(defaultValue = " ") String nome, @RequestParam(defaultValue = " ") String cpf) {
        try {
            return new ResponseEntity<>(service.getAllPage(page, size, active,nome,cpf), HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(500));
        }
    }

    @PutMapping("/mudarstatus/{id}")
    public ResponseEntity<?> removeClinete(@PathVariable long id, @RequestBody MudarStatusClienteDTO cliente) {
        try {
            return new ResponseEntity<>(service.mudarStatus(id, cliente), HttpStatus.valueOf(200));
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(500));
        }
    }
}
