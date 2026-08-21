# feature-0018: Tests

- Se comprueba que el runtime incluido puede ejecutarse en la plataforma actual.
- Se comprueba que el runtime generado con `jlink` contiene únicamente el módulo `java.base`, requerido por la aplicación.
- Se comprueba que el runtime incluido utiliza Java 21.
- Se comprueba que el runtime conserva la licencia y los avisos legales de Java.
- Se comprueba que `run.sh` y `run.bat` arrancan el JAR mediante el Java incluido, sin depender del comando `java` del sistema.
- Se comprueba que `run.sh` conserva el permiso de ejecución en sistemas compatibles.
- Se comprueba que el runtime incluido puede iniciar realmente el `Launcher` y poner el servidor del juego en escucha.
