package com.unibague.agentes.controller;

import com.unibague.agentes.dto.AgenteNavegacionInput;
import com.unibague.agentes.model.AgenteNavegacion;
import com.unibague.agentes.servicios.FechaUtil;
import com.unibague.agentes.servicios.IServicioAgenteNavegacion;
import com.unibague.agentes.servicios.ServicioAgenteNavegacion;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/** Controlador (MVC) que expone los microservicios GraphQL del DETALLE AgenteNavegacion. */
@Controller
public class AgenteNavegacionController {

    private final IServicioAgenteNavegacion servicio = ServicioAgenteNavegacion.getInstance();

    @QueryMapping
    public List<AgenteNavegacion> agentesNavegacion(@Argument String destino, @Argument Integer idAgenteInteligente) {
        return servicio.listar(destino, idAgenteInteligente);
    }

    @QueryMapping
    public AgenteNavegacion agenteNavegacionPorId(@Argument int idNavegacion) {
        return servicio.buscarPorId(idNavegacion).orElse(null);
    }

    @QueryMapping
    public AgenteNavegacion agenteNavegacionPorCriterios(@Argument String nombre, @Argument Integer idAgenteInteligente) {
        return servicio.buscarPorCriterios(nombre, idAgenteInteligente).orElse(null);
    }

    @MutationMapping
    public AgenteNavegacion crearAgenteNavegacion(@Argument AgenteNavegacionInput input) throws Exception {
        return servicio.adicionar(input);
    }

    @MutationMapping
    public AgenteNavegacion actualizarAgenteNavegacion(@Argument AgenteNavegacionInput input) throws Exception {
        return servicio.actualizar(input);
    }

    @MutationMapping
    public AgenteNavegacion eliminarAgenteNavegacion(@Argument int idNavegacion) throws Exception {
        return servicio.eliminar(idNavegacion);
    }

    @SchemaMapping(typeName = "AgenteNavegacion", field = "fechaUltimaCalibracion")
    public String fechaUltimaCalibracion(AgenteNavegacion agente) {
        return FechaUtil.formatear(agente.getFechaUltimaCalibracion());
    }
}
