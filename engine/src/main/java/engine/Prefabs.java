package engine;

import engine.ecs.GameObject;
import engine.ecs.Transform;
import engine.ecs.components.Sprite;
import engine.ecs.components.SpriteRenderer;
import org.joml.Vector2f;

public class Prefabs {
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY){
        GameObject block = new GameObject("Sprite_Object_Gen", new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }
}
