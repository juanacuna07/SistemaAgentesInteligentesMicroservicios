// Capa de servicios del cliente: consume los microservicios GraphQL de AgenteNavegacion.
import GraphQLClient from './GraphQLClient';
import AgenteNavegacionObservable from '../modelo/AgenteNavegacionObservable';

const CAMPOS = 'idNavegacion nombre origen destino precisionGPS fechaUltimaCalibracion idAgenteInteligente';
const cliente = GraphQLClient.getInstance();
const observable = AgenteNavegacionObservable.getInstance();

const AgenteNavegacionService = {
  async listar(destino = null, idAgenteInteligente = null) {
    const data = await cliente.ejecutar(
      `query ($destino: String, $idAI: Int) { agentesNavegacion(destino: $destino, idAgenteInteligente: $idAI) { ${CAMPOS} } }`,
      { destino, idAI: idAgenteInteligente }
    );
    return data.agentesNavegacion ?? [];
  },

  async buscarPorId(idNavegacion) {
    const data = await cliente.ejecutar(
      `query ($id: Int!) { agenteNavegacionPorId(idNavegacion: $id) { ${CAMPOS} } }`, { id: idNavegacion }
    );
    return data.agenteNavegacionPorId;
  },

  async buscarPorCriterios(nombre = null, idAgenteInteligente = null) {
    const data = await cliente.ejecutar(
      `query ($nombre: String, $idAI: Int) { agenteNavegacionPorCriterios(nombre: $nombre, idAgenteInteligente: $idAI) { ${CAMPOS} } }`,
      { nombre, idAI: idAgenteInteligente }
    );
    return data.agenteNavegacionPorCriterios;
  },

  async adicionar(input) {
    const data = await cliente.ejecutar(
      `mutation ($input: AgenteNavegacionInput!) { crearAgenteNavegacion(input: $input) { ${CAMPOS} } }`, { input }
    );
    observable.notificar({ tipo: 'ADICIONADO', agente: data.crearAgenteNavegacion });
    return data.crearAgenteNavegacion;
  },

  async actualizar(input) {
    const data = await cliente.ejecutar(
      `mutation ($input: AgenteNavegacionInput!) { actualizarAgenteNavegacion(input: $input) { ${CAMPOS} } }`, { input }
    );
    observable.notificar({ tipo: 'ACTUALIZADO', agente: data.actualizarAgenteNavegacion });
    return data.actualizarAgenteNavegacion;
  },

  async eliminar(idNavegacion) {
    const data = await cliente.ejecutar(
      `mutation ($id: Int!) { eliminarAgenteNavegacion(idNavegacion: $id) { ${CAMPOS} } }`, { id: idNavegacion }
    );
    observable.notificar({ tipo: 'ELIMINADO', agente: data.eliminarAgenteNavegacion });
    return data.eliminarAgenteNavegacion;
  },

  // Agentes inteligentes (maestro) para escoger a cuál pertenece el agente de navegación
  async listarAgentesInteligentes() {
    const data = await cliente.ejecutar('query { agentesInteligentes { idAgente nombre } }');
    return data.agentesInteligentes ?? [];
  }
};

export default AgenteNavegacionService;
