# feature-0010

Implelentación de escena de juego.


# Objetivo

Aquí vamos a poner todo en funcionamiento.

Implementaremos la escena de juego, en la que se representará todo: UI, laberinto, entidades, etcétera. También será encargada de capturar las pulsaciones del teclado y de ratón.



# Estado

- [X]En proceso.
- [X]En pruebas.
- [X]Finalizado.


# Notas

- Revisa la asignación de movimientos al teclado:
  - ARROW UP: invocará el método moveForward() de Player. Lo que hará que avance una habitación en la dirección en la que mira el jugador.
    - Si mira al norte, avanza hacia arriba (y - 1).
    - Si mira al sur, avanza hacia abajo (y + 1).
    - Si mira al este, avanza hacia la derecha (x + 1).
    - Si mira al oeste, avanza hacia la izquierda (x - 1).
  - ARROW DOWN: invocará el método moveBackward() de Player. Hace que el jugador retroceda en dirección contraria a la que está mirando, pero continúa mirando en la dirección original.
    - Si mira al norte, retrocede hacia abajo (y + 1).
    - Si mira al sur, retrocece hacia arriba (y - 1).
    - Si mira al este, retrocede hacia la izquierda (x - 1).
    - Si mira al oeste, retrocede hacia la derecha (x + 1).
  - ARROW LEFT: invocará el método rotateLeft() de Player.
    - Si mira al norte, gira hasta mirar al oeste.
    - Si mira al oeste, gira hasta mirar al sur.
    - Si mira al sur, gira hasta mirar al este.
    - Si mira al este, gira hasta mirar al norte.
  - ARROW RIGHT: invocará el método rotateRight() de Player.
    - Si mira al norte, gira hasta mirar al este.
    - Si mira al este, gira hasta mirar al sur.
    - Si mira al sur, gira hasta mirar al oeste.
    - Si mira al oeste, gira hasta mirar al norte.
- A tener en cuenta en el renderizado del campo de visión (1, 2 y 3 significa dentro del campo de visión, 0 significa fuera del campo de visión):

