'use strict';

const MAIN_MENU_PANEL_WIDTH = 200;
const MAIN_MENU_PANEL_HEIGHT = 580;
const MAIN_MENU_BUTTON_WIDTH = 180;
const MAIN_MENU_BUTTON_HEIGHT = 32;
const MAIN_MENU_BUTTON_TEXT_SIZE = 18;
const MAIN_MENU_FONT_NAME = 'Monospace';

const MAIN_MENU_CREDITS_FONT_SIZE = 18;
const MAIN_MENU_CREDITS_CONCEPT_FONT_MULTIPLIER = 2;
const MAIN_MENU_CREDITS_MAIN_DURATION = 15000;
const MAIN_MENU_CREDITS_CONCEPT_DURATION = 5000;
const MAIN_MENU_CREDITS_COLOR = '#ffffff';
const MAIN_MENU_CREDITS_ALIGN = 'center';
const MAIN_MENU_CREDITS_WRAP_WIDTH = 600;

const MAIN_MENU_CREDITS_MAIN_SECTIONS = `CAST
====

Frontend Programmers
----
A free AI agent specialized in programming

Backend Programmers
----
The same AI programming agent

Assets Designers
----
Two very famous general-purpose agents

Q&A Team
----
The AI agent that actively participated in the development of the frontend and backend (... and a bit of me)`;

const MAIN_MENU_CREDITS_CONCEPT_SECTION = `... and the last but not least... in fact, the most important thing of all...


Concept, design and leading
---
Me :)`;

class MainMenuScene extends Phaser.Scene {

    constructor() {
        super('MainMenuScene');
    }

    create() {
        this.drawMenu();
    }

    drawMenu() {
        this.menuPanel = new UIPanel(this, {
            id: 'MenuPanel001',
            x: 10,
            y: 10,
            width: MAIN_MENU_PANEL_WIDTH,
            height: MAIN_MENU_PANEL_HEIGHT,
            backgroundColor: 'rgba(0.8, 0.8, 0.8, 0.75)',
            borderColor: 'rgba(1.0, 1.0, 1.0, 1.0)',
            enabled: true,
            visible: true
        });

        const newGameButton = this.createMenuButton({
            id: 'MenuButtonNewGame',
            x: 10,
            y: 10,
            backgroundColor: 'rgba(0.6, 0.6, 0.6, 1.0)',
            borderColor: 'rgba(0.75, 0.75, 0.75, 1.0)',
            text: 'Nuevo juego',
            onClickFunction: () => {
                this.menuPanel.setVisible(false);
                this.scene.start('GameScene');
            }
        });
        this.menuPanel.addElement(newGameButton);

        const testUiButton = this.createMenuButton({
            id: 'MenuButtonTestUI',
            x: 10,
            y: 47,
            backgroundColor: 'rgba(0.6, 0.2, 0.2, 1.0)',
            borderColor: 'rgba(0.75, 0.1, 0.1, 1.0)',
            text: 'Test UI',
            onClickFunction: () => {
                this.scene.start('UITestScene');
            }
        });
        this.menuPanel.addElement(testUiButton);

        const creditsButton = this.createMenuButton({
            id: 'MenuButtonCredits',
            x: 10,
            y: 79,
            backgroundColor: 'rgba(0.6, 0.6, 0.6, 1.0)',
            borderColor: 'rgba(0.75, 0.75, 0.75, 1.0)',
            text: 'Créditos',
            onClickFunction: () => {
                this.showCredits();
            }
        });
        this.menuPanel.addElement(creditsButton);

        const exitButton = this.createMenuButton({
            id: 'MenuButtonExit',
            x: 10,
            y: 116,
            backgroundColor: 'rgba(0.8, 0.1, 0.1, 1.0)',
            borderColor: 'rgba(0.95, 0.05, 0.05, 1.0)',
            text: 'Salir del juego',
            onClickFunction: () => {
                this.game.destroy(true);
            }
        });
        this.menuPanel.addElement(exitButton);
    }

    createMenuButton(options) {
        return new UIButton(this, {
            id: options.id,
            x: options.x,
            y: options.y,
            width: MAIN_MENU_BUTTON_WIDTH,
            height: MAIN_MENU_BUTTON_HEIGHT,
            backgroundColor: options.backgroundColor,
            borderColor: options.borderColor,
            textColor: 'rgba(0.0, 0.0, 0.0, 1.0)',
            text: options.text,
            textSize: MAIN_MENU_BUTTON_TEXT_SIZE,
            fontName: MAIN_MENU_FONT_NAME,
            enabled: true,
            visible: true,
            onClickFunction: options.onClickFunction
        });
    }

    showCredits() {
        this.menuPanel.setVisible(false);

        this.creditsMainText = this.add.text(
            this.scale.width / 2,
            0,
            MAIN_MENU_CREDITS_MAIN_SECTIONS,
            {
                fontFamily: MAIN_MENU_FONT_NAME,
                fontSize: MAIN_MENU_CREDITS_FONT_SIZE + 'px',
                color: MAIN_MENU_CREDITS_COLOR,
                align: MAIN_MENU_CREDITS_ALIGN,
                wordWrap: { width: MAIN_MENU_CREDITS_WRAP_WIDTH }
            }
        );
        this.creditsMainText.setOrigin(0.5, 0.5);
        this.creditsMainText.setY(this.scale.height + this.creditsMainText.height / 2);

        this.tweens.add({
            targets: this.creditsMainText,
            y: -(this.creditsMainText.height / 2),
            duration: MAIN_MENU_CREDITS_MAIN_DURATION,
            onComplete: () => {
                this.creditsMainText.destroy();
                this.showConceptCredits();
            }
        });
    }

    showConceptCredits() {
        this.creditsConceptText = this.add.text(
            this.scale.width / 2,
            0,
            MAIN_MENU_CREDITS_CONCEPT_SECTION,
            {
                fontFamily: MAIN_MENU_FONT_NAME,
                fontSize: (MAIN_MENU_CREDITS_FONT_SIZE * MAIN_MENU_CREDITS_CONCEPT_FONT_MULTIPLIER) + 'px',
                color: MAIN_MENU_CREDITS_COLOR,
                align: MAIN_MENU_CREDITS_ALIGN,
                wordWrap: { width: MAIN_MENU_CREDITS_WRAP_WIDTH }
            }
        );
        this.creditsConceptText.setOrigin(0.5, 0.5);
        this.creditsConceptText.setY(this.scale.height + this.creditsConceptText.height / 2);

        this.tweens.add({
            targets: this.creditsConceptText,
            y: -(this.creditsConceptText.height / 2),
            duration: MAIN_MENU_CREDITS_CONCEPT_DURATION,
            onComplete: () => {
                this.creditsConceptText.destroy();
                this.menuPanel.setVisible(true);
            }
        });
    }
}
