package com.example.thymeleaf.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.thymeleaf.entity.Locacoes;

import jakarta.transaction.Transactional;

@Repository
public interface IRepositoryLocacao extends JpaRepository<Locacoes,Long> {

    Page<Locacoes> findAll(Pageable pageable);

}
