package com.example.thymeleaf.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.thymeleaf.entity.Marca;

@Repository
public interface IRepositoryMarca extends JpaRepository<Marca,Long>{

    Page<Marca> findAll(Pageable pageable);

}
