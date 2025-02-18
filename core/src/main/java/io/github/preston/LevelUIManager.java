package io.github.preston;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

public class LevelUIManager {
    private final Table table;
    private final Skin skin;

    // UI Elements
    private TextButton saveLevelButton;
    private TextButton loadLevelButton;
    private TextButton deleteLevelButton;
    private SelectBox<String> levelSelector;

    public LevelUIManager(Table table, Skin skin) {
        this.table = table;
        this.skin = skin;
        createUI();
    }

    private void createUI() {
        // Save Level Button
        saveLevelButton = new TextButton("Save Level", skin);
        table.add(saveLevelButton).pad(10).row();

        // Load Level Button
        loadLevelButton = new TextButton("Load Level", skin);
        table.add(loadLevelButton).pad(10).row();

        // Level Selector
        levelSelector = new SelectBox<>(skin);
        levelSelector.setItems("Level 1", "Level 2", "Level 3"); // Example levels
        table.add(levelSelector).pad(10).row();

        // Delete Level Button
        deleteLevelButton = new TextButton("Delete Level", skin);
        table.add(deleteLevelButton).pad(10).row();
    }

    public TextButton getSaveLevelButton() {
        return saveLevelButton;
    }

    public TextButton getLoadLevelButton() {
        return loadLevelButton;
    }

    public TextButton getDeleteLevelButton() {
        return deleteLevelButton;
    }

    public SelectBox<String> getLevelSelector() {
        return levelSelector;
    }

    public void updateLevelSelector(Array<String> levels) {
        levelSelector.setItems(levels);
    }
}