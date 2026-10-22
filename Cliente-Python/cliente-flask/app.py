"""Cliente Web Python (Flask) - Sistema de Agentes Inteligentes.
Gestiona el objeto MAESTRO (AgenteInteligente) consumiendo los microservicios GraphQL del servidor Java.

Ejecutar:  pip install -r requirements.txt   y luego   python app.py
Abrir:     http://localhost:5000
"""
from flask import Flask

from controladores.agente_inteligente_controller import agente_bp
from controladores.principal_controller import principal_bp

VERSION = "1.0.0"
# Integrantes del equipo (verificar)
INTEGRANTES = ["Juan Acuña", "Luis Hernández", "Stephany Trujillo"]


def crear_app() -> Flask:
    app = Flask(__name__)
    app.secret_key = "agentes-inteligentes-2026B"
    app.register_blueprint(principal_bp)
    app.register_blueprint(agente_bp)

    @app.context_processor
    def datos_globales():
        return {"VERSION": VERSION, "INTEGRANTES": INTEGRANTES}

    return app


if __name__ == "__main__":
    crear_app().run(host="0.0.0.0", port=5000, debug=True)
