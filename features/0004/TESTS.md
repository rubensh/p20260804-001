# feature-0004: Tests

Tests a implementar para la página principal del juego (`webcontent/pages/index.html`,
`webcontent/styles/styles.css`, `webcontent/js/index.js` y el framework Phaser).

## Escenarios

| ID  | Descripción                                                                                   | Resultado esperado                                                        |
|-----|-----------------------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| T01 | Comprobar la existencia de los recursos de la página principal.                               | `index.html`, `styles.css`, `index.js` y `phaser.min.js` existen.        |
| T02 | Comprobar las referencias del HTML a estilos, script y Phaser.                                | `index.html` referencia `styles.css`, `index.js` y `phaser.min.js`.      |
| T03 | Comprobar los fondos negros y el centrado del canvas en la hoja de estilos.                   | Fondo de página y contenedor del canvas negros; canvas de 800 px centrado. |
| T04 | Comprobar la configuración de Phaser en el script.                                            | `Phaser.Game` con canvas de 800 x 600 montado en el contenedor `#game`.  |
| T05 | Comprobar que el fichero de Phaser descargado no está vacío.                                  | `phaser.min.js` contiene código.                                         |
| T06 | Servir la página principal y sus recursos a través del servidor.                              | GET 200 con `Content-Type` HTML, CSS y JavaScript correctos.             |

## Verificaciones adicionales

- El recurso `webcontent/pages/index.html` anterior fue renombrado a `webcontent/pages/landing.html`.
- `webcontent/pages/landing.html` sigue siendo servible a través del servidor.
- La página principal es accesible, responsive y adaptable según los requisitos de la feature.
