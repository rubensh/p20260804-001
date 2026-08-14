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
    }

    generateMaze() {
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

        const downStaircase = { x: current.x, y: current.y };
        let upStaircase;
        if (currentLevel === MAZE_FIRST_LEVEL) {
            upStaircase = { x: center.x, y: center.y };
        } else {
            upStaircase = { x: start.x, y: start.y };
        }

        matrix[downStaircase.y][downStaircase.x] = MAZE_STAIRCASE_DOWN;
        matrix[upStaircase.y][upStaircase.x] = MAZE_STAIRCASE_UP;

        this.levels[currentLevel] = matrix;
        this.visibility[currentLevel] = visibilityMatrix;

        return matrix;
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
