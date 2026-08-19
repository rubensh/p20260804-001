package com.rsh.p20260804001.game;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Verifica la música ambiental y los efectos de combate de la feature 0017. */
class AudioSceneTest
{
    private static final Path BOOTLOADER_JS = Path.of("webcontent/js/scene/BootloaderScene.js");
    private static final Path MAIN_MENU_JS = Path.of("webcontent/js/scene/MainMenuScene.js");
    private static final Path INTRO_SCENE_JS = Path.of("webcontent/js/scene/IntroScene.js");
    private static final Path GAME_SCENE_JS = Path.of("webcontent/js/scene/GameScene.js");
    private static final Path UI_TEST_SCENE_JS = Path.of("webcontent/js/scene/UITestScene.js");
    private static final Path BACKGROUND_MUSIC =
            Path.of("webcontent/assets/audio/Buried-Dark-World-Loop.wav");
    private static final Path SWORD_CLASH_SOUND =
            Path.of("webcontent/assets/audio/Sword-Clash-001.wav");
    private static final Path ENEMY_DEFEATED_SOUND =
            Path.of("webcontent/assets/audio/Goblin-Pain-001.wav");

    @Test
    void audioAssetsExist()
    {
        assertTrue(Files.isRegularFile(BACKGROUND_MUSIC), "Falta la música de fondo");
        assertTrue(Files.isRegularFile(SWORD_CLASH_SOUND), "Falta el sonido de ataque");
        assertTrue(Files.isRegularFile(ENEMY_DEFEATED_SOUND), "Falta el sonido de enemigo derrotado");
    }

    @Test
    void bootloaderLoadsEveryAudioAsset() throws IOException
    {
        String scene = Files.readString(BOOTLOADER_JS);

        assertTrue(scene.contains("/assets/audio/Buried-Dark-World-Loop.wav"));
        assertTrue(scene.contains("/assets/audio/Sword-Clash-001.wav"));
        assertTrue(scene.contains("/assets/audio/Goblin-Pain-001.wav"));
        assertTrue(scene.contains("this.load.audio(GAME_BACKGROUND_MUSIC_KEY"));
        assertTrue(scene.contains("this.load.audio(GAME_SWORD_CLASH_SOUND_KEY"));
        assertTrue(scene.contains("this.load.audio(GAME_ENEMY_DEFEATED_SOUND_KEY"));
    }

    @Test
    void backgroundMusicLoopsOutsideBootAndIntroScenes() throws IOException
    {
        String bootloader = Files.readString(BOOTLOADER_JS);
        String intro = Files.readString(INTRO_SCENE_JS);
        String mainMenu = Files.readString(MAIN_MENU_JS);
        String game = Files.readString(GAME_SCENE_JS);
        String uiTest = Files.readString(UI_TEST_SCENE_JS);

        assertTrue(bootloader.contains("{ loop: true }"), "La música no está configurada en bucle");
        assertTrue(bootloader.contains("stopGameBackgroundMusic(this)"),
                "BootloaderScene no garantiza que la música esté detenida");
        assertTrue(intro.contains("stopGameBackgroundMusic(this)"),
                "IntroScene no garantiza que la música esté detenida");
        assertTrue(mainMenu.contains("playGameBackgroundMusic(this)"));
        assertTrue(game.contains("playGameBackgroundMusic(this)"));
        assertTrue(uiTest.contains("playGameBackgroundMusic(this)"));
    }

    @Test
    void combatPlaysAttackAndEnemyDefeatedSounds() throws IOException
    {
        String scene = Files.readString(GAME_SCENE_JS);

        assertTrue(scene.contains("this.sound.play(GAME_SWORD_CLASH_SOUND_KEY)"),
                "Los ataques no reproducen el choque de espadas");
        assertTrue(scene.contains("if (enemyAhead.isDead())"),
                "No se detecta la muerte después del ataque del jugador");
        assertTrue(scene.contains("this.sound.play(GAME_ENEMY_DEFEATED_SOUND_KEY)"),
                "No se reproduce el sonido al derrotar al enemigo");
    }
}
