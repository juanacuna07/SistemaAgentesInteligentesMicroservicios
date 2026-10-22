import React, { useEffect, useState } from 'react';
import VistaInicio from './componentes/VistaInicio';
import VistaAdicionar from './componentes/VistaAdicionar';
import VistaEliminar from './componentes/VistaEliminar';
import VistaListar from './componentes/VistaListar';
import VistaActualizar from './componentes/VistaActualizar';
import VistaConsultar from './componentes/VistaConsultar';
import AcercaDe, { VERSION } from './componentes/AcercaDe';
import Icono from './componentes/Icono';

// Ventana principal (controlador de navegación del MVC del cliente)
const VISTAS = {
  inicio: { componente: VistaInicio, nombre: 'Inicio' },
  adicionar: { componente: VistaAdicionar, nombre: 'Adicionar Agente de Navegación' },
  eliminar: { componente: VistaEliminar, nombre: 'Eliminar Agente de Navegación' },
  listar: { componente: VistaListar, nombre: 'Listar Agentes de Navegación' },
  actualizar: { componente: VistaActualizar, nombre: 'Actualizar Agente de Navegación' },
  consultar: { componente: VistaConsultar, nombre: 'Consultar Agente de Navegación' }
};

export default function App() {
  const [vista, setVista] = useState('inicio');
  const [menuAbierto, setMenuAbierto] = useState(null);
  const [mostrarAcerca, setMostrarAcerca] = useState(false);

  useEffect(() => {
    const cerrar = () => setMenuAbierto(null);
    document.addEventListener('click', cerrar);
    return () => document.removeEventListener('click', cerrar);
  }, []);

  const abrirMenu = (e, menu) => {
    e.stopPropagation();
    setMenuAbierto(menuAbierto === menu ? null : menu);
  };

  const ir = (v) => { setVista(v); setMenuAbierto(null); };
  const Vista = VISTAS[vista].componente;

  return (
    <div className="app">
      <nav className="menu">
        <span className="marca"><Icono nombre="marca" tamano={20} /> Agentes IA</span>

        <div className="menu-item">
          <button onClick={e => abrirMenu(e, 'archivo')}>Archivo <Icono nombre="flecha" tamano={14} /></button>
          {menuAbierto === 'archivo' && (
            <div className="desplegable">
              <button onClick={() => ir('inicio')}><Icono nombre="inicio" /> Inicio</button>
            </div>
          )}
        </div>

        <div className="menu-item">
          <button onClick={e => abrirMenu(e, 'navegacion')}>Agente de Navegación <Icono nombre="flecha" tamano={14} /></button>
          {menuAbierto === 'navegacion' && (
            <div className="desplegable">
              <button onClick={() => ir('adicionar')}><Icono nombre="adicionar" /> Adicionar</button>
              <button onClick={() => ir('eliminar')}><Icono nombre="eliminar" /> Eliminar</button>
              <button onClick={() => ir('listar')}><Icono nombre="listar" /> Listar</button>
              <button onClick={() => ir('actualizar')}><Icono nombre="actualizar" /> Actualizar</button>
              <button onClick={() => ir('consultar')}><Icono nombre="consultar" /> Consultar</button>
            </div>
          )}
        </div>

        <div className="menu-item">
          <button onClick={e => abrirMenu(e, 'ayuda')}>Ayuda <Icono nombre="flecha" tamano={14} /></button>
          {menuAbierto === 'ayuda' && (
            <div className="desplegable">
              <button onClick={() => { setMostrarAcerca(true); setMenuAbierto(null); }}><Icono nombre="info" /> Acerca de...</button>
            </div>
          )}
        </div>
      </nav>

      <main className="contenido">
        <Vista key={vista} />
      </main>

      <footer className="estado-barra">
        <span>Vista activa: <strong>{VISTAS[vista].nombre}</strong></span>
        <span>Servidor: localhost:8081/graphql · v{VERSION}</span>
      </footer>

      {mostrarAcerca && <AcercaDe onCerrar={() => setMostrarAcerca(false)} />}
    </div>
  );
}