```text

X = posición del jugador.
1, 2, 3: nivel de profundidad.

+---+---+---+---+---+   +---+---+---+---+---+   +---+---+---+---+---+   +---+---+---+---+---+
| 3 | 3 | 3 | 3 | 3 |   | 0 | 0 | 0 | 0 | 0 |   | 0 | 0 | 0 | 0 | 3 |   | 3 | 0 | 0 | 0 | 0 |
| 0 | 2 | 2 | 2 | 0 |   | 0 | / | X | \ | 0 |   | 0 | / | 0 | 2 | 3 |   | 3 | 2 | 0 | \ | 0 |
| 0 | 0 | 1 | 0 | 0 |   | 0 | 0 | 1 | 0 | 0 |   | 0 | X | 1 | 2 | 3 |   | 3 | 2 | 1 | X | 0 |
| 0 | \ | X | / | 0 |   | 0 | 2 | 2 | 2 | 0 |   | 0 | \ | 0 | 2 | 3 |   | 3 | 2 | 0 | / | 0 |
| 0 | 0 | 0 | 0 | 0 |   | 3 | 3 | 3 | 3 | 3 |   | 0 | 0 | 0 | 0 | 3 |   | 3 | 0 | 0 | 0 | 0 |
+---+---+---+---+---+   +---+---+---+---+---+   +---+---+---+---+---+   +---+---+---+---+---+
Norte                   Sur                     Este                    Oeste

```
  - Las imágenes de las paredes frontales tienen dimensiones 256 x 256. trabajaremos con porcentajes, por lo que en primer lugar, escalaremos las imágenes al ancho x alto del recuadro de campo de visión (2).
  - Las imágenes de las paredes en perspectiva tienen dimensiones 128 x 512. trabajaremos con porcentajes, pero aplicaremos el mismo factor de escalado que con las imágenes frontales.
  - Las imágenes en perspectiva que estén a derecha de la pantalla se mostrarán invertidas horizontalmente.
  - Por cada nivel de profunidad, primero pinta las paredes en perspectiva, y luego las paredes frontales.

  - Renderizado de profundidad = 3:
    - Las imágenes estarán centradas verticalmente, pero en esta capa tendrán un desplazamiento vertical hacia abajo de 16 píxels.
    - Para simular profundidad, sobre las imágenes frontales ya escaladas previamente, multiplicaremos por 0.2 el factor de escalado. También oscureceremos la imagen en un factor de 0.5.
    - En profundidad 3 deberían mostrarse 4 paredes en perspectiva: dos a la izquierda (una en cada lateral izquierdo de cada pared, y cada esquina superior izquierda debe coincidir con la esquina superior derecha de la pared bajo la que está) y dos a la derecha (2 en cada lateral derecho de cada pared, y cada esquina superior derecha debe coincidir con la esquina superior izquierda de la pared bajo la que está). Las que están en perspectiva ademas del factor de escalado, se multiplicarán por 0.5.
    - En profundidad 3 deberían mostrarse 5 paredes frontales: dos a la izquierda, una en el centro, y dos a la derecha.
    - Si miramos al norte, las paredes en perspectiva corresponderían a la posición relativa al jugador (-2, -3), (-1, -3), (+1, -3) y (+2, -3).

  - Renderizado de profundidad = 2:
    - Las imágenes estarán centradas verticalmente, pero en esta capa tendrán un desplazamiento vertical hacia abajo de 8 píxels.
    - Sobre las imágenes frontales ya escaladas previamente, multiplicaremos por 0.4 el factor de escalado. También oscureceremos la imagen en un factor de 0.25.
    - En profundidad 2 deberían mostrarse 2 paredes en perspectiva: una a la izquierda (en el lateral izquierdo de la pared, y su esquina superior izquierda debe coincidir con la esquina superior derecha de la pared bajo la que está) y otra a la derecha (en el lateral derecho de la pared, y su esquina superior derecha debe coincidir con la esquina superior izqueirda de la pared bajo la que está). Las que están en perspectiva ademas del factor de escalado, se multiplicarán por 0.5.
    - En profundidad 2 deberían mostrarse 3 paredes: una a la izquierda, una en el centro, y una a la derecha.
    - Si miramos al norte, las paredes en perspectiva corresponderían a la posición relativa al jugador (-2, -2), (-1, -2), (+1, -2) y (+2, -2).

  - Renderizado de profundidad = 1:
    - Las imágenes estarán centradas verticalmente, pero en esta capa tendrán un desplazamiento vertical hacia abajo de 4 píxels.
    - Sobre las imágenes frontales ya escaladas previamente, multiplicaremos por 0.8 el factor de escalado. La imagen no se oscurecerá.
    - En profundidad 1 deberían mostrarse 2 paredes en perspectiva: una a la izquierda (en el lateral izquierdo de la pared, y su esquina superior izquierda debe coincidir con la esquina superior derecha de la pared bajo la que está) y otra a la derecha (en el lateral derecho de la pared, y su esquina superior derecha debe coincidir con la esquina superior izquierda de la pared bajo la que está). Las que están en perspectiva además del factor de escalado, se multiplicarán por 0.5.
    - Si miramos al norte, las paredes en perspectiva corresponderían a la posición relativa al jugador (-1, -1) y (+1, -1).

  - Renderizado extra en la posición del jugador:
    - Las imágenes estarán centradas verticalmente, pero en esta capa tendrán un desplazamiento vertical hacia abajo de 4 píxels.
    - Si la celda a la izquierda del jugador es no visitable, se mostrará una pared en perspectiva a la izquierda de la pared frontal. Lo mismo sucederá con la celda derecha.
    - Si la celda ubicada delante a la izquieda no es visitable, mostraremos una pared a la izquierda de la pared frontal. Lo mismo pasa por la derecha. Ambas imágenes extra estarán recortadas para no salirse del campo de visión.
    - El borde derecho de la pared en perspectiva izquierda colindará con el borde izquierdo de la pared frontal. A su vez, el borde izquierdo de pared en perspectiva derecha colindará con el dborde derecho de la pared frontal, pero recortarás ambas imágenes en perspectiva para que no se vean fuera del recuadro del campo de visión.
    - Esta profundidad extra sólo tiene el escalado calculado para el campo de visión.
    - Solo para el renderizado extra, primero se pintan las paredes frontales y luego las paredes en perspectiva.


# Restricciones

*Ninguna*


# Implementación

- Crea la escena GameScene, localizada en el fichero "webcontent/js/scene/GameScene.js". Añádela al array de escenas de index.js, justo tras MainMenuScene. Por supuesto, declárala en index.html.
- La escena (visualmente) se descompone aproximadamente de la siguiente forma: 1) Canvas usado por Phaser (ya existe). 2) Campo de visión. 3) Botón de menú. 4) Minimapa del laberinto. 5) Log de eventos del juego. 6) Barra de salud del jugador.

+-(1)----------------------------------------------------------------+
|+-(2)-----------------------------+ +-(6)--------------------+ +(3)+|
||                                 | |                        | |   ||
||                                 | +------------------------+ +---+|
||                                 | +-(4)--------------------------+|
||                                 | |                              ||
||                                 | |                              ||
||                                 | |                              ||
||                                 | |                              ||
||                                 | |                              ||
||                                 | |                              ||
||                                 | |                              ||
||                                 | |                              ||
||                                 | |                              ||
||                                 | |                              ||
||                                 | |                              ||
||                                 | |                              ||
|+---------------------------------+ +------------------------------+|
|+-(5)--------------------------------------------------------------+|
||                                                                  ||
||                                                                  ||
||                                                                  ||
||                                                                  ||
|+------------------------------------------------------------------+|
+--------------------------------------------------------------------+

