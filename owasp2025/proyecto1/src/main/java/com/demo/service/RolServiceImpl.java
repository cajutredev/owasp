package com.demo.service;

import com.demo.entity.Rol;
import com.demo.repository.RolRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class RolServiceImpl implements RolService {

    @Inject
    private RolRepository rolRepository;

    @Override
    public void create(String nombre, String descripcion, boolean activo) {
        rolRepository.create(nombre, descripcion, activo);
    }

    @Override
    public void delete(Long id) {
        rolRepository.delete(id);
    }

    @Override
    public void update(Long id, String nombre, String descripcion, boolean activo) {
        rolRepository.update(id, nombre, descripcion, activo);
    }

    @Override
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    @Override
    public Rol findById(Long id) {
        return rolRepository.findById(id);
    }

    @Override
    public List<Rol> findByName(String nombre) {
        return rolRepository.findByName(nombre);
    }

    @Override
    public List<Rol> findByActive(boolean activo) {
        return rolRepository.findByActive(activo);
    }
}
