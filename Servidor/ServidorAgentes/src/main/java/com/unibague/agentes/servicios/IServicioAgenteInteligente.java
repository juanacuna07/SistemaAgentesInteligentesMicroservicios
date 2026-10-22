package com.unibague.agentes.servicios;

import com.unibague.agentes.dto.AgenteInteligenteInput;
import com.unibague.agentes.model.AgenteInteligente;

import java.util.List;
import java.util.Optional;

/** Abstraccion (D de SOLID): los controladores dependen de esta interfaz, no de la implementacion. */
public interface IServicioAgenteInteligente {
    List<AgenteInteligente> listar(String nombre, String estadoOperativo);

    Optional<AgenteInteligente> buscarPorId(int idAgente);

    Optional<AgenteInteligente> buscarPorCriterios(String nombre, String estadoOperativo);

    AgenteInteligente adicionar(AgenteInteligenteInput input) throws Exception;

    AgenteInteligente actualizar(AgenteInteligenteInput input) throws Exception;

    AgenteInteligente eliminar(int idAgente) throws Exception;
}
