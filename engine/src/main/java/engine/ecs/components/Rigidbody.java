package engine.ecs.components;

import engine.ecs.Component;
import engine.util.annotations.Slider;
import engine.util.annotations.SliderType;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Rigidbody extends Component {
    private String name;
    private int colliderType = 0;
    private float friction = 0.8f;
    @Slider(handleType = SliderType.Slider, min = 10, max = 20)
    public Vector3f velocity = new Vector3f(0,0.5f,0);
    @Slider(handleType = SliderType.Slider)
    public Matrix4f mat = new Matrix4f();
    public transient Vector4f tmp = new Vector4f(0);
}
