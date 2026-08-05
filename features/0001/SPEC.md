# feature-0001


# Objetivo

Implementar un servidor HTTP ligero y sencillo.


# Estado

- [X]En proceso.
- [ ]En pruebas.


# Notas

*Ninguna*


# Restricciones

- El servidor atenderá peticiones HTTP GET exclusivamente.
- Sólo se hará uso de los componentes básicos de Java incluídos con el JDK.
- No requiere ninguna librería externa.
- No interviene ningún tipo de encriptación en la comunicación con el servidor.


# Implementación

- Crea la clase com.rsh.p20260804001.server.GameServer.
- La clase debe implementar:
  - Un constructor que aceptará un número de puerto por el que escuchar peticiones.
  - Un método init(int port) que usará el número de puerto para poner a la escucha un ServerSocket.
  - Todo esto debe correr en segundo plano (demonio).
  - La clase debe exponer también métodos para finalizar el modo escucha del ServerSocket, y por lo tanto, finalizar la ejecución del juego.
  - También debe implementar un método encargado de atender las peticines HTTP GET. Cualquier otro tipo de petición será descartada.
  - El servidor podrá acceder a los contenidos existentes bajo la carpeta webcontent/ y sus subcarpetas. De ninguna forma podrá acceder a contenido Java o de la carpetas src/main/resources o src/test/resources.
