// Patrón OBSERVER: sujeto que notifica a sus observadores (la vista Listar) cada vez que
// la colección de agentes de navegación cambia en el servidor (adicionar / actualizar / eliminar).
class AgenteNavegacionObservable {
  static #instancia = null;
  #observadores = new Set();

  // Patrón SINGLETON
  static getInstance() {
    if (!AgenteNavegacionObservable.#instancia) {
      AgenteNavegacionObservable.#instancia = new AgenteNavegacionObservable();
    }
    return AgenteNavegacionObservable.#instancia;
  }

  suscribir(observador) {
    this.#observadores.add(observador);
    return () => this.desuscribir(observador);
  }

  desuscribir(observador) {
    this.#observadores.delete(observador);
  }

  notificar(evento) {
    this.#observadores.forEach(observador => observador(evento));
  }
}

export default AgenteNavegacionObservable;
