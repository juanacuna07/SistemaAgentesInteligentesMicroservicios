import React, { useState } from 'react';
import AgenteNavegacionService from '../servicios/AgenteNavegacionService';
import { Ventana, Mensaje, DetalleAgenteNavegacion, BuscadorPorId, useAgentesInteligentes } from './Comunes';

export default function VistaEliminar() {
  const maestros = useAgentesInteligentes();
  const [agente, setAgente] = useState(null);
  const [mensaje, setMensaje] = useState(null);

  const eliminar = async () => {
    if (!window.confirm(`¿Está seguro de eliminar el agente de navegación #${agente.idNavegacion}?`)) return;
    try {
      await AgenteNavegacionService.eliminar(parseInt(agente.idNavegacion));
      setMensaje({ tipo: 'exito', texto: `Agente de navegación #${agente.idNavegacion} eliminado.` });
      setAgente(null);
    } catch (err) {
      setMensaje({ tipo: 'error', texto: err.message });
    }
  };

  return (
    <Ventana titulo="Eliminar Agente de Navegación" descripcion="Busque el agente, revise toda su información y confirme la eliminación.">
      <BuscadorPorId
        onEncontrado={(a) => { setAgente(a); setMensaje(null); }}
        onError={(texto) => { setAgente(null); setMensaje({ tipo: 'error', texto }); }}
      />
      <DetalleAgenteNavegacion agente={agente} maestros={maestros} />
      <Mensaje mensaje={mensaje} />
      {agente && (
        <div className="acciones">
          <button className="btn peligro" onClick={eliminar}>Eliminar</button>
        </div>
      )}
    </Ventana>
  );
}
