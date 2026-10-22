package com.unibague.agentes.servicios;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/** Conversion entre LocalDateTime y el texto ISO que viaja por GraphQL. */
public final class FechaUtil {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private FechaUtil() {
    }

    /** Acepta "2026-10-22T14:30", "2026-10-22T14:30:00" o "2026-10-22 14:30". */
    public static LocalDateTime parse(String texto) throws Exception {
        if (texto == null || texto.isBlank()) {
            throw new Exception("La fecha es obligatoria");
        }
        try {
            return LocalDateTime.parse(texto.trim().replace(' ', 'T'));
        } catch (DateTimeParseException e) {
            throw new Exception("Formato de fecha invalido. Use yyyy-MM-ddTHH:mm (ej. 2026-10-22T14:30)");
        }
    }

    public static String formatear(LocalDateTime fecha) {
        return fecha == null ? null : fecha.format(FORMATO);
    }
}
