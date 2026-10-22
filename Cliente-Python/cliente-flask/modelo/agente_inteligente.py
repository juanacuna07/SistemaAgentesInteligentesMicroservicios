"""Modelo del cliente: entidad AgenteInteligente (maestro) y su Builder."""
from dataclasses import dataclass, field
from datetime import datetime

ESTADOS = ["ACTIVO", "INACTIVO", "MANTENIMIENTO"]


@dataclass
class AgenteInteligente:
    id_agente: int
    nombre: str
    nivel_autonomia: float
    estado_operativo: str
    nivel_confianza: float
    fecha_registro: datetime
    agentes_navegacion: list = field(default_factory=list)

    @property
    def fecha_texto(self) -> str:
        return self.fecha_registro.strftime("%Y-%m-%d %H:%M")

    @property
    def fecha_input(self) -> str:
        return self.fecha_registro.strftime("%Y-%m-%dT%H:%M")

    def a_input(self) -> dict:
        """Convierte el objeto en el AgenteInteligenteInput que espera el servidor GraphQL."""
        return {
            "idAgente": self.id_agente,
            "nombre": self.nombre,
            "nivelAutonomia": self.nivel_autonomia,
            "estadoOperativo": self.estado_operativo,
            "nivelConfianza": self.nivel_confianza,
            "fechaRegistro": self.fecha_input,
        }

    def a_formulario(self) -> dict:
        return {"idAgente": self.id_agente, "nombre": self.nombre, "nivelAutonomia": self.nivel_autonomia,
                "estadoOperativo": self.estado_operativo, "nivelConfianza": self.nivel_confianza,
                "fechaRegistro": self.fecha_input}

    @staticmethod
    def desde_json(d: dict) -> "AgenteInteligente":
        return AgenteInteligente(
            id_agente=int(d["idAgente"]),
            nombre=d["nombre"],
            nivel_autonomia=float(d["nivelAutonomia"]),
            estado_operativo=d["estadoOperativo"],
            nivel_confianza=float(d["nivelConfianza"]),
            fecha_registro=datetime.fromisoformat(d["fechaRegistro"]),
            agentes_navegacion=d.get("agentesNavegacion") or [],
        )


class AgenteInteligenteBuilder:
    """Patrón BUILDER: construye y valida un AgenteInteligente a partir del formulario."""

    def __init__(self):
        self._datos = {}
        self._errores = []

    def _rango(self, campo, valor, etiqueta):
        try:
            v = float(valor)
            if not 0 <= v <= 100:
                raise ValueError
            self._datos[campo] = v
        except (TypeError, ValueError):
            self._errores.append(f"{etiqueta} debe ser un número entre 0 y 100.")

    def id_agente(self, valor):
        try:
            v = int(valor)
            if v <= 0:
                raise ValueError
            self._datos["id_agente"] = v
        except (TypeError, ValueError):
            self._errores.append("El ID debe ser un entero positivo.")
        return self

    def nombre(self, valor):
        if not valor or not valor.strip():
            self._errores.append("El nombre es obligatorio.")
        else:
            self._datos["nombre"] = valor.strip()
        return self

    def nivel_autonomia(self, valor):
        self._rango("nivel_autonomia", valor, "El nivel de autonomía")
        return self

    def estado_operativo(self, valor):
        if valor not in ESTADOS:
            self._errores.append("Seleccione un estado operativo válido.")
        else:
            self._datos["estado_operativo"] = valor
        return self

    def nivel_confianza(self, valor):
        self._rango("nivel_confianza", valor, "El nivel de confianza")
        return self

    def fecha_registro(self, valor):
        try:
            self._datos["fecha_registro"] = datetime.fromisoformat(valor)
        except (TypeError, ValueError):
            self._errores.append("La fecha de registro no es válida.")
        return self

    def build(self) -> AgenteInteligente:
        if self._errores:
            raise ValueError("\n".join(self._errores))
        return AgenteInteligente(**self._datos)

    @staticmethod
    def desde_formulario(form) -> AgenteInteligente:
        return (AgenteInteligenteBuilder()
                .id_agente(form.get("idAgente"))
                .nombre(form.get("nombre"))
                .nivel_autonomia(form.get("nivelAutonomia"))
                .estado_operativo(form.get("estadoOperativo", ""))
                .nivel_confianza(form.get("nivelConfianza"))
                .fecha_registro(form.get("fechaRegistro"))
                .build())
