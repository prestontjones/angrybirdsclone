package io.github.preston;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture backgroundTexture;
   // private FPSCounter fpsCounter;
    private GameWorld gameWorld;


    @Override
    public void create() {
        backgroundTexture = new Texture(Gdx.files.internal("angryBirdsBackGround.jpg"));
        batch = new SpriteBatch();
        //fpsCounter = new FPSCounter();
    
        // Initialize the camera
        OrthographicCamera camera = new OrthographicCamera(1920, 1080);
        camera.position.set(960, 540, 0);
        camera.update();
    
        gameWorld = new GameWorld(new Vector2(0, -98.0f), camera);
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the game world
        gameWorld.update(deltaTime);

        // Render the background and game objects
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        //fpsCounter.render(batch);
        gameWorld.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        //fpsCounter.dispose();
    }
}
