# feature-0013

Implementación de enemigos.


# Objetivo

Generar entidades enemigas, ubicarlas en el mapa y añadir todas acciones y eventos relacionados: ataques, muertes, movimiento, etcétera.


# Estado

- [X]En proceso.
- [X]En pruebas.
- [X]Finalizado.


# Notas

*Ninguna*

# Restricciones

*Ninguna*


# Implementación

- Vamos a añadir tres tipos de enemigos: goblins con espadas ("webcontent/assets/images/Goblin-001.png"), goblins con hachas  ("webcontent/assets/images/Goblin-003.png") y goblins con lanzas ("webcontent/assets/images/Goblin-003.png").
- Todos los enemigos compartirán clase Javascript. Se denominará Enemy y se ubicará en "webcontent/js/entity/Enemy.js". Dicha clase extiende de Entity e incluye las siguientes propiedades y métodos:
  - enemyType: un valor indicando si es un goblin con espada, con hacha o con lanza. enemyType = Math.round(3) + 1.
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
- Añade la clase Enemy.js a index.html.
- Modifica GameScene para albergar un array de enemigos. El tamaño será de 200 enemigos.
- Modifica el método generateMap() para añadir la fase de creación de los enemigos:
  - El reparto de enemigos por nivel no será homogéneo. Es decir, en los niveles más profundos habrán más enemigos que en los niveles superiores. Busca una fórmula que reparta de esa manera: por ejemplo, para 5 niveles, la concentración de los 200 enemigos podría ser (nivel 1: 5%, nivel 2: 8%, nivel 3: 12%, nivel 4: 30%, nivel 5: 45%).
- No pueden generarse enemigos en las casillas de escaleras.
- Tampoco pueden haber múltiples enemigos en la misma casilla.
- Inicialmente los enemigos no se moverán entre habitaciones ni niveles.
- Cuando tenemos un enemigo en una casilla adyacente (N, S, E, O) éste nos atacará. Si nosotros lo tenemos justo delante, lo atacaremos nosotros también.
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
- Cuando el goblin muere, eliminamos la entidad del array de enemigos y dejamos renderizarlo.
- Modifica GameScene para que si nuestra salud es inferior 0, se muestre en el centro de la pantalla las palabras "Game Over", y 5 segundos más tarde volvamos al menú principal.
- Además, el renderizado en perspectiva debe incluir las paredes de las celdas laterales extremas situadas delante del jugador: en profundidad 2 se añadirán las posiciones relativas −2 y +2 dentro del mismo pase que el resto de paredes de ese nivel, aplicando la política visual de profundidad 2 y desplazándolas hacia el interior una distancia equivalente a 0.5 veces el ancho escalado de profundidad 3 —la izquierda en positivo y la derecha en negativo—; en profundidad 1 también se incluirán las posiciones −2 y +2 dentro del mismo pase, aplicando la política visual de profundidad 1 y desplazándolas hacia el exterior una distancia equivalente a 0.25 veces el ancho escalado de profundidad 2 —la izquierda en negativo y la derecha en positivo—, sin modificar las paredes existentes en −1 y +1. Asimismo, la imagen del enemigo situado en profundidad 3 tendrá un desplazamiento vertical de −160 px.
