package com.example.proyecto_acueducto.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.proyecto_acueducto.Model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCorreo(String correo);

    Optional<Cliente> findByDocumentoIdentidad(Long documentoIdentidad);

    Optional<Cliente> findByLecturaMedidor(Long lecturaMedidor);
}