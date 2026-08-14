# feature-0013: Tests

- La clase `Enemy` existe, extiende `Entity` y calcula sus atributos según el nivel y el tipo.
- Los enemigos implementan ataque, defensa, muerte y movimiento en ocho direcciones.
- El laberinto crea 200 enemigos con una concentración creciente por profundidad.
- Los enemigos solo se sitúan en habitaciones vacías, sin ocupar escaleras ni compartir casilla.
- El jugador no puede atravesar enemigos vivos.
- El jugador ataca al enemigo situado delante y los enemigos cardinalmente adyacentes contraatacan.
- Los enemigos muertos se eliminan y dejan de renderizarse.
- Los tres goblins se precargan y se dibujan con la escala, desplazamiento e iluminación especificados.
- La profundidad 2 dibuja las paredes en perspectiva de sus celdas extremas junto con las demás paredes del nivel y las desplaza hacia el interior en `0.5` veces el ancho escalado de profundidad 3.
- La profundidad 1 mantiene sin cambios las paredes en perspectiva de las posiciones laterales `-1` y `+1`, añade las posiciones extremas `-2` y `+2` en el mismo pase y desplaza estas últimas hacia el exterior en `0.25` veces el ancho escalado de profundidad 2.
- Al llegar la salud del jugador a cero se muestra `Game Over`, se bloquean nuevas acciones y se vuelve al menú después de cinco segundos.
- `Enemy.js` y los tres PNG se sirven correctamente por HTTP.