- Al inicio, se generan los mapas de cada nivel.
- Como se ha dicho anteriormente, comenzamos el juego en el nivel 0 (aunque visualmente se mostrará como level + 1), en las coordendas del centro del laberinto, y mirando hacia el norte.
- Teniendo en cuenta el campo de visión obtenido, generamos el campo de visión (2). Para esto seguimos estas reglas:
  - Existen dos backgrounds: se mostrará uno u otro en función de si x + y es par (mostramos "webcontent/assets/images/Background001.png") o impar (mostramos mostramos "webcontent/assets/images/Background002.png").
  - Existen 3 niveles de profundidad del campo de visión:
    - Primero renderizamos el nivel más lejano: aquí se visualizan las 2 celdas a la izquierda, la celda frontal, y las 2 celdas a la derecha que hay dos celdas por delante en la dirección en la que mira el jugador. Para renderizar las imágenes las escalaremos un factor de 0.2 (correspondiente a 0.8 * 0.5 (1 celda) * 0.5 (2 celdas)), y oscureceremos la iluminación al 0.5. Las 5 imágenes concatenadas ocupan el ancho del recuadro de campo de visión (2).
    - En segundo lugar, renderizamos el nivel intermedio: aquí mostramos 1 celda a la izquierda, la celda frontal y 1 celda a la derecha de la posición que hay una celda por delante en la dirección en la que mira el jugador. Escalaremos las imágenes un factor de 0.4 (correspondiente a 0.8 * 0.5 (1 celda)). Renderizamos las imágenes oscureciendo la iluminación al 0.25. Las 3 imágenes concatenadas (sin tener en cuenta las que están en perspectiva) ocupan el ancho del recuadro de campo de visión (2).
    - En último lugar, renderizamos el nivel más cerano: aquí mostramos la celda frontal de la celda por delante en la dirección en la que mira el jugador, y un 10% de la celda que hay por la izquierda y un 10% de la celda que hay por la derecha. Renderizamos las imágenes en un factor de 0.8. Si la celda a la izquierda de la celda frontal no es transitable, mostramos el 10% derecho de la imagen de la pared. De igual forma, si la celda a la derecha de la celda fronta no es transitable, pintaremos el 10% izquierdo la imagen de la pared. Si la celda de enfrente no es visitable, dibujaremos la imagen de la pared.
    - La lógica a seguir con las imágenes de paredes, independientemente de hacia dónde se mire (tanto planas como en perspectiva) es la siguiente:
      - Si (xCelda + yCelda) % 3 = 0, las texturas de esa habitación son "webcontent/assets/images/Wall001.png".
      - Si (xCelda + yCelda) % 3 = 1, las texturas de esa habitación son "webcontent/assets/images/Wall002.png".
      - Si (xCelda + yCelda) % 3 = 2, las texturas de esa habitación son "webcontent/assets/images/Wall003.png".
- En la barra de salud (6) dibuja un rectángulo rojo con un borde de 2 píxeles de grosor y 1 píxel de margen de interno para dibujar otro rectánulo rojo oscuro que representa el porcentaje de salud.
- En el minimapa (4), divide el espacio disponible en ancho entre el width del mapa y el alto entre el height del mapa. Representa todas las celdas del nivel actual de la siguiente manera:
  - Dibuja de forma matricial el mapa del nivel actual en el que se encuentra el jugador.
  - Si la celda no es visible, dibuja un rectángulo gris relleno. Si la celda es visible y visitable, dibuja un rectángulo negro. Si la celda es visible y no visitable, dibuja un rectángulo gris oscuro. En la casilla del minimapa donde se encuentre el jugador (x, y), pon un rectángulo rojo con 1 píxel de margen.
  - De momento, el recuadro de log de eventos (5) lo dejamos representado pero sin acciones ningunas.
- Ten en cuenta las restricciones de movimiento. Sólo puedes avanzar o retroceder hacia celdas visitables.
- Cuando nos movamos o giremos, marcaremos las celdas del cono de visión como "visibles", y actualizarás el minimapa.
- La escena capturará las teclas de dirección del teclado:
  - KEY_UP: invocará el método moveForward() de Player.
  - KEY_DOWN: invocará el método moveBackward() de Player.
  - KEY_LEFT: invocará el método rotateLeft() de Player.
  - KEY_RIGHT: invocará el método rotateRight() de Player.
- Modifica MainMenuScene para que al hacer onClick en el botón "Nuevo Juego" se oculte la escena del menú y se lance la escena GameScene.
