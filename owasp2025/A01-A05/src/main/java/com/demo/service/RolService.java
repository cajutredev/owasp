package com.demo.service;

import com.demo.entity.Rol;
import java.util.List;

public interface RolService {

    public void create(String nombre, String descripcion, boolean activo);

    public void delete(Long id);

    public void update(Long id, String nombre, String descripcion, boolean activo);

    public List<Rol> findAll();

    public Rol findById(Long id);

    public List<Rol> findByName(String nombre);

    public List<Rol> findByActive(boolean activo);
}
