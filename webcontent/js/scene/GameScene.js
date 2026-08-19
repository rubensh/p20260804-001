'use strict';

const GAME_PLAYER_ID = 'Player001';
const GAME_PLAYER_LEVEL = 0;
const GAME_PLAYER_FACING_NORTH = 'N';
const GAME_PLAYER_HEALTH = 100;
const GAME_PLAYER_MIN_DAMAGE = 1;
const GAME_PLAYER_MAX_DAMAGE = 6;
const GAME_PLAYER_MIN_DEFENSE = 0;
const GAME_PLAYER_MAX_DEFENSE = 3;
const GAME_ENEMY_COUNT = 200;
const GAME_ENEMY_ALIVE_HEALTH_THRESHOLD = 0;
const GAME_ENEMY_ADJACENT_DISTANCE = 1;
const GAME_ENEMY_TEXTURE_PREFIX = 'Goblin-00';
const GAME_BOSS_TEXTURE = 'Orc-001';
const GAME_POTION_TEXTURE = 'Potion001';
const GAME_POTION_FAR_SCALE = 0.25;
const GAME_POTION_MIDDLE_SCALE = 0.5;
const GAME_POTION_NEAR_SCALE = 1.0;
const GAME_POTION_FAR_VERTICAL_OFFSET = 60;
const GAME_POTION_MIDDLE_VERTICAL_OFFSET = 80;
const GAME_POTION_NEAR_VERTICAL_OFFSET = 100;
const GAME_POTION_FAR_ILLUMINATION = 0.5;
const GAME_POTION_MIDDLE_ILLUMINATION = 0.8;
const GAME_POTION_NEAR_ILLUMINATION = 1.0;
const GAME_ATTACK_DELAY_MILLISECONDS = 1000;
const GAME_COMBAT_JITTER_PIXELS = 5;
const GAME_COMBAT_JITTER_RANGE = GAME_COMBAT_JITTER_PIXELS * 2 + 1;
const GAME_LOG_MAX_MESSAGES = 5;
const GAME_LOG_PADDING = 10;
const GAME_LOG_TEXT_COLOR = '#ffffff';
const GAME_LOG_TEXT_SIZE = '16px';
const GAME_LOG_FONT_FAMILY = 'Monospace';
const GAME_LOG_ENEMY_ATTACK_PREFIX = 'El goblin realiza un ataque con ';
const GAME_LOG_PLAYER_ATTACK_PREFIX = 'Golpeas al goblin con un ataque de ';
const GAME_LOG_BOSS_ATTACK_PREFIX = 'El orco realiza un ataque con ';
const GAME_LOG_PLAYER_BOSS_ATTACK_PREFIX = 'Golpeas al orco con un ataque de ';
const GAME_LOG_DAMAGE_SUFFIX = ' de daño';
const GAME_OVER_HEALTH_THRESHOLD = 0;
const GAME_OVER_DELAY_MILLISECONDS = 5000;
const GAME_OVER_TEXT = 'Game Over';
const GAME_OVER_TEXT_COLOR = '#ffffff';
const GAME_OVER_TEXT_SIZE = '48px';
const GAME_OVER_FONT_FAMILY = 'Monospace';
const GAME_OVER_TEXT_DEPTH = 1;
const GAME_OVER_TEXT_ORIGIN = 0.5;
const GAME_MAIN_MENU_SCENE = 'MainMenuScene';
const GAME_VICTORY_DELAY_MILLISECONDS = 5000;
const GAME_VICTORY_TEXT = 'You win!!!';

const GAME_FOV_X = 10;
const GAME_FOV_Y = 10;
const GAME_FOV_WIDTH = 380;
const GAME_FOV_HEIGHT = 390;
const GAME_FOV_FAR_SCALE = 0.2;
const GAME_FOV_MIDDLE_SCALE = 0.4;
const GAME_FOV_NEAR_SCALE = 0.8;
const GAME_FOV_FAR_DARKNESS = 0.5;
const GAME_FOV_MIDDLE_DARKNESS = 0.25;
const GAME_FOV_NEAR_DARKNESS = 0;
const GAME_FOV_FAR_DEPTH = 3;
const GAME_FOV_MIDDLE_DEPTH = 2;
const GAME_FOV_NEAR_DEPTH = 1;
const GAME_FOV_FAR_RADIUS = 2;
const GAME_FOV_MIDDLE_RADIUS = 1;
const GAME_FOV_NEAR_RADIUS = 0;
const GAME_FOV_FAR_PERSPECTIVE_RADIUS = 2;
const GAME_FOV_MIDDLE_PERSPECTIVE_RADIUS = 2;
const GAME_FOV_NEAR_PERSPECTIVE_RADIUS = 2;
const GAME_FOV_ALIGN_TO_FIELD_EDGE = true;
const GAME_FOV_OUTER_PERSPECTIVE_OFFSET_FACTOR = 0.5;
const GAME_FOV_NEAR_OUTER_PERSPECTIVE_OFFSET_FACTOR = 0.25;
const GAME_FOV_FAR_VERTICAL_OFFSET = 16;
const GAME_FOV_MIDDLE_VERTICAL_OFFSET = 8;
const GAME_FOV_NEAR_VERTICAL_OFFSET = 4;
const GAME_FOV_PLAYER_VERTICAL_OFFSET = 4;
const GAME_FOV_PERSPECTIVE_SCALE = 0.5;
const GAME_FOV_WALL_SOURCE_SIZE = 256;
const GAME_FOV_PERSPECTIVE_SOURCE_WIDTH = 128;
const GAME_FOV_PERSPECTIVE_SOURCE_HEIGHT = 512;
const GAME_FOV_PLAYER_DEPTH = 0;
const GAME_FOV_PLAYER_SIDE_DISTANCE = 1;
const GAME_FOV_FORWARD_DEPTH = 1;
const GAME_FOV_LEFT_LATERAL = -1;
const GAME_FOV_RIGHT_LATERAL = 1;
const GAME_FOV_NORTH_EXCLUDED_X = 2;
const GAME_FOV_NORTH_EXCLUDED_Y = -1;
const GAME_STAIRS_FAR_SCALE = 0.25;
const GAME_STAIRS_MIDDLE_SCALE = 0.5;
const GAME_STAIRS_NEAR_SCALE = 1.0;
const GAME_STAIRS_FAR_VERTICAL_OFFSET = 14;
const GAME_STAIRS_MIDDLE_VERTICAL_OFFSET = 2;
const GAME_STAIRS_NEAR_VERTICAL_OFFSET = 0;
const GAME_STAIRS_FAR_ILLUMINATION = 0.25;
const GAME_STAIRS_MIDDLE_ILLUMINATION = 0.5;
const GAME_STAIRS_NEAR_ILLUMINATION = 1.0;
const GAME_ENEMY_FAR_SCALE = 0.25;
const GAME_ENEMY_MIDDLE_SCALE = 0.5;
const GAME_ENEMY_NEAR_SCALE = 1.0;
const GAME_ENEMY_FAR_VERTICAL_OFFSET = -160;
const GAME_ENEMY_MIDDLE_VERTICAL_OFFSET = -128;
const GAME_ENEMY_NEAR_VERTICAL_OFFSET = -64;
const GAME_ENEMY_FAR_ILLUMINATION = 0.25;
const GAME_ENEMY_MIDDLE_ILLUMINATION = 0.5;
const GAME_ENEMY_NEAR_ILLUMINATION = 1.0;
const GAME_BOSS_FAR_SCALE = 0.25;
const GAME_BOSS_MIDDLE_SCALE = 0.5;
const GAME_BOSS_NEAR_SCALE = 1.0;
const GAME_BOSS_FAR_VERTICAL_OFFSET = -160;
const GAME_BOSS_MIDDLE_VERTICAL_OFFSET = -128;
const GAME_BOSS_NEAR_VERTICAL_OFFSET = -64;
const GAME_BOSS_FAR_ILLUMINATION = 0.25;
const GAME_BOSS_MIDDLE_ILLUMINATION = 0.5;
const GAME_BOSS_NEAR_ILLUMINATION = 1.0;
const GAME_COLOR_CHANNEL_MAX = 255;
const GAME_COLOR_GREEN_MULTIPLIER = 0x100;
const GAME_COLOR_RED_MULTIPLIER = 0x10000;

