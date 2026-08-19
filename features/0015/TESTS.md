# feature-0015: Tests

- La clase `Potion` existe, extiende `Entity` y permite consumirse una sola vez.
- Al consumir una poción, la salud del jugador se restaura hasta su valor máximo sin sobrepasarlo.
- El laberinto genera cinco pociones aleatorias por nivel en habitaciones libres.
- Las pociones no comparten habitación con escaleras, enemigos, otras pociones ni con la posición inicial del jugador.
- Al coincidir el jugador y una poción, esta se consume y se elimina del laberinto.
- `Potion001.png` se precarga y solo se representa a distancias entre 1 y 3.
- A distancia 1 se representa con escala `1.0`, desplazamiento `(0, 100)` e iluminación `1.0`.
- A distancia 2 se representa con escala `0.5`, desplazamiento `(0, 80)` e iluminación `0.8`.
- A distancia 3 se representa con escala `0.25`, desplazamiento `(0, 60)` e iluminación `0.5`.
- `Potion.js` y `Potion001.png` se sirven correctamente por HTTP.
