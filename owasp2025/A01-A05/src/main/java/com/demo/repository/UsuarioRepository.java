
package com.demo.repository;

import com.demo.entity.Usuario;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class UsuarioRepository {

        @PersistenceContext(unitName = "UsuariosPU")
        private EntityManager entityManager;

        // ALTA
        @Transactional
        public void create(
                        String nombre,
                        String primerApellido,
                        String segundoApellido,
                        String email,
                        String passwordHash,
                        String telefono,
                        Long departamentoId,
                        Long rolId) {

                String sql = "INSERT INTO usuario "
                                + "(nombre, primer_apellido, segundo_apellido, "
                                + "email, password_hash, telefono, "
                                + "departamento_id, rol_id) "
                                + "VALUES ('" + nombre + "', '"
                                + primerApellido + "', '"
                                + segundoApellido + "', '"
                                + email + "', '"
                                + passwordHash + "', '"
                                + telefono + "', "
                                + departamentoId + ", "
                                + rolId + ")";

                entityManager.createNativeQuery(sql).executeUpdate();
        }

        // BAJA
        @Transactional
        public void delete(Long id) {

                String sql = "DELETE FROM usuario WHERE id = " + id;

                entityManager.createNativeQuery(sql).executeUpdate();
        }

        // ACTUALIZACION
        @Transactional
        public void update(
                        Long id,
                        String nombre,
                        String primerApellido,
                        String segundoApellido,
                        String email,
                        String telefono,
                        Long departamentoId,
                        Long rolId) {

                String sql = "UPDATE usuario SET "
                                + "nombre = '" + nombre + "', "
                                + "primer_apellido = '" + primerApellido + "', "
                                + "segundo_apellido = '" + segundoApellido + "', "
                                + "email = '" + email + "', "
                                + "telefono = '" + telefono + "', "
                                + "departamento_id = " + departamentoId + ", "
                                + "rol_id = " + rolId
                                + " WHERE id = " + id;

                entityManager.createNativeQuery(sql).executeUpdate();
        }

        @Transactional
        public void updatePartial(
                        Long id,
                        String nombre,
                        String primerApellido,
                        String segundoApellido,
                        String email,
                        String telefono,
                        Long departamentoId,
                        Long rolId) {

                String sql = "UPDATE usuario SET ";
                boolean first = true;

                if (nombre != null) {
                        sql += "nombre = '" + nombre + "'";
                        first = false;
                }

                if (primerApellido != null) {
                        if (!first)
                                sql += ", ";
                        sql += "primer_apellido = '" + primerApellido + "'";
                        first = false;
                }

                if (segundoApellido != null) {
                        if (!first)
                                sql += ", ";
                        sql += "segundo_apellido = '" + segundoApellido + "'";
                        first = false;
                }

                if (email != null) {
                        if (!first)
                                sql += ", ";
                        sql += "email = '" + email + "'";
                        first = false;
                }

                if (telefono != null) {
                        if (!first)
                                sql += ", ";
                        sql += "telefono = '" + telefono + "'";
                        first = false;
                }

                if (departamentoId != null) {
                        if (!first)
                                sql += ", ";
                        sql += "departamento_id = " + departamentoId;
                        first = false;
                }

                if (rolId != null) {
                        if (!first)
                                sql += ", ";
                        sql += "rol_id = " + rolId;
                        first = false;
                }

                if (first) {
                        return;
                }

                sql += " WHERE id = " + id;

                entityManager.createNativeQuery(sql).executeUpdate();
        }

        // CONSULTAR TODOS
        @SuppressWarnings("unchecked")
        public List<Usuario> findAll() {

                String sql = "SELECT * FROM usuario";

                return entityManager
                                .createNativeQuery(sql, Usuario.class)
                                .getResultList();
        }

        // CONSULTAR POR ID
        public Usuario findById(Long id) {

                String sql = "SELECT * FROM usuario WHERE id = " + id;

                @SuppressWarnings("unchecked")
                List<Usuario> resultados = entityManager
                                .createNativeQuery(sql, Usuario.class)
                                .getResultList();

                return resultados.isEmpty() ? null : resultados.get(0);
        }

        // BUSCAR POR EMAIL
        public Usuario findByEmail(String email) {

                String sql = "SELECT * FROM usuario WHERE email = '"
                                + email + "'";

                @SuppressWarnings("unchecked")
                List<Usuario> resultados = entityManager
                                .createNativeQuery(sql, Usuario.class)
                                .getResultList();

                return resultados.isEmpty() ? null : resultados.get(0);
        }

        // BUSCAR POR NOMBRE
        @SuppressWarnings("unchecked")
        public List<Usuario> findByName(String nombre) {

                String sql = "SELECT * FROM usuario WHERE nombre = '"
                                + nombre + "'";

                return entityManager
                                .createNativeQuery(sql, Usuario.class)
                                .getResultList();
        }

        @SuppressWarnings("unchecked")
        public List<Usuario> findByNameXss(String nombre) {

                return entityManager
                                .createNativeQuery(
                                                "SELECT * FROM usuario WHERE nombre = ?1",
                                                Usuario.class)
                                .setParameter(1, nombre)
                                .getResultList();
        }

        // ALTA
        @Transactional
        public void createXss(
                        String nombre,
                        String primerApellido,
                        String segundoApellido,
                        String email,
                        String passwordHash,
                        String telefono,
                        Long departamentoId,
                        Long rolId) {

                String sql = "INSERT INTO usuario "
                                + "(nombre, primer_apellido, segundo_apellido, "
                                + "email, password_hash, telefono, "
                                + "departamento_id, rol_id) "
                                + "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)";

                entityManager.createNativeQuery(sql)
                                .setParameter(1, nombre)
                                .setParameter(2, primerApellido)
                                .setParameter(3, segundoApellido)
                                .setParameter(4, email)
                                .setParameter(5, passwordHash)
                                .setParameter(6, telefono)
                                .setParameter(7, departamentoId)
                                .setParameter(8, rolId)
                                .executeUpdate();
        }

        // BUSCAR POR DEPARTAMENTO
        @SuppressWarnings("unchecked")
        public List<Usuario> findByDepartmentId(
                        Long departamentoId) {

                String sql = "SELECT * FROM usuario WHERE departamento_id = "
                                + departamentoId;

                return entityManager
                                .createNativeQuery(sql, Usuario.class)
                                .getResultList();
        }

        // BUSCAR POR ROL
        @SuppressWarnings("unchecked")
        public List<Usuario> findByRoleId(Long rolId) {

                String sql = "SELECT * FROM usuario WHERE rol_id = " + rolId;

                return entityManager
                                .createNativeQuery(sql, Usuario.class)
                                .getResultList();
        }

        // BUSCAR POR ESTADO
        @SuppressWarnings("unchecked")
        public List<Usuario> findByActive(boolean activo) {

                String sql = "SELECT * FROM usuario WHERE activo = " + activo;

                return entityManager
                                .createNativeQuery(sql, Usuario.class)
                                .getResultList();
        }

        @SuppressWarnings("unchecked")
        public List<Object[]> findByNameUnion(String nombre) {

                String sql = "SELECT "
                                + "id, "
                                + "nombre, "
                                + "primer_apellido, "
                                + "email, "
                                + "activo "
                                + "FROM usuario "
                                + "WHERE nombre = '" + nombre + "'";

                return entityManager
                                .createNativeQuery(sql)
                                .getResultList();
        }

        @SuppressWarnings("unchecked")
        public Usuario autenticathe(String email, String password) {

                String sql = "SELECT * FROM usuario "
                                + "WHERE email = '" + email + "' "
                                + "AND password_hash = '" + password + "'";

                List<Usuario> resultados = entityManager
                                .createNativeQuery(sql, Usuario.class)
                                .getResultList();

                return resultados.isEmpty() ? null : resultados.get(0);
        }

        @SuppressWarnings("unchecked")
        public Usuario autenticatheXss(String email, String password) {

                String sql = "SELECT * FROM usuario "
                                + "WHERE email = ?1 "
                                + "AND password_hash = ?2";

                List<Usuario> resultados = entityManager
                                .createNativeQuery(sql, Usuario.class)
                                .setParameter(1, email)
                                .setParameter(2, password)
                                .getResultList();

                return resultados.isEmpty() ? null : resultados.get(0);
        }
}
