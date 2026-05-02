package com.example.proyecto_acueducto.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "lecturas")
public class Lectura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // 🔹 Periodo (Ej: 2026-04)
    @Column(name = "periodo", nullable = false, length = 7)
    private String periodo;

    // 🔹 Fecha de lectura
    @Column(name = "fecha_lectura", nullable = false)
    private LocalDate fechaLectura;

    // 🔹 Lectura anterior del medidor
    @Column(name = "lectura_anterior", nullable = false, precision = 12, scale = 3)
    private BigDecimal lecturaAnterior;

    // 🔹 Lectura actual
    @Column(name = "lectura_actual", nullable = false, precision = 12, scale = 3)
    private BigDecimal lecturaActual;

    // 🔹 Consumo calculado
    @Column(name = "consumo_m3", nullable = false, precision = 12, scale = 3)
    private BigDecimal consumoM3;

    // 🔹 Observaciones
    @Column(name = "observacion", length = 255)
    private String observacion;

    // 🔹 Fecha de creación automática
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (fechaLectura == null) {
            fechaLectura = LocalDate.now();
        }

        // Calcular consumo automáticamente
        if (lecturaActual != null && lecturaAnterior != null) {
            consumoM3 = lecturaActual.subtract(lecturaAnterior);
        }
    }

    // GETTERS Y SETTERS

    public Long getId() { return id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public LocalDate getFechaLectura() { return fechaLectura; }
    public void setFechaLectura(LocalDate fechaLectura) { this.fechaLectura = fechaLectura; }

    public BigDecimal getLecturaAnterior() { return lecturaAnterior; }
    public void setLecturaAnterior(BigDecimal lecturaAnterior) { this.lecturaAnterior = lecturaAnterior; }

    public BigDecimal getLecturaActual() { return lecturaActual; }
    public void setLecturaActual(BigDecimal lecturaActual) { this.lecturaActual = lecturaActual; }

    public BigDecimal getConsumoM3() { return consumoM3; }
    public void setConsumoM3(BigDecimal consumoM3) { this.consumoM3 = consumoM3; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
