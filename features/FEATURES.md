# Listado de features

En este fichero se registra, de manera resumida, cada una de las *features* definidas
para el proyecto. Su nomenclatura seguirá el patrón `featureXXXX`, donde `XXXX` es un
ID numérico secuencial que identifica de forma unívoca la característica.

Cada *feature* dispone de su propia carpeta `./features/XXXX/` que contiene, como mínimo:

- `SPEC.md`: definición detallada de la especificación de la *feature*.
- `TESTS.md`: declaración de los tests que debe implementar y superar la *feature*.

---

## Registro de features

````text
Feature ID: 0001
Nombre: feature-0001
Descripción: Implementación de servidor HTTP sencillo.
Etiquetas: [backend][java]
````

````text
Feature ID: 0002
Nombre: feature-0002
Descripción: Implementación de clase "launcher" que sirva como punto de entrada al juego, y que arranque el servidor desde terminal.
Etiquetas: [backend][java]
````

````text
Feature ID: 0003
Nombre: feature-0003
Descripción: Creación de una página web mockup que sirva para probar el acceso a recursos del servidor vía navegador web.
Etiquetas: [frontend][html][css][js]
````

````text
Feature ID: 0004
Nombre: feature-0004
Descripción: Creación de la página web principal, donde se mostrará la vista del juego.
Etiquetas: [frontend][html][css][js][phaser 3]
````

````text
Feature ID: 0005
Nombre: feature-0005
Descripción: Desarrollo de escena de arranque (bootloader).
Etiquetas: [phaser 3][assets][images]
````

````text
Feature ID: 0006
Nombre: feature-0006
Descripción: Desarrollo de elementos de UI
Etiquetas: [phaser 3][ui][ui-element][ui-button][ui-panel]
````

````text
Feature ID: 0007
Nombre: feature-0007
Descripción: Creación de una escena para mostrar el menú principal y los créditos.
Etiquetas: [phaser 3]
````

````text
Feature ID: 0008
Nombre: feature-0008
Descripción: Implementación de mapa.
Etiquetas: [phaser 3][entities]
````

````text
Feature ID: 0009
Nombre: feature-0009
Descripción: Implementación de entidades: entidad base, jugador, enemigo.
Etiquetas: [phaser 3][entities]
````
