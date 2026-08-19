'use strict';

const INTRO_SCENE_KEY = 'IntroScene';
const INTRO_VIDEO_KEY = 'BuriedDarkWorldIntro';
const INTRO_NEXT_SCENE = 'MainMenuScene';
const INTRO_DURATION_MILLISECONDS = 10000;
const INTRO_CENTER_ORIGIN = 0.5;
const INTRO_VIDEO_VOLUME = 1;

class IntroScene extends Phaser.Scene {

    constructor() {
        super(INTRO_SCENE_KEY);
        this.transitioning = false;
    }

    create() {
        stopGameBackgroundMusic(this);
        this.cameras.main.setBackgroundColor('#000000');
        this.introVideo = this.add.video(
            this.scale.width,
            this.scale.height,
            INTRO_VIDEO_KEY
        );
        this.introVideo.setOrigin(1, 1.5);
        this.fitVideoToCanvas();
        this.introVideo.setMute(false);
        this.introVideo.setVolume(INTRO_VIDEO_VOLUME);
        this.introVideo.once(Phaser.GameObjects.Events.VIDEO_PLAYING, () => {
            this.fitVideoToCanvas();
        });
        this.introVideo.play(false);

        this.input.once('pointerdown', () => {
            this.finishIntro();
        });
        this.input.keyboard.once('keydown', () => {
            this.finishIntro();
        });

        this.time.delayedCall(INTRO_DURATION_MILLISECONDS, () => {
            this.finishIntro();
        });
    }

    fitVideoToCanvas() {
        this.introVideo.setDisplaySize(this.scale.width * 0.2, this.scale.height * 0.2);
    }

    finishIntro() {
        if (this.transitioning) {
            return;
        }
        this.transitioning = true;
        this.introVideo.stop();
        this.scene.start(INTRO_NEXT_SCENE);
    }
}
