package com.pesoas.api.service;

import com.pesoas.api.entity.Pais;
import com.pesoas.api.repository.PaisRepository;
import com.pesoas.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaisService {
    @Autowired private PaisRepository paisRepository;



    // pais por id
    public Pais findById(Long id){
        Optional<Pais> obj = paisRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Pais não encontrado! Id: " + id + " Pais : " + Pais.class.getName()));
    }

}