const GAME_HUD_X = 400;
const GAME_HUD_WIDTH = 390;
const GAME_HEALTH_Y = 10;
const GAME_HEALTH_WIDTH = 310;
const GAME_HEALTH_HEIGHT = 40;
const GAME_HEALTH_BORDER_WIDTH = 2;
const GAME_HEALTH_INNER_MARGIN = 1;
const GAME_HEALTH_BORDER_COLOR = 0xff0000;
const GAME_HEALTH_FILL_COLOR = 0x660000;
const GAME_MENU_X = 720;
const GAME_MENU_Y = 10;
const GAME_MENU_WIDTH = 70;
const GAME_MENU_HEIGHT = 40;
const GAME_MINIMAP_Y = 60;
const GAME_MINIMAP_HEIGHT = 340;
const GAME_LOG_X = 10;
const GAME_LOG_Y = 410;
const GAME_LOG_WIDTH = 780;
const GAME_LOG_HEIGHT = 180;
const GAME_PANEL_BORDER_COLOR = 0x777777;
const GAME_PANEL_FILL_COLOR = 0x000000;
const GAME_MINIMAP_HIDDEN_COLOR = 0x888888;
const GAME_MINIMAP_WALL_COLOR = 0x333333;
const GAME_MINIMAP_VISITABLE_COLOR = 0x000000;
const GAME_MINIMAP_PLAYER_COLOR = 0xff0000;
const GAME_MINIMAP_STAIRCASE_DOWN_COLOR = 0xff0000;
const GAME_MINIMAP_STAIRCASE_UP_COLOR = 0x0000ff;
const GAME_MINIMAP_PLAYER_MARGIN = 1;
const GAME_CENTER_DIVISOR = 2;
const GAME_LEVEL_LABEL_X = 410;
const GAME_LEVEL_LABEL_Y = 68;
const GAME_LEVEL_TEXT_COLOR = '#ffffff';

const GAME_ASSETS = [
    { key: 'Background001', file: '/assets/images/Background001.png' },
    { key: 'Background002', file: '/assets/images/Background002.png' },
    { key: 'Wall001', file: '/assets/images/Wall001.png' },
    { key: 'Wall002', file: '/assets/images/Wall002.png' },
    { key: 'Wall003', file: '/assets/images/Wall003.png' },
    { key: 'Wall-Persp001', file: '/assets/images/Wall-Persp001.png' },
    { key: 'Wall-Persp002', file: '/assets/images/Wall-Persp002.png' },
    { key: 'Wall-Persp003', file: '/assets/images/Wall-Persp003.png' },
    { key: 'Stairs-Up', file: '/assets/images/Stairs-Up.png' },
    { key: 'Stairs-Down', file: '/assets/images/Stairs-Down.png' },
    { key: 'Goblin-001', file: '/assets/images/Goblin-001.png' },
    { key: 'Goblin-002', file: '/assets/images/Goblin-002.png' },
    { key: 'Goblin-003', file: '/assets/images/Goblin-003.png' },
    { key: GAME_BOSS_TEXTURE, file: '/assets/images/Orc-001.png' },
    { key: GAME_POTION_TEXTURE, file: '/assets/images/Potion001.png' }
];

class GameScene extends Phaser.Scene {

    constructor() {
        super('GameScene');
    }

    preload() {
        GAME_ASSETS.forEach(asset => {
            this.load.image(asset.key, asset.file);
        });
    }

    create() {
        this.cameras.main.setBackgroundColor('#000000');
        this.maze = new Maze();
        this.maze.generateMaze(GAME_ENEMY_COUNT);
        this.enemies = this.maze.enemies;
        this.boss = this.maze.boss;
        this.potions = this.maze.potions;
        this.gameOver = false;
        this.combatMessages = [];
        this.playerAttackTargetId = null;

        const center = this.maze.getCenter(this.maze.width, this.maze.height);
        this.player = new Player(
            GAME_PLAYER_ID,
            center.x,
            center.y,
            GAME_PLAYER_LEVEL,
            this.maze,
            GAME_PLAYER_FACING_NORTH,
            GAME_PLAYER_HEALTH,
            GAME_PLAYER_MIN_DAMAGE,
            GAME_PLAYER_MAX_DAMAGE,
            GAME_PLAYER_MIN_DEFENSE,
            GAME_PLAYER_MAX_DEFENSE
        );

        this.createHud();
        this.registerKeyboardControls();
        this.refreshView();
        this.resolveCombat();
        this.events.once(Phaser.Scenes.Events.SHUTDOWN, () => this.stopCombatIntervals());
    }

