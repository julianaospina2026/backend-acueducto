package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Model.Lectura;
import com.example.proyecto_acueducto.Service.LecturaService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lecturas")
@CrossOrigin(origins = "*")
public class LecturaController {

    private final LecturaService lecturaService;

    public LecturaController(LecturaService lecturaService) {
        this.lecturaService = lecturaService;
    }

    // ✅ CREAR LECTURA
    @PostMapping
    public ResponseEntity<Lectura> registrar(@RequestBody Lectura lectura) {
        Lectura nueva = lecturaService.guardar(lectura);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // ✅ OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Lectura> obtenerPorId(@PathVariable Long id) {
        return lecturaService.buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }
    // ✅ LISTAR CON PAGINACIÓN
    @GetMapping
    public ResponseEntity<Page<Lectura>> listar(
        @RequestParam(required = false) Long clienteId,
        @RequestParam(required = false) String periodo,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "fechaLectura") String sort,
        @RequestParam(defaultValue = "desc") String dir
    ) {
        
        Sort.Direction direction = dir.equalsIgnoreCase("asc")
        ? Sort.Direction.ASC
        : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<Lectura> resultado = lecturaService.listarConFiltros(clienteId, periodo, pageable);

        return ResponseEntity.ok(resultado);
    }

    // ✅ ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        lecturaService.eliminar(id);
        return ResponseEntity.ok("Lectura eliminada correctamente");
    }
}