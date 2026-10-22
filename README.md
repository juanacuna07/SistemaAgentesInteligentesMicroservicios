# Sistema de Agentes Inteligentes — Microservicios GraphQL

Diseño de Soluciones · Universidad de Ibagué · Segundo Proyecto 2026B · Versión 1.0.0

**Integrantes:** Juan Acuña, Luis Hernández, Stephany Trujillo

Aplicación web distribuida: un **servidor Java (Spring Boot + GraphQL)** y **dos clientes web** en lenguajes diferentes al servidor y entre sí.

| Componente | Tecnología | Gestiona | Puerto |
|---|---|---|---|
| `Servidor/ServidorAgentes` | Java 17 · Spring Boot · Spring GraphQL | Las 2 clases, en listas en memoria | 8081 |
| `Cliente-Python/cliente-flask` | Python 3.10+ · Flask | **AgenteInteligente** (maestro) | 5000 |
| `Cliente-React/cliente-react` | JavaScript · React + Vite | **AgenteNavegacion** (detalle) | 5173 |

## Modelo (2 clases, maestro – detalle)

`AgenteInteligente 1..1 ──── 0..* AgenteNavegacion`

| AgenteInteligente (maestro) | Tipo | AgenteNavegacion (detalle) | Tipo |
|---|---|---|---|
| idAgente | int | idNavegacion | int |
| nombre | String | nombre | String |
| nivelAutonomia (0-100) | double | origen / destino | String |
| estadoOperativo (ACTIVO / INACTIVO / MANTENIMIENTO) | String | precisionGPS (0-100) | double |
| nivelConfianza (0-100) | double | fechaUltimaCalibracion | LocalDateTime |
| fechaRegistro | LocalDateTime | idAgenteInteligente | int |
| agentesNavegacion | List&lt;AgenteNavegacion&gt; | | |

Al eliminar un agente inteligente también se eliminan sus agentes de navegación. El servidor arranca con 3 agentes inteligentes y 4 agentes de navegación de prueba.

## Estructura

```text
Servidor/ServidorAgentes/src/main/java/com/unibague/agentes/
├── model/        AgenteInteligente, AgenteNavegacion   (las 2 clases, con Builder)
├── dto/          AgenteInteligenteInput, AgenteNavegacionInput
├── servicios/    Interfaces + ServicioAgenteInteligente, ServicioAgenteNavegacion (Singleton, List en memoria)
├── controller/   AgenteInteligenteController, AgenteNavegacionController (GraphQL), CorsConfig
└── excepciones/  ManejadorErroresGraphQL (mensajes de error legibles)
Cliente-Python/cliente-flask/   modelo/ · servicios/ · controladores/ · templates/ (vistas)
Cliente-React/cliente-react/src/ modelo/ · servicios/ · componentes/ (vistas)
```

## Cómo ejecutar (en este orden)

**1. Servidor** (JDK 17+): abrir `Servidor/ServidorAgentes` en IntelliJ / NetBeans como proyecto Maven y ejecutar `ServidorAgentesApplication`, o:
```bash
cd Servidor/ServidorAgentes
mvnw.cmd spring-boot:run        # Mac/Linux: ./mvnw spring-boot:run
```
Pruebas en http://localhost:8081/graphiql (ver `consultas-ejemplo.graphql`).

**2. Cliente Python (Agentes Inteligentes)**
```bash
cd Cliente-Python/cliente-flask
pip install -r requirements.txt
python app.py                   # http://localhost:5000
```

**3. Cliente React (Agentes de Navegación)**
```bash
cd Cliente-React/cliente-react
npm install
npm run dev                     # http://localhost:5173
```

## Operaciones GraphQL

| Caso de uso | AgenteInteligente | AgenteNavegacion |
|---|---|---|
| Adicionar | `crearAgenteInteligente` | `crearAgenteNavegacion` |
| Eliminar (búsqueda previa) | `eliminarAgenteInteligente` | `eliminarAgenteNavegacion` |
| Actualizar (búsqueda previa) | `actualizarAgenteInteligente` | `actualizarAgenteNavegacion` |
| Búsqueda individual | `agenteInteligentePorId` | `agenteNavegacionPorId` |
| Consulta de 1 objeto con 1 o 2 parámetros | `agenteInteligentePorCriterios(nombre, estadoOperativo)` | `agenteNavegacionPorCriterios(nombre, idAgenteInteligente)` |
| Listar todos / con filtro | `agentesInteligentes` / `agentesInteligentes(nombre, estadoOperativo)` | `agentesNavegacion` / `agentesNavegacion(destino, idAgenteInteligente)` |

## Patrones de diseño y SOLID

- **MVC** en el servidor y en ambos clientes (modelo / vistas / controladores-servicios).
- **Singleton**: `ServicioAgenteInteligente` y `ServicioAgenteNavegacion` (servidor); `GraphQLClient` y `AgenteInteligenteService` (Flask); `GraphQLClient` y `AgenteNavegacionObservable` (React).
- **Builder**: `AgenteInteligente.builder()` y `AgenteNavegacion.builder()` (servidor); `AgenteInteligenteBuilder` (Flask).
- **Observer** (Listar): en React, `VistaListar` se suscribe a `AgenteNavegacionObservable` y la grilla se refresca sola al adicionar, actualizar o eliminar.
- **SOLID**: un controlador y un servicio por clase (S), controladores dependen de las interfaces `IServicioAgenteInteligente` / `IServicioAgenteNavegacion` (D).

## Requisitos de interfaz

- Cada ventana implementa un solo caso de uso (Adicionar, Eliminar, Listar, Actualizar, Consultar).
- Eliminar y Actualizar hacen búsqueda previa y muestran TODA la información antes de operar.
- Menú principal y **Ayuda → Acerca de...** con integrantes y versión.
