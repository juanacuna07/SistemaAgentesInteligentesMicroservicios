package com.unibague.agentes.model;

import java.time.LocalDateTime;

/**
 * Clase DETALLE (Clase B). Cada agente de navegacion pertenece a exactamente 1 agente inteligente.
 * Atributos: int, String, String, String, double, LocalDateTime e int (agente inteligente al que pertenece).
 * Se construye con el patron Builder: AgenteNavegacion.builder()...build()
 */
public class AgenteNavegacion {

    private int idNavegacion;
    private String nombre;
    private String origen;
    private String destino;
    private double precisionGPS;                 // 0 - 100
    private LocalDateTime fechaUltimaCalibracion;
    private int idAgenteInteligente;

    private AgenteNavegacion() {
    }

    public static Builder builder() {
        return new Builder();
    }

    // ================================ BUILDER ================================
    public static class Builder {
        private final AgenteNavegacion agente = new AgenteNavegacion();

        public Builder idNavegacion(int idNavegacion) throws Exception {
            agente.setIdNavegacion(idNavegacion);
            return this;
        }

        public Builder nombre(String nombre) throws Exception {
            agente.setNombre(nombre);
            return this;
        }

        public Builder origen(String origen) throws Exception {
            agente.setOrigen(origen);
            return this;
        }

        public Builder destino(String destino) throws Exception {
            agente.setDestino(destino);
            return this;
        }

        public Builder precisionGPS(double precisionGPS) throws Exception {
            agente.setPrecisionGPS(precisionGPS);
            return this;
        }

        public Builder fechaUltimaCalibracion(LocalDateTime fecha) throws Exception {
            agente.setFechaUltimaCalibracion(fecha);
            return this;
        }

        public Builder idAgenteInteligente(int idAgenteInteligente) throws Exception {
            agente.setIdAgenteInteligente(idAgenteInteligente);
            return this;
        }

        public AgenteNavegacion build() throws Exception {
            if (agente.idNavegacion <= 0 || agente.nombre == null || agente.origen == null
                    || agente.destino == null || agente.fechaUltimaCalibracion == null
                    || agente.idAgenteInteligente <= 0) {
                throw new Exception("Faltan datos obligatorios del agente de navegacion");
            }
            return agente;
        }
    }

    // =========================== GETTERS / SETTERS ===========================
    public int getIdNavegacion() {
        return idNavegacion;
    }

    public void setIdNavegacion(int idNavegacion) throws Exception {
        if (idNavegacion <= 0) {
            throw new Exception("El ID del agente de navegacion debe ser un entero positivo (mayor que 0)");
        }
        this.idNavegacion = idNavegacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws Exception {
        if (nombre == null || nombre.isBlank()) {
            throw new Exception("El nombre del agente de navegacion es obligatorio");
        }
        this.nombre = nombre.trim();
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) throws Exception {
        if (origen == null || origen.isBlank()) {
            throw new Exception("El origen es obligatorio");
        }
        this.origen = origen.trim();
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) throws Exception {
        if (destino == null || destino.isBlank()) {
            throw new Exception("El destino es obligatorio");
        }
        this.destino = destino.trim();
    }

    public double getPrecisionGPS() {
        return precisionGPS;
    }

    public void setPrecisionGPS(double precisionGPS) throws Exception {
        if (precisionGPS < 0 || precisionGPS > 100) {
            throw new Exception("La precision GPS debe estar entre 0 y 100");
        }
        this.precisionGPS = precisionGPS;
    }

    public LocalDateTime getFechaUltimaCalibracion() {
        return fechaUltimaCalibracion;
    }

    public void setFechaUltimaCalibracion(LocalDateTime fechaUltimaCalibracion) throws Exception {
        if (fechaUltimaCalibracion == null) {
            throw new Exception("La fecha de ultima calibracion es obligatoria");
        }
        if (fechaUltimaCalibracion.isAfter(LocalDateTime.now().plusMinutes(1))) {
            throw new Exception("La fecha de ultima calibracion no puede estar en el futuro");
        }
        this.fechaUltimaCalibracion = fechaUltimaCalibracion;
    }

    public int getIdAgenteInteligente() {
        return idAgenteInteligente;
    }

    public void setIdAgenteInteligente(int idAgenteInteligente) throws Exception {
        if (idAgenteInteligente <= 0) {
            throw new Exception("Debe indicar el agente inteligente al que pertenece");
        }
        this.idAgenteInteligente = idAgenteInteligente;
    }
}
