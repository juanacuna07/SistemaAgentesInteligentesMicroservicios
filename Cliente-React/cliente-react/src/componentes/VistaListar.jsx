import React, { useCallback, useEffect, useState } from 'react';
import AgenteNavegacionService from '../servicios/AgenteNavegacionService';
import AgenteNavegacionObservable from '../modelo/AgenteNavegacionObservable';
import { formatearFecha } from '../modelo/AgenteNavegacion';
import Icono from './Icono';
import { Ventana, Mensaje, SelectorMaestro, nombreMaestro, useAgentesInteligentes } from './Comunes';

// Listar con grilla y filtros. Esta vista es un OBSERVADOR de AgenteNavegacionObservable:
// cuando un agente de navegación se adiciona, actualiza o elimina, la grilla se refresca sola.
export default function VistaListar() {
  const maestros = useAgentesInteligentes();
  const [agentes, setAgentes] = useState([]);
  const [destino, setDestino] = useState('');
  const [idMaestro, setIdMaestro] = useState('');
  const [mensaje, setMensaje] = useState(null);
  const [ultimoEvento, setUltimoEvento] = useState(null);
  const [autoRefresco, setAutoRefresco] = useState(false);

  const cargar = useCallback(async () => {
    try {
      setAgentes(await AgenteNavegacionService.listar(destino.trim() || null, idMaestro ? parseInt(idMaestro) : null));
      setMensaje(null);
    } catch (err) {
      setMensaje({ tipo: 'error', texto: err.message });
    }
  }, [destino, idMaestro]);

  useEffect(() => { cargar(); }, [cargar]);

  // Suscripción del observador
  useEffect(() => {
    const observador = (evento) => {
      setUltimoEvento(`Agente de navegación #${evento.agente?.idNavegacion} ${evento.tipo.toLowerCase()}`);
      cargar();
    };
    return AgenteNavegacionObservable.getInstance().suscribir(observador);
  }, [cargar]);

  // Refresco periódico opcional (cambios hechos desde otros clientes)
  useEffect(() => {
    if (!autoRefresco) return;
    const t = setInterval(cargar, 3000);
    return () => clearInterval(t);
  }, [autoRefresco, cargar]);

  return (
    <Ventana titulo="Listar Agentes de Navegación" descripcion="Consulte todos los agentes de navegación y filtre por destino y/o agente inteligente.">
      <div className="barra-busqueda">
        <label>Destino (contiene)
          <input type="text" value={destino} onChange={e => setDestino(e.target.value)} placeholder="Ej: aeropuerto" />
        </label>
        <label>Agente inteligente
          <SelectorMaestro value={idMaestro} onChange={e => setIdMaestro(e.target.value)} maestros={maestros} textoVacio="-- Todos --" />
        </label>
        <button className="btn secundario" onClick={() => { setDestino(''); setIdMaestro(''); }}>Quitar filtros</button>
        <label className="check">
          <input type="checkbox" checked={autoRefresco} onChange={e => setAutoRefresco(e.target.checked)} />
          Auto-refrescar
        </label>
      </div>
      <Mensaje mensaje={mensaje} />
      {ultimoEvento && <p className="evento"><Icono nombre="refrescar" tamano={14} /> Observer: {ultimoEvento} → grilla actualizada</p>}
      <div className="tabla-contenedor">
        <table className="grilla">
          <thead>
            <tr>
              <th>ID</th><th>Nombre</th><th>Origen</th><th>Destino</th>
              <th>Precisión GPS</th><th>Última calibración</th><th>Agente inteligente</th>
            </tr>
          </thead>
          <tbody>
            {agentes.length === 0 ? (
              <tr><td colSpan="7" className="vacio">No hay agentes de navegación para mostrar.</td></tr>
            ) : agentes.map(a => (
              <tr key={a.idNavegacion}>
                <td>{a.idNavegacion}</td>
                <td>{a.nombre}</td>
                <td>{a.origen}</td>
                <td>{a.destino}</td>
                <td>{a.precisionGPS} %</td>
                <td>{formatearFecha(a.fechaUltimaCalibracion)}</td>
                <td>{nombreMaestro(maestros, a.idAgenteInteligente)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <p className="total">Total: {agentes.length} agente(s) de navegación</p>
    </Ventana>
  );
}
