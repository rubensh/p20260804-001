'use strict';

const GAME_MAP_VISITABLE_PERCENT = 0.45;
const GAME_PLAYER_ID = 'Player001';
const GAME_PLAYER_LEVEL = 0;
const GAME_PLAYER_FACING_NORTH = 'N';
const GAME_PLAYER_HEALTH = 100;
const GAME_PLAYER_MIN_DAMAGE = 1;
const GAME_PLAYER_MAX_DAMAGE = 6;
const GAME_PLAYER_MIN_DEFENSE = 0;
const GAME_PLAYER_MAX_DEFENSE = 3;

const GAME_FOV_X = 10;
const GAME_FOV_Y = 10;
const GAME_FOV_WIDTH = 380;
const GAME_FOV_HEIGHT = 390;
const GAME_FOV_FAR_SCALE = 0.2;
const GAME_FOV_MIDDLE_SCALE = 0.4;
const GAME_FOV_NEAR_SCALE = 0.8;
const GAME_FOV_FAR_DARKNESS = 0.5;
const GAME_FOV_MIDDLE_DARKNESS = 0.25;
const GAME_FOV_NEAR_SLIVER = 0.1;
const GAME_FOV_FAR_DEPTH = 2;
const GAME_FOV_MIDDLE_DEPTH = 1;

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
const GAME_MINIMAP_PLAYER_MARGIN = 1;
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
    { key: 'Wall-Persp003', file: '/assets/images/Wall-Persp003.png' }
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
        this.maze.generateMaze(GAME_MAP_VISITABLE_PERCENT);

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
                this.scene.start('MainMenuScene');
            }
        });
    }

    registerKeyboardControls() {
        this.input.keyboard.on('keydown-UP', () => {
            this.player.moveForward();
            this.refreshView();
        });
        this.input.keyboard.on('keydown-DOWN', () => {
            this.player.moveBackward();
            this.refreshView();
        });
        this.input.keyboard.on('keydown-LEFT', () => {
            this.player.rotateLeft();
            this.refreshView();
        });
        this.input.keyboard.on('keydown-RIGHT', () => {
            this.player.rotateRight();
            this.refreshView();
        });
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
    }

    renderFarLevel() {
        const cells = this.getRowInView(GAME_FOV_FAR_DEPTH, 2);
        const cellWidth = GAME_FOV_WIDTH / cells.length;
        cells.forEach((cell, index) => {
            if (!this.isVisitable(cell.x, cell.y)) {
                const wall = this.add.image(cellWidth * (index + 0.5), GAME_FOV_HEIGHT * 0.35,
                    this.selectWall(cell.x, cell.y));
                wall.setScale(GAME_FOV_FAR_SCALE);
                wall.setDisplaySize(cellWidth, GAME_FOV_HEIGHT * GAME_FOV_FAR_SCALE);
                wall.setTint(0x808080);
                wall.setAlpha(1 - GAME_FOV_FAR_DARKNESS);
                this.fieldOfViewContainer.add(wall);
            }
        });
    }

    renderMiddleLevel() {
        const cells = this.getRowInView(GAME_FOV_MIDDLE_DEPTH, 1);
        const cellWidth = GAME_FOV_WIDTH / cells.length;
        cells.forEach((cell, index) => {
            if (!this.isVisitable(cell.x, cell.y)) {
                const wall = this.add.image(cellWidth * (index + 0.5), GAME_FOV_HEIGHT * 0.48,
                    this.selectWall(cell.x, cell.y));
                wall.setScale(GAME_FOV_MIDDLE_SCALE);
                wall.setDisplaySize(cellWidth, GAME_FOV_HEIGHT * GAME_FOV_MIDDLE_SCALE);
                wall.setTint(0xbfbfbf);
                wall.setAlpha(1 - GAME_FOV_MIDDLE_DARKNESS);
                this.fieldOfViewContainer.add(wall);
            }
        });
    }

    renderNearLevel() {
        const forward = this.getRelativeCell(1, 0);
        const left = this.getRelativeCell(1, -1);
        const right = this.getRelativeCell(1, 1);
        const nearWidth = GAME_FOV_WIDTH * GAME_FOV_NEAR_SCALE;
        const nearHeight = GAME_FOV_HEIGHT * GAME_FOV_NEAR_SCALE;

        if (!this.isVisitable(forward.x, forward.y)) {
            const frontWall = this.add.image(GAME_FOV_WIDTH / 2, GAME_FOV_HEIGHT / 2,
                this.selectWall(forward.x, forward.y));
            frontWall.setScale(GAME_FOV_NEAR_SCALE);
            frontWall.setDisplaySize(nearWidth, nearHeight);
            this.fieldOfViewContainer.add(frontWall);
        }

        this.drawNearSliver(left, true, nearWidth, nearHeight);
        this.drawNearSliver(right, false, nearWidth, nearHeight);
    }

    drawNearSliver(cell, isLeft, nearWidth, nearHeight) {
        if (this.isVisitable(cell.x, cell.y)) {
            return;
        }
        const perspectiveWall = this.add.image(
            isLeft ? nearWidth * GAME_FOV_NEAR_SLIVER / 2 : GAME_FOV_WIDTH - nearWidth * GAME_FOV_NEAR_SLIVER / 2,
            GAME_FOV_HEIGHT / 2,
            this.selectPerspectiveWall(cell.x, cell.y)
        );
        perspectiveWall.setDisplaySize(nearWidth * GAME_FOV_NEAR_SLIVER, nearHeight);
        if (!isLeft) {
            perspectiveWall.setFlipX(true);
        }
        this.fieldOfViewContainer.add(perspectiveWall);
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
                if (x >= 0 && x < this.maze.width && y >= 0 && y < this.maze.height) {
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

    isVisitable(x, y) {
        const level = this.maze.levels[this.player.level];
        return Boolean(level && level[y] && level[y][x] !== MAZE_ROOM_WALL);
    }
}
