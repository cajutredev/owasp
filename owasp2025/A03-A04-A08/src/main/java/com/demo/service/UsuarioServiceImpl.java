package com.demo.service;

import com.demo.entity.Usuario;
import com.demo.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Cache;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    @Inject
    private UsuarioRepository usuarioRepository;

    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager entityManager;

    @Override
    public void create(
            String nombre,
            String primerApellido,
            String segundoApellido,
            String email,
            String passwordHash,
            String telefono,
            Long departamentoId,
            Long rolId) {
        usuarioRepository.create(
                nombre,
                primerApellido,
                segundoApellido,
                email,
                passwordHash,
                telefono,
                departamentoId,
                rolId);
    }

    @Override
    public void createXss(
            String nombre,
            String primerApellido,
            String segundoApellido,
            String email,
            String passwordHash,
            String telefono,
            Long departamentoId,
            Long rolId) {
        usuarioRepository.createXss(
                nombre,
                primerApellido,
                segundoApellido,
                email,
                passwordHash,
                telefono,
                departamentoId,
                rolId);
    }

    @Override
    public void delete(Long id) {
        usuarioRepository.delete(id);
    }

    @Override
    public void update(
            Long id,
            String nombre,
            String primerApellido,
            String segundoApellido,
            String email,
            String telefono,
            Long departamentoId,
            Long rolId) {
        usuarioRepository.update(
                id,
                nombre,
                primerApellido,
                segundoApellido,
                email,
                telefono,
                departamentoId,
                rolId);
        Cache cache = entityManager.getEntityManagerFactory().getCache();
        cache.evict(Usuario.class, id);
    }

    @Override
    public void updatePartial(
            Long id,
            String nombre,
            String primerApellido,
            String segundoApellido,
            String email,
            String telefono,
            Long departamentoId,
            Long rolId) {
        usuarioRepository.updatePartial(
                id,
                nombre,
                primerApellido,
                segundoApellido,
                email,
                telefono,
                departamentoId,
                rolId);
        Cache cache = entityManager.getEntityManagerFactory().getCache();
        cache.evict(Usuario.class, id);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public List<Usuario> findByName(String nombre) {
        return usuarioRepository.findByName(nombre);
    }

    @Override
    public List<Object[]> findByNameUnion(String nombre) {
        return usuarioRepository.findByNameUnion(nombre);
    }

    @Override
    public List<Usuario> findByDepartmentId(Long departamentoId) {
        return usuarioRepository.findByDepartmentId(departamentoId);
    }

    @Override
    public List<Usuario> findByRoleId(Long rolId) {
        return usuarioRepository.findByRoleId(rolId);
    }

    @Override
    public List<Usuario> findByActive(boolean activo) {
        return usuarioRepository.findByActive(activo);
    }

    @Override
    public List<Usuario> findByNameXss(String nombre) {
        return usuarioRepository.findByNameXss(nombre);
    }
}
