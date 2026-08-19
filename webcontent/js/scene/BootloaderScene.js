'use strict';

const BOOTLOADER_INTRO_VIDEO_KEY = 'BuriedDarkWorldIntro';
const BOOTLOADER_INTRO_VIDEO_FILE = '/assets/videos/Buried-Dark-World-Intro.mp4';
const GAME_BACKGROUND_MUSIC_KEY = 'BuriedDarkWorldLoop';
const GAME_BACKGROUND_MUSIC_FILE = '/assets/audio/Buried-Dark-World-Loop.wav';
const GAME_SWORD_CLASH_SOUND_KEY = 'SwordClash001';
const GAME_SWORD_CLASH_SOUND_FILE = '/assets/audio/Sword-Clash-001.wav';
const GAME_ENEMY_DEFEATED_SOUND_KEY = 'GoblinPain001';
const GAME_ENEMY_DEFEATED_SOUND_FILE = '/assets/audio/Goblin-Pain-001.wav';

function playGameBackgroundMusic(scene) {
    let backgroundMusic = scene.sound.get(GAME_BACKGROUND_MUSIC_KEY);
    if (!backgroundMusic) {
        backgroundMusic = scene.sound.add(GAME_BACKGROUND_MUSIC_KEY, { loop: true });
    }
    if (!backgroundMusic.isPlaying) {
        backgroundMusic.play();
    }
}

function stopGameBackgroundMusic(scene) {
    const backgroundMusic = scene.sound.get(GAME_BACKGROUND_MUSIC_KEY);
    if (backgroundMusic && backgroundMusic.isPlaying) {
        backgroundMusic.stop();
    }
}

class BootloaderScene extends Phaser.Scene {

    constructor() {
        super('BootloaderScene');

        this.assets = [
            { key: 'SplashScreenLogo', file: '/assets/images/SplashScreenLogo.png' }
        ];

        this.nextScene = 'IntroScene';
        this.transitioning = false;
    }

    create() {
        stopGameBackgroundMusic(this);
        this.cameras.main.setBackgroundColor('#000000');

        this.drawLoadingUi();
        this.registerLoadEvents();
        this.startLoading();
    }

    drawLoadingUi() {
        const centerX = this.scale.width / 2;
        const centerY = this.scale.height / 2;

        this.loadingText = this.add.text(centerX, centerY, 'Cargando...', {
            fontFamily: 'Monospace',
            fontSize: '12px',
            color: '#ffffff'
        }).setOrigin(0.5);

        this.progressBorder = this.add.rectangle(centerX, centerY + 20, 200, 7)
            .setStrokeStyle(1, 0xffffff);

        this.progressBar = this.add.rectangle(centerX - 99, centerY + 20, 1, 5, 0xffffff)
            .setOrigin(0, 0.5);
    }

    registerLoadEvents() {
        this.load.on('progress', progress => {
            this.progressBar.width = Math.round(198 * progress);
        });

        this.load.on('complete', () => {
            this.showSplashScreen();
        });
    }

    startLoading() {
        this.assets.forEach(asset => {
            this.load.image(asset.key, asset.file);
        });
        this.load.video(
            BOOTLOADER_INTRO_VIDEO_KEY,
            BOOTLOADER_INTRO_VIDEO_FILE,
            'loadeddata',
            false,
            false
        );
        this.load.audio(GAME_BACKGROUND_MUSIC_KEY, GAME_BACKGROUND_MUSIC_FILE);
        this.load.audio(GAME_SWORD_CLASH_SOUND_KEY, GAME_SWORD_CLASH_SOUND_FILE);
        this.load.audio(GAME_ENEMY_DEFEATED_SOUND_KEY, GAME_ENEMY_DEFEATED_SOUND_FILE);
        this.load.start();
    }

    showSplashScreen() {
        this.loadingText.destroy();
        this.progressBorder.destroy();
        this.progressBar.destroy();

        this.splashImage = this.add.image(this.scale.width / 2, this.scale.height / 2, 'SplashScreenLogo')
            .setAlpha(0);

        this.tweens.add({
            targets: this.splashImage,
            alpha: 1,
            duration: 2500,
            onComplete: () => {
                this.holdSplashScreen();
            }
        });
    }

    holdSplashScreen() {
        this.time.delayedCall(5000, () => {
            this.fadeOutSplashScreen();
        });

        this.input.once('pointerdown', () => {
            this.fadeOutSplashScreen();
        });
    }

    fadeOutSplashScreen() {
        if (this.transitioning) {
            return;
        }
        this.transitioning = true;

        this.tweens.add({
            targets: this.splashImage,
            alpha: 0,
            duration: 2500,
            onComplete: () => {
                this.scene.start(this.nextScene);
            }
        });
    }
}
