# feature-0015: Tests

- La clase `Potion` existe, extiende `Entity` y permite consumirse una sola vez.
- Al consumir una poción, la salud del jugador se restaura hasta su valor máximo sin sobrepasarlo.
- El laberinto genera cinco pociones aleatorias por nivel en habitaciones libres.
- Las pociones no comparten habitación con escaleras, enemigos, otras pociones ni con la posición inicial del jugador.
- Al coincidir el jugador y una poción, esta se consume y se elimina del laberinto.
- `Potion001.png` se precarga y se representa 30 píxeles por debajo del centro del campo de visión.
- `Potion.js` y `Potion001.png` se sirven correctamente por HTTP.
