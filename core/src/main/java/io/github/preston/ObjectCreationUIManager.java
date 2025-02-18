package io.github.preston;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ObjectCreationUIManager {
    private final Table table;
    private final Skin skin;
    private final World world; // Add a reference to the World

    // UI Elements
    private TextButton createBoxButton;
    private TextButton createCustomBoxButton;
    private TextButton spawnPigButton;
    private Slider widthSlider;
    private Slider heightSlider;
    private Dialog customBoxDialog;

    // Preview Box
    private final ShapeRenderer shapeRenderer;
    private Table previewTable; // Container for the preview box

    public ObjectCreationUIManager(Table table, Skin skin, World world) { // Add World as a parameter
        this.table = table;
        this.skin = skin;
        this.world = world; // Initialize the World reference
        this.shapeRenderer = new ShapeRenderer();
        createUI();
    }

    private void createUI() {
        // Create Box Button
        createBoxButton = new TextButton("Create Box", skin);
        table.add(createBoxButton).pad(10).row();

        // Create Custom Box Button
        createCustomBoxButton = new TextButton("Create Custom Box", skin);
        table.add(createCustomBoxButton).pad(10).row();

        // Spawn Pig Button
        spawnPigButton = new TextButton("Spawn Pig", skin);
        table.add(spawnPigButton).pad(10).row();

        // Custom Box Dialog (Only Declare Once)
        customBoxDialog = new Dialog("Custom Box", skin) {
            @Override
            protected void result(Object object) {
                if (object.equals(true)) { // "Create" button was clicked
                    float width = widthSlider.getValue();
                    float height = heightSlider.getValue();

                    // Ensure world is available
                    if (world != null) {
                        new Box(world, new Vector2(500, 400), width, height);
                    } else {
                        System.err.println("World is not initialized!");
                    }
                }
            }
        };

        // Initialize sliders
        widthSlider = new Slider(10, 300, 1, false, skin);
        heightSlider = new Slider(10, 300, 1, false, skin);

        // Preview Table
        previewTable = new Table();
        previewTable.setBackground(skin.getDrawable("default-pane")); // Optional: Add a background
        previewTable.add(new BoxPreview(50, 50, shapeRenderer)).size(50, 50); // Default size

        // Add UI elements to the dialog in the correct order
        customBoxDialog.getContentTable().add(previewTable).colspan(2).pad(10).row();
        customBoxDialog.getContentTable().add(new Label("Width:", skin)).pad(10);
        customBoxDialog.getContentTable().add(widthSlider).pad(10).row();
        customBoxDialog.getContentTable().add(new Label("Height:", skin)).pad(10);
        customBoxDialog.getContentTable().add(heightSlider).pad(10).row();

        // Add default size buttons
        TextButton longSkinnyButton = new TextButton("Long Skinny", skin);
        TextButton shortMediumButton = new TextButton("Short Medium", skin);
        TextButton smallDotButton = new TextButton("Small Dot", skin);

        customBoxDialog.getContentTable().add(longSkinnyButton).colspan(2).pad(10).row();
        customBoxDialog.getContentTable().add(shortMediumButton).colspan(2).pad(10).row();
        customBoxDialog.getContentTable().add(smallDotButton).colspan(2).pad(10).row();

        // Add listeners for default size buttons
        longSkinnyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                widthSlider.setValue(200); // Long and skinny
                heightSlider.setValue(50);
                updatePreviewBox();
            }
        });

        shortMediumButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                widthSlider.setValue(100); // Short and medium
                heightSlider.setValue(100);
                updatePreviewBox();
            }
        });

        smallDotButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                widthSlider.setValue(20); // Small dot
                heightSlider.setValue(20);
                updatePreviewBox();
            }
        });

        // Clear any existing buttons and add only "Create" and "Cancel"
        customBoxDialog.getButtonTable().clearChildren();
        customBoxDialog.button("Create", true);
        customBoxDialog.button("Cancel", false);

        // Add listeners to update the preview box
        widthSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updatePreviewBox();
            }
        });

        heightSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updatePreviewBox();
            }
        });
    }

    private void updatePreviewBox() {
        // Clear the preview table and redraw the preview box
        previewTable.clear();

        // Use the actual slider values without capping
        float previewWidth = widthSlider.getValue();
        float previewHeight = heightSlider.getValue();

        // Add the preview box with the updated size
        previewTable.add(new BoxPreview(previewWidth, previewHeight, shapeRenderer))
                     .size(previewWidth, previewHeight); // Set the size dynamically
    }

    // Getters for UI elements
    public TextButton getCreateBoxButton() {
        return createBoxButton;
    }

    public TextButton getCreateCustomBoxButton() {
        return createCustomBoxButton;
    }

    public TextButton getSpawnPigButton() {
        return spawnPigButton;
    }

    public Slider getWidthSlider() {
        return widthSlider;
    }

    public Slider getHeightSlider() {
        return heightSlider;
    }

    public Dialog getCustomBoxDialog() {
        return customBoxDialog;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }
}