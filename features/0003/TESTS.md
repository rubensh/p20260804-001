# feature-0003: Tests

Tests a implementar para la landing page (`webcontent/pages/index.html` y `webcontent/styles/landing.css`).

## Escenarios

| ID  | Descripción                                                                             | Resultado esperado                                                      |
|-----|-----------------------------------------------------------------------------------------|------------------------------------------------------------------------|
| T01 | Comprobar la existencia de los recursos de la landing.                                  | `index.html` y `landing.css` existen en sus rutas esperadas.           |
| T02 | Comprobar la referencia a la hoja de estilos desde el HTML.                             | `index.html` referencia `../styles/landing.css`.                       |
| T03 | Comprobar los elementos esenciales de accesibilidad y estructura del HTML.              | `lang`, meta `viewport`, `title`, skip-link, landmarks y un único `h1`. |
| T04 | Servir la landing page a través del servidor.                                           | GET `/pages/index.html` → 200 con `Content-Type` HTML.                 |
| T05 | Servir la hoja de estilos a través del servidor.                                        | GET `/styles/landing.css` → 200 con `Content-Type` CSS.                |

## Restricciones verificadas

- Todos los estilos residen en `landing.css` (CSS3), sin estilos en línea en el HTML.
- La página es accesible, responsive y adaptable según los requisitos de la feature.
