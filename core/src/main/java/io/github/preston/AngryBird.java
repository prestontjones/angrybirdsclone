package io.github.preston;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class AngryBird extends GameObject {
    float renderWidth = 48f;
    float renderHeight = 48f;
    @SuppressWarnings("unused")
    private final Vector2 position;

    // Custom pivot point (relative to the texture's top-left corner)
    private final float pivotX = renderWidth / 2 + 3f; // Adjust this to set the X pivot
    private final float pivotY = renderHeight / 2 - 3f; // Adjust this to set the Y pivot

    public AngryBird(World world, Vector2 position) {
        super(BodyBuilder.createCircle(world, position, 20f), new Texture("Angry Red Bird.png"));
        this.position = position;
    }

    @Override
    public void render(SpriteBatch batch) {
        // Render the bird with correct position and rotation
        float rotation = (float) Math.toDegrees(body.getAngle()); // Get the body's rotation in degrees

        batch.draw(
            texture, // Texture to render
            body.getPosition().x - pivotX, // X position (adjusted for pivot)
            body.getPosition().y - pivotY, // Y position (adjusted for pivot)
            pivotX, // Origin X (pivot point X)
            pivotY, // Origin Y (pivot point Y)
            renderWidth, // Width of the rendered texture
            renderHeight, // Height of the rendered texture
            1, // Scale X
            1, // Scale Y
            rotation, // Rotation in degrees
            0, // Source X (for texture regions)
            0, // Source Y (for texture regions)
            texture.getWidth(), // Source width
            texture.getHeight(), // Source height
            false, // Flip X
            false // Flip Y
        );
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

}