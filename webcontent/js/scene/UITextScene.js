'use strict';

const UI_TEXT_PANEL_WIDTH = 300;
const UI_TEXT_PANEL_HEIGHT = 400;
const UI_TEXT_DISABLED_MILLIS = 2500;
const UI_TEXT_HIDDEN_MILLIS = 2500;

class UITextScene extends Phaser.Scene {

    constructor() {
        super('UITextScene');
    }

    create() {
        this.drawTestUi();
    }

    drawTestUi() {
        const centerX = this.scale.width / 2;
        const centerY = this.scale.height / 2;

        this.testPanel = new UIPanel(this, {
            id: 'testPanel001',
            x: Math.round(centerX - UI_TEXT_PANEL_WIDTH / 2),
            y: Math.round(centerY - UI_TEXT_PANEL_HEIGHT / 2),
            width: UI_TEXT_PANEL_WIDTH,
            height: UI_TEXT_PANEL_HEIGHT,
            backgroundColor: 'rgba(0.5, 0.5, 0.5, 1.0)',
            enabled: true,
            visible: true
        });

        const testButton = new UIButton(this, {
            id: 'testButton001',
            x: 10,
            y: 10,
            width: 280,
            height: 32,
            backgroundColor: 'rgba(0.75, 0.50, 0.25, 1.0)',
            borderColor: 'rgba(0.8, 0.6, 0.4, 1.0)',
            textColor: 'rgba(0.0, 0.0, 0.0, 1.0)',
            text: 'Prueba',
            textSize: 12,
            fontName: 'Monospace',
            enabled: true,
            visible: true,
            onClickFunction: () => {
                console.log('El elemento con ID "' + testButton.id + '" ha recibido el evento "onClick".');
                this.runPanelAnimation();
            }
        });

        this.testPanel.addElement(testButton);
        this.testButton = testButton;
    }

    runPanelAnimation() {
        this.testPanel.setEnabled(false);

        setTimeout(() => {
            this.testPanel.setVisible(false);
        }, UI_TEXT_DISABLED_MILLIS);

        setTimeout(() => {
            this.testPanel.setEnabled(true);
            this.testPanel.setVisible(true);
        }, UI_TEXT_DISABLED_MILLIS + UI_TEXT_HIDDEN_MILLIS);
    }
}
