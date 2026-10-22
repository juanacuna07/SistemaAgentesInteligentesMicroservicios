import React, { useState } from 'react';
import AgenteNavegacionService from '../servicios/AgenteNavegacionService';
import { Ventana, Mensaje, DetalleAgenteNavegacion, SelectorMaestro, useAgentesInteligentes } from './Comunes';

// Consulta de UN solo agente de navegación a partir de uno o dos parámetros
export default function VistaConsultar() {
  const maestros = useAgentesInteligentes();
  const [criterio, setCriterio] = useState('codigo');
  const [id, setId] = useState('');
  const [nombre, setNombre] = useState('');
  const [idMaestro, setIdMaestro] = useState('');
  const [agente, setAgente] = useState(null);
  const [mensaje, setMensaje] = useState(null);

  const consultar = async (e) => {
    e.preventDefault();
    setAgente(null);
    setMensaje(null);
    try {
      let resultado;
      if (criterio === 'codigo') {
        if (!(parseInt(id) > 0)) return setMensaje({ tipo: 'error', texto: 'Ingrese un ID válido.' });
        resultado = await AgenteNavegacionService.buscarPorId(parseInt(id));
      } else {
        if (!nombre.trim() && !idMaestro)
          return setMensaje({ tipo: 'error', texto: 'Ingrese el nombre y/o seleccione un agente inteligente.' });
        resultado = await AgenteNavegacionService.buscarPorCriterios(nombre.trim() || null, idMaestro ? parseInt(idMaestro) : null);
      }
      if (!resultado) setMensaje({ tipo: 'error', texto: 'No se encontró ningún agente de navegación con esos criterios.' });
      setAgente(resultado);
    } catch (err) {
      setMensaje({ tipo: 'error', texto: err.message });
    }
  };

  return (
    <Ventana titulo="Consultar Agente de Navegación" descripcion="Busque un agente por su ID, o por nombre y/o agente inteligente.">
      <div className="pestanas">
        <button className={criterio === 'codigo' ? 'activa' : ''} onClick={() => setCriterio('codigo')}>Por ID</button>
        <button className={criterio === 'criterios' ? 'activa' : ''} onClick={() => setCriterio('criterios')}>Por nombre / agente inteligente</button>
      </div>
      <form className="barra-busqueda" onSubmit={consultar}>
        {criterio === 'codigo' ? (
          <label>ID del agente de navegación
            <input type="number" min="1" value={id} onChange={e => setId(e.target.value)} />
          </label>
        ) : (
          <>
            <label>Nombre (contiene)
              <input type="text" value={nombre} onChange={e => setNombre(e.target.value)} placeholder="Ej: navi" />
            </label>
            <label>Agente inteligente
              <SelectorMaestro value={idMaestro} onChange={e => setIdMaestro(e.target.value)} maestros={maestros} textoVacio="-- Cualquiera --" />
            </label>
          </>
        )}
        <button type="submit" className="btn">Consultar</button>
      </form>
      <Mensaje mensaje={mensaje} />
      <DetalleAgenteNavegacion agente={agente} maestros={maestros} />
    </Ventana>
  );
}
