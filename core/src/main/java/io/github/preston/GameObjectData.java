package io.github.preston;

public class GameObjectData {
    public String type; // "box", "pig", or "bird"
    public String name;
    public Float x, y, width, height, radius;
    public Float rotation; // Rotation in radians;

    public GameObjectData() {
        // Default constructor for serialization
    }

    public GameObjectData(String type, float x, float y, float width, float height, float rotation) { // For Boxes
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    public GameObjectData(String type, float x, float y, float radius, float rotation) { // For Pigs
        this.type = type;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.rotation = rotation;
    }
}