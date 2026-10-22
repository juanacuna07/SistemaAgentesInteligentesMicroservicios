"""Controlador de la ventana principal y del menú Ayuda."""
from flask import Blueprint, render_template

principal_bp = Blueprint("principal", __name__)


@principal_bp.route("/")
def inicio():
    return render_template("inicio.html")


@principal_bp.route("/acerca-de")
def acerca_de():
    return render_template("acerca.html")
