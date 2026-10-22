import React from 'react';

// Integrantes del equipo (verificar)
export const INTEGRANTES = [
  'Juan Acuña',
  'Luis Hernández',
  'Stephany Trujillo'
];
export const VERSION = '1.0.0';

export default function AcercaDe({ onCerrar }) {
  return (
    <div className="modal-fondo" onClick={onCerrar}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <h2>Acerca de...</h2>
        <p><strong>Sistema de Agentes Inteligentes</strong> — Cliente Web React (Agentes de Navegación)</p>
        <p>Versión {VERSION}</p>
        <p>Integrantes:</p>
        <ul>{INTEGRANTES.map(n => <li key={n}>{n}</li>)}</ul>
        <p className="pie">Diseño de Soluciones · Universidad de Ibagué · 2026B</p>
        <button className="btn" onClick={onCerrar}>Cerrar</button>
      </div>
    </div>
  );
}
