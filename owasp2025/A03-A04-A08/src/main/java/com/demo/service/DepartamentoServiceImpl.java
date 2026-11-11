package com.demo.service;

import com.demo.entity.Departamento;
import com.demo.repository.DepartamentoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class DepartamentoServiceImpl implements DepartamentoService {

    @Inject
    private DepartamentoRepository departamentoRepository;

    @Override
    public void create(String nombre) {
        departamentoRepository.create(nombre);
    }

    @Override
    public void delete(Long id) {
        departamentoRepository.delete(id);
    }

    @Override
    public void update(Long id, String nombre) {
        departamentoRepository.update(id, nombre);
    }

    @Override
    public List<Departamento> findAll() {
        return departamentoRepository.findAll();
    }

    @Override
    public Departamento findById(Long id) {
        return departamentoRepository.findById(id);
    }

    @Override
    public List<Departamento> findByName(String nombre) {
        return departamentoRepository.findByName(nombre);
    }
}
