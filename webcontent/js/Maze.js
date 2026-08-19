'use strict';

const MAZE_DEFAULT_WIDTH = 32;
const MAZE_DEFAULT_HEIGHT = 32;
const MAZE_DEFAULT_DEPTH = 5;

const MAZE_ROOM_EMPTY = 0;
const MAZE_ROOM_WALL = 1;
const MAZE_STAIRCASE_DOWN = 2;
const MAZE_STAIRCASE_UP = 3;

const MAZE_FIRST_LEVEL = 0;
const MAZE_LEVEL_NUMBER_OFFSET = 1;
const MAZE_BASE_CORRIDOR_LENGTH = 8;
const MAZE_CORRIDOR_STEP_MULTIPLIER = 2.0;
const MAZE_VISITABLE_DEPTH_FACTOR = 0.8;
const MAZE_ENEMY_LEVEL_OFFSET = 1;
const MAZE_ENEMY_WEIGHT_EXPONENT = 2;
const MAZE_ENEMY_ID_OFFSET = 1;
const MAZE_ENEMY_ID_PREFIX = 'Enemy';
const MAZE_NO_ENEMIES = 0;
const MAZE_POTIONS_PER_LEVEL = 5;
const MAZE_POTION_ID_OFFSET = 1;
const MAZE_POTION_ID_PREFIX = 'Potion';

const MAZE_VISION_VISIBLE = 1;
const MAZE_VISION_RADIUS = 2;

const MAZE_DIRECTIONS = [
    { x: 0, y: -1 },
    { x: 0, y: 1 },
    { x: 1, y: 0 },
    { x: -1, y: 0 }
];

const MAZE_VISION_MASKS = {
    north: [
        [1, 1, 1, 1, 1],
        [0, 1, 1, 1, 0],
        [0, 0, 1, 0, 0],
        [0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0]
    ],
    south: [
        [0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0],
        [0, 0, 1, 0, 0],
        [0, 1, 1, 1, 0],
        [1, 1, 1, 1, 1]
    ],
    east: [
        [0, 0, 0, 0, 1],
        [0, 0, 0, 1, 1],
        [0, 0, 1, 1, 1],
        [0, 0, 0, 1, 1],
        [0, 0, 0, 0, 1]
    ],
    west: [
        [1, 0, 0, 0, 0],
        [1, 1, 0, 0, 0],
        [1, 1, 1, 0, 0],
        [1, 1, 0, 0, 0],
        [1, 0, 0, 0, 0]
    ]
};

class Maze {

