package com.rsh.p20260804001.game;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Verifica la carga, presentación y transición del vídeo de introducción. */
class IntroSceneTest
{
    private static final String INTRO_VIDEO_CANVAS_SCALE = "0.2";

    private static final Path INTRO_SCENE_JS = Path.of("webcontent/js/scene/IntroScene.js");
    private static final Path BOOTLOADER_JS = Path.of("webcontent/js/scene/BootloaderScene.js");
    private static final Path INDEX_JS = Path.of("webcontent/js/index.js");
    private static final Path INTRO_VIDEO =
            Path.of("webcontent/assets/videos/Buried-Dark-World-Intro.mp4");

    @Test
    void introAssetsExist()
    {
        assertTrue(Files.isRegularFile(INTRO_SCENE_JS), "No existe IntroScene.js");
        assertTrue(Files.isRegularFile(INTRO_VIDEO), "No existe el vídeo de introducción");
    }

    @Test
    void bootloaderLoadsIntroVideo() throws IOException
    {
        String scene = Files.readString(BOOTLOADER_JS);

        assertTrue(scene.contains("this.load.video("), "BootloaderScene no carga un vídeo");
        assertTrue(scene.contains("/assets/videos/Buried-Dark-World-Intro.mp4"),
                "BootloaderScene no carga el vídeo de introducción");
        assertTrue(scene.contains("this.nextScene = 'IntroScene'"),
                "BootloaderScene no da paso a IntroScene");
    }

    @Test
    void introFitsVideoToTwentyPercentOfCanvasDimensions() throws IOException
    {
        String scene = Files.readString(INTRO_SCENE_JS);
        String expectedSize = "setDisplaySize(this.scale.width * " + INTRO_VIDEO_CANVAS_SCALE
                + ", this.scale.height * " + INTRO_VIDEO_CANVAS_SCALE + ")";

        assertTrue(scene.contains(expectedSize),
                "IntroScene no ajusta el vídeo al 20% de las dimensiones del canvas");
        assertTrue(scene.contains("Phaser.GameObjects.Events.VIDEO_PLAYING"),
                "IntroScene no reaplica el tamaño después de inicializar el vídeo");
    }

    @Test
    void introPlaysVideoAudio() throws IOException
    {
        String scene = Files.readString(INTRO_SCENE_JS);

        assertTrue(scene.contains("this.introVideo.setMute(false)"),
                "El audio del vídeo no está habilitado");
        assertTrue(scene.contains("this.introVideo.setVolume(INTRO_VIDEO_VOLUME)"),
                "El vídeo no configura un volumen audible");
    }

    @Test
    void introCanBeSkippedWithMouseOrKeyboard() throws IOException
    {
        String scene = Files.readString(INTRO_SCENE_JS);

        assertTrue(scene.contains("this.input.once('pointerdown'"),
                "La introducción no se puede omitir con el ratón");
        assertTrue(scene.contains("this.input.keyboard.once('keydown'"),
                "La introducción no se puede omitir con el teclado");
        assertTrue(scene.contains("this.finishIntro()"),
                "Los controles no finalizan la introducción");
    }

    @Test
    void introStartsMainMenuAfterTenSeconds() throws IOException
    {
        String scene = Files.readString(INTRO_SCENE_JS);

        assertTrue(scene.contains("INTRO_DURATION_MILLISECONDS = 10000"),
                "La introducción no dura diez segundos");
        assertTrue(scene.contains("delayedCall(INTRO_DURATION_MILLISECONDS"),
                "IntroScene no programa la transición final");
        assertTrue(scene.contains("INTRO_NEXT_SCENE = 'MainMenuScene'"),
                "IntroScene no apunta al menú principal");
        assertTrue(scene.contains("this.scene.start(INTRO_NEXT_SCENE)"),
                "IntroScene no inicia el menú principal");
    }

    @Test
    void introIsRegisteredBetweenBootloaderAndMainMenu() throws IOException
    {
        String index = Files.readString(INDEX_JS);
        int bootloader = index.indexOf("BootloaderScene");
        int intro = index.indexOf("IntroScene");
        int mainMenu = index.indexOf("MainMenuScene");

        assertTrue(bootloader >= 0, "BootloaderScene no está registrada");
        assertTrue(intro > bootloader, "IntroScene no está tras BootloaderScene");
        assertTrue(mainMenu > intro, "MainMenuScene no está tras IntroScene");
    }
}
