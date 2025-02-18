package io.github.preston;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

public class InputManager extends InputAdapter {
    private final InputListener listener;
    private final OrthographicCamera camera;
    private World world; // Reference to the current World
    private GameObject draggedObject; // Track either a Box or AngryPig
    private final Slingshot slingshot;

    public InputManager(InputListener listener, OrthographicCamera camera, World world, Slingshot slingshot) {
        this.listener = listener;
        this.camera = camera;
        this.world = world;
        this.slingshot = slingshot; 
    }

    // Update the World reference when loading a new level
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 worldPos = screenToWorld(screenX, screenY);

        if (button == Input.Buttons.RIGHT) { //Right Click to delete box
            GameObject object = getObjectAtPosition(worldPos);
            if (object instanceof Box) {
                world.destroyBody(object.getBody()); // Destroy the box's physics body
                GameObject.removeObject(object); // Remove the box from the game objects list
                System.out.println("Box deleted!");
            }
            return true; // Stop further processing
        }

        if (button == Input.Buttons.MIDDLE) { // Middle-click to duplicate a box
            GameObject object = getObjectAtPosition(worldPos);
            if (object instanceof Box) {
                Box box = (Box) object;
                // Duplicate the box with the same properties
                new Box(world, new Vector2(400, 500), box.width, box.height);
                System.out.println("Box duplicated!");
            }
            return true; // Stop further processing
        }

        if (slingshot.isBirdLoaded()) {
            AngryBird bird = slingshot.getLoadedBird();
            Vector2 birdPos = bird.getBody().getPosition();
            float clickRadius = 25.0f; // Radius in world units (adjust as needed)
            
            if (birdPos.dst(worldPos) <= clickRadius) {
                slingshot.startDrag(worldPos);
                return true; // Bird drag takes priority
            }
        }

        // Handle dragging for boxes and pigs
        draggedObject = getObjectAtPosition(worldPos);
        if (draggedObject != null) {
            System.out.println("InputManager: Dragging object: " + draggedObject.getClass().getSimpleName());
            if (draggedObject instanceof Box) {
                ((Box) draggedObject).startDrag(worldPos);
            } else if (draggedObject instanceof AngryPig) {
                ((AngryPig) draggedObject).startDrag(worldPos);
            }
        }

        listener.onTouchDown(worldPos.x, worldPos.y);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 worldPos = screenToWorld(screenX, screenY);

        if (slingshot.isDragging()) {
            slingshot.updateDrag(worldPos);
        } else if (draggedObject != null) {
            if (draggedObject instanceof Box) {
                ((Box) draggedObject).updateDrag(worldPos);
            } else if (draggedObject instanceof AngryPig) {
                ((AngryPig) draggedObject).updateDrag(worldPos);
            }
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Vector2 worldPos = screenToWorld(screenX, screenY);

        if (slingshot.isDragging()) {
            slingshot.release();
        } else if (draggedObject != null) {
            if (draggedObject instanceof Box) {
                ((Box) draggedObject).endDrag();
            } else if (draggedObject instanceof AngryPig) {
                ((AngryPig) draggedObject).endDrag();
            }
            draggedObject = null;
        }

        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (draggedObject != null) {
            if (keycode == Input.Keys.Q) {
                if (draggedObject instanceof Box) {
                    ((Box) draggedObject).rotate(5); // Rotate left
                } else if (draggedObject instanceof AngryPig) {
                    ((AngryPig) draggedObject).rotate(5); // Rotate left
                }
            } else if (keycode == Input.Keys.E) {
                if (draggedObject instanceof Box) {
                    ((Box) draggedObject).rotate(-5); // Rotate right
                } else if (draggedObject instanceof AngryPig) {
                    ((AngryPig) draggedObject).rotate(-5); // Rotate right
                }
            }
        }
        return true;
    }

    private GameObject getObjectAtPosition(Vector2 position) {
        final GameObject[] foundObject = { null };

        // Query the world for fixtures at the position
        world.QueryAABB(fixture -> {
            if (fixture.testPoint(position)) {
                // Get the GameObject associated with the fixture's body
                Object userData = fixture.getBody().getUserData();
                if (userData instanceof GameObject) { // Ensure the user data is a GameObject
                    foundObject[0] = (GameObject) userData;
                    return false; // Stop the query
                }
            }
            return true; // Continue the query
        }, position.x - 0.1f, position.y - 0.1f, position.x + 0.1f, position.y + 0.1f);

        return foundObject[0]; // This will be null if no valid GameObject was found
    }

    private Vector2 screenToWorld(int screenX, int screenY) {
        Vector3 worldCoords = new Vector3(screenX, screenY, 0);
        camera.unproject(worldCoords);
        return new Vector2(worldCoords.x, worldCoords.y);
    }

    public interface InputListener {
        void onTouchDown(float x, float y);
        void onTouchDragged(float x, float y);
        void onTouchUp(float x, float y);
    }
}