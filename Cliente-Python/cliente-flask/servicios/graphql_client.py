"""Patrón SINGLETON: una única instancia del cliente HTTP GraphQL."""
import requests

ENDPOINT = "http://localhost:8081/graphql"


class GraphQLError(Exception):
    pass


class GraphQLClient:
    _instancia = None

    def __new__(cls):
        if cls._instancia is None:
            cls._instancia = super().__new__(cls)
            cls._instancia.endpoint = ENDPOINT
            cls._instancia.sesion = requests.Session()
        return cls._instancia

    @classmethod
    def get_instance(cls) -> "GraphQLClient":
        return cls()

    def ejecutar(self, query: str, variables: dict | None = None) -> dict:
        try:
            respuesta = self.sesion.post(self.endpoint, json={"query": query, "variables": variables or {}}, timeout=10)
        except requests.RequestException:
            raise GraphQLError("No se pudo conectar con el servidor GraphQL (¿está encendido en el puerto 8081?)")
        resultado = respuesta.json()
        if resultado.get("errors"):
            raise GraphQLError("\n".join(e.get("message", "Error") for e in resultado["errors"]))
        return resultado.get("data") or {}
