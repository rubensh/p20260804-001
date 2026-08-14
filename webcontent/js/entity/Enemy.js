'use strict';

const ENEMY_TYPE_SWORD = 1;
const ENEMY_TYPE_AXE = 2;
const ENEMY_TYPE_SPEAR = 3;
const ENEMY_HEALTH_LEVEL_FACTOR = 10;
const ENEMY_MIN_DAMAGE_LEVEL_FACTOR = 2;
const ENEMY_MAX_DAMAGE_LEVEL_FACTOR = 5;
const ENEMY_MIN_DEFENSE_LEVEL_FACTOR = 1;
const ENEMY_MAX_DEFENSE_LEVEL_FACTOR = 2;
const ENEMY_ATTACK_DELAY_FACTOR = 1.5;
const ENEMY_ATTACK_DELAY_OFFSET = 0.5;
const ENEMY_MIN_HEALTH = 0;
const ENEMY_LEVEL_OFFSET = 1;
const ENEMY_MOVE_BACKWARD = -1;
const ENEMY_MOVE_NONE = 0;
const ENEMY_MOVE_FORWARD = 1;

class Enemy extends Entity {

    constructor(id, x, y, currentLevel, maze) {
        super(id, x, y, currentLevel);
        this.maze = maze;
        const levelFactor = currentLevel + ENEMY_LEVEL_OFFSET;
        this.enemyType = this.getRandomBetween(ENEMY_TYPE_SWORD, ENEMY_TYPE_SPEAR);
        this.health = levelFactor * ENEMY_HEALTH_LEVEL_FACTOR
            * this.getRandomBetween(ENEMY_TYPE_SWORD, ENEMY_TYPE_SPEAR);
        this.minDamage = levelFactor * ENEMY_MIN_DAMAGE_LEVEL_FACTOR;
        this.maxDamage = levelFactor * ENEMY_MAX_DAMAGE_LEVEL_FACTOR;
        this.minDefense = levelFactor * ENEMY_MIN_DEFENSE_LEVEL_FACTOR;
        this.maxDefense = levelFactor * ENEMY_MAX_DEFENSE_LEVEL_FACTOR;
        this.attackDelay = this.enemyType * ENEMY_ATTACK_DELAY_FACTOR - ENEMY_ATTACK_DELAY_OFFSET;
    }

    attack(player) {
        const damage = this.getRandomBetween(this.minDamage, this.maxDamage);
        player.defend(damage);
    }

    defend(damage) {
        const defense = this.getRandomBetween(this.minDefense, this.maxDefense);
        const difference = damage - defense;
        if (difference > ENEMY_MIN_HEALTH) {
            this.health = Math.max(ENEMY_MIN_HEALTH, this.health - difference);
        }
    }

    moveUp() {
        this.move(ENEMY_MOVE_NONE, ENEMY_MOVE_BACKWARD);
    }

    moveDown() {
        this.move(ENEMY_MOVE_NONE, ENEMY_MOVE_FORWARD);
    }

    moveLeft() {
        this.move(ENEMY_MOVE_BACKWARD, ENEMY_MOVE_NONE);
    }

    moveRight() {
        this.move(ENEMY_MOVE_FORWARD, ENEMY_MOVE_NONE);
    }

    moveUpLeft() {
        this.move(ENEMY_MOVE_BACKWARD, ENEMY_MOVE_BACKWARD);
    }

    moveUpRight() {
        this.move(ENEMY_MOVE_FORWARD, ENEMY_MOVE_BACKWARD);
    }

    moveDownLeft() {
        this.move(ENEMY_MOVE_BACKWARD, ENEMY_MOVE_FORWARD);
    }

    moveDownRight() {
        this.move(ENEMY_MOVE_FORWARD, ENEMY_MOVE_FORWARD);
    }

    move(deltaX, deltaY) {
        const targetX = this.x + deltaX;
        const targetY = this.y + deltaY;
        if (this.isVisitable(targetX, targetY)) {
            this.x = targetX;
            this.y = targetY;
        }
    }

    isDead() {
        return this.health === ENEMY_MIN_HEALTH;
    }

    isVisitable(x, y) {
        const level = this.maze.levels[this.level];
        return Boolean(level && level[y] && level[y][x] !== MAZE_ROOM_WALL);
    }

    getRandomBetween(min, max) {
        return Math.floor(Math.random() * (max - min + ENEMY_LEVEL_OFFSET)) + min;
    }
}
