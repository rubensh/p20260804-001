# feature-0006: Tests

## UI

1. `uiClassesAndTestSceneExist`: los ficheros de las clases de UI y de la escena de prueba existen.
2. `uiElementIsBaseClass`: la clase base declara sus propiedades `id` y `parentElement`.
3. `uiButtonExtendsUiElementAndHasProperties`: el botón extiende la clase base y declara sus propiedades.
4. `uiButtonHandlesStateAndEvents`: el botón gestiona el estado activo, el resaltado y el clic.
5. `uiPanelExtendsUiElementAndHasProperties`: el panel extiende la clase base y declara sus propiedades.
6. `uiTextSceneDeclaresTestUi`: la escena de prueba declara el panel y el botón de prueba.
7. `bootloaderTargetsUiTextScene`: la escena de arranque apunta a la escena de prueba de UI.
8. `indexJsRegistersUiTextSceneAfterBootloader`: la configuración del juego registra la escena de prueba tras la de arranque.
9. `indexHtmlLoadsUiScriptsBeforeIndexJs`: el HTML carga los scripts de UI antes del script principal.
10. `uiResourcesAreServed`: los recursos de UI son servidos por el servidor.
11. `uiClassesAvoidUnsupportedContainerApi`: los elementos de UI no usan `container.setOrigin`, una API no soportada por el contenedor de Phaser.
12. `uiButtonHitAreaCompensatesContainerOrigin`: el área de interacción del botón compensa el origen centrado del contenedor de Phaser.
