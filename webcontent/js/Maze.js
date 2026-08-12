'use strict';

const MAZE_DEFAULT_WIDTH = 32;
const MAZE_DEFAULT_HEIGHT = 32;
const MAZE_DEFAULT_DEPTH = 5;

const MAZE_ROOM_EMPTY = 0;
const MAZE_ROOM_WALL = 1;
const MAZE_STAIRCASE_DOWN = 2;
const MAZE_STAIRCASE_UP = 3;

const MAZE_FIRST_LEVEL = 0;

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
        [0, 1, 1, 1, 1],
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

    generateMaze(percentVisitable) {
        for (let currentLevel = MAZE_FIRST_LEVEL; currentLevel < this.depth; currentLevel++) {
            this.generateMap(this.width, this.height, percentVisitable, currentLevel);
        }
        this.applyInitialFieldOfView();
    }

    generateMap(width, height, percentVisitable, currentLevel) {
        const matrix = this.createLevel(width, height);
        const visibilityMatrix = this.createVisibilityLevel(width, height);

        const center = {
            x: Math.floor(width / 2),
            y: Math.floor(height / 2)
        };

        const start = this.getStartPosition(currentLevel, center, width, height);

        const targetVisitableCells = Math.floor(width * height * percentVisitable);
        const maxVisitableCells = (width - 2) * (height - 2);

        let visitableCells = 0;
        let current = { x: start.x, y: start.y };

        const markVisitable = (x, y) => {
            matrix[y][x] = MAZE_ROOM_EMPTY;
            visitableCells++;
        };

        markVisitable(current.x, current.y);

        while (visitableCells < targetVisitableCells && visitableCells < maxVisitableCells) {
            const direction = this.getRandomDirection();
            const next = { x: current.x + direction.x, y: current.y + direction.y };

            if (this.isBorderCell(next.x, next.y, width, height)) {
                current = { x: start.x, y: start.y };
                continue;
            }

            if (matrix[next.y][next.x] !== MAZE_ROOM_WALL) {
                current = this.findNextVisitableCell(matrix, width, height);
                if (!current) {
                    break;
                }
                markVisitable(current.x, current.y);
                continue;
            }

            current = next;
            markVisitable(current.x, current.y);
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

    findNextVisitableCell(matrix, width, height) {
        for (let y = 1; y < height - 1; y++) {
            for (let x = 1; x < width - 1; x++) {
                if (matrix[y][x] === MAZE_ROOM_WALL) {
                    return { x, y };
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
