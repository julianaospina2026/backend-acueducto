package com.example.proyecto_acueducto.Repository;

import com.example.proyecto_acueducto.Model.Lectura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Repository
public interface LecturaRepository extends JpaRepository<Lectura, Long> {

    // Validar si ya existe una lectura para un cliente en un periodo
    boolean existsByClienteIdAndPeriodo(Long clienteId, String periodo);

    // Obtener la última lectura de un cliente (para calcular consumo)
    Optional<Lectura> findTopByCliente_IdOrderByFechaLecturaDesc(Long clienteId);

    // Listar lecturas por cliente (con paginación)
    Page<Lectura> findByClienteId(Long clienteId, Pageable pageable);

    // Listar lecturas por periodo (con paginación)
    Page<Lectura> findByPeriodo(String periodo, Pageable pageable);

    // Listar por cliente y periodo (muy útil para filtros combinados)
    Page<Lectura> findByClienteIdAndPeriodo(Long clienteId, String periodo, Pageable pageable);
}