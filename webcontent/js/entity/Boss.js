'use strict';

const BOSS_ENEMY_TYPE = 1;
const BOSS_HEALTH_LEVEL_FACTOR = 10;
const BOSS_HEALTH_MULTIPLIER = 3;
const BOSS_MIN_DAMAGE_LEVEL_FACTOR = 2;
const BOSS_MAX_DAMAGE_LEVEL_FACTOR = 5;
const BOSS_MIN_DEFENSE_LEVEL_FACTOR = 1;
const BOSS_MAX_DEFENSE_LEVEL_FACTOR = 2;
const BOSS_ATTACK_DELAY_FACTOR = 1.5;
const BOSS_ATTACK_DELAY_OFFSET = 0.5;
const BOSS_MIN_HEALTH = 0;
const BOSS_LEVEL_OFFSET = 1;
const BOSS_MOVE_BACKWARD = -1;
const BOSS_MOVE_NONE = 0;
const BOSS_MOVE_FORWARD = 1;

class Boss extends Entity {

    constructor(id, x, y, currentLevel, maze) {
        super(id, x, y, currentLevel);
        this.maze = maze;
        const levelFactor = currentLevel + BOSS_LEVEL_OFFSET;
        this.enemyType = BOSS_ENEMY_TYPE;
        this.health = levelFactor * BOSS_HEALTH_LEVEL_FACTOR * BOSS_HEALTH_MULTIPLIER;
        this.minDamage = levelFactor * BOSS_MIN_DAMAGE_LEVEL_FACTOR;
        this.maxDamage = levelFactor * BOSS_MAX_DAMAGE_LEVEL_FACTOR;
        this.minDefense = levelFactor * BOSS_MIN_DEFENSE_LEVEL_FACTOR;
        this.maxDefense = levelFactor * BOSS_MAX_DEFENSE_LEVEL_FACTOR;
        this.attackDelay = this.enemyType * BOSS_ATTACK_DELAY_FACTOR - BOSS_ATTACK_DELAY_OFFSET;
        this.attackInterval = null;
    }

    attack(player) {
        const damage = this.getRandomBetween(this.minDamage, this.maxDamage);
        player.defend(damage);
        return damage;
    }

    defend(damage) {
        const defense = this.getRandomBetween(this.minDefense, this.maxDefense);
        const difference = damage - defense;
        if (difference > BOSS_MIN_HEALTH) {
            this.health = Math.max(BOSS_MIN_HEALTH, this.health - difference);
        }
    }

    moveUp() {
        this.move(BOSS_MOVE_NONE, BOSS_MOVE_BACKWARD);
    }

    moveDown() {
        this.move(BOSS_MOVE_NONE, BOSS_MOVE_FORWARD);
    }

    moveLeft() {
        this.move(BOSS_MOVE_BACKWARD, BOSS_MOVE_NONE);
    }

    moveRight() {
        this.move(BOSS_MOVE_FORWARD, BOSS_MOVE_NONE);
    }

    moveUpLeft() {
        this.move(BOSS_MOVE_BACKWARD, BOSS_MOVE_BACKWARD);
    }

    moveUpRight() {
        this.move(BOSS_MOVE_FORWARD, BOSS_MOVE_BACKWARD);
    }

    moveDownLeft() {
        this.move(BOSS_MOVE_BACKWARD, BOSS_MOVE_FORWARD);
    }

    moveDownRight() {
        this.move(BOSS_MOVE_FORWARD, BOSS_MOVE_FORWARD);
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
        return this.health === BOSS_MIN_HEALTH;
    }

    isVisitable(x, y) {
        const level = this.maze.levels[this.level];
        return Boolean(level && level[y] && level[y][x] !== MAZE_ROOM_WALL);
    }

    getRandomBetween(min, max) {
        return Math.floor(Math.random() * (max - min + BOSS_LEVEL_OFFSET)) + min;
    }
}
