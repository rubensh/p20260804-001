# feature-0017: Tests

- Se comprueba que `landing.html` ya no existe, que `index.html` no lo enlaza y que el servidor responde 404 para su antigua ruta.
- Se comprueba que el menú principal elimina las opciones `Test UI` y `Salir del juego`.
- Se comprueba que `Alternar pantalla completa` activa y desactiva el modo de pantalla completa.
- Se comprueba que `BootloaderScene` carga `Buried-Dark-World-Intro.mp4` y da paso a `IntroScene`.
- Se comprueba que `IntroScene` ajusta el vídeo al 100% del canvas, también después de que Phaser inicialice sus dimensiones, y abre `MainMenuScene` a los 10 segundos.
- Se comprueba que el vídeo de introducción mantiene su audio habilitado.
- Se comprueba que la introducción se puede omitir con cualquier clic o pulsación de teclado.
- Se comprueba que `IntroScene` queda registrada entre `BootloaderScene` y `MainMenuScene`.
- Se comprueba que `BootloaderScene` carga la música ambiental y los dos efectos de sonido.
- Se comprueba que la música ambiental se reproduce en bucle en todas las escenas salvo `BootloaderScene` e `IntroScene`.
- Se comprueba que cada ataque reproduce `Sword-Clash-001.wav` y cada enemigo derrotado reproduce `Goblin-Pain-001.wav`.
- Se comprueba que el servidor entrega los recursos MP4 y WAV con tipos MIME compatibles con el navegador.
