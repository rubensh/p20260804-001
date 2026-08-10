# feature-0005


# Objetivo

Desarrollo de la escena de arranque del juego. Esta escena tiene como finalidad cargar los recursos mínimos necesarios para el inicio del juego.


# Estado

- [X]En proceso.
- [X]En pruebas.
- [X]Finalizado.


# Notas

- Requiere el asset: "webcontent/assets/images/SplashScreenLogo.png".


# Restricciones

*Ninguna*


# Implementación

- Crea la escena de Phaser "webcontent/js/scene/BootloaderScene.js" y añádela como primera escena al array de escenas de la configuración del juego en "webcontent/js/index.js". Elimina toda configuración de escenas anteriores.
- La escena es muy minimalista. Fondo negro, con texto y gráficos blancos.
- Mostrará en el centro de la pantalla un texto de cabecera, con fuente Monospace de 12px, el texto "Cargando..."
- Bajo el texto se mostrará una bara de progreso, balanca también, con las siguientes características:
  - Borde: rectángulo hueco de 200px x 7px con espaciado interno de 1px.
  - Barra: rectángulo relleno de 198px x 5px, enmarcado en el borde anterior, con 1px de margen.
- La barra de progreso irá creciendo a medida que se carguen los assets del juego.
- Los assets se declarán en un array dentro de la escena.
- Dichos assets deben estar disponibles para otras escenas.
- Inicialmente cargaremos el asset correspondiente a la splash screen: "webcontent/assets/images/SplashScreenLogo.png"
- Una vez cargados lo assets se mostrará el asset correspondiente a la splash screen, con un efecto de fade in de duración 2.5 segundos.
- La splash screen segurirá mostrándose durante 5 segundos, o hasta que hagamos click en la pantalla, tras lo cual se realizará un fade out de 2.5 segundos.
- Prepara la escena para declarar en una variable, qué escena debe ser mostrada tras el fade out anterior.
