# feature-0001: Tests

Tests a implementar para la clase `com.rsh.p20260804001.server.GameServer`.

## Escenarios

| ID  | Descripción                                                                              | Resultado esperado                                                        |
|-----|------------------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| T01 | Inicializar el servidor con un puerto libre.                                             | El servidor queda en modo escucha en segundo plano (hilo demonio).       |
| T02 | Enviar una petición GET de un recurso existente.                                         | 200 + contenido + cabecera `Content-Type` correcta.                      |
| T03 | Enviar una petición GET de un recurso existente en una subcarpeta.                       | 200 + contenido del recurso.                                             |
| T04 | Enviar una petición GET de un recurso inexistente.                                       | 404.                                                                     |
| T05 | Intentar acceder a un recurso fuera de la carpeta raíz mediante *path traversal*.        | 403, sin servirse contenido externo.                                     |
| T06 | Intentar acceder a una ruta absoluta de sistema fuera de la carpeta raíz.                | 404 (la ruta no existe bajo la carpeta raíz).                            |
| T07 | Enviar peticiones con métodos distintos de GET (POST, PUT, DELETE).                      | 405, el recurso no es servido.                                           |
| T08 | Enviar una petición GET con *query string*.                                              | 200, la *query* es ignorada.                                             |
| T09 | Enviar una petición GET con ruta codificada.                                             | 200, la ruta es decodificada antes de resolverse.                        |
| T10 | Enviar una petición malformada.                                                          | 400.                                                                     |
| T11 | Inicializar un servidor que ya está en escucha.                                          | `IllegalStateException`.                                                 |
| T12 | Finalizar la escucha del servidor.                                                       | El servidor deja de estar en escucha y rechaza nuevas conexiones.        |

## Restricciones verificadas

- Sólo se atienden peticiones HTTP GET.
- Los contenidos servibles se limitan exclusivamente a la carpeta `webcontent/` y subcarpetas.
- Nunca se sirve contenido de `src/main/resources`, `src/test/resources` ni código Java.
- No se emplea ninguna librería externa.
