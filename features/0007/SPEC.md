# feature-0007


# Objetivo

Crea una escena en Phaser denominada "MainMenuScene", y ubicada en el archivo "webcontent/js/scene/MainMenuScene.js". Cárgala en el array de escenas de la configuración de Phaser que se define en "webcontent/js/index.js". Esta escena debe ir en segunda posición del array.

Esta escena define el menú principal de la aplicación. Las opciones de menú disponibles son:

- Nuevo juego.
- Créditos.
- Test UI.
- Salir del juego.


# Estado

- [X]En proceso.
- [X]En pruebas.
- [X]Finalizado.


# Notas

*Ninguna*


# Restricciones

*Ninguna*


# Implementación

- BootSceneLoader, en lugar de dar paso a UITestScene, da paso a MainMenuScene.
- El menú se compone de la siguiente forma:
- Un UIPanel:
  - Id: MenuPanel001
  - x: 10.
  - y: 10.
  - width: 200.
  - height: 580.
  - backgroundColor: rgba(0.8, 0.8, 0.8, 0.75).
  - borderColor: rgba(1.0, 1.0, 1.0, 1.0).
  - elements:
    - Un UIButton:
      - Id: MenuButtonNewGame.
      - x: 10.
      - y: 10.
      - width: 180.
      - height: 32.
      - backgroundColor: rgba(0.6, 0.6, 0.6, 1.0).
      - borderColor: rgba(0.75, 0.75, 0.75, 1.0).
      - text: Nuevo juego
      - textColor: rgba(0.0, 0.0, 0.0, 1.0).
      - textSize: 18.
      - fontName: Monospace.
    - Un UIButton:
      - Id: MenuButtonTestUI.
      - x: 10.
      - y: 47.
      - width: 180.
      - height: 32.
      - backgroundColor: rgba(0.6, 0.2, 0.2, 1.0).
      - borderColor: rgba(0.75, 0.1, 0.1, 1.0).
      - text: Test UI
      - textColor: rgba(0.0, 0.0, 0.0, 1.0).
      - textSize: 18.
      - fontName: Monospace.
      - onClickFunction: la escena dará paso a UITestScene. MainMenuScene finaliza y deja de ejecutarse.
    - Un UIButton:
      - Id: MenuButtonCredits.
      - x: 10.
      - y: 79.
      - width: 180.
      - height: 32.
      - backgroundColor: rgba(0.6, 0.6, 0.6, 1.0).
      - borderColor: rgba(0.75, 0.75, 0.75, 1.0).
      - text: Créditos
      - textColor: rgba(0.0, 0.0, 0.0, 1.0).
      - textSize: 18.
      - fontName: Monospace.
      - onClickFunction: oculta temporalmente el panel MenuPanel001, y muestra un texto centrado, a modo de créditos de película, que van desde la parte inferior de la pantalla a la superior, donde las secciones son:
      ````text
      CAST
      ====

      Frontend Programmers
      ----
      A free AI agent specialized in programming



      Backend Programmers
      ----
      The same AI programming agent



      Assets Designers
      ----
      Two very famous general-purpose agents



      Q&A Team
      ----
      The AI agent that actively participated in the development of the frontend and backend (... and a bit of me)



      ... and the last but not least... in fact, the most important thing of all...


      Concept, design and leading
      ---
      Me :)

      ````
      Para no aburrir, desde "Frontend Programmers" hasta "Q&A Team se mostrarán en un periodo de duración de 15 segundos desde que salen de la parte inferior hasta la superior. La parte de "Concept, design and leading", tendrá un tamaño de fuente del doble de tamaño y tardará 5 segundos en recorrer el alto de la pantalla. Una vez finalizado, volverá a mostrarse el panel MenuPanel001.

- Modifica UITestScene para añadir un botón "Volver", que vuelva al menú principal:
  - Un UIButton:
    - Id: testButtonBack.
    - x: 10.
    - y: 256.
    - width: 280.
    - height: 32.
    - backgroundColor: rgba(0.6, 0.6, 0.6, 1.0).
    - borderColor: rgba(0.75, 0.75, 0.75, 1.0).
    - text: Volver al menú principal
    - textColor: rgba(0.0, 0.0, 0.0, 1.0).
    - textSize: 18.
    - fontName: Monospace.
    - onClickFunction: la escena dará paso a MainMenuScene. UITestScene finaliza y deja de ejecutarse.
- Comprueba que todos los botones de UITestScene están dentro del panel testPanel001, y que el parent de cada uno de ellos es dicho panel.
