package com.example.thymeleaf.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thymeleaf.entity.Modelo;

public interface IRepositoryModelo extends JpaRepository<Modelo,Long>{

    Page<Modelo> findAll(Pageable pageable);
}
