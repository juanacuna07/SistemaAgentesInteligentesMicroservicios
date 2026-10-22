// Modelo del cliente: utilidades de la entidad AgenteNavegacion (detalle de AgenteInteligente).
const ahoraLocal = () =>
  new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16);

export const agenteNavegacionVacio = () => ({
  idNavegacion: '',
  nombre: '',
  origen: '',
  destino: '',
  precisionGPS: '',
  fechaUltimaCalibracion: ahoraLocal(),
  idAgenteInteligente: ''
});

// Convierte el formulario en el AgenteNavegacionInput que espera el servidor
export const aInput = (f) => ({
  idNavegacion: parseInt(f.idNavegacion),
  nombre: String(f.nombre).trim(),
  origen: String(f.origen).trim(),
  destino: String(f.destino).trim(),
  precisionGPS: parseFloat(f.precisionGPS),
  fechaUltimaCalibracion: f.fechaUltimaCalibracion.slice(0, 16),
  idAgenteInteligente: parseInt(f.idAgenteInteligente)
});

export const aFormulario = (a) => ({
  ...a,
  fechaUltimaCalibracion: a.fechaUltimaCalibracion.slice(0, 16),
  idAgenteInteligente: String(a.idAgenteInteligente)
});

export const validar = (f) => {
  if (!Number.isInteger(+f.idNavegacion) || +f.idNavegacion <= 0) return 'El ID debe ser un entero positivo.';
  if (!String(f.nombre).trim()) return 'El nombre es obligatorio.';
  if (!String(f.origen).trim()) return 'El origen es obligatorio.';
  if (!String(f.destino).trim()) return 'El destino es obligatorio.';
  const p = parseFloat(f.precisionGPS);
  if (isNaN(p) || p < 0 || p > 100) return 'La precisión GPS debe estar entre 0 y 100.';
  if (!f.fechaUltimaCalibracion) return 'La fecha de última calibración es obligatoria.';
  if (new Date(f.fechaUltimaCalibracion) > new Date()) return 'La fecha de última calibración no puede estar en el futuro.';
  if (!f.idAgenteInteligente) return 'Debe seleccionar el agente inteligente al que pertenece.';
  return null;
};

export const formatearFecha = (iso) => (iso ? iso.replace('T', ' ').slice(0, 16) : '');
