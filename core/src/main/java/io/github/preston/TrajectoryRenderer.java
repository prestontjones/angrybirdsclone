package io.github.preston;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class TrajectoryRenderer {
    private final ShapeRenderer shapeRenderer;
    private final int dotCount;
    private final float dotSize;
    private final float simulationTimeStep;

    public TrajectoryRenderer() {
        this.shapeRenderer = new ShapeRenderer();
        this.dotCount = 18; // Enough dots to show the arc
        this.dotSize = 4f;
        this.simulationTimeStep = 1/165f; // Fixed physics step for trajectory prediction
    }

    public void render(Vector2 startPos, Vector2 velocity, Vector2 gravity) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(1, 1, 1, 0.3f));
        
        Vector2 position = new Vector2(startPos);
        Vector2 currentVelocity = new Vector2(velocity);
        
        for (int i = 0; i < dotCount; i++) {
            // Update physics
            currentVelocity.mulAdd(gravity, simulationTimeStep);
            position.mulAdd(currentVelocity, simulationTimeStep);
            
            // Fade dots along the path
            float alpha = 1 - (i / (float) dotCount);
            shapeRenderer.setColor(1, 1, 1, alpha * 0.3f);
            shapeRenderer.circle(position.x, position.y, dotSize * alpha);
        }
        
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}