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
9. `gameSceneDrawsPerspectiveWalls`: la escena dibuja primero las paredes en perspectiva de los laterales no visitables, usando radios independientes por profundidad, invirtiendo la derecha y pintando después las paredes frontales.
10. `gameSceneDrawsNearPerspectiveWalls`: el nivel cercano muestra una pared frontal y dos paredes en perspectiva correspondientes a los laterales `-1` y `+1`.
11. `gameSceneDrawsMinimap`: el minimapa representa las celdas con sus colores y la casilla del jugador.
12. `gameSceneDrawsHealthBar`: la barra de salud es roja con borde de 2 píxeles, margen de 1 píxel y relleno rojo oscuro por porcentaje.
13. `gameSceneHandlesKeyboardControls`: las teclas de dirección invocan los movimientos y giros del jugador.
14. `indexJsRegistersGameScene`: la configuración registra GameScene tras MainMenuScene.
15. `indexHtmlDeclaresGameScene`: la página principal declara GameScene.js.
16. `gameSceneAssetsAreServed`: los assets de la escena son servidos por el servidor.
17. `gameSceneResourceIsServed`: la escena de juego es servida por el servidor.
18. `gameSceneUsesCorrectCardinalCoordinates`: el campo de visión usa norte `y - 1`, sur `y + 1`, este `x + 1` y oeste `x - 1`.
19. `gameSceneScalesPerspectiveWalls`: las paredes en perspectiva conservan sus proporciones `128 × 512`, aplican el factor adicional `0.5`; la izquierda une su esquina superior izquierda con la superior derecha de la pared frontal, y la derecha une su esquina superior derecha con la superior izquierda.
20. `gameSceneRendersPlayerLevelSideWalls`: la capa situada en la posición del jugador dibuja primero las paredes frontales y después las laterales en perspectiva, con un desplazamiento vertical de 4 píxeles, colindantes y recortadas por el campo de visión.
21. `gameSceneExcludesNorthRelativeCell`: al mirar al norte, la celda relativa `(x + 2, y - 1)` queda fuera del campo de visión, mientras que `(x + 1, y - 1)` permanece visible.
