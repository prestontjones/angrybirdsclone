package io.github.preston;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BoxPreview extends Actor {
    private final float width;
    private final float height;
    private final ShapeRenderer shapeRenderer;

    public BoxPreview(float width, float height, ShapeRenderer shapeRenderer) {
        this.width = width;
        this.height = height;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
        batch.end(); // End the SpriteBatch to use ShapeRenderer

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE); // Set the color of the preview box
        shapeRenderer.rect(getX(), getY(), width, height); // Draw the box
        shapeRenderer.end();

        batch.begin(); // Restart the SpriteBatch for other UI rendering
    }
}