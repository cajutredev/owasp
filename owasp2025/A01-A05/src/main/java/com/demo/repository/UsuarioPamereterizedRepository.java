package com.demo.repository;

import java.util.List;

import com.demo.entity.Usuario;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped 
public class UsuarioPamereterizedRepository {

        @PersistenceContext(unitName = "UsuariosPU")
        private EntityManager entityManager;

        @SuppressWarnings("unchecked")
        public List<Usuario> findByName(String name) {
            return entityManager
                .createNativeQuery("SELECT * FROM usuario WHERE nombre = ?1", Usuario.class)
                .setParameter(1, name)
                .getResultList();

        }

        @SuppressWarnings("unchecked")
        public List<Object[]> findByNameUnion(String name) {
            return entityManager
                .createNativeQuery("""
                    SELECT id, nombre, primer_apellido, email, activo 
                    FROM usuario 
                    WHERE nombre = ?1""", Usuario.class)
                .setParameter(1, name)
                .getResultList();

        }

        @SuppressWarnings("unchecked")
        public Usuario autenticathe(String email, String password) {

            List<Usuario> usuarios = entityManager.createNativeQuery("""
                    SELECT * FROM usuario
                    WHERE email = ?1 AND password_hash = ?2
                """, Usuario.class)
                .setParameter(1, email)
                .setParameter(2, password)
                .getResultList();

            return usuarios.isEmpty() ? null : usuarios.get(0);
        }
}
