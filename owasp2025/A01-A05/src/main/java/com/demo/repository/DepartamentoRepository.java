package com.demo.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

import com.demo.entity.Departamento;

@ApplicationScoped
public class DepartamentoRepository {

    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager entityManager;

    // ALTA: INSERTAR DEPARTAMENTO
    public void create(String nombre) {

        String sql = "INSERT INTO departamento (nombre) VALUES ('"
                + nombre + "')";

        entityManager
                .createNativeQuery(sql)
                .executeUpdate();
    }

    // BAJA: ELIMINAR DEPARTAMENTO
    public void delete(Long id) {

        String sql = "DELETE FROM departamento WHERE id = "
                + id;

        entityManager
                .createNativeQuery(sql)
                .executeUpdate();
    }

    // ACTUALIZACION: MODIFICAR DEPARTAMENTO
    public void update(Long id, String nombre) {

        String sql = "UPDATE departamento SET nombre = '"
                + nombre
                + "' WHERE id = "
                + id;

        entityManager
                .createNativeQuery(sql)
                .executeUpdate();
    }

    // CONSULTAR TODOS LOS DEPARTAMENTOS
    @SuppressWarnings("unchecked")
    public List<Departamento> findAll() {

        String sql = "SELECT * FROM departamento";

        return entityManager
                .createNativeQuery(sql, Departamento.class)
                .getResultList();
    }

    // BUSCAR POR ID
    public Departamento findById(Long id) {

        String sql = "SELECT * FROM departamento WHERE id = "
                + id;

        @SuppressWarnings("unchecked")
        List<Departamento> resultados = entityManager
                .createNativeQuery(sql, Departamento.class)
                .getResultList();

        return resultados.isEmpty() ? null : resultados.get(0);
    }

    
    @SuppressWarnings("unchecked")
    public List<Departamento> findByName(String nombre) {

        String sql = "SELECT * FROM departamento WHERE nombre = '"
                + nombre + "'";

        return entityManager
                .createNativeQuery(sql, Departamento.class)
                .getResultList();
    }

}
