package io.github.preston;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;


public class GameWorld implements InputManager.InputListener {
    private World world;
    private final InputManager inputManager;
    private final OrthographicCamera camera;
    private final UIManager uiManager;
    private final LevelManager levelManager;
    private final Slingshot slingshot; 
    private Ground ground;
    private final float groundWidth = 1920; // Width of the ground
    private final float groundHeight = 10;  // Height of the ground
    private final Vector2 groundPosition = new Vector2(960, 190); // Center of the ground
    private float accumulator = 0f;
    private static final float TIME_STEP = 1 / 165f;

    public GameWorld(Vector2 gravity, OrthographicCamera camera) {
        this.world = new World(gravity, true);
        this.camera = camera;
        this.slingshot = new Slingshot(world, new Vector2(200, 370));
        this.inputManager = new InputManager(this, camera, world, slingshot);
        this.uiManager = new UIManager(world);
        this.levelManager = new LevelManager(world, this);
        this.ground = new Ground(world, groundPosition, groundWidth, groundHeight, null);

        // Use an InputMultiplexer to handle both UI and game input
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiManager.getStage()); // Add the Stage first
        multiplexer.addProcessor(inputManager); // Add the InputManager second
        Gdx.input.setInputProcessor(multiplexer);

        // Ensure the "levels" folder exists
        FileHandle levelsDir = Gdx.files.local("levels");
        if (!levelsDir.exists()) {
            levelsDir.mkdirs();
        }

        updateLevelSelector();
        attachUIListeners();
    }

    private void attachUIListeners() {
        // Save Level Button
        uiManager.getLevelUIManager().getSaveLevelButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String levelName = "Level" + (levelManager.getSavedLevels().size + 1); // Default name
                levelManager.saveLevel(levelName); // Save the level with a default name
                updateLevelSelector(); // Refresh the level selector
            }
        });

        // Load Level Button
        uiManager.getLevelUIManager().getLoadLevelButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedLevel = uiManager.getLevelUIManager().getLevelSelector().getSelected();
                if (selectedLevel != null) {
                    levelManager.loadLevel(selectedLevel + ".json"); // Load the selected level
                    inputManager.setWorld(world);
                }
            }
        });

        // Delete Level Button
        uiManager.getLevelUIManager().getDeleteLevelButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedLevel = uiManager.getLevelUIManager().getLevelSelector().getSelected();
                if (selectedLevel != null) {
                    FileHandle file = Gdx.files.local("levels/" + selectedLevel + ".json");
                    if (file.exists()) {
                        file.delete();
                        updateLevelSelector(); // Refresh the level selector
                        System.out.println("Level deleted: " + selectedLevel);
                    }
                }
            }
        });

        // Create Box Button
        uiManager.getObjectCreationUIManager().getCreateBoxButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                new Box(world, new Vector2(500, 400), 100, 100); // Example box
            }
        });

        // Create Custom Box Button
        uiManager.getObjectCreationUIManager().getCreateCustomBoxButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Show the custom box dialog
                uiManager.getObjectCreationUIManager().getCustomBoxDialog().show(uiManager.getStage());
            }
        });

        // Spawn Pig Button
        uiManager.getObjectCreationUIManager().getSpawnPigButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                new AngryPig(world, new Vector2(500, 400), 24f); // Example pig
            }
        });
    }

    private void updateLevelSelector() {
        Array<String> levels = levelManager.getSavedLevels();
        uiManager.getLevelUIManager().updateLevelSelector(levels);
    }

    public void update(float deltaTime) {
        accumulator += deltaTime;
    
        if (world != null) {
            while (accumulator >= TIME_STEP) {
                world.step(TIME_STEP, 6, 2);
                accumulator -= TIME_STEP;
            }
    
            // Clean up destroyed bodies
            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);
            for (Body body : bodies) {
                if (body.getUserData() instanceof GameObject) {
                    GameObject gameObject = (GameObject) body.getUserData();
                    if (!gameObject.getBody().isActive()) {
                        gameObject.dispose(); // Clean up the game object
                    }
                }
            }
        } else {
            System.out.println("World is null during update!");
        }
    
        for (GameObject gameObject : GameObject.getGameObjects()) {
            gameObject.update(deltaTime);
        }
    }

    public void render(SpriteBatch mainBatch) {
        camera.update();
        // Set the SpriteBatch's projection matrix to the camera's combined matrix
        mainBatch.setProjectionMatrix(camera.combined);

        for (GameObject gameObject : GameObject.getGameObjects()) {
            gameObject.render(mainBatch);
        }

        ground.render(mainBatch);
        uiManager.render();
        slingshot.render(mainBatch);
    }

    public void setWorld(World world) {
        this.world = world;
        this.inputManager.setWorld(world);
        this.slingshot.setWorld(world);

        if (ground != null) {
            ground = new Ground(world, groundPosition, groundWidth, groundHeight, null);
        }

    }

    public World getWorld() {
        return world;
    }

    @Override
    public void onTouchDown(float x, float y) {
    }

    @Override
    public void onTouchDragged(float x, float y) {
    }

    @Override
    public void onTouchUp(float x, float y) {
    }
}