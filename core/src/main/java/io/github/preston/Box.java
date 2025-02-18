package io.github.preston;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Box extends GameObject {
    private final Vector2 position;
    public final float width;
    public final float height;
    private boolean isDragging = false;
    private final Vector2 dragStartPosition = new Vector2();
    private final Vector2 lastPosition = new Vector2();
    private boolean isGround;
    private boolean isDraggable = true; // Controls whether the box can be dragged

    public Box(World world, Vector2 position, float width, float height) {
        super(BodyBuilder.createBox(world, position, width, height), new Texture("box.png"));  
        this.position = position;
        this.width = width;
        this.height = height;
        this.body.setUserData(this); // Set user data to this box
    }

    public Box(World world, Vector2 position, float width, float height, boolean isStatic) {
        super(BodyBuilder.createBox(world, position, width, height), new Texture("box.png"));  
        this.position = position;
        this.width = width;
        this.height = height;
        if (isStatic) {
            setKinematic();
        }
        this.body.setUserData(this); // Set user data to this box
    }

    public boolean isGround() {
        return isGround;
    }

    @Override
    public void render(SpriteBatch batch) {
        // If the texture is null, don't render anything (invisible box)
        if (texture == null) {
            return;
        }
        float renderX = body.getPosition().x - width / 2;
        float renderY = body.getPosition().y - height / 2;
        float rotation = (float) Math.toDegrees(body.getAngle());

        batch.draw(
            texture, // Texture to render
            renderX, // X position (centered)
            renderY, // Y position (centered)
            width / 2, // Origin X (center of the box)
            height / 2, // Origin Y (center of the box)
            width, // Width of the box
            height, // Height of the box
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
            // System.out.println("Box: Start dragging at (" + worldPos.x + ", " + worldPos.y + ")");
            isDragging = true;
            body.setLinearVelocity(0, 0);
            body.setAngularVelocity(0);
            body.setType(BodyDef.BodyType.KinematicBody); // Switch to kinematic for dragging
            lastPosition.set(body.getPosition());
        }
    }
    
    public void updateDrag(Vector2 worldPos) {
        if (isDragging) {
            // System.out.println("Box: Dragging to (" + worldPos.x + ", " + worldPos.y + ")");
            body.setTransform(worldPos, body.getAngle());
        }
    }
    
    public void endDrag() {
        if (isDragging) {
            // System.out.println("Box: End dragging");
            isDragging = false;
            body.setType(BodyDef.BodyType.DynamicBody);
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
            "box", // Type
            body.getPosition().x, // X position
            body.getPosition().y, // Y position
            width, // Width
            height, // Height
            body.getAngle() // Rotation (in radians)
        );
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose(); // Dispose of the texture
        }
        super.dispose(); // Remove the box from the game objects list
    }

}