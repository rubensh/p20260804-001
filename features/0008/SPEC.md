# feature-0008


# Objetivo

Implementación del laberinto.


# Estado

- [X]En proceso.
- [X]En pruebas.
- [X]Finalizado.


# Notas

*Ninguna*


# Restricciones

*Ninguna*


# Implementación

- Crear la clase Maze, ubicada en "webcontent/js/Maze.js".
- Esta clase contiene los datos generados proceduralmente de los diferentes niveles del laberinto.
- Debe contener:
  - width: ancho del mapa (en habitaciones). Por defecto 32.
  - height: alto del mapa (en habitaciones). Por defecto 32.
  - depth: profundidad del mapa (en niveles). Por defecto 5.
  - levels: array de matrices donde cada matriz es un nivel de **width**x**height** habitaciones. Se codifica con un número entero de la siguiente forma: 0: habitación hueca sin paredes, 1: habitación ocupada por pared, 2: escalera hacia abajo, 3: escalera hacia arriba.
  - visibility: un array de matrices con las mismas dimensiones que "levels". Estas matrices indicarán por cada nivel, si la habitación de la posición (x, y) ha sido visualizada (es decir, que ha entrado en el campo de visión del jugador). El campo de visión es el que va desde la posición actual del jugador, hasta dos posiciones por delante. Por ejemplo, si miramos al norte, una habitación por delante abarca una celda a izquierda y otra a derecha. Dos posiciones por delante abarca dos celdas a izquierda y dos a derecha. Posibles campos de visión son:

+---+---+---+---+---+   +---+---+---+---+---+   +---+---+---+---+---+   +---+---+---+---+---+
| 1 | 1 | 1 | 1 | 1 |   | 0 | 0 | 0 | 0 | 0 |   | 0 | 0 | 0 | 0 | 1 |   | 1 | 0 | 0 | 0 | 0 |
| 0 | 1 | 1 | 1 | 0 |   | 0 | 0 | 0 | 0 | 0 |   | 0 | 0 | 0 | 1 | 1 |   | 1 | 1 | 0 | 0 | 0 |
| 0 | 0 | 1 | 0 | 0 |   | 0 | 0 | 1 | 0 | 0 |   | 0 | 0 | 1 | 1 | 1 |   | 1 | 1 | 1 | 0 | 0 |
| 0 | 0 | 0 | 0 | 0 |   | 0 | 1 | 1 | 1 | 0 |   | 0 | 0 | 0 | 1 | 1 |   | 1 | 1 | 0 | 0 | 0 |
| 0 | 0 | 0 | 0 | 0 |   | 1 | 1 | 1 | 1 | 1 |   | 0 | 0 | 0 | 0 | 1 |   | 1 | 0 | 0 | 0 | 0 |
+---+---+---+---+---+   +---+---+---+---+---+   +---+---+---+---+---+   +---+---+---+---+---+
Norte                   Sur                     Este                    Oeste

- Además, debe implementar:
  - Método generateMap(width, height, percentVisitable, currentLevel): algoritmo de tipo "drunken miner". Si currentLevel = 0, partimos del centro del laberinto. Si no, partimos de la posición de la escalera de bajada del nivel (currentLevel - 1). Con todas las casillas no visitables, con movimientos aleatorios a partir del punto actual se moverá a norte, sur, este u oeste, y marcará la casilla como visitable. Las casillas en las fila 0 y fila (height - 1) y en las columna 0 y columna (width - 1) no pueden ser visitables. Si volvemos a visitar una casilla previamente visitada, trasladamos la posición hasta la siguiente casilla que no esté marcada como visitable y pueda ser visitable. En caso de toparnos con las primeras o últimas filas o columnas, volveremos al centro del laberinto y seguiremos con el proceso. El proceso finaliza cuando el número de celdas visitables se corresponda con el porcentaje indicado como parámetro (de 0.0 a 1.0). Por cada nivel solo habrá una escalera de bajada y una de subida. La escalera de subida del nivel 0 está en el centro. La del resto de niveles está donde esté la escalera de bajada del nivel anterior. para determinar la posición de la escalera de bajada de un nivel, no nos complicaremos y la pondremos allá donde total_celda_visitables = FLOOR((width * height) * percentVisitable).
- Cuando generamos el mapa, generamos todos los niveles. Todas las habitaciones están como no visualizadas, a excepción de las que estén en el campo visual inicial (posición centrada en el laberinto y mirando al norte).
- Añade la clase Maze.js a index.html.
