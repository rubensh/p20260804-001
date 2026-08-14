# feature-0011

Generación de mapas más parecidos a pasillos y habitaciones, que a cuevas.


# Objetivo

Ahora mismo se generan niveles que acaban pareciendo agujeros, sin habitaciones ni pasillos. Queremos conseguir el efecto contrario.


# Estado

- [X]En proceso.
- [X]En pruebas.
- [X]Finalizado.


# Notas

*Ninguna*


# Restricciones

*Ninguna*


# Implementación

- Vamos a modificar el método generateMap() de la clase Maze, en "webcontent/js/Maze.js". Actualmente su firma es: generateMap(width, height, percentVisitable, currentLevel).
- Vamos a añadir un nuevo parámetro "steps", que indicará los pasos seguidos que dará en una dirección. De esta forma en lugar de ir paso a paso aleatorizando la dirección, aseguramos que haga un recorrido recto. Nueva firma del método: generateMap(width, height, percentVisitable, steps, currentLevel).
- En cada iteración para aleatorizar la dirección, caminaremos "steps" pasos hacia adelante en esa dirección, marcando como "visitada" dicha habitación y actualizando el porcentaje. Seguimos considerando las restricciones de no poder visitar las filas 0 y (height - 1) y las columnas 0 y (width - 1).
- Para hacer los laberintos adaptados a cada nivel, tanto "steps" como "percentVisitable" se calcularán en base al "level":
  - steps = (8 - level) * 2.0.
  - percentVisitable = width * height * (level + 1) / (MAZE_DEFAULT_DEPTH * 0.8).
