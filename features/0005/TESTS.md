# feature-0005: Tests

Tests a implementar para la escena de arranque del juego (`webcontent/js/scene/BootloaderScene.js`)
y su integración en la configuración de Phaser.

## Escenarios

| ID  | Descripción                                                                                   | Resultado esperado                                                              |
|-----|-----------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| T01 | Comprobar la existencia de la escena y del asset de la splash screen.                         | `BootloaderScene.js` y `SplashScreenLogo.png` existen.                         |
| T02 | Comprobar que la configuración del juego usa la escena de arranque como primera escena.       | `index.js` declara `scene: [BootloaderScene]` y elimina configuraciones previas.|
| T03 | Comprobar el orden de carga de los scripts en el HTML.                                        | `BootloaderScene.js` se carga antes que `index.js`.                           |
| T04 | Comprobar los elementos mínimos de la interfaz de carga.                                      | Texto `Cargando...` (Monospace 12px), borde 200x7 y barra 198x5, asset de splash. |
| T05 | Comprobar los tiempos de la splash screen y la siguiente escena.                              | Fade in 2.5s, 5s de duración o click, fade out 2.5s y variable `nextScene`.    |
| T06 | Comprobar que el asset de la splash screen es un PNG válido.                                  | Cabecera PNG correcta y fichero no vacío.                                      |
| T07 | Servir la escena y el asset a través del servidor.                                            | GET 200 con `Content-Type` JavaScript y PNG correctos.                        |

## Verificaciones adicionales

- Los assets se declaran en un array dentro de la escena.
- Los assets cargados quedan disponibles para otras escenas (cache de Phaser).
- Fondo negro, con texto y gráficos blancos.
