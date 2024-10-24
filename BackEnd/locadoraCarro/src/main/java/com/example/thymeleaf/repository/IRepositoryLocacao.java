package com.example.thymeleaf.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.thymeleaf.entity.Locacoes;


@Repository
public interface IRepositoryLocacao extends JpaRepository<Locacoes,Long> {

    Page<Locacoes> findAll(Pageable pageable);

    Page<Locacoes> findByFinalizada(Pageable pageable, boolean finalizada);
}
