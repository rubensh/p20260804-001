# feature-0014: Tests

- El jugador y cada enemigo mantienen referencias independientes a sus intervalos de ataque.
- Los intervalos se crean con el retardo de cada entidad y se cancelan al romper la adyacencia, morir, terminar la partida o abandonar la escena.
- El jugador ataca automáticamente solo al enemigo situado justo delante.
- Todos los enemigos vivos situados al norte, sur, este u oeste atacan automáticamente al jugador.
- Los enemigos en combate reciben un desplazamiento visual aleatorio máximo de cinco píxeles en ambos ejes.
- Cada ataque añade al log el mensaje y el daño correspondientes.
- El log conserva y representa únicamente los cinco mensajes más recientes.
