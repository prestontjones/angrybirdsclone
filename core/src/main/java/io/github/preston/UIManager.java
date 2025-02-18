package io.github.preston;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class UIManager {
    private final CustomStage stage;
    private final Skin skin;
    private final BitmapFont font;
    private final SpriteBatch batch;

    // UI Managers
    private final LevelUIManager levelUIManager;
    private final ObjectCreationUIManager objectCreationUIManager;

    private boolean fpsToggle = true; // Default to showing FPS

    public UIManager(World world) { // Add World as a parameter
        this.batch = new SpriteBatch();
        this.stage = new CustomStage(new ScreenViewport(), batch);
        this.skin = new Skin(Gdx.files.internal("ui/uiskin.json")); // Load a skin file
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE); // Set font color

        // Create a single table for the entire UI
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().left(); // Align the table to the top-left corner
        mainTable.pad(20); // Add padding around the table
        stage.addActor(mainTable);

        // Initialize UI managers and pass the main table
        this.levelUIManager = new LevelUIManager(mainTable, skin);
        this.objectCreationUIManager = new ObjectCreationUIManager(mainTable, skin, world); // Pass the World reference
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (fpsToggle) {
            batch.begin();
            font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, Gdx.graphics.getHeight() - 20);
            batch.end();
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
        font.dispose();
        batch.dispose();
    }

    public Stage getStage() {
        return stage;
    }

    public void toggleFps() {
        fpsToggle = !fpsToggle;
    }

    // Getters for UI managers
    public LevelUIManager getLevelUIManager() {
        return levelUIManager;
    }

    public ObjectCreationUIManager getObjectCreationUIManager() {
        return objectCreationUIManager;
    }
}