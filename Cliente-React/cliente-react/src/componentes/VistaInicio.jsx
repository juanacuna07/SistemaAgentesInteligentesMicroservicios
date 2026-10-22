import React from 'react';
import Icono from './Icono';

export default function VistaInicio() {
  return (
    <section className="inicio">
      <Icono nombre="navegacion" tamano={56} className="icono-inicio" />
      <h1>Sistema de Agentes Inteligentes</h1>
      <p className="subtitulo">Módulo de gestión de <strong>Agentes de Navegación</strong> (detalle)</p>
      <p>Seleccione una opción del menú <strong>Agente de Navegación</strong> para comenzar.</p>
    </section>
  );
}
