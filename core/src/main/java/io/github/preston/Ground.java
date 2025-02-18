package io.github.preston;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Ground {
    private final Body body;
    private final Texture texture; // Can be null
    private final float width, height;

    public Ground(World world, Vector2 position, float width, float height, String texturePath) {
        this.width = width;
        this.height = height;

        // Load texture if a path is provided
        if (texturePath != null) {
            this.texture = new Texture(texturePath);
        } else {
            this.texture = null; // No texture (invisible ground)
        }

        // Create the ground body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.StaticBody; // Ground is static

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2); // Box centered around its position

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f; // Ground has no density (it's static)
        fixtureDef.friction = 0.6f; // Adjust friction as needed
        fixtureDef.restitution = 0f; // No bounce

        body.createFixture(fixtureDef);
        shape.dispose(); // Clean up shape memory
    }

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

    public void dispose() {
        if (texture != null) {
            texture.dispose(); // Dispose of the texture if it exists
        }
    }

    public float getWidth() {
        return width;
    }
    
    public float getHeight() {
        return height;
    }
    
}