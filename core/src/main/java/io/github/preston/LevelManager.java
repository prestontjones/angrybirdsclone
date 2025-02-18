package io.github.preston;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class LevelManager {
    private World world;
    private final GameWorld gameWorld;
    Array<GameObject> gameObjects = new Array<>(); 

    public LevelManager(World world, GameWorld gameWorld) {
        this.world = world;
        this.gameWorld = gameWorld;
    }

    public void saveLevel(String levelName) {
        Array<GameObjectData> objectData = new Array<>();
        String filePath = "levels/" + levelName + ".json"; // Save in a "levels" folder

        for (GameObject gameObject : GameObject.getGameObjects()) {
            if (gameObject instanceof Box) {
                objectData.add(((Box) gameObject).getData());
            } else if (gameObject instanceof AngryPig) {
                objectData.add(((AngryPig) gameObject).getData());
            } else if (gameObject instanceof AngryBird) {
            }
        }

        // Serialize the data to JSON
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String jsonData = json.prettyPrint(objectData);

        // Write the JSON data to a file
        FileHandle file = Gdx.files.local(filePath);
        file.writeString(jsonData, false);

        System.out.println("Level saved to: " + filePath);
    }

    public void loadLevel(String fileName) {
        String filePath = "levels/" + fileName; // Load from the "levels" folder
        FileHandle file = Gdx.files.local(filePath);
    
        if (!file.exists()) {
            System.out.println("Level file not found: " + filePath);
            return;
        }
    
        // Replace the old world
        world.dispose();
        world = new World(new Vector2(0, -9.8f), true);
        gameWorld.setWorld(world);
    
        // Clear existing game objects
        Array<GameObject> toRemove = new Array<>();
        for (GameObject obj : GameObject.getGameObjects()) {
            if (!(obj instanceof AngryBird)) {
                toRemove.add(obj);
            }
        }
        GameObject.getGameObjects().removeAll(toRemove, true);

        // Read JSON data
        String jsonData = file.readString();
    
        // Deserialize JSON data
        Json json = new Json();
        @SuppressWarnings("unchecked")
        Array<GameObjectData> objectData = json.fromJson(Array.class, GameObjectData.class, jsonData);
    
        // Recreate objects from the saved data
        for (GameObjectData data : objectData) {
            try {
                switch (data.type) {
                    case "box":
                        if (data.x == null || data.y == null || data.width == null || data.height == null) {
                            System.out.println("Missing required fields for box object: " + data);
                            break;
                        }
                        gameObjects.add(new Box(world, new Vector2(data.x, data.y), data.width, data.height));
                        break;
                    case "pig":
                        if (data.x == null || data.y == null || data.radius == null) {
                            System.out.println("Missing required fields for pig object: " + data);
                            break;
                        }
                        gameObjects.add(new AngryPig(world, new Vector2(data.x, data.y + 1f), data.radius));
                        break;
                    case "bird":
                        if (data.x == null || data.y == null) {
                            System.out.println("Missing required fields for bird object: " + data);
                            break;
                        }
                        // gameObjects.add(new AngryBird(world, new Vector2(data.x, data.y)));
                        break;
                    default:
                        System.out.println("Unknown object type: " + data.type);
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error creating object from data: " + data);
                e.printStackTrace();
            }
        }
    
        System.out.println("Level loaded from: " + filePath);
    }
    public Array<String> getSavedLevels() {
        Array<String> levels = new Array<>();
        FileHandle levelsDir = Gdx.files.local("levels"); // Folder to save levels
        if (levelsDir.exists() && levelsDir.isDirectory()) {
            for (FileHandle file : levelsDir.list()) {
                if (file.extension().equals("json")) {
                    levels.add(file.nameWithoutExtension());
                }
            }
        }
        return levels;
    }
}