    createHud() {
        this.fieldOfViewGraphics = this.add.graphics();
        this.fieldOfViewGraphics.lineStyle(1, GAME_PANEL_BORDER_COLOR);
        this.fieldOfViewGraphics.strokeRect(GAME_FOV_X, GAME_FOV_Y, GAME_FOV_WIDTH, GAME_FOV_HEIGHT);
        this.fieldOfViewMaskGraphics = this.make.graphics({ add: false });
        this.fieldOfViewMaskGraphics.fillStyle(0xffffff, 1);
        this.fieldOfViewMaskGraphics.fillRect(GAME_FOV_X, GAME_FOV_Y, GAME_FOV_WIDTH, GAME_FOV_HEIGHT);

        this.healthGraphics = this.add.graphics();
        this.minimapGraphics = this.add.graphics();
        this.logGraphics = this.add.graphics();
        this.logGraphics.fillStyle(GAME_PANEL_FILL_COLOR, 1);
        this.logGraphics.fillRect(GAME_LOG_X, GAME_LOG_Y, GAME_LOG_WIDTH, GAME_LOG_HEIGHT);
        this.logGraphics.lineStyle(1, GAME_PANEL_BORDER_COLOR);
        this.logGraphics.strokeRect(GAME_LOG_X, GAME_LOG_Y, GAME_LOG_WIDTH, GAME_LOG_HEIGHT);
        this.logText = this.add.text(
            GAME_LOG_X + GAME_LOG_PADDING,
            GAME_LOG_Y + GAME_LOG_PADDING,
            '',
            {
                fontFamily: GAME_LOG_FONT_FAMILY,
                fontSize: GAME_LOG_TEXT_SIZE,
                color: GAME_LOG_TEXT_COLOR,
                wordWrap: { width: GAME_LOG_WIDTH - GAME_LOG_PADDING * 2 }
            }
        );

        this.levelLabel = this.add.text(GAME_LEVEL_LABEL_X, GAME_LEVEL_LABEL_Y, '', {
            fontFamily: 'Monospace',
            fontSize: '12px',
            color: GAME_LEVEL_TEXT_COLOR
        });

        this.menuButton = new UIButton(this, {
            id: 'GameButtonMenu',
            x: GAME_MENU_X,
            y: GAME_MENU_Y,
            width: GAME_MENU_WIDTH,
            height: GAME_MENU_HEIGHT,
            backgroundColor: 'rgba(0.6, 0.6, 0.6, 1.0)',
            borderColor: 'rgba(0.75, 0.75, 0.75, 1.0)',
            textColor: 'rgba(0.0, 0.0, 0.0, 1.0)',
            text: 'Menú',
            textSize: 14,
            fontName: 'Monospace',
            enabled: true,
            visible: true,
            onClickFunction: () => {
                this.scene.start(GAME_MAIN_MENU_SCENE);
            }
        });
    }

    registerKeyboardControls() {
        this.input.keyboard.on('keydown-UP', () => {
            this.performPlayerAction(() => this.player.moveForward());
        });
        this.input.keyboard.on('keydown-DOWN', () => {
            this.performPlayerAction(() => this.player.moveBackward());
        });
        this.input.keyboard.on('keydown-LEFT', () => {
            this.performPlayerAction(() => this.player.rotateLeft());
        });
        this.input.keyboard.on('keydown-RIGHT', () => {
            this.performPlayerAction(() => this.player.rotateRight());
        });
    }

    performPlayerAction(action) {
        if (this.gameOver) {
            return;
        }
        action();
        this.consumePotionAtPlayerPosition();
        this.resolveCombat();
        this.refreshView();
        if (this.player.health <= GAME_OVER_HEALTH_THRESHOLD) {
            this.showGameOver();
        }
    }

    showGameOver() {
        if (this.gameOver) {
            return;
        }
        this.gameOver = true;
        this.stopCombatIntervals();
        this.gameOverText = this.add.text(
            this.scale.width / GAME_CENTER_DIVISOR,
            this.scale.height / GAME_CENTER_DIVISOR,
            GAME_OVER_TEXT,
            {
                fontFamily: GAME_OVER_FONT_FAMILY,
                fontSize: GAME_OVER_TEXT_SIZE,
                color: GAME_OVER_TEXT_COLOR
            }
        );
        this.gameOverText.setOrigin(GAME_OVER_TEXT_ORIGIN);
        this.gameOverText.setDepth(GAME_OVER_TEXT_DEPTH);
        this.time.delayedCall(GAME_OVER_DELAY_MILLISECONDS, () => {
            this.scene.start(GAME_MAIN_MENU_SCENE);
        });
    }

    showVictory() {
        if (this.gameOver) {
            return;
        }
        this.gameOver = true;
        this.stopCombatIntervals();
        this.victoryText = this.add.text(
            this.scale.width / GAME_CENTER_DIVISOR,
            this.scale.height / GAME_CENTER_DIVISOR,
            GAME_VICTORY_TEXT,
            {
                fontFamily: GAME_OVER_FONT_FAMILY,
                fontSize: GAME_OVER_TEXT_SIZE,
                color: GAME_OVER_TEXT_COLOR
            }
        );
        this.victoryText.setOrigin(GAME_OVER_TEXT_ORIGIN);
        this.victoryText.setDepth(GAME_OVER_TEXT_DEPTH);
        this.time.delayedCall(GAME_VICTORY_DELAY_MILLISECONDS, () => {
            this.scene.start(GAME_MAIN_MENU_SCENE);
        });
    }

    resolveCombat() {
        this.synchronizeCombatIntervals();
    }

