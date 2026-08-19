# feature-0017

Cambios finales y retoques.


# Objetivo

Dar las últimas pinceladas al juego para darle un acabado estético más profesional.


# Estado

- [X]En proceso.
- [ ]En pruebas.
- [ ]Finalizado.


# Notas

*Ninguna*

# Restricciones

*Ninguna*


# Implementación

- En "index.html", elimina el enlace a "landing.html". Elimina tambien el archivo "landing.html".
- Elimina del menú principal las opciones de "Test UI" y "Salir del juego".
- Añade una opción en el menú principal bajo "Nuevo juego" que diga "Alternar pantalla completa" que alterne entre full screen y normal screen.
- En la escena BootScene, carga el vídeo de introducción que hay en "webcontent/assets/videos/Buried-Dark-World-Intro.mp4", y muéstralo en una escena nueva denominada IntroScene. Esta escena se debe declarar en el array de escenas tras BootScene y antes de MainMenuScene. Muestra el vídeo manteniendo las proporciones de éste pero ajustándolas al canvas. Y al finalizar (10 segundos) pasa a mostrar la MainMenuScene.
- En la BootScene, carga la música de fondo "webcontent/assets/audio/Buried-Dark-World-Loop.wav". Esta música suena de fondo y en bucle infinito en todas las escenas excepto en BootScene e IntroScene.
- Carga los sonidos "webcontent/assets/audio/Sword-Clash-001.wav" y "webcontent/assets/audio/Goblin-Pain-001.wav" en BootScene. Durante el juego, cuando luchemos contra un enemigo, sonará en cada ataque "Sword-Clash-001", mientras que "Goblin-Pain-001" sonará cuando acabemos con un enemigo.
