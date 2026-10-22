package com.unibague.agentes.servicios;

import com.unibague.agentes.dto.AgenteNavegacionInput;
import com.unibague.agentes.model.AgenteInteligente;
import com.unibague.agentes.model.AgenteNavegacion;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Logica de negocio del DETALLE. Patron SINGLETON: una sola instancia y una sola
 * coleccion (List) en memoria principal compartida por todos los clientes.
 */
public class ServicioAgenteNavegacion implements IServicioAgenteNavegacion {

    private static ServicioAgenteNavegacion instancia;
    private final List<AgenteNavegacion> agentes = new CopyOnWriteArrayList<>();

    private ServicioAgenteNavegacion() {
    }

    public static synchronized ServicioAgenteNavegacion getInstance() {
        if (instancia == null) {
            instancia = new ServicioAgenteNavegacion();
            instancia.cargarDatosDePrueba();
        }
        return instancia;
    }

    // ================================ LISTAR ================================
    @Override
    public List<AgenteNavegacion> listar(String destino, Integer idAgenteInteligente) {
        String texto = destino == null ? "" : destino.trim().toLowerCase();
        return agentes.stream()
                .filter(a -> texto.isEmpty() || a.getDestino().toLowerCase().contains(texto))
                .filter(a -> idAgenteInteligente == null || a.getIdAgenteInteligente() == idAgenteInteligente)
                .sorted(Comparator.comparingInt(AgenteNavegacion::getIdNavegacion))
                .toList();
    }

    // ================================ BUSCAR ================================
    @Override
    public Optional<AgenteNavegacion> buscarPorId(int idNavegacion) {
        return agentes.stream().filter(a -> a.getIdNavegacion() == idNavegacion).findFirst();
    }

    @Override
    public Optional<AgenteNavegacion> buscarPorCriterios(String nombre, Integer idAgenteInteligente) {
        boolean sinNombre = nombre == null || nombre.isBlank();
        if (sinNombre && idAgenteInteligente == null) {
            return Optional.empty();
        }
        return agentes.stream()
                .filter(a -> sinNombre || a.getNombre().toLowerCase().contains(nombre.trim().toLowerCase()))
                .filter(a -> idAgenteInteligente == null || a.getIdAgenteInteligente() == idAgenteInteligente)
                .min(Comparator.comparingInt(AgenteNavegacion::getIdNavegacion));
    }

    // =============================== ADICIONAR ===============================
    @Override
    public AgenteNavegacion adicionar(AgenteNavegacionInput input) throws Exception {
        validarObligatorios(input);
        if (buscarPorId(input.idNavegacion()).isPresent()) {
            throw new Exception("Ya existe un agente de navegacion con ID " + input.idNavegacion());
        }
        AgenteInteligente maestro = buscarMaestro(input.idAgenteInteligente());
        AgenteNavegacion nuevo = construir(input);
        maestro.addAgenteNavegacion(nuevo);
        agentes.add(nuevo);
        return nuevo;
    }

    // ============================== ACTUALIZAR ==============================
    @Override
    public AgenteNavegacion actualizar(AgenteNavegacionInput input) throws Exception {
        validarObligatorios(input);
        AgenteNavegacion existente = buscarPorId(input.idNavegacion())
                .orElseThrow(() -> new Exception("No existe un agente de navegacion con ID " + input.idNavegacion()));
        AgenteInteligente maestroNuevo = buscarMaestro(input.idAgenteInteligente());
        AgenteNavegacion actualizado = construir(input);
        // Se retira de su maestro anterior (puede ser el mismo u otro) y se asigna al nuevo
        ServicioAgenteInteligente.getInstance().buscarPorId(existente.getIdAgenteInteligente())
                .ifPresent(m -> m.delAgenteNavegacion(existente));
        maestroNuevo.addAgenteNavegacion(actualizado);
        agentes.set(agentes.indexOf(existente), actualizado);
        return actualizado;
    }

    // =============================== ELIMINAR ===============================
    @Override
    public AgenteNavegacion eliminar(int idNavegacion) throws Exception {
        AgenteNavegacion existente = buscarPorId(idNavegacion)
                .orElseThrow(() -> new Exception("No existe un agente de navegacion con ID " + idNavegacion));
        ServicioAgenteInteligente.getInstance().buscarPorId(existente.getIdAgenteInteligente())
                .ifPresent(m -> m.delAgenteNavegacion(existente));
        agentes.remove(existente);
        return existente;
    }

    // ============================== UTILIDADES ==============================
    private AgenteInteligente buscarMaestro(int idAgenteInteligente) throws Exception {
        return ServicioAgenteInteligente.getInstance().buscarPorId(idAgenteInteligente)
                .orElseThrow(() -> new Exception("No existe un agente inteligente con ID " + idAgenteInteligente));
    }

    private AgenteNavegacion construir(AgenteNavegacionInput in) throws Exception {
        return AgenteNavegacion.builder()
                .idNavegacion(in.idNavegacion())
                .nombre(in.nombre())
                .origen(in.origen())
                .destino(in.destino())
                .precisionGPS(in.precisionGPS())
                .fechaUltimaCalibracion(FechaUtil.parse(in.fechaUltimaCalibracion()))
                .idAgenteInteligente(in.idAgenteInteligente())
                .build();
    }

    private void validarObligatorios(AgenteNavegacionInput in) throws Exception {
        if (in == null || in.idNavegacion() == null || in.nombre() == null || in.origen() == null
                || in.destino() == null || in.precisionGPS() == null || in.fechaUltimaCalibracion() == null
                || in.idAgenteInteligente() == null) {
            throw new Exception("Faltan datos obligatorios del agente de navegacion");
        }
    }

    private void cargarDatosDePrueba() {
        try {
            adicionar(new AgenteNavegacionInput(1, "NaviBot Centro", "Universidad de Ibague",
                    "Centro Comercial Multicentro", 95.5, "2026-09-10T08:30", 1));
            adicionar(new AgenteNavegacionInput(2, "RutaSegura Norte", "Terminal de Transportes",
                    "Hospital Federico Lleras", 80.0, "2026-08-25T14:00", 2));
            adicionar(new AgenteNavegacionInput(3, "GuiaTolima", "Plaza de Bolivar",
                    "Aeropuerto Perales", 70.25, "2026-07-05T17:45", 3));
            adicionar(new AgenteNavegacionInput(4, "Explorador Sur", "Barrio Picalena",
                    "Estadio Manuel Murillo Toro", 91.3, "2026-09-29T09:00", 1));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
