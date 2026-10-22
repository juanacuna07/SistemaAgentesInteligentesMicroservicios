package com.unibague.agentes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase MAESTRO (Clase A). Un agente inteligente coordina 0..* agentes de navegacion.
 * Atributos: int, String, double, String (estado), double, LocalDateTime y la lista de detalles.
 * Se construye con el patron Builder: AgenteInteligente.builder()...build()
 */
public class AgenteInteligente {

    private int idAgente;
    private String nombre;
    private double nivelAutonomia;      // 0 - 100
    private String estadoOperativo;     // ACTIVO / INACTIVO / MANTENIMIENTO
    private double nivelConfianza;      // 0 - 100
    private LocalDateTime fechaRegistro;
    private final List<AgenteNavegacion> agentesNavegacion = new ArrayList<>();

    private AgenteInteligente() {
    }

    public static Builder builder() {
        return new Builder();
    }

    // ================================ BUILDER ================================
    public static class Builder {
        private final AgenteInteligente agente = new AgenteInteligente();

        public Builder idAgente(int idAgente) throws Exception {
            agente.setIdAgente(idAgente);
            return this;
        }

        public Builder nombre(String nombre) throws Exception {
            agente.setNombre(nombre);
            return this;
        }

        public Builder nivelAutonomia(double nivelAutonomia) throws Exception {
            agente.setNivelAutonomia(nivelAutonomia);
            return this;
        }

        public Builder estadoOperativo(String estadoOperativo) throws Exception {
            agente.setEstadoOperativo(estadoOperativo);
            return this;
        }

        public Builder nivelConfianza(double nivelConfianza) throws Exception {
            agente.setNivelConfianza(nivelConfianza);
            return this;
        }

        public Builder fechaRegistro(LocalDateTime fechaRegistro) throws Exception {
            agente.setFechaRegistro(fechaRegistro);
            return this;
        }

        public AgenteInteligente build() throws Exception {
            if (agente.idAgente <= 0 || agente.nombre == null || agente.estadoOperativo == null
                    || agente.fechaRegistro == null) {
                throw new Exception("Faltan datos obligatorios del agente inteligente");
            }
            return agente;
        }
    }

    // =========================== GETTERS / SETTERS ===========================
    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) throws Exception {
        if (idAgente <= 0) {
            throw new Exception("El ID del agente debe ser un numero entero positivo (mayor que 0)");
        }
        this.idAgente = idAgente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws Exception {
        if (nombre == null || nombre.isBlank()) {
            throw new Exception("El nombre del agente es obligatorio");
        }
        this.nombre = nombre.trim();
    }

    public double getNivelAutonomia() {
        return nivelAutonomia;
    }

    public void setNivelAutonomia(double nivelAutonomia) throws Exception {
        if (nivelAutonomia < 0 || nivelAutonomia > 100) {
            throw new Exception("Nivel de autonomia debe estar entre 0 y 100");
        }
        this.nivelAutonomia = nivelAutonomia;
    }

    public String getEstadoOperativo() {
        return estadoOperativo;
    }

    public void setEstadoOperativo(String estadoOperativo) throws Exception {
        if (estadoOperativo != null && (estadoOperativo.equalsIgnoreCase("ACTIVO")
                || estadoOperativo.equalsIgnoreCase("INACTIVO")
                || estadoOperativo.equalsIgnoreCase("MANTENIMIENTO"))) {
            this.estadoOperativo = estadoOperativo.toUpperCase();
        } else {
            throw new Exception("Estado operativo invalido. Use ACTIVO, INACTIVO o MANTENIMIENTO");
        }
    }

    public double getNivelConfianza() {
        return nivelConfianza;
    }

    public void setNivelConfianza(double nivelConfianza) throws Exception {
        if (nivelConfianza < 0 || nivelConfianza > 100) {
            throw new Exception("Nivel de confianza debe estar entre 0 y 100");
        }
        this.nivelConfianza = nivelConfianza;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) throws Exception {
        if (fechaRegistro == null) {
            throw new Exception("La fecha de registro es obligatoria");
        }
        this.fechaRegistro = fechaRegistro;
    }

    // ======================== RELACION MAESTRO - DETALLE ========================
    public List<AgenteNavegacion> getAgentesNavegacion() {
        return List.copyOf(agentesNavegacion);
    }

    public void addAgenteNavegacion(AgenteNavegacion agenteNavegacion) {
        agentesNavegacion.add(agenteNavegacion);
    }

    public void delAgenteNavegacion(AgenteNavegacion agenteNavegacion) {
        agentesNavegacion.remove(agenteNavegacion);
    }
}