    synchronizeCombatIntervals() {
        if (this.gameOver) {
            this.stopCombatIntervals();
            return;
        }

        const target = this.player.getForwardCell();
        const enemyAhead = this.findOpponentAt(target.x, target.y);
        const targetChanged = enemyAhead
            ? this.playerAttackTargetId !== enemyAhead.id
            : this.playerAttackTargetId !== null;
        if (targetChanged) {
            this.stopPlayerCombat();
        }
        if (enemyAhead && this.player.attackInterval === null) {
            this.playerAttackTargetId = enemyAhead.id;
            this.player.attackInterval = setInterval(
                () => this.executePlayerAttack(enemyAhead),
                this.player.attackDelay * GAME_ATTACK_DELAY_MILLISECONDS
            );
        }

        this.enemies.forEach(enemy => {
            if (this.isEnemyAdjacent(enemy)) {
                if (enemy.attackInterval === null) {
                    enemy.attackInterval = setInterval(
                        () => this.executeEnemyAttack(enemy),
                        enemy.attackDelay * GAME_ATTACK_DELAY_MILLISECONDS
                    );
                }
            } else {
                this.stopEnemyCombat(enemy);
            }
        });
        if (this.boss !== null) {
            if (this.isEnemyAdjacent(this.boss)) {
                if (this.boss.attackInterval === null) {
                    this.boss.attackInterval = setInterval(
                        () => this.executeEnemyAttack(this.boss),
                        this.boss.attackDelay * GAME_ATTACK_DELAY_MILLISECONDS
                    );
                }
            } else {
                this.stopEnemyCombat(this.boss);
            }
        }
    }

    executePlayerAttack(enemyAhead) {
        const target = this.player.getForwardCell();
        if (this.gameOver || enemyAhead.isDead()
            || enemyAhead.level !== this.player.level
            || enemyAhead.x !== target.x || enemyAhead.y !== target.y) {
            this.synchronizeCombatIntervals();
            return;
        }

        const damage = this.player.attack(enemyAhead);
        const attackPrefix = this.isBoss(enemyAhead)
            ? GAME_LOG_PLAYER_BOSS_ATTACK_PREFIX
            : GAME_LOG_PLAYER_ATTACK_PREFIX;
        this.addCombatLog(attackPrefix + damage + GAME_LOG_DAMAGE_SUFFIX);
        this.removeDeadEnemies();
        this.synchronizeCombatIntervals();
        this.refreshView();
    }

    executeEnemyAttack(enemy) {
        if (this.gameOver || !this.isEnemyAdjacent(enemy)) {
            this.stopEnemyCombat(enemy);
            return;
        }

        const damage = enemy.attack(this.player);
        const attackPrefix = this.isBoss(enemy)
            ? GAME_LOG_BOSS_ATTACK_PREFIX
            : GAME_LOG_ENEMY_ATTACK_PREFIX;
        this.addCombatLog(attackPrefix + damage + GAME_LOG_DAMAGE_SUFFIX);
        this.refreshView();
        if (this.player.health <= GAME_OVER_HEALTH_THRESHOLD) {
            this.showGameOver();
        }
    }

    isEnemyAdjacent(enemy) {
        return !enemy.isDead()
            && enemy.level === this.player.level
            && Math.abs(enemy.x - this.player.x) + Math.abs(enemy.y - this.player.y)
                === GAME_ENEMY_ADJACENT_DISTANCE;
    }

    isBoss(enemy) {
        return this.boss !== null && enemy === this.boss;
    }

    stopPlayerCombat() {
        if (this.player && this.player.attackInterval !== null) {
            clearInterval(this.player.attackInterval);
            this.player.attackInterval = null;
        }
        this.playerAttackTargetId = null;
    }

    stopEnemyCombat(enemy) {
        if (enemy.attackInterval !== null) {
            clearInterval(enemy.attackInterval);
            enemy.attackInterval = null;
        }
    }

    stopCombatIntervals() {
        this.stopPlayerCombat();
        if (Array.isArray(this.enemies)) {
            this.enemies.forEach(enemy => this.stopEnemyCombat(enemy));
        }
        if (this.boss !== null && this.boss !== undefined) {
            this.stopEnemyCombat(this.boss);
        }
    }

    addCombatLog(message) {
        this.combatMessages.push(message);
        this.combatMessages = this.combatMessages.slice(-GAME_LOG_MAX_MESSAGES);
        this.logText.setText(this.combatMessages.join('\n'));
    }

    removeDeadEnemies() {
        this.enemies
            .filter(enemy => enemy.health <= GAME_ENEMY_ALIVE_HEALTH_THRESHOLD)
            .forEach(enemy => this.stopEnemyCombat(enemy));
        this.enemies = this.enemies.filter(
            enemy => enemy.health > GAME_ENEMY_ALIVE_HEALTH_THRESHOLD
        );
        this.maze.enemies = this.enemies;
        if (this.boss !== null && this.boss.health <= GAME_ENEMY_ALIVE_HEALTH_THRESHOLD) {
            this.stopEnemyCombat(this.boss);
            this.boss = null;
            this.maze.boss = null;
            this.showVictory();
        }
    }

    findEnemyAt(x, y) {
        return this.enemies.find(enemy => enemy.level === this.player.level
            && enemy.x === x
            && enemy.y === y);
    }

    findOpponentAt(x, y) {
        const enemy = this.findEnemyAt(x, y);
        if (enemy) {
            return enemy;
        }
        return this.boss !== null
            && this.boss.level === this.player.level
            && this.boss.x === x
            && this.boss.y === y
            ? this.boss
            : undefined;
    }

    findPotionAt(x, y) {
        return this.potions.find(potion => potion.level === this.player.level
            && potion.x === x && potion.y === y);
    }

    consumePotionAtPlayerPosition() {
        const potion = this.findPotionAt(this.player.x, this.player.y);
        if (!potion || !potion.consume(this.player)) {
            return;
        }
        this.potions = this.potions.filter(currentPotion => currentPotion !== potion);
        this.maze.potions = this.potions;
    }

    refreshView() {
        this.markVisibleFieldOfView();
        this.renderFieldOfView();
        this.drawHealthBar();
        this.drawMinimap();
        this.levelLabel.setText('Nivel ' + (this.player.level + 1));
    }

