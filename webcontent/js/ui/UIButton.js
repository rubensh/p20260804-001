'use strict';

class UIButton extends UIElement {

    constructor(scene, {
        id,
        parentElement,
        x,
        y,
        width,
        height,
        backgroundColor,
        borderColor,
        textColor,
        text,
        textSize,
        fontName,
        enabled,
        visible,
        onClickFunction
    }) {
        super(id, parentElement);

        this.scene = scene;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.textColor = textColor;
        this.text = text;
        this.textSize = textSize;
        this.fontName = fontName;
        this.enabled = enabled;
        this.visible = visible;
        this.onClickFunction = onClickFunction;

        this.panelEnabled = true;
        this.panelVisible = true;

        this.bgRgba = UIElement.parseColor(this.backgroundColor);
        this.textRgba = UIElement.parseColor(this.textColor);
        this.borderRgba = this.borderColor ? UIElement.parseColor(this.borderColor) : null;

        this.createGameObjects();

        if (!this.parentElement) {
            this.setScenePosition(this.x, this.y);
        }

        this.applyState();
    }

    createGameObjects() {
        this.container = this.scene.add.container(0, 0);

        this.bg = this.scene.add.rectangle(0, 0, this.width, this.height,
            UIElement.toColorInt(this.bgRgba.r, this.bgRgba.g, this.bgRgba.b), this.bgRgba.a);
        this.bg.setOrigin(0, 0);

        const children = [this.bg];

        if (this.borderRgba) {
            this.border = this.scene.add.rectangle(0, 0, this.width, this.height);
            this.border.setStrokeStyle(UIElement.BORDER_WIDTH,
                UIElement.toColorInt(this.borderRgba.r, this.borderRgba.g, this.borderRgba.b), this.borderRgba.a);
            this.border.setOrigin(0, 0);
            this.border.setFillStyle(UIElement.COLOR_BLACK, UIElement.ALPHA_TRANSPARENT);
            children.push(this.border);
        }

        this.label = this.scene.add.text(this.width / 2, this.height / 2, this.text, {
            fontFamily: this.fontName,
            fontSize: this.textSize + 'px',
            color: UIElement.toHex(this.textRgba.r, this.textRgba.g, this.textRgba.b)
        });
        this.label.setOrigin(0.5);
        this.label.setAlpha(this.textRgba.a);
        children.push(this.label);

        this.container.add(children);
        this.container.setSize(this.width, this.height);
        this.enableInteractivity();

        this.container.on('pointerover', () => {
            this.onPointerOver();
        });
        this.container.on('pointerout', () => {
            this.onPointerOut();
        });
        this.container.on('pointerup', () => {
            this.onClick();
        });
    }

    enableInteractivity() {
        const originX = this.width / 2;
        const originY = this.height / 2;
        this.container.setInteractive(
            new Phaser.Geom.Rectangle(originX, originY, this.width, this.height),
            Phaser.Geom.Rectangle.Contains
        );
    }

    disableInteractivity() {
        this.container.disableInteractive();
    }

    setEnabled(value) {
        this.enabled = value;
        this.applyState();
    }

    setVisible(value) {
        this.visible = value;
        this.applyState();
    }

    setPanelEnabled(value) {
        this.panelEnabled = value;
        this.applyState();
    }

    setPanelVisible(value) {
        this.panelVisible = value;
        this.applyState();
    }

    setScenePosition(sceneX, sceneY) {
        this.container.setPosition(sceneX, sceneY);
    }

    isEffectivelyEnabled() {
        return this.enabled && this.panelEnabled;
    }

    isEffectivelyVisible() {
        return this.visible && this.panelVisible;
    }

    applyState() {
        this.container.setVisible(this.isEffectivelyVisible());

        if (this.isEffectivelyEnabled()) {
            this.enableInteractivity();
            this.applyNormalStyles();
        } else {
            this.disableInteractivity();
            this.applyGrayscaleStyles();
        }
    }

    applyNormalStyles() {
        this.bg.setFillStyle(UIElement.toColorInt(this.bgRgba.r, this.bgRgba.g, this.bgRgba.b), this.bgRgba.a);

        if (this.border) {
            this.border.setStrokeStyle(UIElement.BORDER_WIDTH,
                UIElement.toColorInt(this.borderRgba.r, this.borderRgba.g, this.borderRgba.b), this.borderRgba.a);
        }

        this.label.setColor(UIElement.toHex(this.textRgba.r, this.textRgba.g, this.textRgba.b));
    }

    applyGrayscaleStyles() {
        const grayscale = UIElement.toGrayscale(this.bgRgba);
        this.bg.setFillStyle(UIElement.toColorInt(grayscale.r, grayscale.g, grayscale.b), grayscale.a);

        if (this.border) {
            const borderGrayscale = UIElement.toGrayscale(this.borderRgba);
            this.border.setStrokeStyle(UIElement.BORDER_WIDTH,
                UIElement.toColorInt(borderGrayscale.r, borderGrayscale.g, borderGrayscale.b), borderGrayscale.a);
        }

        const textGrayscale = UIElement.toGrayscale(this.textRgba);
        this.label.setColor(UIElement.toHex(textGrayscale.r, textGrayscale.g, textGrayscale.b));
    }

    onPointerOver() {
        if (!this.isEffectivelyEnabled()) {
            return;
        }

        const lightened = UIElement.lighten(this.bgRgba, UIElement.LIGHTEN_AMOUNT);
        this.bg.setFillStyle(UIElement.toColorInt(lightened.r, lightened.g, lightened.b), lightened.a);

        if (this.border) {
            const lightenedBorder = UIElement.lighten(this.borderRgba, UIElement.LIGHTEN_AMOUNT);
            this.border.setStrokeStyle(UIElement.BORDER_WIDTH,
                UIElement.toColorInt(lightenedBorder.r, lightenedBorder.g, lightenedBorder.b), lightenedBorder.a);
        }
    }

    onPointerOut() {
        this.applyNormalStyles();
    }

    onClick() {
        if (!this.isEffectivelyEnabled()) {
            return;
        }

        this.onClickFunction();
    }
}
