package com.example.thymeleaf.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.thymeleaf.dto.CarroAndModeloDTO;
import com.example.thymeleaf.dto.CarroDTO;
import com.example.thymeleaf.dto.StatusCarroDTO;
import com.example.thymeleaf.entity.Carro;
import com.example.thymeleaf.entity.Modelo;
import com.example.thymeleaf.exceptions.NoFindCarroException;
import com.example.thymeleaf.exceptions.PlacaExistException;
import com.example.thymeleaf.repository.IRepositoryCarro;

import jakarta.transaction.Transactional;

@Service
public class CarroService {

    @Autowired
    private final IRepositoryCarro repositoryCarro;

    public CarroService(IRepositoryCarro repositoryCarro) {
        this.repositoryCarro = repositoryCarro;
    }

    @Transactional
    public long save(Carro carro) {

        if (repositoryCarro.findByPlaca(carro.getPlaca()) != null) {
            throw new PlacaExistException("Placa de carro ja cadastrada no sistema");
        }
        carro.setDisponivel(true);
        Carro c = repositoryCarro.save(carro);
        return c.getId();
    }

    public List<CarroDTO> getAll() {

        return repositoryCarro.findAll().stream()
                .map(carro -> getCarro(carro)).collect(Collectors.toList());
    }

    public Page<CarroDTO> findAll(int page, int size, boolean disponivel) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Carro> carro = repositoryCarro.findByDisponivel(pageable, disponivel);

        return carro.map(this::getCarro);
    }

    public CarroDTO getCarro(Carro carro) {
        return CarroDTO.builder()
                .id(carro.getId())
                .disponivel(carro.isDisponivel())
                .km(carro.getKm())
                .placa(carro.getPlaca())
                .modeloCarro(getModeloCarro(carro.getModelo()))
                .status(carro.getStatus())
                .build();
    }

    public CarroAndModeloDTO getModeloCarro(Modelo modelo) {
        return CarroAndModeloDTO.builder()
                .abs(modelo.isAbs())
                .air_bag(modelo.isAir_bag())
                .lugares(modelo.getLugares())
                .name(modelo.getName())
                .num_portas(modelo.getNum_portas()).build();
    }

    @Transactional
    public CarroDTO edit(long id, CarroDTO carro) {

        Carro c = repositoryCarro.findById(id).orElseThrow(() -> new NoFindCarroException("Carro nao encontrado"));

        c.setDisponivel(carro.isDisponivel());
        c.setId(id);
        c.setKm(carro.getKm());
        c.setPlaca(carro.getPlaca());
        return getCarro(c);

    }

    @Transactional
    public long delete(long id) {
        repositoryCarro.deleteById(id);
        return id;
    }
    @Transactional
    public String relatarStatus(StatusCarroDTO carro , long id){

        Carro c = repositoryCarro.findById(id).orElseThrow(() -> new NoFindCarroException("O carro não esta no sitema"));

        c.setDisponivel(carro.isStatus());
        c.setStatus(carro.getMenssagemStatus());
        
        repositoryCarro.save(c);

        return "Status do carro alterado com sucesso";
    }
}