    renderFieldOfView() {
        if (this.fieldOfViewContainer) {
            this.fieldOfViewContainer.destroy(true);
        }
        this.fieldOfViewContainer = this.add.container(GAME_FOV_X, GAME_FOV_Y);
        this.fieldOfViewContainer.setMask(
            this.fieldOfViewMaskGraphics.createGeometryMask()
        );

        const backgroundKey = this.selectBackground(this.player.x, this.player.y);
        const background = this.add.image(GAME_FOV_WIDTH / 2, GAME_FOV_HEIGHT / 2, backgroundKey);
        background.setDisplaySize(GAME_FOV_WIDTH, GAME_FOV_HEIGHT);
        this.fieldOfViewContainer.add(background);

        this.renderFarLevel();
        this.renderMiddleLevel();
        this.renderNearLevel();
        this.renderPlayerLevel();
    }

    renderFarLevel() {
        this.renderDepthLevel(
            GAME_FOV_FAR_DEPTH,
            GAME_FOV_FAR_RADIUS,
            GAME_FOV_FAR_SCALE,
            GAME_FOV_FAR_DARKNESS,
            GAME_FOV_FAR_VERTICAL_OFFSET,
            GAME_FOV_FAR_DEPTH,
            GAME_FOV_FAR_PERSPECTIVE_RADIUS,
            GAME_STAIRS_FAR_SCALE,
            GAME_STAIRS_FAR_VERTICAL_OFFSET,
            GAME_STAIRS_FAR_ILLUMINATION,
            GAME_ENEMY_FAR_SCALE,
            GAME_ENEMY_FAR_VERTICAL_OFFSET,
            GAME_ENEMY_FAR_ILLUMINATION,
            GAME_POTION_FAR_SCALE,
            GAME_POTION_FAR_VERTICAL_OFFSET,
            GAME_POTION_FAR_ILLUMINATION,
            GAME_BOSS_FAR_SCALE,
            GAME_BOSS_FAR_VERTICAL_OFFSET,
            GAME_BOSS_FAR_ILLUMINATION
        );
    }

    renderMiddleLevel() {
        this.renderDepthLevel(
            GAME_FOV_MIDDLE_DEPTH,
            GAME_FOV_MIDDLE_RADIUS,
            GAME_FOV_MIDDLE_SCALE,
            GAME_FOV_MIDDLE_DARKNESS,
            GAME_FOV_MIDDLE_VERTICAL_OFFSET,
            GAME_FOV_MIDDLE_DEPTH,
            GAME_FOV_MIDDLE_PERSPECTIVE_RADIUS,
            GAME_STAIRS_MIDDLE_SCALE,
            GAME_STAIRS_MIDDLE_VERTICAL_OFFSET,
            GAME_STAIRS_MIDDLE_ILLUMINATION,
            GAME_ENEMY_MIDDLE_SCALE,
            GAME_ENEMY_MIDDLE_VERTICAL_OFFSET,
            GAME_ENEMY_MIDDLE_ILLUMINATION,
            GAME_POTION_MIDDLE_SCALE,
            GAME_POTION_MIDDLE_VERTICAL_OFFSET,
            GAME_POTION_MIDDLE_ILLUMINATION,
            GAME_BOSS_MIDDLE_SCALE,
            GAME_BOSS_MIDDLE_VERTICAL_OFFSET,
            GAME_BOSS_MIDDLE_ILLUMINATION
        );
    }

    renderNearLevel() {
        this.renderDepthLevel(
            GAME_FOV_NEAR_DEPTH,
            GAME_FOV_NEAR_RADIUS,
            GAME_FOV_NEAR_SCALE,
            GAME_FOV_NEAR_DARKNESS,
            GAME_FOV_NEAR_VERTICAL_OFFSET,
            GAME_FOV_NEAR_DEPTH,
            GAME_FOV_NEAR_PERSPECTIVE_RADIUS,
            GAME_STAIRS_NEAR_SCALE,
            GAME_STAIRS_NEAR_VERTICAL_OFFSET,
            GAME_STAIRS_NEAR_ILLUMINATION,
            GAME_ENEMY_NEAR_SCALE,
            GAME_ENEMY_NEAR_VERTICAL_OFFSET,
            GAME_ENEMY_NEAR_ILLUMINATION,
            GAME_POTION_NEAR_SCALE,
            GAME_POTION_NEAR_VERTICAL_OFFSET,
            GAME_POTION_NEAR_ILLUMINATION,
            GAME_BOSS_NEAR_SCALE,
            GAME_BOSS_NEAR_VERTICAL_OFFSET,
            GAME_BOSS_NEAR_ILLUMINATION
        );
    }

    renderDepthLevel(depth, frontRadius, scale, darkness, verticalOffset,
        perspectiveDepth, perspectiveRadius, staircaseScale, staircaseVerticalOffset,
        staircaseIllumination,
        enemyScale, enemyVerticalOffset, enemyIllumination,
        potionScale, potionVerticalOffset, potionIllumination,
        bossScale, bossVerticalOffset, bossIllumination) {
        const wallWidth = GAME_FOV_WIDTH * scale;
        const wallHeight = GAME_FOV_HEIGHT * scale;
        const centerY = GAME_FOV_HEIGHT / 2 + verticalOffset;
        const perspectiveCells = this.getRowInView(perspectiveDepth, perspectiveRadius);
        const frontCells = this.getRowInView(depth, frontRadius);

        perspectiveCells.forEach((cell, index) => {
            const lateral = index - perspectiveRadius;
            if (lateral !== 0 && !this.isVisitable(cell.x, cell.y)) {
                const isMiddleOuterWall = perspectiveDepth === GAME_FOV_MIDDLE_DEPTH
                    && (lateral === -GAME_FOV_MIDDLE_PERSPECTIVE_RADIUS
                        || lateral === GAME_FOV_MIDDLE_PERSPECTIVE_RADIUS);
                const isNearOuterWall = perspectiveDepth === GAME_FOV_NEAR_DEPTH
                    && (lateral === -GAME_FOV_NEAR_PERSPECTIVE_RADIUS
                        || lateral === GAME_FOV_NEAR_PERSPECTIVE_RADIUS);
                const outerWallOffsetScale = isNearOuterWall
                    ? GAME_FOV_MIDDLE_SCALE
                    : GAME_FOV_FAR_SCALE;
                const outerWallOffsetFactor = isNearOuterWall
                    ? GAME_FOV_NEAR_OUTER_PERSPECTIVE_OFFSET_FACTOR
                    : GAME_FOV_OUTER_PERSPECTIVE_OFFSET_FACTOR;
                this.drawPerspectiveWall(cell, lateral, wallWidth, wallHeight, centerY, darkness,
                    (isMiddleOuterWall || isNearOuterWall) && GAME_FOV_ALIGN_TO_FIELD_EDGE,
                    outerWallOffsetScale,
                    isMiddleOuterWall,
                    outerWallOffsetFactor);
            }
        });

        frontCells.forEach((cell, index) => {
            const lateral = index - frontRadius;
            this.drawStaircase(cell, lateral, wallWidth, staircaseScale,
                staircaseVerticalOffset, staircaseIllumination);
            this.drawPotion(cell, lateral, wallWidth, potionScale,
                potionVerticalOffset, potionIllumination);
            this.drawEnemy(cell, lateral, wallWidth, enemyScale,
                enemyVerticalOffset, enemyIllumination);
            this.drawBoss(cell, lateral, wallWidth, bossScale,
                bossVerticalOffset, bossIllumination);
        });

        frontCells.forEach((cell, index) => {
            if (!this.isVisitable(cell.x, cell.y)) {
                const lateral = index - frontRadius;
                this.drawFrontWall(cell, lateral, wallWidth, wallHeight, centerY, darkness);
            }
        });
    }

