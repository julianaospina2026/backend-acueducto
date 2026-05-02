package com.example.proyecto_acueducto.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.proyecto_acueducto.Model.Cliente;
import com.example.proyecto_acueducto.Repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clientesRepository;

    // ✅ Listar todos los clientes
    public List<Cliente> listarTodos() {
        return clientesRepository.findAll();
    }

    // ✅ Buscar cliente por ID
    public Optional<Cliente> buscarPorId(Long id) {
        return clientesRepository.findById(id);
    }

    // ✅ Buscar por correo
    public Optional<Cliente> buscarPorCorreo(String correo) {
        return clientesRepository.findByCorreo(correo);
    }

    // ✅ Guardar cliente
    public Cliente guardar(Cliente cliente) {
        return clientesRepository.save(cliente);
    }

    // ✅ Actualizar cliente
    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        return clientesRepository.findById(id).map(cliente -> {

            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setApellido(clienteActualizado.getApellido());
            cliente.setCodigoCliente(clienteActualizado.getCodigoCliente());
            cliente.setDireccion(clienteActualizado.getDireccion());
            cliente.setTelefono(clienteActualizado.getTelefono());
            cliente.setCorreo(clienteActualizado.getCorreo());
            cliente.setDocumentoIdentidad(clienteActualizado.getDocumentoIdentidad());
            cliente.setLecturaMedidor(clienteActualizado.getLecturaMedidor());
            cliente.setEstado(clienteActualizado.getEstado());
            cliente.setEstrato(clienteActualizado.getEstrato());

            return clientesRepository.save(cliente);

        }).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    // ✅ Eliminar cliente
    public void eliminar(Long id) {
        clientesRepository.deleteById(id);
    }
}

