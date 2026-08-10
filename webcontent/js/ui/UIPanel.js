'use strict';

class UIPanel extends UIElement {

    constructor(scene, {
        id,
        parentElement,
        x,
        y,
        width,
        height,
        backgroundColor,
        borderColor,
        enabled,
        visible
    }) {
        super(id, parentElement);

        this.scene = scene;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.enabled = enabled;
        this.visible = visible;

        this.panelEnabled = true;
        this.panelVisible = true;
        this.elements = [];

        this.bgRgba = UIElement.parseColor(this.backgroundColor);
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

        this.container.add(children);
        this.container.setSize(this.width, this.height);
    }

    addElement(element) {
        element.parentElement = this;
        element.setScenePosition(this.x + element.x, this.y + element.y);
        element.setPanelEnabled(this.isEffectivelyEnabled());
        element.setPanelVisible(this.isEffectivelyVisible());
        this.elements.push(element);
    }

    setEnabled(value) {
        this.enabled = value;
        this.applyState();
        this.elements.forEach(element => {
            element.setPanelEnabled(this.isEffectivelyEnabled());
        });
    }

    setVisible(value) {
        this.visible = value;
        this.applyState();
        this.elements.forEach(element => {
            element.setPanelVisible(this.isEffectivelyVisible());
        });
    }

    setPanelEnabled(value) {
        this.panelEnabled = value;
        this.applyState();
        this.elements.forEach(element => {
            element.setPanelEnabled(this.isEffectivelyEnabled());
        });
    }

    setPanelVisible(value) {
        this.panelVisible = value;
        this.applyState();
        this.elements.forEach(element => {
            element.setPanelVisible(this.isEffectivelyVisible());
        });
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
            this.applyNormalStyles();
        } else {
            this.applyGrayscaleStyles();
        }
    }

    applyNormalStyles() {
        this.bg.setFillStyle(UIElement.toColorInt(this.bgRgba.r, this.bgRgba.g, this.bgRgba.b), this.bgRgba.a);

        if (this.border) {
            this.border.setStrokeStyle(UIElement.BORDER_WIDTH,
                UIElement.toColorInt(this.borderRgba.r, this.borderRgba.g, this.borderRgba.b), this.borderRgba.a);
        }
    }

    applyGrayscaleStyles() {
        const grayscale = UIElement.toGrayscale(this.bgRgba);
        this.bg.setFillStyle(UIElement.toColorInt(grayscale.r, grayscale.g, grayscale.b), grayscale.a);

        if (this.border) {
            const borderGrayscale = UIElement.toGrayscale(this.borderRgba);
            this.border.setStrokeStyle(UIElement.BORDER_WIDTH,
                UIElement.toColorInt(borderGrayscale.r, borderGrayscale.g, borderGrayscale.b), borderGrayscale.a);
        }
    }
}
