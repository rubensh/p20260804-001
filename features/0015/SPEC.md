# feature-0015

Creación de powerups.


# Objetivo

Crear un powerup que regenere la salud del jugador.


# Estado

- [X]En proceso.
- [ ]En pruebas.
- [ ]Finalizado.


# Notas

*Ninguna*

# Restricciones

*Ninguna*


# Implementación

- Implementa la clase Potion que representa pociones que están tiradas por el laberinto y regeneran la salud del personaje.
- Cuando el jugador está en la misma habitación que la pción, ésta se consume (desaparece) y se regenera la vida del ps¡ersonaje.
- En cada nivel del laberinto hay 5 pociones distribuídas aleatoriamente.
- La imagen de la poción se encuentra en "webcontent/assets/images/Potion001.png".
- Cuando dibujes la poción, ponla 30 píxeles por debajo del centro de la vista del personaje.
- Sigue las reglas de visualización existentes para otros gráficos:
  - Sólo será visible en habitaciones cuya distancia sea menor o igual a 3.
  - En distancia 1: la escala será 1.0 de la imagen. La posición estará desplazada (0, 30) píxeles. La iluminación será 1.0.
  - En distancia 2: la escala será 0.5 de la imagen. La posición estará desplazada (0, 50) píxeles. La iluminación será 0.8.
  - En distancia 3: la escala será 0.25 de la imagen. La posición estará desplazada (0, 60) píxeles. La iluminación será 0.5.
