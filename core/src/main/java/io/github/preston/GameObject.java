package io.github.preston;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;

public abstract class GameObject {
    protected Body body; 
    protected Texture texture;
    protected float width, height;
    private static Array<GameObject> gameObjects = new Array<>();

    public GameObject(Body body, Texture texture) {
        this.body = body;
        this.texture = texture;
        gameObjects.add(this); // Add this object to the global list
        System.out.println("Created GameObject: " + this.getClass().getSimpleName());
    }

    public void update(float deltaTime) {
        // Update logic for the object
    }

    public void render(SpriteBatch batch) {
        // Render the object
        batch.draw(texture, body.getPosition().x, body.getPosition().y);
    }

    public void dispose() {
        System.out.println("Disposing GameObject: " + this.getClass().getSimpleName());
        if (texture != null) {
            texture.dispose(); // Dispose of the texture
        }
        if (body != null) {
            body.getWorld().destroyBody(body); // Destroy the body
        }
        removeObject(this);
    }

    public static void removeObject(GameObject object) {
        gameObjects.removeValue(object, true); // Remove the object from the global list
    }

    public void setKinematic() {
        if (body.getType() != BodyDef.BodyType.KinematicBody) {
            body.setType(BodyDef.BodyType.KinematicBody);
        }
    }

    public void setDynamic() {
        if (body.getType() != BodyDef.BodyType.DynamicBody) {
            body.setType(BodyDef.BodyType.DynamicBody);
        }
    }

    public static void addGameObject(GameObject object) {
        gameObjects.add(object); // Add a new object to the global list
    }

    public Body getBody() {
        return body;
    }

    public Texture getTexture() {
        return texture;
    }

    public static Array<GameObject> getGameObjects() {
        return gameObjects; // Return the global list of objects
    }

    public GameObject() {
        gameObjects.add(this);
    }

    public static void clearLevelObjects() {
        // Only remove non-bird objects when loading new levels
            Array<GameObject> toRemove = new Array<>(gameObjects);
        for (GameObject obj : toRemove) {
            obj.dispose();
        }
        gameObjects.clear();
    }
}