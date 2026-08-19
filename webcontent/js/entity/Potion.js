'use strict';

class Potion extends Entity {

    constructor(id, x, y, level) {
        super(id, x, y, level);
        this.consumed = false;
    }

    consume(player) {
        if (this.consumed) {
            return false;
        }
        player.restoreHealth();
        this.consumed = true;
        return true;
    }
}
