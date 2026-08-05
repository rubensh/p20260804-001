'use strict';

const game = new Phaser.Game({
    type: Phaser.AUTO,
    parent: 'game',
    width: 800,
    height: 600,
    backgroundColor: '#000000',
    scene: {
        create: function () {
            this.add.text(400, 300, 'Buried Dark World', {
                fontFamily: 'Arial, sans-serif',
                fontSize: '36px',
                color: '#ffffff'
            }).setOrigin(0.5);
        }
    }
});
