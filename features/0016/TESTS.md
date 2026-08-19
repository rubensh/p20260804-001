# feature-0016: Tests

- La clase `Boss` existe, extiende `Entity` y declara las propiedades de combate especificadas.
- El jefe implementa ataque, defensa, muerte y movimiento en ocho direcciones.
- El laberinto genera un solo jefe, exclusivamente en el último nivel.
- El jefe se sitúa en una habitación libre, sin escaleras, enemigos ni pociones.
- El jugador no puede atravesar al jefe vivo.
- El jugador ataca al jefe situado delante y el jefe ataca al jugador desde una casilla cardinalmente adyacente.
- El jefe mantiene su propio intervalo de ataque, iniciado según `attackDelay` y cancelado al romper la adyacencia.
- Mientras combate, la textura del orco recibe un desplazamiento aleatorio máximo de cinco píxeles en ambos ejes.
- Los ataques del jefe y del jugador contra el jefe generan los mensajes específicos del orco en el log.
- `Orc-001.png` se precarga y se dibuja con la escala, desplazamiento e iluminación especificados para las tres profundidades.
- Al morir, el jefe se elimina y deja de renderizarse.
- La muerte del jugador conserva la transición `Game Over` y la muerte del jefe muestra `You win!!!`; ambas vuelven al menú tras cinco segundos.
- `Boss.js` y `Orc-001.png` se sirven correctamente por HTTP.
