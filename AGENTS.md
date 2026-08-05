# p20260804-001

"Buried Dark World"

----

# Objetivo

Este proyecto está creado siguiendo patrones y directrices de SDD (Specification-Driven Development) y FDD (Feature-Driven Development).

Los agentes únicamente se centrarán en implementar la última especificación existente (aquella que tenga el identificador mayor). Una vez implementado, comenzará la etapa de prueba donde se procederá a la ejecución de los tests existentes.


# Etapas

1. Especificación (desarrollador)
2. Revisión (desarrollador)
3. Código (IA)
4. Tests (IA)
5. Documentación (ID)
6. Revisión y validación (desarrollador)


# Requisitos

- Java JDK 21+.
- Maven 3.x.
- Git 2.x.
- Librería Phaser-3.x.js.
- Navegador web moderno (Firefox, Chrome, Edge, Opera, Brave).
- WSL + OpenCode.
- Eclipse IDE 2025-12+.


# Arquitectura

- Proyecto Java con Maven.
- El proyecto se denominará p20260804-001 (que será la carpeta raíz).
- Incluirá un único pom.xml en la raíz del proyecto.
- Proyecto monolítico. No hay subproyectos.
- Sigue los patrones establecidos por Maven:
  - Dentro de la carpeta raíz, crea: src/main/java, src/main/resources, src/test/java y src/test/resources, que serán carpetas fuente (source folders) de Eclipse.
  - También a partir de la carpeta raíz, crea las carpetas webcontent/assets, webcontent/pages, webcontent/styles, webcontent/js y Docs/.