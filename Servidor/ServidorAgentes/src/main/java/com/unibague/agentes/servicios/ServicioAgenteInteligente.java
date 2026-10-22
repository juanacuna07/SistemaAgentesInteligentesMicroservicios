package com.unibague.agentes.servicios;

import com.unibague.agentes.dto.AgenteInteligenteInput;
import com.unibague.agentes.model.AgenteInteligente;
import com.unibague.agentes.model.AgenteNavegacion;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Logica de negocio del MAESTRO. Patron SINGLETON: una sola instancia y una sola
 * coleccion (List) en memoria principal compartida por todos los clientes.
 */
public class ServicioAgenteInteligente implements IServicioAgenteInteligente {

    private static ServicioAgenteInteligente instancia;
    private final List<AgenteInteligente> agentes = new CopyOnWriteArrayList<>();

    private ServicioAgenteInteligente() {
    }

    public static synchronized ServicioAgenteInteligente getInstance() {
        if (instancia == null) {
            instancia = new ServicioAgenteInteligente();
        }
        return instancia;
    }

    // ================================ LISTAR ================================
    @Override
    public List<AgenteInteligente> listar(String nombre, String estadoOperativo) {
        String texto = nombre == null ? "" : nombre.trim().toLowerCase();
        String estado = estadoOperativo == null ? "" : estadoOperativo.trim();
        return agentes.stream()
                .filter(a -> texto.isEmpty() || a.getNombre().toLowerCase().contains(texto))
                .filter(a -> estado.isEmpty() || a.getEstadoOperativo().equalsIgnoreCase(estado))
                .sorted(Comparator.comparingInt(AgenteInteligente::getIdAgente))
                .toList();
    }

    // ================================ BUSCAR ================================
    @Override
    public Optional<AgenteInteligente> buscarPorId(int idAgente) {
        return agentes.stream().filter(a -> a.getIdAgente() == idAgente).findFirst();
    }

    @Override
    public Optional<AgenteInteligente> buscarPorCriterios(String nombre, String estadoOperativo) {
        boolean sinNombre = nombre == null || nombre.isBlank();
        boolean sinEstado = estadoOperativo == null || estadoOperativo.isBlank();
        if (sinNombre && sinEstado) {
            return Optional.empty();
        }
        return listar(nombre, estadoOperativo).stream().findFirst();
    }

    // =============================== ADICIONAR ===============================
    @Override
    public AgenteInteligente adicionar(AgenteInteligenteInput input) throws Exception {
        validarObligatorios(input);
        if (buscarPorId(input.idAgente()).isPresent()) {
            throw new Exception("Ya existe un agente inteligente con ID " + input.idAgente());
        }
        AgenteInteligente nuevo = construir(input);
        agentes.add(nuevo);
        return nuevo;
    }

    // ============================== ACTUALIZAR ==============================
    @Override
    public AgenteInteligente actualizar(AgenteInteligenteInput input) throws Exception {
        validarObligatorios(input);
        AgenteInteligente existente = buscarPorId(input.idAgente())
                .orElseThrow(() -> new Exception("No existe un agente inteligente con ID " + input.idAgente()));
        // Se valida todo en un objeto nuevo: si un dato es invalido, el original no queda a medias
        AgenteInteligente actualizado = construir(input);
        for (AgenteNavegacion detalle : existente.getAgentesNavegacion()) {
            actualizado.addAgenteNavegacion(detalle);   // conserva sus agentes de navegacion
        }
        agentes.set(agentes.indexOf(existente), actualizado);
        return actualizado;
    }

    // =============================== ELIMINAR ===============================
    @Override
    public AgenteInteligente eliminar(int idAgente) throws Exception {
        AgenteInteligente existente = buscarPorId(idAgente)
                .orElseThrow(() -> new Exception("No existe un agente inteligente con ID " + idAgente));
        // Relacion 1..1 -> 0..*: los detalles no pueden quedar huerfanos, se eliminan con el maestro
        for (AgenteNavegacion detalle : existente.getAgentesNavegacion()) {
            ServicioAgenteNavegacion.getInstance().eliminar(detalle.getIdNavegacion());
        }
        agentes.remove(existente);
        return existente;
    }

    // ============================== UTILIDADES ==============================
    private AgenteInteligente construir(AgenteInteligenteInput in) throws Exception {
        return AgenteInteligente.builder()
                .idAgente(in.idAgente())
                .nombre(in.nombre())
                .nivelAutonomia(in.nivelAutonomia())
                .estadoOperativo(in.estadoOperativo())
                .nivelConfianza(in.nivelConfianza())
                .fechaRegistro(FechaUtil.parse(in.fechaRegistro()))
                .build();
    }

    private void validarObligatorios(AgenteInteligenteInput in) throws Exception {
        if (in == null || in.idAgente() == null || in.nombre() == null || in.nivelAutonomia() == null
                || in.estadoOperativo() == null || in.nivelConfianza() == null || in.fechaRegistro() == null) {
            throw new Exception("Faltan datos obligatorios del agente inteligente");
        }
    }

}
