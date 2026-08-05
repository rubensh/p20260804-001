# feature-0002


# Objetivo

Crear la clase "com.rsh.p20260804001.launcher.Launcher" que implemente el método "main(String[] args)". Esta clase servirá como launcher para arrancar el servidor en segundo plano desde un terminal.


# Estado

- [X]En proceso.
- [X]En pruebas.
- [X]Finalizado.


# Notas

*Ninguna*


# Restricciones



# Implementación

- Crea la clase com.rsh.p20260804001.launcher.Launcher.
- La clase debe:
  - Instanciar un objeto de la clase GameServer que atienda peticiones por el puerto 8088.
  - Mostrar mensaje de arranque y estado del servidor (puerto de escucha, etcétera).
  - Parar y liberar todo recurso adquirido por la aplicación (incluyendo el GameServer) al finalizar la ejecución vía CTRL-C, o al cerrarse el terminal.
- Esta feature implica que la aplicación se pueda lanzar en terminal mediante comando Maven. Gestiona el plugin correspondiente org.apache.maven.plugins:maven-jar-plugin.
- En la declaración del plugin añade un "manifestEntries" con el valor "Created-By" Rubén Santana Hernández.
