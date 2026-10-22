"""Controlador (MVC) de AgenteInteligente: cada ruta atiende UN solo caso de uso / una sola ventana."""
from datetime import datetime

from flask import Blueprint, flash, redirect, render_template, request, url_for

from modelo.agente_inteligente import ESTADOS, AgenteInteligenteBuilder
from servicios.agente_inteligente_service import AgenteInteligenteService
from servicios.graphql_client import GraphQLError

agente_bp = Blueprint("agente", __name__, url_prefix="/agentes")
servicio = AgenteInteligenteService()


@agente_bp.context_processor
def datos_vista():
    return {"ESTADOS": ESTADOS}


def _buscar(id_texto, avisar=True):
    try:
        id_agente = int(id_texto)
        if id_agente <= 0:
            raise ValueError
    except (TypeError, ValueError):
        flash("Ingrese un ID válido (entero positivo).", "error")
        return None
    try:
        agente = servicio.buscar_por_id(id_agente)
    except GraphQLError as e:
        flash(str(e), "error")
        return None
    if agente is None and avisar:
        flash(f"No existe ningún agente inteligente con el ID {id_agente}.", "error")
    return agente


# ------------------------------------------------------------------ Adicionar
@agente_bp.route("/adicionar", methods=["GET", "POST"])
def adicionar():
    form = request.form if request.method == "POST" else {
        "fechaRegistro": datetime.now().strftime("%Y-%m-%dT%H:%M"), "estadoOperativo": "ACTIVO"}
    if request.method == "POST":
        try:
            agente = servicio.adicionar(AgenteInteligenteBuilder.desde_formulario(request.form))
            flash(f"Agente inteligente #{agente.id_agente} '{agente.nombre}' adicionado correctamente.", "exito")
            return redirect(url_for("agente.adicionar"))
        except (ValueError, GraphQLError) as e:
            flash(str(e), "error")
    return render_template("adicionar.html", form=form)


# ------------------------------------------------------------------ Eliminar
@agente_bp.route("/eliminar", methods=["GET", "POST"])
def eliminar():
    agente = None
    id_buscado = request.values.get("id", "")
    if request.method == "POST" and request.form.get("confirmar") == "si":
        try:
            eliminado = servicio.eliminar(int(id_buscado))
            flash(f"Agente inteligente #{eliminado.id_agente} '{eliminado.nombre}' eliminado "
                  f"(junto con {len(eliminado.agentes_navegacion)} agente(s) de navegación).", "exito")
            return redirect(url_for("agente.eliminar"))
        except (ValueError, GraphQLError) as e:
            flash(str(e), "error")
    elif id_buscado:
        agente = _buscar(id_buscado)
    return render_template("eliminar.html", agente=agente, id_buscado=id_buscado)


# ------------------------------------------------------------------ Listar
@agente_bp.route("/listar")
def listar():
    nombre = request.args.get("nombre", "").strip()
    estado = request.args.get("estado", "")
    agentes = []
    try:
        agentes = servicio.listar(nombre, estado)
    except GraphQLError as e:
        flash(str(e), "error")
    return render_template("listar.html", agentes=agentes, nombre=nombre, estado=estado)


# ------------------------------------------------------------------ Actualizar
@agente_bp.route("/actualizar", methods=["GET", "POST"])
def actualizar():
    agente, form = None, None
    id_buscado = request.values.get("id", "") or request.form.get("idAgente", "")
    if request.method == "POST":
        try:
            actualizado = servicio.actualizar(AgenteInteligenteBuilder.desde_formulario(request.form))
            flash(f"Agente inteligente #{actualizado.id_agente} actualizado correctamente.", "exito")
            return redirect(url_for("agente.actualizar", id=actualizado.id_agente))
        except (ValueError, GraphQLError) as e:
            flash(str(e), "error")
            form = request.form
            agente = _buscar(id_buscado, avisar=False)
    elif id_buscado:
        agente = _buscar(id_buscado)
        if agente:
            form = agente.a_formulario()
    return render_template("actualizar.html", agente=agente, form=form, id_buscado=id_buscado)


# ------------------------------------------------------------------ Consultar
@agente_bp.route("/consultar")
def consultar():
    criterio = request.args.get("criterio", "codigo")
    agente, buscado = None, False
    try:
        if criterio == "codigo" and request.args.get("id"):
            buscado = True
            agente = _buscar(request.args.get("id"), avisar=False)
        elif criterio == "criterios" and (request.args.get("nombre", "").strip() or request.args.get("estado")):
            buscado = True
            agente = servicio.buscar_por_criterios(request.args.get("nombre", "").strip(), request.args.get("estado"))
        if buscado and not agente:
            flash("No se encontró ningún agente inteligente con esos criterios.", "error")
    except GraphQLError as e:
        flash(str(e), "error")
    return render_template("consultar.html", agente=agente, criterio=criterio, args=request.args)
