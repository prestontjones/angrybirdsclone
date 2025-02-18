package io.github.preston;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyBuilder {

    public static Body createCircle(World world, Vector2 position, float radius) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyType.DynamicBody;

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 3.4f;
        fixtureDef.friction = .6f;
        fixtureDef.restitution = .1f; // Bounce effect

        body.createFixture(fixtureDef);
        shape.dispose(); // Clean up shape memory

        return body;
    }

    public static Body createBox(World world, Vector2 position, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyType.DynamicBody;

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2); // Box centered around its position

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = .5f;
        fixtureDef.friction = .4f;
        fixtureDef.restitution = .2f; // Controls bounce effect

        body.createFixture(fixtureDef);
        shape.dispose(); // Clean up shape memory
        
        return body;
    }
}
