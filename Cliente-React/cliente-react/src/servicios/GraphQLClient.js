// Patrón SINGLETON: una sola instancia del cliente HTTP GraphQL para toda la aplicación.
const ENDPOINT = 'http://localhost:8081/graphql';

class GraphQLClient {
  static #instancia = null;

  static getInstance() {
    if (!GraphQLClient.#instancia) {
      GraphQLClient.#instancia = new GraphQLClient(ENDPOINT);
    }
    return GraphQLClient.#instancia;
  }

  constructor(endpoint) {
    this.endpoint = endpoint;
  }

  async ejecutar(query, variables = {}) {
    let respuesta;
    try {
      respuesta = await fetch(this.endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ query, variables })
      });
    } catch {
      throw new Error('No se pudo conectar con el servidor GraphQL (¿está encendido en el puerto 8081?)');
    }
    const resultado = await respuesta.json();
    if (resultado.errors && resultado.errors.length > 0) {
      throw new Error(resultado.errors.map(e => e.message).join('\n'));
    }
    return resultado.data;
  }
}

export default GraphQLClient;
