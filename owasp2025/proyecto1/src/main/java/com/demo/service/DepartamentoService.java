package com.demo.service;

import com.demo.entity.Departamento;
import java.util.List;

public interface DepartamentoService {

    public void create(String nombre);

    public void delete(Long id);

    public void update(Long id, String nombre);

    public List<Departamento> findAll();

    public Departamento findById(Long id);

    public List<Departamento> findByName(String nombre);
}
