package com.unibague.agentes.dto;

/** Datos que envia el cliente en crear/actualizar AgenteInteligente. Fecha en texto ISO (2026-10-22T14:30). */
public record AgenteInteligenteInput(Integer idAgente,
                                     String nombre,
                                     Double nivelAutonomia,
                                     String estadoOperativo,
                                     Double nivelConfianza,
                                     String fechaRegistro) {
}
