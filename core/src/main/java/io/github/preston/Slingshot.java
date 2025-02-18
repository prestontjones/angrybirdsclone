package io.github.preston;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

public class Slingshot {
    private AngryBird loadedBird;
    private World world;
    private boolean isDragging;
    private final Vector2 basePosition;
    private final TrajectoryRenderer trajectoryRenderer;
    private final Texture texture;
    private final Vector2 textureOffset = new Vector2(-50f, -180f);
    private final Vector2 dragStart = new Vector2();
    private final Vector2 currentDrag = new Vector2();
    private final Vector2 launchVelocity = new Vector2();
    
    // Configuration
    private static final float MAX_PULL_DISTANCE = 50f;
    private static final float LAUNCH_MULTIPLIER = 100f;

    public Slingshot(World world, Vector2 position) {
        this.world = world;
        this.basePosition = new Vector2(position);
        this.texture = new Texture(Gdx.files.internal("slingshot.png"));
        this.trajectoryRenderer = new TrajectoryRenderer();
        spawnBird();
    }

    private void spawnBird() {
        // Create bird outside of GameObject system
        loadedBird = new AngryBird(world, basePosition.cpy());
        loadedBird.setKinematic();
    }

    public void updateDrag(Vector2 worldPoint) {
        if (!isDragging || loadedBird == null) return;
        
        currentDrag.set(worldPoint);
        Vector2 pullVector = new Vector2(currentDrag).sub(basePosition);
        
        if (pullVector.len() > MAX_PULL_DISTANCE) {
            pullVector.setLength(MAX_PULL_DISTANCE);
        }
        
        loadedBird.getBody().setTransform(
            basePosition.cpy().sub(pullVector), 
            loadedBird.getBody().getAngle()
        );
        
        launchVelocity.set(pullVector).scl(LAUNCH_MULTIPLIER);
    }

    public void startDrag(Vector2 worldPoint) {
        isDragging = true;
        dragStart.set(worldPoint);
        currentDrag.set(worldPoint);
    }

    public void release() {
        if (!isDragging || loadedBird == null) return;
        
        // Apply final velocity
        loadedBird.setDynamic();
        loadedBird.getBody().setLinearVelocity(launchVelocity);
        
        // Reset state
        isDragging = false;
        loadedBird = null;
        
        // Respawn bird after delay
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() { spawnBird(); }
        }, 1.5f);
    }

    public void setWorld(World newWorld) {
        // Cleanup old bird
        if (loadedBird != null) {
            loadedBird = null;
        }
        
        this.world = newWorld;
        spawnBird();
    }

    public void render(SpriteBatch batch) {
        // Draw slingshot texture
        float slingshotWidth = 100;
        float slingshotHeight = 200;
        batch.draw( texture,
                    basePosition.x + textureOffset.x,
                    basePosition.y + textureOffset.y,
                    slingshotWidth,
                    slingshotHeight);

        if (isDragging && loadedBird != null) {
            batch.end();
            trajectoryRenderer.render(
                loadedBird.getPosition(),
                launchVelocity,
                world.getGravity()
            );
            batch.begin();
        }
    }

    public AngryBird getLoadedBird() {
        return loadedBird;
    }

    public boolean isDragging() {
        return isDragging;
    }
    
    public boolean isBirdLoaded() {
        return loadedBird != null;
    }
}