# feature-0012

Ubicar escaleras de acceso a otros niveles.


# Objetivo

Habilitar accesos de un nivel a otros contiguos.


# Estado

- [X]En proceso.
- [X]En pruebas.
- [X]Finalizado.


# Notas

*Ninguna*


# Restricciones

*Ninguna*


# Implementación

- Las escaleras de subida se representan con el asset "webcontent/assets/images/Stairs-Up.png", y las de bajada con "webcontent/assets/images/Stairs-Down.png".
- En todos los niveles habrá una escalera de subida para acceder al nivel (currentLevel - 1) y otra de bajada para acceder al nivel (currentLevel + 1), a excepción del primer nivel donde no habrá escaleras de subida, ni al último nivel, donde no habrá escaleras de bajada.
- Escalas de representación, posicionamiento e iluminación de la imagen:
  - Profundidad 3:
    - Escala: 0.25.
    - Desplazamiento en y: +14.
    - Iluminación: 0.25
  - Profundidad 2:
    - Escala: 0.5.
    - Desplazamiento en y: +2.
    - Iluminación: 0.5
  - Profundidad 1:
    - Escala: 1.0.
    - Desplazamiento en y: 0.
- Cuando la imagen se salga de los límites del campo de visión (por ejemplo, en profundad 1), debe ser recortada a dicho campo de visión.
- No puede haber escaleras de subida y de bajada en la misma habitación.
- Cuando tengamos una escalera delante en profundidad 1, y avancemos, entonces subiremos al nivel superior, o bajaramos al inferior, en función de la escalera que sea.
- Modifica el algoritmo de generación de nivel para añadir la ubicación de las escaleras que correspondan al nivel. La escalera de subida debe estar en el punto inicial de generación de ese mapa. Las de bajada estarán en la última habitación generada.
- Las habitaciones con escaleras visualizadas se mostrarán en el minimapa. Las de bajada con un triángulo hacia abajo de color rojo. Las de subida con un triángulo hacia arriba azul. Los triángulos son del tamaño del recuadro que represnta la celda.
- Cuando cambiemos de nivel, actualiza el minimapa y sus celdas visitadas.

