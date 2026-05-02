package com.example.proyecto_acueducto.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.proyecto_acueducto.Model.Cliente;
import com.example.proyecto_acueducto.Service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // ✅ Listar todos los clientes
    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listarTodos();
    }

    // ✅ Buscar cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Buscar por correo
    @GetMapping("/correo")
    public ResponseEntity<Cliente> buscarPorCorreo(@RequestParam String correo) {
        return clienteService.buscarPorCorreo(correo)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Crear cliente
    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.guardar(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    // ✅ Actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.actualizar(id, cliente));
    }

    // ✅ Eliminar cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}