    drawEnemy(cell, lateral, wallWidth, scale, verticalOffset, illumination) {
        const enemy = this.findEnemyAt(cell.x, cell.y);
        if (!enemy || enemy.isDead()) {
            return;
        }
        const jitterX = this.getEnemyCombatJitter(enemy);
        const jitterY = this.getEnemyCombatJitter(enemy);
        const goblin = this.add.image(
            GAME_FOV_WIDTH / 2 + lateral * wallWidth + jitterX,
            GAME_FOV_HEIGHT + verticalOffset + jitterY,
            GAME_ENEMY_TEXTURE_PREFIX + enemy.enemyType
        );
        goblin.setScale(scale);
        goblin.setTint(this.getIlluminationTint(illumination));
        this.fieldOfViewContainer.add(goblin);
    }

    drawBoss(cell, lateral, wallWidth, scale, verticalOffset, illumination) {
        if (this.boss === null || this.boss.isDead()
            || this.boss.level !== this.player.level
            || this.boss.x !== cell.x || this.boss.y !== cell.y) {
            return;
        }
        const jitterX = this.getEnemyCombatJitter(this.boss);
        const jitterY = this.getEnemyCombatJitter(this.boss);
        const orc = this.add.image(
            GAME_FOV_WIDTH / GAME_CENTER_DIVISOR + lateral * wallWidth + jitterX,
            GAME_FOV_HEIGHT + verticalOffset + jitterY,
            GAME_BOSS_TEXTURE
        );
        orc.setScale(scale);
        orc.setTint(this.getIlluminationTint(illumination));
        this.fieldOfViewContainer.add(orc);
    }

    getEnemyCombatJitter(enemy) {
        if (enemy.attackInterval === null) {
            return 0;
        }
        return Math.floor(Math.random() * GAME_COMBAT_JITTER_RANGE)
            - GAME_COMBAT_JITTER_PIXELS;
    }

    drawPotion(cell, lateral, wallWidth, scale, verticalOffset, illumination) {
        const potion = this.findPotionAt(cell.x, cell.y);
        if (!potion || potion.consumed) {
            return;
        }
        const image = this.add.image(
            GAME_FOV_WIDTH / GAME_CENTER_DIVISOR + lateral * wallWidth,
            GAME_FOV_HEIGHT / GAME_CENTER_DIVISOR + verticalOffset,
            GAME_POTION_TEXTURE
        );
        image.setScale(scale);
        image.setTint(this.getIlluminationTint(illumination));
        this.fieldOfViewContainer.add(image);
    }

    drawStaircase(cell, lateral, wallWidth, staircaseScale, verticalOffset, illumination) {
        const room = this.getRoom(cell.x, cell.y);
        if (room !== MAZE_STAIRCASE_UP && room !== MAZE_STAIRCASE_DOWN) {
            return;
        }
        const texture = room === MAZE_STAIRCASE_UP ? 'Stairs-Up' : 'Stairs-Down';
        const staircase = this.add.image(
            GAME_FOV_WIDTH / 2 + lateral * wallWidth,
            GAME_FOV_HEIGHT / 2 + verticalOffset,
            texture
        );
        staircase.setDisplaySize(
            GAME_FOV_WIDTH * staircaseScale,
            GAME_FOV_HEIGHT * staircaseScale
        );
        staircase.setTint(this.getIlluminationTint(illumination));
        this.fieldOfViewContainer.add(staircase);
    }

    getIlluminationTint(illumination) {
        const channel = Math.round(GAME_COLOR_CHANNEL_MAX * illumination);
        return channel * GAME_COLOR_RED_MULTIPLIER
            + channel * GAME_COLOR_GREEN_MULTIPLIER
            + channel;
    }

    drawFrontWall(cell, lateral, wallWidth, wallHeight, centerY, darkness) {
        const wall = this.add.image(
            GAME_FOV_WIDTH / 2 + lateral * wallWidth,
            centerY,
            this.selectWall(cell.x, cell.y)
        );
        wall.setDisplaySize(wallWidth, wallHeight);
        wall.setTint(this.getDarknessTint(darkness));
        this.fieldOfViewContainer.add(wall);
    }

