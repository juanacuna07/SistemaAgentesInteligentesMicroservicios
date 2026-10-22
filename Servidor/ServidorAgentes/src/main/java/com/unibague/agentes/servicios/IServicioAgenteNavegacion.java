package com.unibague.agentes.servicios;

import com.unibague.agentes.dto.AgenteNavegacionInput;
import com.unibague.agentes.model.AgenteNavegacion;

import java.util.List;
import java.util.Optional;

public interface IServicioAgenteNavegacion {
    List<AgenteNavegacion> listar(String destino, Integer idAgenteInteligente);

    Optional<AgenteNavegacion> buscarPorId(int idNavegacion);

    Optional<AgenteNavegacion> buscarPorCriterios(String nombre, Integer idAgenteInteligente);

    AgenteNavegacion adicionar(AgenteNavegacionInput input) throws Exception;

    AgenteNavegacion actualizar(AgenteNavegacionInput input) throws Exception;

    AgenteNavegacion eliminar(int idNavegacion) throws Exception;
}
