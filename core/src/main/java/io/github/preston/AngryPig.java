package io.github.preston;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class AngryPig extends GameObject {
    float renderWidth = 48f;
    float renderHeight = 48f;
    float radius = 20f;
    private boolean isDragging = false;
    private final Vector2 lastPosition = new Vector2();
    private boolean isDraggable = true;
    @SuppressWarnings("unused")
    private final Vector2 position; 


    public AngryPig(World world, Vector2 position, float radius) {
        super(BodyBuilder.createCircle(world, position, radius), new Texture("AngryPig.png"));  
        this.position = position;
        this.body.setUserData(this);
    }

    @Override
    public void render(SpriteBatch batch) {
        // Render the pig with correct position and rotation
        float rotation = (float) Math.toDegrees(body.getAngle()); // Get the body's rotation in degrees

        batch.draw(
            texture, // Texture to render
            body.getPosition().x - renderWidth / 2, // X position (centered)
            body.getPosition().y - renderHeight / 2, // Y position (centered)
            renderWidth / 2, // Origin X (center of the pig)
            renderHeight / 2, // Origin Y (center of the pig)
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

    public void startDrag(Vector2 worldPos) {
        if (isDraggable) {
            isDragging = true;
            body.setLinearVelocity(0, 0);
            body.setAngularVelocity(0);
            body.setType(BodyDef.BodyType.KinematicBody); // Switch to kinematic for dragging
            lastPosition.set(body.getPosition());
        }
    }

    public void updateDrag(Vector2 worldPos) {
        if (isDragging) {
            body.setTransform(worldPos, body.getAngle());
        }
    }

    public void endDrag() {
        if (isDragging) {
            isDragging = false;
            body.setType(BodyDef.BodyType.DynamicBody);

            // Calculate velocity based on drag movement
            // Vector2 velocity = new Vector2(body.getPosition()).sub(lastPosition).scl(60); // Scale for smoothness
            // body.setLinearVelocity(velocity);
        }
    }

    public void rotate(float degrees) {
        if (isDragging) {
            body.setTransform(body.getPosition(), body.getAngle() + (float) Math.toRadians(degrees));
        }
    }

    public void setImmovable() {
        isDraggable = false;
        body.setType(BodyDef.BodyType.KinematicBody); // Set to static
    }

    public GameObjectData getData() {
        return new GameObjectData(
            "pig", // Type
            body.getPosition().x, // X position
            body.getPosition().y, // Y position
            radius, // Radius
            body.getAngle() // Rotation (in radians)
        );
    }

}