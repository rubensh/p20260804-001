'use strict';

const PLAYER_ALIVE_HEALTH_THRESHOLD = 0;
const PLAYER_ATTACK_DELAY_SECONDS = 1;

class Player extends Entity {

    constructor(id, x, y, level, maze, facing, health, minDamage, maxDamage, minDefense, maxDefense) {
        super(id, x, y, level);
        this.maze = maze;
        this.facing = facing;
        this.health = health;
        this.maxHealth = health;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.minDefense = minDefense;
        this.maxDefense = maxDefense;
        this.attackDelay = PLAYER_ATTACK_DELAY_SECONDS;
        this.attackInterval = null;
    }

    attack(entity) {
        const damage = this.getRandomBetween(this.minDamage, this.maxDamage);
        entity.defend(damage);
        return damage;
    }

    defend(damage) {
        const defense = this.getRandomBetween(this.minDefense, this.maxDefense);
        const difference = damage - defense;
        if (difference > 0) {
            this.health = Math.max(0, this.health - difference);
        }
    }

    restoreHealth() {
        this.health = this.maxHealth;
    }

    moveForward() {
        const target = this.getForwardCell();
        if (this.isVisitable(target.x, target.y)) {
            const room = this.getRoom(target.x, target.y);
            this.x = target.x;
            this.y = target.y;
            this.changeLevelForStaircase(room);
        }
    }

    moveBackward() {
        const target = this.getBackwardCell();
        if (this.isVisitable(target.x, target.y)) {
            this.x = target.x;
            this.y = target.y;
        }
    }

    rotateLeft() {
        if (this.facing === 'N') {
            this.facing = 'W';
        } else if (this.facing === 'S') {
            this.facing = 'E';
        } else if (this.facing === 'E') {
            this.facing = 'N';
        } else if (this.facing === 'W') {
            this.facing = 'S';
        }
    }

    rotateRight() {
        if (this.facing === 'N') {
            this.facing = 'E';
        } else if (this.facing === 'S') {
            this.facing = 'W';
        } else if (this.facing === 'E') {
            this.facing = 'S';
        } else if (this.facing === 'W') {
            this.facing = 'N';
        }
    }

    getForwardCell() {
        const target = { x: this.x, y: this.y };
        if (this.facing === 'N') {
            target.y--;
        } else if (this.facing === 'S') {
            target.y++;
        } else if (this.facing === 'E') {
            target.x++;
        } else if (this.facing === 'W') {
            target.x--;
        }
        return target;
    }

    getBackwardCell() {
        const target = { x: this.x, y: this.y };
        if (this.facing === 'N') {
            target.y++;
        } else if (this.facing === 'S') {
            target.y--;
        } else if (this.facing === 'E') {
            target.x--;
        } else if (this.facing === 'W') {
            target.x++;
        }
        return target;
    }

    getRandomBetween(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }

    getRoom(x, y) {
        const level = this.maze.levels[this.level];
        if (!level || !level[y]) {
            return MAZE_ROOM_WALL;
        }
        return level[y][x];
    }

    changeLevelForStaircase(room) {
        if (room === MAZE_STAIRCASE_DOWN
            && this.level < this.maze.depth - MAZE_LEVEL_NUMBER_OFFSET) {
            this.level++;
        } else if (room === MAZE_STAIRCASE_UP && this.level > MAZE_FIRST_LEVEL) {
            this.level--;
        }
    }

    isVisitable(x, y) {
        const level = this.maze.levels[this.level];
        if (!level || y < 0 || y >= level.length || x < 0 || x >= level[y].length) {
            return false;
        }
        return level[y][x] !== MAZE_ROOM_WALL && !this.isOccupiedByEnemy(x, y);
    }

    isOccupiedByEnemy(x, y) {
        const enemies = Array.isArray(this.maze.enemies) ? this.maze.enemies : [];
        return enemies.some(enemy => enemy.level === this.level
            && enemy.x === x
            && enemy.y === y
            && enemy.health > PLAYER_ALIVE_HEALTH_THRESHOLD);
    }
}
