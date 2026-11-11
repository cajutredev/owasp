
package com.demo.repository;

import com.demo.entity.Rol;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class RolRepository {

    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager entityManager;

    // ALTA
    @Transactional
    public void create(String nombre, String descripcion,
                                   boolean activo) {

        String sql = "INSERT INTO rol (nombre, descripcion, activo) "
                + "VALUES ('" + nombre + "', '"
                + descripcion + "', "
                + activo + ")";

        entityManager.createNativeQuery(sql).executeUpdate();
    }

    // BAJA
    @Transactional
    public void delete(Long id) {

        String sql = "DELETE FROM rol WHERE id = " + id;

        entityManager.createNativeQuery(sql).executeUpdate();
    }

    // ACTUALIZACIÓN
    @Transactional
    public void update(Long id, String nombre,
                                     String descripcion,
                                     boolean activo) {

        String sql = "UPDATE rol SET nombre = '"
                + nombre + "', descripcion = '"
                + descripcion + "', activo = "
                + activo
                + " WHERE id = " + id;

        entityManager.createNativeQuery(sql).executeUpdate();
    }

    // CONSULTAR TODOS
    @SuppressWarnings("unchecked")
public List<Rol> findAll() {

        String sql = "SELECT * FROM rol";

        return entityManager
                .createNativeQuery(sql, Rol.class)
                .getResultList();
    }

    // CONSULTAR POR ID
    public Rol findById(Long id) {

        String sql = "SELECT * FROM rol WHERE id = " + id;

        @SuppressWarnings("unchecked")
        List<Rol> resultados = entityManager
                .createNativeQuery(sql, Rol.class)
                .getResultList();

        return resultados.isEmpty() ? null : resultados.get(0);
    }

    // BUSCAR POR NOMBRE
    @SuppressWarnings("unchecked")
public List<Rol> findByName(String nombre) {

        String sql = "SELECT * FROM rol WHERE nombre = '"
                + nombre + "'";

        return entityManager
                .createNativeQuery(sql, Rol.class)
                .getResultList();
    }

    // BUSCAR POR ESTADO
    @SuppressWarnings("unchecked")
public List<Rol> findByActive(boolean activo) {

        String sql = "SELECT * FROM rol WHERE activo = " + activo;

        return entityManager
                .createNativeQuery(sql, Rol.class)
                .getResultList();
    }
}
