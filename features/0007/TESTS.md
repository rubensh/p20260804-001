# feature-0007: Tests

## MainMenuScene

1. `mainMenuSceneFileExists`: el fichero de la escena del menú principal existe.
2. `mainMenuSceneExtendsPhaserSceneAndHasPanel`: la escena extiende Phaser.Scene y declara el panel del menú.
3. `mainMenuSceneHasMenuButtons`: la escena declara las opciones del menú (nuevo juego, test UI, créditos y salida).
4. `mainMenuTestUiButtonStartsUiTestScene`: el botón de prueba de UI da paso a la escena UITestScene.
5. `mainMenuExitButtonDestroysGame`: el botón de salida finaliza el juego.
6. `bootloaderTargetsMainMenuScene`: la escena de arranque apunta al menú principal.
7. `indexJsRegistersMainMenuSecond`: la configuración del juego registra el menú principal en segunda posición.
8. `indexHtmlLoadsMainMenuSceneBeforeIndexJs`: el HTML carga la escena del menú principal antes del script principal.
9. `uiButtonHandlesMissingOnClickFunction`: el botón de UI soporta la ausencia de `onClickFunction`.
10. `mainMenuResourcesAreServed`: la escena del menú principal es servida por el servidor.
11. `mainMenuCreditsButtonHidesPanelAndShowsCredits`: el botón de créditos oculta el panel del menú y muestra los créditos.
12. `mainMenuCreditsDeclareSections`: la escena declara las secciones y acreditaciones de los créditos.
13. `mainMenuCreditsMainScrollsInFifteenSeconds`: las secciones principales de los créditos recorren la pantalla en quince segundos.
14. `mainMenuCreditsConceptUsesDoubleFontAndFiveSeconds`: la sección final de los créditos usa doble tamaño de fuente y cinco segundos.
15. `mainMenuCreditsRestoresPanelWhenFinished`: al finalizar los créditos se vuelve a mostrar el panel del menú.

## UITestScene

16. `uiTestSceneHasBackButton`: la escena de prueba declara el botón de vuelta al menú principal.
