package io.github.preston;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CustomStage extends Stage {
    public CustomStage(Viewport viewport, SpriteBatch batch) {
        super(viewport, batch);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean handled = super.touchDown(screenX, screenY, pointer, button);
        return handled;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean handled = super.touchDragged(screenX, screenY, pointer);
        return handled;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean handled = super.touchUp(screenX, screenY, pointer, button);
        return handled;
    }
}