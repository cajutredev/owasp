package com.demo.repository;

import java.util.List;
import java.util.Optional;

import com.demo.entity.Departamento;
import com.demo.entity.Rol;
import com.demo.entity.Usuario;

import jakarta.data.repository.DataRepository;
import jakarta.data.repository.Insert;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Update;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Repository(dataStore = "jdbc/Usuarios")
public interface UsuarioRepositoryData extends DataRepository<Usuario, Long> {

    @Insert
    Usuario insertUsuario(Usuario usuario);

    @Update
    Usuario updateUsuario(Usuario usuario);

    @Query("select u from Usuario u")
    List<Usuario> findAll();

    @Query("select u from Usuario u where u.id = ?1")
    Optional<Usuario> findUsuarioById(Long id);

    @Query("select u from Usuario u where u.email = ?1")
    Optional<Usuario> findUsuarioByEmail(String email);

    @Query("select u from Usuario u where u.nombre = ?1")
    List<Usuario> findUsuariosByName(String nombre);

    @Query("select d from Departamento d where d.id = ?1")
    Optional<Departamento> findDepartamentoById(Long id);

    @Query("select r from Rol r where r.id = ?1")
    Optional<Rol> findRolById(Long id);

    @Query("delete from Usuario u where u.id = ?1")
    void delete(Long id);

    @Query("""
            select u.id, u.nombre, u.primerApellido, u.email, u.activo
            from Usuario u
            where u.nombre = ?1
            """)
    List<Object[]> findByNameUnion(String nombre);

    @Query("""
            select u from Usuario u
            where u.departamento.id = ?1
            """)
    List<Usuario> findByDepartmentId(Long departamentoId);

    @Query("select u from Usuario u where u.rol.id = ?1")
    List<Usuario> findByRoleId(Long rolId);

    @Query("select u from Usuario u where u.activo = ?1")
    List<Usuario> findByActive(boolean activo);

    @Query("""
            select u from Usuario u
            where u.email = ?1 and u.passwordHash = ?2 and u.activo = true
            """)
    Optional<Usuario> findAuthenticatedUsuario(String email, String password);

    @Transactional
    default void create(
            String nombre,
            String primerApellido,
            String segundoApellido,
            String email,
            String passwordHash,
            String telefono,
            Long departamentoId,
            Long rolId) {

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setPrimerApellido(primerApellido);
        usuario.setSegundoApellido(segundoApellido);
        usuario.setEmail(email);
        usuario.setPasswordHash(passwordHash);
        usuario.setTelefono(telefono);
        usuario.setDepartamento(requireDepartamento(departamentoId));
        usuario.setRol(requireRol(rolId));
        insertUsuario(usuario);
    }

    @Transactional
    default void createXss(
            String nombre,
            String primerApellido,
            String segundoApellido,
            String email,
            String passwordHash,
            String telefono,
            Long departamentoId,
            Long rolId) {
        create(nombre, primerApellido, segundoApellido, email, passwordHash, telefono, departamentoId, rolId);
    }

    @Transactional
    default void update(
            Long id,
            String nombre,
            String primerApellido,
            String segundoApellido,
            String email,
            String telefono,
            Long departamentoId,
            Long rolId) {

        Optional<Usuario> resultado = findUsuarioById(id);
        if (resultado.isEmpty()) {
            return;
        }

        Usuario usuario = resultado.get();
        usuario.setNombre(nombre);
        usuario.setPrimerApellido(primerApellido);
        usuario.setSegundoApellido(segundoApellido);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setDepartamento(requireDepartamento(departamentoId));
        usuario.setRol(requireRol(rolId));
        updateUsuario(usuario);
    }

    @Transactional
    default void updatePartial(
            Long id,
            String nombre,
            String primerApellido,
            String segundoApellido,
            String email,
            String telefono,
            Long departamentoId,
            Long rolId) {

        if (nombre == null
                && primerApellido == null
                && segundoApellido == null
                && email == null
                && telefono == null
                && departamentoId == null
                && rolId == null) {
            return;
        }

        Optional<Usuario> resultado = findUsuarioById(id);
        if (resultado.isEmpty()) {
            return;
        }

        Usuario usuario = resultado.get();
        if (nombre != null) {
            usuario.setNombre(nombre);
        }
        if (primerApellido != null) {
            usuario.setPrimerApellido(primerApellido);
        }
        if (segundoApellido != null) {
            usuario.setSegundoApellido(segundoApellido);
        }
        if (email != null) {
            usuario.setEmail(email);
        }
        if (telefono != null) {
            usuario.setTelefono(telefono);
        }
        if (departamentoId != null) {
            usuario.setDepartamento(requireDepartamento(departamentoId));
        }
        if (rolId != null) {
            usuario.setRol(requireRol(rolId));
        }
        updateUsuario(usuario);
    }

    default Usuario findById(Long id) {
        return findUsuarioById(id).orElse(null);
    }

    default Usuario findByEmail(String email) {
        return findUsuarioByEmail(email).orElse(null);
    }

    default List<Usuario> findByName(String nombre) {
        return findUsuariosByName(nombre);
    }

    default List<Usuario> findByNameXss(String nombre) {
        return findUsuariosByName(nombre);
    }

    default Usuario autenticathe(String email, String password) {
        return findAuthenticatedUsuario(email, password).orElse(null);
    }

    default Usuario autenticatheXss(String email, String password) {
        return findAuthenticatedUsuario(email, password).orElse(null);
    }

    private Departamento requireDepartamento(Long id) {
        return findDepartamentoById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento not found: " + id));
    }

    private Rol requireRol(Long id) {
        return findRolById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol not found: " + id));
    }
}
