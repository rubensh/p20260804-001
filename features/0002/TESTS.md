# feature-0002: Tests

Tests a implementar para la clase `com.rsh.p20260804001.launcher.Launcher`.

## Escenarios

| ID  | Descripción                                                                             | Resultado esperado                                                        |
|-----|-----------------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| T01 | El launcher por defecto se instancia con el puerto 8088.                                | `getPort()` devuelve 8088.                                               |
| T02 | Arrancar el launcher con un puerto libre.                                               | El servidor queda en escucha en segundo plano y responde peticiones GET. |
| T03 | Detener el servidor tras el arranque.                                                   | El hilo de ejecución del launcher finaliza.                              |
| T04 | Lanzar la aplicación y enviar una señal de terminación (CTRL-C / cierre de terminal).   | El proceso finaliza correctamente liberando el puerto y los recursos.    |

## Verificaciones adicionales

- El `pom.xml` declara el plugin `org.apache.maven.plugins:maven-jar-plugin`.
- El manifiesto generado contiene `Main-Class: com.rsh.p20260804001.launcher.Launcher`.
- El manifiesto generado contiene la entrada `Created-By: Rubén Santana Hernández`.
- La aplicación es lanzable en terminal mediante comando Maven (`mvn package` + `java -jar`).
