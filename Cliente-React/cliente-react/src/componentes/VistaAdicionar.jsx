import React, { useState } from 'react';
import AgenteNavegacionService from '../servicios/AgenteNavegacionService';
import { agenteNavegacionVacio, aInput, validar } from '../modelo/AgenteNavegacion';
import { Ventana, Mensaje, FormularioAgenteNavegacion, useAgentesInteligentes } from './Comunes';

export default function VistaAdicionar() {
  const maestros = useAgentesInteligentes();
  const [form, setForm] = useState(agenteNavegacionVacio());
  const [mensaje, setMensaje] = useState(null);

  const guardar = async (e) => {
    e.preventDefault();
    const error = validar(form);
    if (error) return setMensaje({ tipo: 'error', texto: error });
    try {
      const a = await AgenteNavegacionService.adicionar(aInput(form));
      setMensaje({ tipo: 'exito', texto: `Agente de navegación #${a.idNavegacion} '${a.nombre}' adicionado correctamente.` });
      setForm(agenteNavegacionVacio());
    } catch (err) {
      setMensaje({ tipo: 'error', texto: err.message });
    }
  };

  return (
    <Ventana titulo="Adicionar Agente de Navegación" descripcion="Registre un nuevo agente de navegación y asígnelo a un agente inteligente.">
      <form onSubmit={guardar}>
        <FormularioAgenteNavegacion form={form} setForm={setForm} maestros={maestros} />
        <Mensaje mensaje={mensaje} />
        <div className="acciones">
          <button type="button" className="btn secundario" onClick={() => { setForm(agenteNavegacionVacio()); setMensaje(null); }}>Limpiar</button>
          <button type="submit" className="btn">Guardar</button>
        </div>
      </form>
    </Ventana>
  );
}