    drawPerspectiveWall(cell, lateral, wallWidth, wallHeight, centerY, darkness,
        alignToFieldEdge = false, outerWallOffsetScale = GAME_FOV_FAR_SCALE,
        shiftTowardCenter = true,
        outerWallOffsetFactor = GAME_FOV_OUTER_PERSPECTIVE_OFFSET_FACTOR) {
        const sourceWidthRatio = GAME_FOV_PERSPECTIVE_SOURCE_WIDTH / GAME_FOV_WALL_SOURCE_SIZE;
        const sourceHeightRatio = GAME_FOV_PERSPECTIVE_SOURCE_HEIGHT / GAME_FOV_WALL_SOURCE_SIZE;
        const perspectiveWidth = wallWidth * sourceWidthRatio * GAME_FOV_PERSPECTIVE_SCALE;
        const perspectiveHeight = wallHeight * sourceHeightRatio * GAME_FOV_PERSPECTIVE_SCALE;
        const isLeft = lateral < 0;
        const frontWallX = GAME_FOV_WIDTH / 2 + lateral * wallWidth;
        const frontWallLeft = frontWallX - wallWidth / 2;
        const frontWallRight = frontWallX + wallWidth / 2;
        const edgeOffset = GAME_FOV_WIDTH * outerWallOffsetScale
            * outerWallOffsetFactor;
        const perspectiveX = alignToFieldEdge
            ? (shiftTowardCenter
                ? (isLeft
                    ? perspectiveWidth / 2 + edgeOffset
                    : GAME_FOV_WIDTH - perspectiveWidth / 2 - edgeOffset)
                : (isLeft
                    ? perspectiveWidth / 2 - edgeOffset
                    : GAME_FOV_WIDTH - perspectiveWidth / 2 + edgeOffset))
            : (isLeft
                ? frontWallRight + perspectiveWidth / 2
                : frontWallLeft - perspectiveWidth / 2);
        const perspectiveWall = this.add.image(perspectiveX, centerY,
            this.selectPerspectiveWall(cell.x, cell.y));
        perspectiveWall.setDisplaySize(perspectiveWidth, perspectiveHeight);
        perspectiveWall.setTint(this.getDarknessTint(darkness));
        if (!isLeft) {
            perspectiveWall.setFlipX(true);
        }
        this.fieldOfViewContainer.add(perspectiveWall);
    }

    getDarknessTint(darkness) {
        const channel = Math.round(GAME_COLOR_CHANNEL_MAX * (1 - darkness));
        return channel * GAME_COLOR_RED_MULTIPLIER
            + channel * GAME_COLOR_GREEN_MULTIPLIER
            + channel;
    }

    renderPlayerLevel() {
        const left = this.getRelativeCell(GAME_FOV_PLAYER_DEPTH, -GAME_FOV_PLAYER_SIDE_DISTANCE);
        const right = this.getRelativeCell(GAME_FOV_PLAYER_DEPTH, GAME_FOV_PLAYER_SIDE_DISTANCE);
        const forwardLeft = this.getRelativeCell(GAME_FOV_FORWARD_DEPTH, GAME_FOV_LEFT_LATERAL);
        const forwardRight = this.getRelativeCell(GAME_FOV_FORWARD_DEPTH, GAME_FOV_RIGHT_LATERAL);
        this.drawPlayerFrontSideWall(forwardLeft, true);
        this.drawPlayerFrontSideWall(forwardRight, false);
        this.drawPlayerSideWall(left, true);
        this.drawPlayerSideWall(right, false);
    }

    drawPlayerSideWall(cell, isLeft) {
        if (this.isVisitable(cell.x, cell.y)) {
            return;
        }
        const perspectiveWidth = GAME_FOV_WIDTH
            * GAME_FOV_PERSPECTIVE_SOURCE_WIDTH / GAME_FOV_WALL_SOURCE_SIZE;
        const perspectiveHeight = GAME_FOV_HEIGHT
            * GAME_FOV_PERSPECTIVE_SOURCE_HEIGHT / GAME_FOV_WALL_SOURCE_SIZE;
        const nearWallHalfWidth = GAME_FOV_WIDTH * GAME_FOV_NEAR_SCALE / 2;
        const nearWallLeftEdge = GAME_FOV_WIDTH / 2 - nearWallHalfWidth;
        const nearWallRightEdge = GAME_FOV_WIDTH / 2 + nearWallHalfWidth;
        const perspectiveX = isLeft
            ? nearWallLeftEdge - perspectiveWidth / 2
            : nearWallRightEdge + perspectiveWidth / 2;
        const perspectiveWall = this.add.image(
            perspectiveX,
            GAME_FOV_HEIGHT / 2 + GAME_FOV_PLAYER_VERTICAL_OFFSET,
            this.selectPerspectiveWall(cell.x, cell.y)
        );
        perspectiveWall.setDisplaySize(perspectiveWidth, perspectiveHeight);
        if (!isLeft) {
            perspectiveWall.setFlipX(true);
        }
        this.fieldOfViewContainer.add(perspectiveWall);
    }

    drawPlayerFrontSideWall(cell, isLeft) {
        if (this.isExcludedFromFieldOfView(cell.x, cell.y) || this.isVisitable(cell.x, cell.y)) {
            return;
        }
        const nearWallWidth = GAME_FOV_WIDTH * GAME_FOV_NEAR_SCALE;
        const nearWallHeight = GAME_FOV_HEIGHT * GAME_FOV_NEAR_SCALE;
        const wall = this.add.image(
            GAME_FOV_WIDTH / 2 + (isLeft ? -nearWallWidth : nearWallWidth),
            GAME_FOV_HEIGHT / 2 + GAME_FOV_PLAYER_VERTICAL_OFFSET,
            this.selectWall(cell.x, cell.y)
        );
        wall.setDisplaySize(nearWallWidth, nearWallHeight);
        this.fieldOfViewContainer.add(wall);
    }

    selectBackground(x, y) {
        return (x + y) % 2 === 0 ? 'Background001' : 'Background002';
    }

    selectWall(x, y) {
        const wallIndex = Math.abs(x + y) % 3 + 1;
        return 'Wall00' + wallIndex;
    }

    selectPerspectiveWall(x, y) {
        const wallIndex = Math.abs(x + y) % 3 + 1;
        return 'Wall-Persp00' + wallIndex;
    }

    drawHealthBar() {
        this.healthGraphics.clear();
        this.healthGraphics.fillStyle(GAME_HEALTH_BORDER_COLOR, 1);
        this.healthGraphics.fillRect(GAME_HUD_X, GAME_HEALTH_Y, GAME_HEALTH_WIDTH, GAME_HEALTH_HEIGHT);
        this.healthGraphics.lineStyle(GAME_HEALTH_BORDER_WIDTH, GAME_HEALTH_BORDER_COLOR);
        this.healthGraphics.strokeRect(GAME_HUD_X, GAME_HEALTH_Y, GAME_HEALTH_WIDTH, GAME_HEALTH_HEIGHT);

        const inset = GAME_HEALTH_BORDER_WIDTH + GAME_HEALTH_INNER_MARGIN;
        const availableWidth = GAME_HEALTH_WIDTH - inset * 2;
        const healthWidth = availableWidth * (this.player.health / GAME_PLAYER_HEALTH);
        this.healthGraphics.fillStyle(GAME_HEALTH_FILL_COLOR, 1);
        this.healthGraphics.fillRect(
            GAME_HUD_X + inset,
            GAME_HEALTH_Y + inset,
            healthWidth,
            GAME_HEALTH_HEIGHT - inset * 2
        );
    }

