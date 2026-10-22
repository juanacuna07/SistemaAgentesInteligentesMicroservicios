package com.unibague.agentes.dto;

/** Datos que envia el cliente en crear/actualizar AgenteNavegacion. Fecha en texto ISO (2026-10-22T14:30). */
public record AgenteNavegacionInput(Integer idNavegacion,
                                    String nombre,
                                    String origen,
                                    String destino,
                                    Double precisionGPS,
                                    String fechaUltimaCalibracion,
                                    Integer idAgenteInteligente) {
}
