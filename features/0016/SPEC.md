# feature-0016

Implementación de un enemigo final.


# Objetivo

Crear un enemigo especial, que actuará a modo de enemigo final. Nuestra misión principal en el juego es acabar con él.


# Estado

- [X]En proceso.
- [X]En pruebas.
- [X]Finalizado.


# Notas

*Ninguna*

# Restricciones

*Ninguna*


# Implementación

- Vamos a añadir un nuevo enemigo: el orco "final boss" ("webcontent/assets/images/Orc-001.png").
- La clase Javascript se denominará Boss y se ubicará en "webcontent/js/entity/Boss.js". Dicha clase extiende de Entity e incluye las siguientes propiedades y métodos:
  - health: un valor indicando la salud (en porcentaje). health = (currentLevel + 1) * 10 * Math.round(3).
  - minDamage: un valor numérico indicando el mínimo daño que puede generar el enemigo. minDamage = (currentLevel + 1) * 2.
  - maxDamage: un valor numérico indicando el máximo daño que puede generar el enemigo. maxDamage = (currentLevel + 1) * 5. 
  - minDefense: un valor numérico indicando el mínimo daño que puede soportar el enemigo sin que reste salud. minDefense = (currentLevel + 1).
  - maxDefense: un valor numérico indicando el máximo daño que puede soportar el enemigo sin que reste salud. minDefense = (currentLevel + 1) * 2.
  - attackDelay = enemyType * 1.5 - 0.5.
  - attack(): método que genera un valor aleatorio entre minDamage y maxDamage, y se lo aplica al jugador. Si el jugador invoca defend(damage) con el valor generado, y es superior a 0, se le resta esta diferencia positiva a la salud del jugador. Si llega a 0 de salud, el jugador muere.
  - defend(damage): método que genera un valor aleatorio entre minDefense y maxDefense. Si el valor generado es superior a 0, se le resta esta diferencia positiva a la salud del enemigo. Si la entidad llega a 0 de salud, muere.
  - moveUp(): método que, si la celda destino del mapa es visitable, mueve al enemigo hacia el norte.
    - y = y - 1;
  - moveDown(): método que, si la celda destino del mapa es visitable, mueve al enemigo hacia el sur.
    - y = y + 1;
  - moveLeft(): método que, si la celda destino del mapa es visitable, mueve al enemigo hacia el oeste.
    - x = x - 1;
  - moveRight(): método que, si la celda destino del mapa es visitable, mueve al enemigo hacia el este.
    - x = x + 1;
  - moveUpLeft(): método que, si la celda destino del mapa es visitable, mueve al enemigo hacia el noroeste.
    - x = x - 1;
    - y = y - 1;
  - moveUpRight(): método que, si la celda destino del mapa es visitable, mueve al enemigo hacia el noreste.
    - x = x + 1;
    - y = y - 1;
  - moveDownLeft(): método que, si la celda destino del mapa es visitable, mueve al enemigo hacia el suroeste.
    - x = x - 1;
    - y = y + 1;
  - moveDownRight(): método que, si la celda destino del mapa es visitable, mueve al enemigo hacia el sureste.
    - x = x + 1;
    - y = y + 1;
- Añade la clase Boss.js a index.html.
- Modifica GameScene para albergar un único orco en el último nivel.
- Modifica el método generateMap() para añadir la fase de creación de jefe final:
- No puede generarse en las casillas de escaleras.
- Tampoco pueden haber múltiples enemigos en la misma casilla.
- Inicialmente no se moverá entre habitaciones ni niveles.
- Cuando tenemos al jefe en una casilla adyacente (N, S, E, O) éste nos atacará. Si nosotros lo tenemos justo delante, lo atacaremos nosotros también.
- Reglas de renderizado:
  - Profundidad 3:
    - Escala: 0.25.
    - Desplazamiento vertical: -160 px.
    - iluminación: 0.25.
  - Profundidad 2:
    - Escala: 0.5.
    - Desplazamiento vertical: -128 px.
    - Iluminación: 0.5.
  - Profundidad 1:
    - Escala: 1.0.
    - Desplazamiento vertical: -64 px.
    - Iluminación: 1.0.
- Cuando el jefe muere, eliminamos la entidad y dejamos renderizarlo.
- Modifica GameScene para que si nuestra salud es inferior 0, se muestre en el centro de la pantalla las palabras "Game Over", y 5 segundos más tarde volvamos al menú principal. Si la salud del jefe llega a 0, se muestra en el centro de la pantalla las palabras "You win!!!", y 5 segundos más tarde volvamos al menú principal.
- Cuando el jefe esté en una habitación adyacente al jugador (al norte, al sur, al este o al oeste) se iniciará un interval de javascript con interval = attackDelay * 1000. Cuando nos separemos de él eliminaremos dicho interval. Mantén la referencia al interval como una propiedad del propio jefe.
- Igualmente, junto con el caso anterior, la textura del enemigo se trasladará 5 píxeles aleatoriamente hacia arriba, abajo, izquierda y/o derecha desde suposición inicial mientras esté luchando.
- Cuando luchemos, la vista de log de eventos del juego (5), mostrará mensajes indicando los ataques, siempre en nuevas líneas:
  - Cuando nos ataquen: "El orco realiza un ataque con X de daño".
  - Cuando ataquemos: "Golpeas al orco con un ataque de X de daño".