    drawMinimap() {
        this.minimapGraphics.clear();
        this.minimapGraphics.fillStyle(GAME_PANEL_FILL_COLOR, 1);
        this.minimapGraphics.fillRect(GAME_HUD_X, GAME_MINIMAP_Y, GAME_HUD_WIDTH, GAME_MINIMAP_HEIGHT);

        const cellWidth = GAME_HUD_WIDTH / this.maze.width;
        const cellHeight = GAME_MINIMAP_HEIGHT / this.maze.height;
        const level = this.maze.levels[this.player.level];
        const visibility = this.maze.visibility[this.player.level];

        for (let y = 0; y < this.maze.height; y++) {
            for (let x = 0; x < this.maze.width; x++) {
                let color = GAME_MINIMAP_HIDDEN_COLOR;
                if (visibility[y][x]) {
                    color = level[y][x] === MAZE_ROOM_WALL
                        ? GAME_MINIMAP_WALL_COLOR
                        : GAME_MINIMAP_VISITABLE_COLOR;
                }
                this.minimapGraphics.fillStyle(color, 1);
                this.minimapGraphics.fillRect(
                    GAME_HUD_X + x * cellWidth,
                    GAME_MINIMAP_Y + y * cellHeight,
                    cellWidth,
                    cellHeight
                );
                if (visibility[y][x]) {
                    this.drawMinimapStaircase(level[y][x], x, y, cellWidth, cellHeight);
                }
            }
        }

        this.minimapGraphics.fillStyle(GAME_MINIMAP_PLAYER_COLOR, 1);
        this.minimapGraphics.fillRect(
            GAME_HUD_X + this.player.x * cellWidth + GAME_MINIMAP_PLAYER_MARGIN,
            GAME_MINIMAP_Y + this.player.y * cellHeight + GAME_MINIMAP_PLAYER_MARGIN,
            Math.max(1, cellWidth - GAME_MINIMAP_PLAYER_MARGIN * 2),
            Math.max(1, cellHeight - GAME_MINIMAP_PLAYER_MARGIN * 2)
        );
        this.minimapGraphics.lineStyle(1, GAME_PANEL_BORDER_COLOR);
        this.minimapGraphics.strokeRect(GAME_HUD_X, GAME_MINIMAP_Y, GAME_HUD_WIDTH, GAME_MINIMAP_HEIGHT);
    }

    drawMinimapStaircase(room, x, y, cellWidth, cellHeight) {
        const cellX = GAME_HUD_X + x * cellWidth;
        const cellY = GAME_MINIMAP_Y + y * cellHeight;
        const centerX = cellX + cellWidth / GAME_CENTER_DIVISOR;
        if (room === MAZE_STAIRCASE_DOWN) {
            this.minimapGraphics.fillStyle(GAME_MINIMAP_STAIRCASE_DOWN_COLOR, 1);
            this.minimapGraphics.fillTriangle(
                cellX, cellY,
                cellX + cellWidth, cellY,
                centerX, cellY + cellHeight
            );
        } else if (room === MAZE_STAIRCASE_UP) {
            this.minimapGraphics.fillStyle(GAME_MINIMAP_STAIRCASE_UP_COLOR, 1);
            this.minimapGraphics.fillTriangle(
                centerX, cellY,
                cellX, cellY + cellHeight,
                cellX + cellWidth, cellY + cellHeight
            );
        }
    }

    markVisibleFieldOfView() {
        const facingKey = {
            N: 'north',
            S: 'south',
            E: 'east',
            W: 'west'
        }[this.player.facing];
        const mask = MAZE_VISION_MASKS[facingKey];
        const visibility = this.maze.visibility[this.player.level];

        for (let maskY = 0; maskY < mask.length; maskY++) {
            for (let maskX = 0; maskX < mask[maskY].length; maskX++) {
                if (mask[maskY][maskX] !== MAZE_VISION_VISIBLE) {
                    continue;
                }
                const x = this.player.x - MAZE_VISION_RADIUS + maskX;
                const y = this.player.y - MAZE_VISION_RADIUS + maskY;
                if (!this.isExcludedFromFieldOfView(x, y)
                    && x >= 0 && x < this.maze.width && y >= 0 && y < this.maze.height) {
                    visibility[y][x] = true;
                }
            }
        }
    }

    getRowInView(depth, radius) {
        const cells = [];
        for (let lateral = -radius; lateral <= radius; lateral++) {
            cells.push(this.getRelativeCell(depth, lateral));
        }
        return cells;
    }

    getRelativeCell(forward, lateral) {
        const directions = {
            N: { forwardX: 0, forwardY: -1, rightX: 1, rightY: 0 },
            S: { forwardX: 0, forwardY: 1, rightX: -1, rightY: 0 },
            E: { forwardX: 1, forwardY: 0, rightX: 0, rightY: 1 },
            W: { forwardX: -1, forwardY: 0, rightX: 0, rightY: -1 }
        };
        const direction = directions[this.player.facing];
        return {
            x: this.player.x + direction.forwardX * forward + direction.rightX * lateral,
            y: this.player.y + direction.forwardY * forward + direction.rightY * lateral
        };
    }

    isExcludedFromFieldOfView(x, y) {
        return this.player.facing === GAME_PLAYER_FACING_NORTH
            && x - this.player.x === GAME_FOV_NORTH_EXCLUDED_X
            && y - this.player.y === GAME_FOV_NORTH_EXCLUDED_Y;
    }

    isVisitable(x, y) {
        return this.getRoom(x, y) !== MAZE_ROOM_WALL;
    }

    getRoom(x, y) {
        const level = this.maze.levels[this.player.level];
        return level && level[y] && level[y][x] !== undefined
            ? level[y][x]
            : MAZE_ROOM_WALL;
    }
}
