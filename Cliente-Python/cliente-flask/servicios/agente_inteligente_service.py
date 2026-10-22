"""Capa de servicios del cliente: consume los microservicios GraphQL de AgenteInteligente."""
from modelo.agente_inteligente import AgenteInteligente
from servicios.graphql_client import GraphQLClient

CAMPOS = ("idAgente nombre nivelAutonomia estadoOperativo nivelConfianza fechaRegistro "
          "agentesNavegacion { idNavegacion nombre origen destino }")


class AgenteInteligenteService:
    _instancia = None

    def __new__(cls):  # Singleton
        if cls._instancia is None:
            cls._instancia = super().__new__(cls)
            cls._instancia.cliente = GraphQLClient.get_instance()
        return cls._instancia

    def listar(self, nombre=None, estado=None) -> list[AgenteInteligente]:
        data = self.cliente.ejecutar(
            f"query ($nombre: String, $estado: String) {{ agentesInteligentes(nombre: $nombre, estadoOperativo: $estado) {{ {CAMPOS} }} }}",
            {"nombre": nombre or None, "estado": estado or None})
        return [AgenteInteligente.desde_json(a) for a in data.get("agentesInteligentes") or []]

    def buscar_por_id(self, id_agente: int) -> AgenteInteligente | None:
        data = self.cliente.ejecutar(
            f"query ($id: Int!) {{ agenteInteligentePorId(idAgente: $id) {{ {CAMPOS} }} }}", {"id": id_agente})
        a = data.get("agenteInteligentePorId")
        return AgenteInteligente.desde_json(a) if a else None

    def buscar_por_criterios(self, nombre=None, estado=None) -> AgenteInteligente | None:
        data = self.cliente.ejecutar(
            f"query ($nombre: String, $estado: String) {{ agenteInteligentePorCriterios(nombre: $nombre, estadoOperativo: $estado) {{ {CAMPOS} }} }}",
            {"nombre": nombre or None, "estado": estado or None})
        a = data.get("agenteInteligentePorCriterios")
        return AgenteInteligente.desde_json(a) if a else None

    def adicionar(self, agente: AgenteInteligente) -> AgenteInteligente:
        data = self.cliente.ejecutar(
            f"mutation ($input: AgenteInteligenteInput!) {{ crearAgenteInteligente(input: $input) {{ {CAMPOS} }} }}",
            {"input": agente.a_input()})
        return AgenteInteligente.desde_json(data["crearAgenteInteligente"])

    def actualizar(self, agente: AgenteInteligente) -> AgenteInteligente:
        data = self.cliente.ejecutar(
            f"mutation ($input: AgenteInteligenteInput!) {{ actualizarAgenteInteligente(input: $input) {{ {CAMPOS} }} }}",
            {"input": agente.a_input()})
        return AgenteInteligente.desde_json(data["actualizarAgenteInteligente"])

    def eliminar(self, id_agente: int) -> AgenteInteligente:
        data = self.cliente.ejecutar(
            f"mutation ($id: Int!) {{ eliminarAgenteInteligente(idAgente: $id) {{ {CAMPOS} }} }}", {"id": id_agente})
        return AgenteInteligente.desde_json(data["eliminarAgenteInteligente"])
