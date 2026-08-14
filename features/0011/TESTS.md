# feature-0011: Tests

## Maze

1. `mazeGenerateMapAcceptsSteps`: `generateMap` incorpora el parámetro `steps` antes de `currentLevel`.
2. `mazeCalculatesGenerationParametersByLevel`: cada nivel calcula sus pasos mediante `(8 - level) × 2.0` y su objetivo visitable mediante `width × height × (level + 1) / (MAZE_DEFAULT_DEPTH × 0.8)`, limitado al interior disponible.
3. `mazeWalksStraightForConfiguredSteps`: una dirección aleatoria se mantiene durante `steps` iteraciones, marcando las nuevas celdas visitables.
4. `mazeStraightWalkPreservesBorders`: cada paso comprueba los límites antes de modificar el mapa y nunca hace visitables las filas o columnas exteriores.
5. `mazeGenerationGuaranteesProgress`: si un tramo no abre celdas, la generación continúa desde una frontera conectada y fuerza una dirección que abre un muro adyacente.
