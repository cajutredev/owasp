package com.demo.repository;

import com.demo.entity.IntentoAutenticacion;
import jakarta.data.repository.DataRepository;
import jakarta.data.repository.Insert;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Update;
import java.util.Optional;

@Repository(dataStore = "jdbc/Usuarios")
public interface IntentoAutenticacionRepository
        extends DataRepository<IntentoAutenticacion, Long> {

    @Query("select i from IntentoAutenticacion i where i.email = ?1")
    Optional<IntentoAutenticacion> findByEmail(String email);

    @Insert
    IntentoAutenticacion insert(IntentoAutenticacion intento);

    @Update
    IntentoAutenticacion update(IntentoAutenticacion intento);
}
