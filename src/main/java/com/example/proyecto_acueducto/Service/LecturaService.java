package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Model.Cliente;
import com.example.proyecto_acueducto.Model.Lectura;
import com.example.proyecto_acueducto.Repository.ClienteRepository;
import com.example.proyecto_acueducto.Repository.LecturaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class LecturaService {

    private final LecturaRepository lecturaRepository;
    private final ClienteRepository clienteRepository;

    private static final BigDecimal CONSUMO_ALTO = new BigDecimal("80");

    public LecturaService(LecturaRepository lecturaRepository,
        ClienteRepository clienteRepository) {
        this.lecturaRepository = lecturaRepository;
        this.clienteRepository = clienteRepository;
    }

    // ✅ GUARDAR LECTURA
    @Transactional
    public Lectura guardar(Lectura lectura) {
            
            if (lectura.getCliente() == null || lectura.getCliente().getId() == null) {
                throw new RuntimeException("Debe enviar el id del cliente");
            }
            
            if (lectura.getLecturaActual() == null) {
                throw new RuntimeException("Debe enviar la lectura actual");
            }
            
            if (lectura.getPeriodo() == null || lectura.getPeriodo().isBlank()) {
                throw new RuntimeException("Debe enviar el periodo");
            }
            
            Cliente cliente = clienteRepository.findById(lectura.getCliente().getId())
            .orElseThrow(() -> new RuntimeException("Cliente no existe"));

        // 1. Validar duplicado en el mismo periodo
        if (lecturaRepository.existsByClienteIdAndPeriodo(cliente.getId(), lectura.getPeriodo())) {
            throw new RuntimeException("Ya existe una lectura para el periodo " + lectura.getPeriodo());
        }

        // 2. Obtener lectura anterior para calcular consumo
        BigDecimal lecturaAnterior = lecturaRepository
        .findTopByCliente_IdOrderByFechaLecturaDesc(cliente.getId())
        .map(Lectura::getLecturaActual)
        .orElse(BigDecimal.ZERO);


        // 3. Validar consistencia (Actual >= Anterior)
        BigDecimal lecturaActual = lectura.getLecturaActual();
        lecturaActual = lecturaActual.setScale(3, RoundingMode.HALF_UP);
        if (lecturaActual.compareTo(lecturaAnterior) < 0) {
            throw new RuntimeException("La lectura actual (" + lecturaActual + ") no puede ser menor a la anterior (" + lecturaAnterior + ")");
        }

        // 4. Calcular consumo
        BigDecimal consumo = lecturaActual
        .subtract(lecturaAnterior)
        .setScale(3, RoundingMode.HALF_UP);

        // 5. Asignar valores calculados
        lectura.setCliente(cliente);
        lectura.setLecturaActual(lecturaActual);
        lectura.setLecturaAnterior(lecturaAnterior);
        lectura.setConsumoM3(consumo);
        if (lectura.getFechaLectura() == null) {
            lectura.setFechaLectura(LocalDate.now());
        }

        // 6. Generar observación automática si está vacía
        if (consumo.compareTo(BigDecimal.ZERO) == 0) {
            lectura.setObservacion("Sin consumo");
        } else if (consumo.compareTo(CONSUMO_ALTO) > 0) {
            lectura.setObservacion("Consumo alto");
        } else if (lectura.getObservacion() == null || lectura.getObservacion().isBlank()) {
            lectura.setObservacion("Consumo normal");
        }

        return lecturaRepository.save(lectura);
    }

    @Transactional(readOnly = true)
    public Optional<Lectura> buscarPorId(Long id) {
        return lecturaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Lectura> listarConFiltros(Long clienteId, String periodo, Pageable pageable) {
        if (clienteId != null && periodo != null && !periodo.isBlank()) {
            return lecturaRepository.findByClienteIdAndPeriodo(clienteId, periodo, pageable);
        }

        if (clienteId != null) {
            return lecturaRepository.findByClienteId(clienteId, pageable);
        }

        if (periodo != null && !periodo.isBlank()) {
            return lecturaRepository.findByPeriodo(periodo, pageable);
        }

        return lecturaRepository.findAll(pageable);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!lecturaRepository.existsById(id)) {
            throw new RuntimeException("Lectura no encontrada");
        }
        lecturaRepository.deleteById(id);
    }
}