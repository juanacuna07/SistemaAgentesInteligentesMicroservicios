import React, { useState } from 'react';
import AgenteNavegacionService from '../servicios/AgenteNavegacionService';
import { aInput, aFormulario, validar } from '../modelo/AgenteNavegacion';
import { Ventana, Mensaje, DetalleAgenteNavegacion, FormularioAgenteNavegacion, BuscadorPorId, useAgentesInteligentes } from './Comunes';

export default function VistaActualizar() {
  const maestros = useAgentesInteligentes();
  const [original, setOriginal] = useState(null);
  const [form, setForm] = useState(null);
  const [mensaje, setMensaje] = useState(null);

  const cargar = (a) => {
    setOriginal(a);
    setForm(aFormulario(a));
    setMensaje(null);
  };

  const guardar = async (e) => {
    e.preventDefault();
    const error = validar(form);
    if (error) return setMensaje({ tipo: 'error', texto: error });
    try {
      const a = await AgenteNavegacionService.actualizar(aInput(form));
      setOriginal(a);
      setMensaje({ tipo: 'exito', texto: `Agente de navegación #${a.idNavegacion} actualizado correctamente.` });
    } catch (err) {
      setMensaje({ tipo: 'error', texto: err.message });
    }
  };

  return (
    <Ventana titulo="Actualizar Agente de Navegación" descripcion="Busque el agente, revise su información actual y modifique uno o varios atributos.">
      <BuscadorPorId
        onEncontrado={cargar}
        onError={(texto) => { setOriginal(null); setForm(null); setMensaje({ tipo: 'error', texto }); }}
      />
      {original && (
        <>
          <h3>Información actual</h3>
          <DetalleAgenteNavegacion agente={original} maestros={maestros} />
          <h3>Nuevos datos</h3>
          <form onSubmit={guardar}>
            <FormularioAgenteNavegacion form={form} setForm={setForm} maestros={maestros} idBloqueado />
            <Mensaje mensaje={mensaje} />
            <div className="acciones">
              <button type="submit" className="btn">Guardar cambios</button>
            </div>
          </form>
        </>
      )}
      {!original && <Mensaje mensaje={mensaje} />}
    </Ventana>
  );
}
