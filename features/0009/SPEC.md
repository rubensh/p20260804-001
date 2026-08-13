# feature-0009


# Objetivo

Implementación de entidades:

- Entidad base: es la entidad que proporciona propiedades y métodos comunes al resto de entidades del juego.
- Entidad jugador: gestiona los datos y representa al jugador.


# Estado

- [X]En proceso.
- [X]En pruebas.
- [X]Finalizado.


# Notas

*Ninguna*


# Restricciones

*Ninguna*


# Implementación

- Crea la clase Entity, localizada en el fichero "webcontent/js/entity/Entity.js". Esta clase será la clase base para todas las entidades participantes en el juego. Contiene las siguientes propiedades:
  - id: identificador de la entidad.
  - x: coordenada x de la habitación del mapa.
  - y: coordenada y de la habitación del mapa.
  - level: nivel de profundiad en el mapa en el que se encuentra localizada.
- Crea la clase Player, localizada en el fichero "webcontent/js/entity/Player.js". Esta clase extiende de Entity, y representa las propiedades y estado del jugador. Contiene las siguientes propiedades:
  - facing: un valor indicando hacia dónde está mirando el jugador. 'N': norte, 'S': sur, 'E': este, 'W': oeste.
  - health: un valor indicando la salud (en porcentaje).
  - minDamage: un valor numérico indicando el mínimo daño que puede generar el jugador.
  - maxDamage: un valor numérico indicando el máximo daño que puede generar el jugador.
  - minDefense: un valor numérico indicando el mínimo daño que puede soportar el jugador sin que reste salud.
  - maxDefense: un valor numérico indicando el máximo daño que puede soportar el jugador sin que reste salud.
  - attack(entity): método que genera un valor aleatorio entre minDamage y maxDamage, y se lo aplica a la entidad referenciada. Si la entidad invoca defend(damage) con el valor generado, y es superior a 0, se le resta esta diferencia positiva a la salud de la entidad. Si la entidad llega a 0 de salud, muere.
  - defend(damage): método que genera un valor aleatorio entre minDefense y maxDefense. Si el valor generado es superior a 0, se le resta esta diferencia positiva a la salud del jugador. Si la entidad llega a 0 de salud, muere.
  - moveForward(): método que, si la celda destino del mapa es visitable, mueve al jugador una celda hacia delante en la dirección que mira.
    - Si facing = 'N': y = y - 1;
    - Si facing = 'S': y = y + 1;
    - Si facing = 'E': x = x + 1;
    - Si facing = 'W': x = x - 1;
 - moveBackward(): método que, si la celda destino del mapa es visitable, mueve al jugador una celda hacia atrás en la dirección que mira.
    - Si facing = 'N': y = y + 1;
    - Si facing = 'S': y = y - 1;
    - Si facing = 'E': x = x - 1;
    - Si facing = 'W': x = x + 1;
  - rotateLeft(): método que gira hacia la izquierda la dirección en la que mira el jugador.
    - Si facing = 'N': facing = 'W';
    - Si facing = 'S': facing = 'E';
    - Si facing = 'E': facing = 'N';
    - Si facing = 'W': facing = 'S';
  - rotateRight(): método que gira hacia la derecha la dirección en la que mira el jugador.
    - Si facing = 'N': facing = 'E';
    - Si facing = 'S': facing = 'W';
    - Si facing = 'E': facing = 'S';
    - Si facing = 'W': facing = 'N';
- Añade las clases Entity.js y Player.js a index.html.
