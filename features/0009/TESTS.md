# feature-0009: Tests

## Entity

1. `entityFileExists`: el fichero de la entidad base existe.
2. `entityClassDeclaresBaseState`: la clase Entity declara el estado base (id, x, y, level).
3. `entityResourcesAreServed`: la entidad base es servida por el servidor.

## Player

4. `playerFileExists`: el fichero de la entidad jugador existe.
5. `playerClassExtendsEntity`: la clase Player extiende de Entity.
6. `playerDeclaresProperties`: la clase Player declara sus propiedades (maze, facing, health, minDamage, maxDamage, minDefense, maxDefense).
7. `playerDeclaresMethods`: la clase Player declara sus métodos (attack, defend, moveForward, moveBackward, rotateLeft, rotateRight).
8. `playerAttackAppliesDamage`: el método attack genera el daño entre minDamage y maxDamage y lo delega en defend de la entidad objetivo.
9. `playerDefendSubtractsPositiveDifference`: el método defend genera la defensa entre minDefense y maxDefense y resta la diferencia positiva a la salud sin superar 0.
10. `playerMovementChecksVisitableCell`: los movimientos comprueban que la celda destino sea visitable en el nivel actual del laberinto.
11. `playerMoveForwardDirections`: el método moveForward usa las direcciones especificadas.
12. `playerMoveBackwardDirections`: el método moveBackward usa las direcciones especificadas.
13. `playerRotateLeftDirections`: el método rotateLeft usa los giros especificados.
14. `playerRotateRightDirections`: el método rotateRight usa los giros especificados.
15. `playerResourcesAreServed`: la entidad jugador es servida por el servidor.
