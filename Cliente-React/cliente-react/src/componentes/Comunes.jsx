// Componentes reutilizables por las vistas (cada vista = una ventana = un caso de uso).
import React, { useEffect, useState } from 'react';
import AgenteNavegacionService from '../servicios/AgenteNavegacionService';
import { formatearFecha } from '../modelo/AgenteNavegacion';

export function Ventana({ titulo, descripcion, children }) {
  return (
    <section className="ventana">
      <header className="ventana-titulo">
        <h2>{titulo}</h2>
        {descripcion && <p>{descripcion}</p>}
      </header>
      <div className="ventana-cuerpo">{children}</div>
    </section>
  );
}

export function Mensaje({ mensaje }) {
  if (!mensaje) return null;
  return <div className={`mensaje ${mensaje.tipo}`}>{mensaje.texto}</div>;
}

// Lista de agentes inteligentes (maestros) para los selectores
export function useAgentesInteligentes() {
  const [agentes, setAgentes] = useState([]);
  useEffect(() => {
    AgenteNavegacionService.listarAgentesInteligentes().then(setAgentes).catch(() => setAgentes([]));
  }, []);
  return agentes;
}

export function nombreMaestro(maestros, id) {
  const m = maestros.find(x => String(x.idAgente) === String(id));
  return m ? `${m.idAgente} - ${m.nombre}` : String(id);
}

export function SelectorMaestro({ value, onChange, maestros, textoVacio = '-- Seleccione --' }) {
  return (
    <select value={value} onChange={onChange}>
      <option value="">{textoVacio}</option>
      {maestros.map(m => <option key={m.idAgente} value={m.idAgente}>{m.idAgente} - {m.nombre}</option>)}
    </select>
  );
}

// Muestra TODA la información (requisito antes de eliminar / actualizar)
export function DetalleAgenteNavegacion({ agente, maestros }) {
  if (!agente) return null;
  const filas = [
    ['ID', agente.idNavegacion],
    ['Nombre', agente.nombre],
    ['Origen', agente.origen],
    ['Destino', agente.destino],
    ['Precisión GPS', `${agente.precisionGPS} %`],
    ['Fecha última calibración', formatearFecha(agente.fechaUltimaCalibracion)],
    ['Agente inteligente', nombreMaestro(maestros, agente.idAgenteInteligente)]
  ];
  return (
    <dl className="detalle">
      {filas.map(([k, v]) => (
        <React.Fragment key={k}>
          <dt>{k}</dt>
          <dd>{v}</dd>
        </React.Fragment>
      ))}
    </dl>
  );
}

export function FormularioAgenteNavegacion({ form, setForm, maestros, idBloqueado = false }) {
  const cambiar = (campo) => (e) => setForm({ ...form, [campo]: e.target.value });
  return (
    <div className="formulario">
      <label>ID (entero)
        <input type="number" min="1" value={form.idNavegacion} onChange={cambiar('idNavegacion')} disabled={idBloqueado} placeholder="Ej: 10" />
      </label>
      <label>Nombre
        <input type="text" value={form.nombre} onChange={cambiar('nombre')} placeholder="Ej: Ruta Express" />
      </label>
      <label>Origen
        <input type="text" value={form.origen} onChange={cambiar('origen')} placeholder="Ej: La Pola" />
      </label>
      <label>Destino
        <input type="text" value={form.destino} onChange={cambiar('destino')} placeholder="Ej: Parque Centenario" />
      </label>
      <label>Precisión GPS (0-100)
        <input type="number" min="0" max="100" step="0.01" value={form.precisionGPS} onChange={cambiar('precisionGPS')} placeholder="Ej: 92.5" />
      </label>
      <label>Fecha y hora de última calibración
        <input type="datetime-local" value={form.fechaUltimaCalibracion} onChange={cambiar('fechaUltimaCalibracion')} />
      </label>
      <label className="ancho">Agente inteligente al que pertenece
        <SelectorMaestro value={form.idAgenteInteligente} onChange={cambiar('idAgenteInteligente')} maestros={maestros} />
      </label>
    </div>
  );
}

// Búsqueda previa por ID (usada por Eliminar y Actualizar)
export function BuscadorPorId({ onEncontrado, onError }) {
  const [id, setId] = useState('');
  const buscar = async (e) => {
    e.preventDefault();
    if (!(parseInt(id) > 0)) return onError('Ingrese un ID válido (entero positivo).');
    try {
      const agente = await AgenteNavegacionService.buscarPorId(parseInt(id));
      if (!agente) return onError(`No existe ningún agente de navegación con el ID ${id}.`);
      onEncontrado(agente);
    } catch (err) {
      onError(err.message);
    }
  };
  return (
    <form className="barra-busqueda" onSubmit={buscar}>
      <label>ID del agente de navegación
        <input type="number" min="1" value={id} onChange={e => setId(e.target.value)} placeholder="Ej: 1" />
      </label>
      <button type="submit" className="btn">Buscar</button>
    </form>
  );
}