    constructor(width = MAZE_DEFAULT_WIDTH, height = MAZE_DEFAULT_HEIGHT, depth = MAZE_DEFAULT_DEPTH) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.levels = [];
        this.visibility = [];
        this.enemies = [];
        this.enemyCountsByLevel = [];
        this.potions = [];
    }

    generateMaze(enemyCount = MAZE_NO_ENEMIES) {
        this.enemies = [];
        this.potions = [];
        this.enemyCountsByLevel = this.calculateEnemyCounts(enemyCount);
        for (let currentLevel = MAZE_FIRST_LEVEL; currentLevel < this.depth; currentLevel++) {
            const level = currentLevel;
            const steps = (MAZE_BASE_CORRIDOR_LENGTH - level) * MAZE_CORRIDOR_STEP_MULTIPLIER;
            const percentVisitable = this.width * this.height * (level + MAZE_LEVEL_NUMBER_OFFSET)
                / (MAZE_DEFAULT_DEPTH * MAZE_VISITABLE_DEPTH_FACTOR);
            this.generateMap(this.width, this.height, percentVisitable, steps, currentLevel);
        }
        this.applyInitialFieldOfView();
    }

    generateMap(width, height, percentVisitable, steps, currentLevel) {
        const matrix = this.createLevel(width, height);
        const visibilityMatrix = this.createVisibilityLevel(width, height);

        const center = {
            x: Math.floor(width / 2),
            y: Math.floor(height / 2)
        };

        const start = this.getStartPosition(currentLevel, center, width, height);

        const maxVisitableCells = (width - 2) * (height - 2);
        const targetVisitableCells = Math.min(Math.floor(percentVisitable), maxVisitableCells);

        let visitableCells = 0;
        let current = { x: start.x, y: start.y };
        let forcedDirection = null;

        const markVisitable = (x, y) => {
            matrix[y][x] = MAZE_ROOM_EMPTY;
            visitableCells++;
        };

        markVisitable(current.x, current.y);

        while (visitableCells < targetVisitableCells && visitableCells < maxVisitableCells) {
            const visitableCellsBeforeWalk = visitableCells;
            const direction = forcedDirection || this.getRandomDirection();
            forcedDirection = null;
            for (let currentStep = 0;
                currentStep < steps && visitableCells < targetVisitableCells;
                currentStep++) {
                const next = { x: current.x + direction.x, y: current.y + direction.y };

                if (this.isBorderCell(next.x, next.y, width, height)) {
                    current = { x: start.x, y: start.y };
                    break;
                }

                current = next;
                if (matrix[current.y][current.x] === MAZE_ROOM_WALL) {
                    markVisitable(current.x, current.y);
                }
            }

            if (visitableCells === visitableCellsBeforeWalk) {
                const frontier = this.findCorridorFrontier(matrix, width, height);
                if (!frontier) {
                    break;
                }
                current = frontier.current;
                forcedDirection = frontier.direction;
            }
        }

        if (currentLevel > MAZE_FIRST_LEVEL) {
            matrix[start.y][start.x] = MAZE_STAIRCASE_UP;
        }
        if (currentLevel < this.depth - MAZE_LEVEL_NUMBER_OFFSET) {
            matrix[current.y][current.x] = MAZE_STAIRCASE_DOWN;
        }

        this.levels[currentLevel] = matrix;
        this.visibility[currentLevel] = visibilityMatrix;
        this.generateEnemiesForLevel(
            matrix,
            currentLevel,
            this.enemyCountsByLevel[currentLevel]
        );
        this.generatePotionsForLevel(matrix, currentLevel);

        return matrix;
    }

    calculateEnemyCounts(enemyCount) {
        const weights = [];
        let totalWeight = MAZE_NO_ENEMIES;
        for (let level = MAZE_FIRST_LEVEL; level < this.depth; level++) {
            const weight = Math.pow(level + MAZE_ENEMY_LEVEL_OFFSET, MAZE_ENEMY_WEIGHT_EXPONENT);
            weights.push(weight);
            totalWeight += weight;
        }

        const counts = weights.map(weight => Math.floor(enemyCount * weight / totalWeight));
        let assignedEnemies = counts.reduce(
            (total, count) => total + count,
            MAZE_NO_ENEMIES
        );
        let level = this.depth - MAZE_ENEMY_LEVEL_OFFSET;
        while (assignedEnemies < enemyCount) {
            counts[level]++;
            assignedEnemies++;
            level--;
            if (level < MAZE_FIRST_LEVEL) {
                level = this.depth - MAZE_ENEMY_LEVEL_OFFSET;
            }
        }
        return counts;
    }

    generateEnemiesForLevel(matrix, currentLevel, enemyCount) {
        const candidates = [];
        for (let y = MAZE_ENEMY_LEVEL_OFFSET; y < matrix.length - MAZE_ENEMY_LEVEL_OFFSET; y++) {
            for (let x = MAZE_ENEMY_LEVEL_OFFSET; x < matrix[y].length - MAZE_ENEMY_LEVEL_OFFSET; x++) {
                if (matrix[y][x] === MAZE_ROOM_EMPTY
                    && !this.isInitialPlayerPosition(currentLevel, x, y)) {
                    candidates.push({ x, y });
                }
            }
        }

        const enemiesToCreate = Math.min(enemyCount, candidates.length);
        for (let enemyIndex = MAZE_NO_ENEMIES; enemyIndex < enemiesToCreate; enemyIndex++) {
            const candidateIndex = Math.floor(Math.random() * candidates.length);
            const position = candidates.splice(candidateIndex, MAZE_ENEMY_LEVEL_OFFSET)[0];
            const id = MAZE_ENEMY_ID_PREFIX + (this.enemies.length + MAZE_ENEMY_ID_OFFSET);
            this.enemies.push(new Enemy(id, position.x, position.y, currentLevel, this));
        }
    }

    generatePotionsForLevel(matrix, currentLevel) {
        const candidates = [];
        for (let y = MAZE_LEVEL_NUMBER_OFFSET;
            y < matrix.length - MAZE_LEVEL_NUMBER_OFFSET;
            y++) {
            for (let x = MAZE_LEVEL_NUMBER_OFFSET;
                x < matrix[y].length - MAZE_LEVEL_NUMBER_OFFSET;
                x++) {
                if (matrix[y][x] === MAZE_ROOM_EMPTY
                    && !this.isInitialPlayerPosition(currentLevel, x, y)
                    && !this.isOccupiedByEnemy(currentLevel, x, y)) {
                    candidates.push({ x, y });
                }
            }
        }

        const potionsToCreate = Math.min(MAZE_POTIONS_PER_LEVEL, candidates.length);
        for (let potionIndex = 0; potionIndex < potionsToCreate; potionIndex++) {
            const candidateIndex = Math.floor(Math.random() * candidates.length);
            const position = candidates.splice(candidateIndex, MAZE_LEVEL_NUMBER_OFFSET)[0];
            const id = MAZE_POTION_ID_PREFIX + (this.potions.length + MAZE_POTION_ID_OFFSET);
            this.potions.push(new Potion(id, position.x, position.y, currentLevel));
        }
    }

    isOccupiedByEnemy(currentLevel, x, y) {
        return this.enemies.some(enemy => enemy.level === currentLevel
            && enemy.x === x && enemy.y === y);
    }

    isInitialPlayerPosition(currentLevel, x, y) {
        const center = this.getCenter(this.width, this.height);
        return currentLevel === MAZE_FIRST_LEVEL && x === center.x && y === center.y;
    }

    createLevel(width, height) {
        const matrix = [];
        for (let y = 0; y < height; y++) {
            const row = [];
            for (let x = 0; x < width; x++) {
                row.push(MAZE_ROOM_WALL);
            }
            matrix.push(row);
        }
        return matrix;
    }

    createVisibilityLevel(width, height) {
        const matrix = [];
        for (let y = 0; y < height; y++) {
            const row = [];
            for (let x = 0; x < width; x++) {
                row.push(false);
            }
            matrix.push(row);
        }
        return matrix;
    }

    getStartPosition(currentLevel, center, width, height) {
        if (currentLevel === MAZE_FIRST_LEVEL) {
            return { x: center.x, y: center.y };
        }

        const downStaircase = this.findStaircaseDown(currentLevel - 1, width, height);
        if (downStaircase) {
            return { x: downStaircase.x, y: downStaircase.y };
        }

        return { x: center.x, y: center.y };
    }

    findStaircaseDown(currentLevel, width, height) {
        const matrix = this.levels[currentLevel];
        if (!matrix) {
            return null;
        }

        for (let y = 0; y < height; y++) {
            for (let x = 0; x < width; x++) {
                if (matrix[y][x] === MAZE_STAIRCASE_DOWN) {
                    return { x, y };
                }
            }
        }
        return null;
    }

    findCorridorFrontier(matrix, width, height) {
        for (let y = 1; y < height - 1; y++) {
            for (let x = 1; x < width - 1; x++) {
                if (matrix[y][x] === MAZE_ROOM_WALL) {
                    continue;
                }
                for (const direction of MAZE_DIRECTIONS) {
                    const nextX = x + direction.x;
                    const nextY = y + direction.y;
                    if (!this.isBorderCell(nextX, nextY, width, height)
                        && matrix[nextY][nextX] === MAZE_ROOM_WALL) {
                        return { current: { x, y }, direction };
                    }
                }
            }
        }
        return null;
    }

    isBorderCell(x, y, width, height) {
        return x === 0 || y === 0 || x === width - 1 || y === height - 1;
    }

    getRandomDirection() {
        const index = Math.floor(Math.random() * MAZE_DIRECTIONS.length);
        return MAZE_DIRECTIONS[index];
    }

    applyInitialFieldOfView() {
        if (this.levels.length === 0) {
            return;
        }

        const mask = MAZE_VISION_MASKS.north;
        const center = this.getCenter(this.width, this.height);

        for (let dy = 0; dy < mask.length; dy++) {
            for (let dx = 0; dx < mask[dy].length; dx++) {
                if (mask[dy][dx] === MAZE_VISION_VISIBLE) {
                    const x = center.x - MAZE_VISION_RADIUS + dx;
                    const y = center.y - MAZE_VISION_RADIUS + dy;
                    if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
                        this.visibility[MAZE_FIRST_LEVEL][y][x] = true;
                    }
                }
            }
        }
    }

    getCenter(width, height) {
        return {
            x: Math.floor(width / 2),
            y: Math.floor(height / 2)
        };
    }
}
