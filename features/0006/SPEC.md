# feature-0006


# Objetivo

Desarrollo de elementos de UI:

- Botón.
- Panel.


# Estado

- [X]En proceso.
- [X]En pruebas.
- [X]Finalizado.


# Notas

- [rev1]: this.container.setOrigin is not a function: createGameObjects, http://localhost:8088/js/ui/UIPanel.js:47
- [rev2]: los eventos asociados al botón no responden a las coordenadas correctas. Parece que son coordenadas absolutas en lugar de coordenadas relativas al contenedor "testPanel001".


# Restricciones

*Ninguna*


# Implementación

- Implementa la clase "webcontent/js/ui/UIElement.js" que será la clase base de la que extienden el resto de elementos de UI del proyecto.
  - Tiene las propiedades:
    - id.
    - parentElement.
  - El id es una string con un nombre "único" (no se comprobará) que identifica al elemento.
  - El parentElement es una referencia al contenedor de este elemento. Si no tiene, será "null".
- Implementa la clase "webcontent/js/ui/UIButton.js". Esta clase extiende de UIElement, y su función es la de instanciar un elemento botón dentro de una escena de Phaser.
  - Tiene las propiedades:
    - x, y.
    - width, height.
    - backgroundColor.
    - borderColor.
    - textColor.
    - text.
    - textSize.
    - fontName.
    - enabled.
    - visible.
    - onClickFunction.
  - Las coordenadas siempre vienen en píxeles.
  - x, y son las coordenadas de la posición superior izquierda del botón, Y SON RELATIVAS AL CONTENEDOR. Si no están sujetas a ningún contenedor, se considererán absolutas al canvas.
  - width, height son respectivamente, el ancho y alto del botón.
  - backgroundColor, borderColor y textColor son los colores que se aplican al fondo, borde y texto respectivamente.
  - text, textSize y fontName aplican al texto que mostrará el botón y son el propio texto, el tamaño en píxeles del texto y en nombre de la fuente que se usará.
  - enabled indicará si el botón está activo, o inactivo. En caso de inactividad se mostrará en escala de grises y no tendrá eventos asociados (resaltado, clic, ...).
  - visible hace que el botón se muestre en la escena si está a true, si no, no se tendrá en cuenta en el renderizado.
  - onClickFunction contendrá el código Javascript a ejecutar en caso de hacer clic sobre el botón.
  - Al pasar el cursor sobre el botón, se aplicará un efecto de iluminación sobre el botón para resaltarlo.
- Implementa la clase "webcontent/js/ui/UIPanel.js". Esta clase extiende de UIElement, y su función es la de instanciar un elemento panel dentro de una escena de Phaser. Funciona a modo de contenedor.
  - Tiene las propiedades:
    - x, y.
    - width, height.
    - backgroundColor.
    - borderColor.
    - enabled.
    - visible.
    - elements.
  - Las coordenadas siempre vienen en píxeles.
  - x, y son las coordenadas de la posición superior izquierda del panel, Y SON RELATIVAS AL CONTENEDOR. Si no están sujetas a ningún contenedor, se considererán absolutas al canvas.
  - width, height son respectivamente, el ancho y alto del panel.
  - backgroundColor y borderColor son los colores que se aplican al fondo y borde respectivamente.
  - enabled indicará si el panel está activo, o inactivo. En caso de inactividad se mostrarán en escala de grises este elemento y sus elementos contenidos, y no tendrán eventos asociados (resaltado, clic, ...).
  - visible hace que el panel, y sus elementos contenidos, se muestren en la escena si está a true, si no, no se tendrán en cuenta en el renderizado.
- Crea una escena para probar los controles de UI:
  - La ubicación será "webcontent/js/scene/UITestScene.js". Añádelo a la carga en "webcontent/pages/index.html".
  - Incluye la clase en el array de escenas de "webcontent/js/index.js", tras BootloaderScene.
  - La escena va a incluir un panel centrado en la pantalla (en vertical y horizontal).
    - Id: testPanel001.
    - Ancho: 300 píxels.
    - Alto: 400 píxels.
    - Color: rgba(0.5, 0.5, 0.5, 1.0).
    - Visible: sí.
    - Habilitado: sí.
    - Elementos:
      - Botón "Prueba", centrado en el panel (sólo en horizontal):
        - Id: testButton001.
        - x: 10 píxeles.
        - y: 10 píxeles.
        - width: 280 píxeles.
        - height: 32 píxeles.
        - backgroundColor: rgba(0.75, 0.50, 0.25, 1.0).
        - borderColor: rgba(0.8, 0.6, 0.4, 1.0).
        - textColor: rgba(0.0, 0.0, 0.0, 1.0).
        - textSize: 12.
        - fontName: Monospace.
        - enabled: true.
        - visible: true.
        - onClickFunction: asignarás una función que muestre en consola un texto diciendo que el elemento con ID (el que corresponda) ha recibido el evento "onClick". Además, pondrás el elemento "testPanel001" enabled = false durante 2.5 segundos, luego visible = false, durante 2.5 segundos y luego enabled y visible = true. Usa setTimeout().
