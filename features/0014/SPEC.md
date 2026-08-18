# feature-0014

Movimiento y dinámica de combates.


# Objetivo

Dotar de movimiento a los enemigos para simular cierto dinamismo y hacer que los combates sean automáticos y no dependan del movimiento del jugador.


# Estado

- [X]En proceso.
- [X]En pruebas.
- [X]Finalizado.


# Notas

*Ninguna*

# Restricciones

*Ninguna*


# Implementación

- Cuando el enemigo esté en una habitación adyacente al jugador (al norte, al sur, al este o al oeste) se iniciará un interval de javascript con interval = attackDelay * 1000. Cuando nos separemos de él eliminaremos dicho interval. Puedes mantener la referencia al interval como una propiedad del propio enemigo, así cada uno tendrá la suya.
- El jugador también tendrá su delay de ataque y también su interval, que funcionará exactamente igual que el de los enemigos, pero sólo podremos atacar al enemigo que tengamos enfrente.
- Igualmente, junto con el caso anterior, la textura del enemigo se trasladará 5 píxeles aleatoriamente hacia arriba, abajo, izquierda y/o derecha desde suposición inicial mientras esté luchando.
- Cuando luchemos, la vista de log de eventos del juego (5), mostrará mensajes indicando los ataques, siempre en nuevas líneas:
  - Cuando nos ataquen: "El goblin realiza un ataque con X de daño".
  - Cuando ataquemos: "Golpeas al goblin con un ataque de X de daño".
  - El log mantendrá los últimos 5 mensajes y luego simulará un scroll de mensajes, pero solo conservando los últimos 5.

