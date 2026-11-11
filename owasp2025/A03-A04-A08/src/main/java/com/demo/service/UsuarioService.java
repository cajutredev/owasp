package com.demo.service;

import com.demo.entity.Usuario;
import java.util.List;

public interface UsuarioService {

    public void create(
            String nombre,
            String primerApellido,
            String segundoApellido,
            String email,
            String passwordHash,
            String telefono,
            Long departamentoId,
            Long rolId);

    public void createXss(
            String nombre,
            String primerApellido,
            String segundoApellido,
            String email,
            String passwordHash,
            String telefono,
            Long departamentoId,
            Long rolId);

    public void delete(Long id);

    public void update(
            Long id,
            String nombre,
            String primerApellido,
            String segundoApellido,
            String email,
            String telefono,
            Long departamentoId,
            Long rolId);

    public void updatePartial(
            Long id,
            String nombre,
            String primerApellido,
            String segundoApellido,
            String email,
            String telefono,
            Long departamentoId,
            Long rolId);

    public List<Usuario> findAll();

    public Usuario findById(Long id);

    public Usuario findByEmail(String email);


    public List<Usuario> findByName(String nombre);
    
    public List<Usuario> findByNameXss(String nombre);

    public List<Object[]> findByNameUnion(String nombre);

    public List<Usuario> findByDepartmentId(Long departamentoId);

    public List<Usuario> findByRoleId(Long rolId);

    public List<Usuario> findByActive(boolean activo);
}
