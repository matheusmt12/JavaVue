package com.example.thymeleaf.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.thymeleaf.dto.ClienteDTO;
import com.example.thymeleaf.entity.Cliente;
import com.example.thymeleaf.exceptions.NoFindClienteException;
import com.example.thymeleaf.repository.IRepositoryCliente;

import jakarta.transaction.Transactional;

@Service
public class ClienteService {


    @Autowired
    private final IRepositoryCliente repositoryCliente;
    public ClienteService(IRepositoryCliente repositoryCliente){
        this.repositoryCliente = repositoryCliente;
    }

    @Transactional
    public long save(Cliente cliente){
        cliente.setActive(true);
        Cliente c = repositoryCliente.save(cliente);
        return c.getId();
    }

    public List<ClienteDTO> findAll(){
        return repositoryCliente.findAll().stream().map(cliente -> 
            getCliente(cliente)    
        ).collect(Collectors.toList());
    }


    public ClienteDTO getCliente(Cliente cliente){
        return ClienteDTO.builder()
            .cpf(cliente.getCpf())
            .email(cliente.getEmail())
            .id(cliente.getId())
            .name(cliente.getName())
            .telefone(cliente.getTelefone())
            .active(cliente.isActive())
            .messageStatus(cliente.getMessageStatus())
            .build();
    }

    @Transactional
    public ClienteDTO edit(long id, ClienteDTO cliente ){
        Cliente c = repositoryCliente.findById(id).orElseThrow(() -> new NoFindClienteException("Cliente nao encontrado"));

        c.setCpf(cliente.getCpf());
        c.setId(id);
        c.setEmail(cliente.getEmail());
        c.setName(cliente.getName());
        c.setTelefone(cliente.getTelefone());
        return getCliente(c);
    }

    @Transactional
    public long delete(long id){
        repositoryCliente.deleteById(id);
        return id;
    }

    
    public Page<ClienteDTO> getAllPage(int page, int size, boolean active){

        Pageable pageable = PageRequest.of(page, size);

        Page <Cliente> cliente = repositoryCliente.findByActive(pageable, active);

        return cliente.map(this::getCliente);

    }

    @Transactional
    public String removeCliente(long id, String messageStatus){

        Cliente cliente = repositoryCliente.findById(id).orElseThrow(() -> new NoFindClienteException("O cliente não foi encontrado"));

        cliente.setActive(false);
        cliente.setMessageStatus(messageStatus);

        repositoryCliente.save(cliente);
        return "O cliente agora esta inativo";
    }

}
