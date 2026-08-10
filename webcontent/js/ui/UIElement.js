'use strict';

class UIElement {

    constructor(id, parentElement) {
        this.id = id;
        this.parentElement = parentElement || null;
    }

    static get BORDER_WIDTH() {
        return 1;
    }

    static get MAX_CHANNEL() {
        return 255;
    }

    static get UNITY_CHANNEL() {
        return 1.0;
    }

    static get RED_FACTOR() {
        return 0.299;
    }

    static get GREEN_FACTOR() {
        return 0.587;
    }

    static get BLUE_FACTOR() {
        return 0.114;
    }

    static get LIGHTEN_AMOUNT() {
        return 25;
    }

    static get ALPHA_TRANSPARENT() {
        return 0;
    }

    static get COLOR_BLACK() {
        return 0x000000;
    }

    static scaleChannel(value) {
        return Math.round(value <= UIElement.UNITY_CHANNEL ? value * UIElement.MAX_CHANNEL : value);
    }

    static parseColor(rgba) {
        const match = /rgba\(([0-9.]+),\s*([0-9.]+),\s*([0-9.]+),\s*([0-9.]+)\)/.exec(rgba);
        return {
            r: UIElement.scaleChannel(Number(match[1])),
            g: UIElement.scaleChannel(Number(match[2])),
            b: UIElement.scaleChannel(Number(match[3])),
            a: Number(match[4])
        };
    }

    static toColorInt(r, g, b) {
        return (r << 16) | (g << 8) | b;
    }

    static toHex(r, g, b) {
        return '#' + [r, g, b]
            .map(channel => channel.toString(16).padStart(2, '0'))
            .join('');
    }

    static toGrayscale(color) {
        const gray = Math.round(
            UIElement.RED_FACTOR * color.r +
            UIElement.GREEN_FACTOR * color.g +
            UIElement.BLUE_FACTOR * color.b
        );
        return {
            r: gray,
            g: gray,
            b: gray,
            a: color.a
        };
    }

    static lighten(color, amount) {
        return {
            r: Math.min(UIElement.MAX_CHANNEL, color.r + amount),
            g: Math.min(UIElement.MAX_CHANNEL, color.g + amount),
            b: Math.min(UIElement.MAX_CHANNEL, color.b + amount),
            a: color.a
        };
    }
}
