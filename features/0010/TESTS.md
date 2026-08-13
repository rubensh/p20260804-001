# feature-0010: Tests

## GameScene

1. `gameSceneFileExists`: el fichero de la escena de juego existe.
2. `gameSceneExtendsPhaserScene`: la escena de juego extiende Phaser.Scene.
3. `gameScenePreloadsAssets`: la escena precarga sus assets (fondos, paredes y paredes en perspectiva).
4. `gameSceneCreatesMazeAndPlayer`: la escena genera el laberinto y crea al jugador en el centro, mirando al norte.
5. `gameSceneDrawsHud`: la escena dibuja el botón de menú, la barra de salud, el minimapa, el log de eventos y la etiqueta de nivel.
6. `gameSceneRendersFieldOfViewLevels`: la escena renderiza los tres niveles del campo de visión con sus escalas y oscurecimientos.
7. `gameSceneSelectsBackgroundsByParity`: la selección de fondos usa la paridad de x + y.
8. `gameSceneSelectsWallsByModulo`: la selección de paredes usa el módulo 3 de x + y.
9. `gameSceneDrawsPerspectiveWalls`: la escena dibuja paredes en perspectiva en los laterales no visitables, invirtiendo la derecha.
10. `gameSceneDrawsNearSlivers`: el nivel cercano muestra la celda frontal y las franjas laterales.
11. `gameSceneDrawsMinimap`: el minimapa representa las celdas con sus colores y la casilla del jugador.
12. `gameSceneDrawsHealthBar`: la barra de salud es roja con borde de 2 píxeles, margen de 1 píxel y relleno rojo oscuro por porcentaje.
13. `gameSceneHandlesKeyboardControls`: las teclas de dirección invocan los movimientos y giros del jugador.
14. `indexJsRegistersGameScene`: la configuración registra GameScene tras MainMenuScene.
15. `indexHtmlDeclaresGameScene`: la página principal declara GameScene.js.
16. `gameSceneAssetsAreServed`: los assets de la escena son servidos por el servidor.
17. `gameSceneResourceIsServed`: la escena de juego es servida por el servidor.
18. `gameSceneUsesCorrectCardinalCoordinates`: el campo de visión usa norte `y - 1`, sur `y + 1`, este `x + 1` y oeste `x - 1`.
