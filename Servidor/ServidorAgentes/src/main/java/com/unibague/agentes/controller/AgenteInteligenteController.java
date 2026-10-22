package com.unibague.agentes.controller;

import com.unibague.agentes.dto.AgenteInteligenteInput;
import com.unibague.agentes.model.AgenteInteligente;
import com.unibague.agentes.servicios.FechaUtil;
import com.unibague.agentes.servicios.IServicioAgenteInteligente;
import com.unibague.agentes.servicios.ServicioAgenteInteligente;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/** Controlador (MVC) que expone los microservicios GraphQL del MAESTRO AgenteInteligente. */
@Controller
public class AgenteInteligenteController {

    private final IServicioAgenteInteligente servicio = ServicioAgenteInteligente.getInstance();

    @QueryMapping
    public List<AgenteInteligente> agentesInteligentes(@Argument String nombre, @Argument String estadoOperativo) {
        return servicio.listar(nombre, estadoOperativo);
    }

    @QueryMapping
    public AgenteInteligente agenteInteligentePorId(@Argument int idAgente) {
        return servicio.buscarPorId(idAgente).orElse(null);
    }

    @QueryMapping
    public AgenteInteligente agenteInteligentePorCriterios(@Argument String nombre, @Argument String estadoOperativo) {
        return servicio.buscarPorCriterios(nombre, estadoOperativo).orElse(null);
    }

    @MutationMapping
    public AgenteInteligente crearAgenteInteligente(@Argument AgenteInteligenteInput input) throws Exception {
        return servicio.adicionar(input);
    }

    @MutationMapping
    public AgenteInteligente actualizarAgenteInteligente(@Argument AgenteInteligenteInput input) throws Exception {
        return servicio.actualizar(input);
    }

    @MutationMapping
    public AgenteInteligente eliminarAgenteInteligente(@Argument int idAgente) throws Exception {
        return servicio.eliminar(idAgente);
    }

    /** GraphQL no tiene tipo fecha: el LocalDateTime se envia como texto ISO. */
    @SchemaMapping(typeName = "AgenteInteligente", field = "fechaRegistro")
    public String fechaRegistro(AgenteInteligente agente) {
        return FechaUtil.formatear(agente.getFechaRegistro());
    }
}
