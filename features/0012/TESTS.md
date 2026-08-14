# feature-0012: Tests

## Escaleras

1. `mazePlacesStaircasesForAdjacentLevels`: el primer nivel no tiene subida, el último no tiene bajada y los niveles intermedios colocan la subida al inicio y la bajada en la última celda generada.
2. `gameScenePreloadsAssets`: los assets `Stairs-Up.png` y `Stairs-Down.png` se precargan y son servidos como PNG.
3. `gameSceneRendersStaircases`: las escaleras se dibujan con escalas `0.25`, `0.5` y `1.0`, desplazamientos verticales `14`, `2` y `0`, iluminación `0.25`, `0.5` y `1.0`, y quedan recortadas por el campo de visión.
4. `playerChangesLevelOnForwardStaircase`: al avanzar hacia una escalera situada delante, el jugador cambia al nivel contiguo correspondiente.
5. `gameSceneHandlesKeyboardControls`: tras el movimiento, la escena actualiza visibilidad, campo de visión, minimapa y etiqueta del nivel.
6. `gameSceneDrawsStaircasesOnMinimap`: las escaleras visibles se representan mediante un triángulo rojo hacia abajo o azul hacia arriba que ocupa su celda del minimapa.
