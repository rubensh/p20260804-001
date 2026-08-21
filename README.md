# p20260804-001

"Buried Dark World"

https://github.com/rubensh/p20260804-001.git

----

## 1. ¿Qué es este proyecto?

**p20260804-001** es el nómbre en código del proyecto de desarrollo de un videojuego *"roguelike"* de mazmorras para navegador web, donde el jugador deberá encontrar una salida del laberinto, mientras diferentes enemigos intentan impedírselo. El juego se presenta en primera persona con un falso 3D.


## 2. Objetivos

La premisa de esta implementación es la aplicación de diferentes herramientas, y técnicas de gestión y desarrollo de proyectos, potenciadas por IA. Como resultado se pretendo obtener un juego sencillo, de gran calidad gráfica y con elementos sonoros épicos, que sea asumible por un solo desarollador en muy poco tiempo (< 1 mes).


### 2.1. Alcance

El alcance previsto incluye:

- Versión del juego con diferentes niveles para ofrecer horas de juego.
- El movimiento de traslación es de casilla en casilla, similar a piezas de ajedrez. El movimiento de rotación es sobre la casilla, de 90º en 90º.
- Creación procedural de los niveles para que cada partida sea totalmente distinta a las anteriores.
- Generación de varios tipos de enemigos, con diferentes dificultades.
- Diseño de varios items: armas, armaduras, consumibles, ..., para completar la experiencia de juego.

Qué no incluye:

- Juego cooperativo / multijugador.
- Comunicaciones / foros / chat.
- Guardado de partidas.
- Personalización de caracteres.
- Editor de niveles.
- Compras en la aplicación.


## 3. Requisitos

- Java JDK 21+.
- Maven 3.x.
- Git 2.x.
- Librería Phaser-3.x.js.
- Navegador web moderno (Firefox, Chrome, Edge, Opera, Brave).
- WSL + OpenCode.
- Eclipse IDE 2025-12+.


## 4. Arquitectura


### 4.1. Backend

Está compuesto principalmente por un pequeño servidor HTTP. El propósito de este servidor es el hacer accesibles los recursos disponibles, como pueden ser *assets* (imágenes, sonidos, ...). Sólo atenderá peticiones HTTP GET y tendrá mapeados previamente los recursos existentes a modo de *whitelist*.


### 4.2. Frontend

Esta formado por una página HTML principal, que incluirá recursos webs comunes, como hojas de estilo CSS3, módulos Javascript ECMAS6, imágenes, etcétera. Como elemento principal de la página, existirá una vista en un componente **canvas**, que se encargará de mostrar todos los aspectos gráficos del videojuego. Ésta página HTML, y su componente canvas, consumirán los recursos expuestos por el backend previamente definido.


## 5. Estructura del proyecto

Se trata de un proyecto Java monolítico, gestionado con Maven para la gestión de dependencias y con GIT como control de versiones.

````text
.
├-- features/
|   |
|   ├-- 0001
|   |   |
|   |   ├-- SPEC.md
|   |   |
|   |   └-- TESTS.md
|   |
|   └-- FEATURES.md
|
├-- AGENTS.md
|
├-- README.md
|
└-- VERSIONS.md
````


## 6. Flujo de desarrollo

Siguiendo un enfoque FDD (Feature-Driven Development) + SDD (Specification-Driven Development) se partirá de un proyecto base y una lista de *features* concretas. La implementación de cada una de estas *features* irá dotando de mayor valor a la aplicación. Las *features* serán implementadas en su mayor parte mediante herramientas IA a través de la especificación detallada existente.


### 6.1. Organización de especificaciones

Cada especificación se añadirá, de manera resumida, al listado existente en **./features/FEATURES.md**. Su nomenclatura seguirá el patrón *featureXXXX*, donde XXXX es un ID numérico secuencial que identifica de forma unívoca la característica. Con esto, se creará la carpeta XXXX que contendrá, como mínimo, los ficheros **./features/XXXX/SPEC.md** y **./features/XXXX/TESTS.md**. El primero define las especificaciones de la feature actual. El segundo declara los tests que debe implementar y pasar dicha feature.


### 6.2. Convenciones de código y documentación

- Nombres de classes, interfaces, etcétera, en PascalCase. Código Java estilo camelCase.
- Nombres de classes, propiedades y métodos en inglés.
- Código autodocumentado con JavaDoc, siguiendo la siguiente plantilla.

````code
/**
 * <h1>
 * {Bloque de ejemplo en definición de classes, interfaces, etcétera.}
 * </h1>
 *
 * @author
 * 	{Nombre del autor, p.ej: Rubén Santana Hernández}
 * 	{Email del autor, p.ej: rshdev.es@gmail.com}
 *
 * @version
 * 	0.0.1: implementación inicial.
 *
 * @since
 * 	p20260804-001:{versión actual del proyecto}
 */
 
 /**
 * <h1>
 * {Bloque de ejemplo en definición de métodos.}
 * </h1>
 *
 * {
 * @param param1
 * 	Un {@link clase_del_parametro} definición del parámetro.
 *
 * @param param2
 * 	Un {@link clase_del_parametro} definición del parámetro.
 *
 * @param paramN
 * 	Un {@link clase_del_parametro} definición del parámetro.
 *
 * return
 * 	Un {@link clase_del_parametro} definición del retorno.
 * }
 *
 * @author
 * 	{Nombre del autor, p.ej: Rubén Santana Hernández}
 * 	{Email del autor, p.ej: rshdev.es@gmail.com}
 *
 * @version
 * 	0.0.1: implementación inicial.
 *
 * @since
 * 	p20260804-001:{versión actual del proyecto}
 */
````


## 7.  Ejecución

### 7.1. Cómo ejecutar el proyecto

El proyecto está autocontenido para ser portable. Desde un terminal Linux o consola Windows, accedemos a la carpeta.

Para ejecutarlo desde sistemas Linux, se puede ejecutar:

````bash
$ ./run.sh
````

Desde sistemas Windows se ejecuta a través de:

````bash
run.bat
````

Esto arranca el miniservidor HTTP incluído en el juego, que escuchará por el puerto 8088 por defecto. Una vez arrancado, desde un navegador, accederemos a la siguiente URL:

http://localhost:8088/pages/index.html


### 7.2. Cómo ejecutar los tests

Los tests automatizados dependen del entorno de Maven.

Para ejecutarlos, desde la carpeta del proyecto, ejecutamos:

````code
$ mvn verify
````

## 8. Licencia

El código y los contenidos propios de **Buried Dark World** se distribuyen bajo PolyForm Noncommercial 1.0.0 para uso personal y no comercial:

- [Licencia en español](LICENSE-ES.md).
- [License in English](LICENSE-EN.md).

Los componentes de terceros quedan excluidos de esta licencia y conservan sus condiciones originales, tal como se detalla en ambos documentos.
