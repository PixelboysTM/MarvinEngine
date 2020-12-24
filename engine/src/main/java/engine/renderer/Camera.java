package engine.renderer;

import engine.util.Settings;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f position;

    public Camera(Vector2f position){
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection(){
        projectionMatrix.identity();
        projectionMatrix.ortho(0, Settings.ViewportWidth, 0, Settings.ViewportHeight, 0,100 );
        projectionMatrix.invert(inverseProjection);

    }
    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0,0,-1);
        Vector3f cameraUp = new Vector3f(0,1,0);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x,position.y, 20),
                cameraFront.add(position.x,position.y,0),
                cameraUp);

        viewMatrix.invert(inverseView);
        return  this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

    public int getWidth(){return 1920;}
    public int getHeight(){return 1080;}

    public Matrix4f getInverseProjection() {
        return inverseProjection;
    }

    public Matrix4f getInverseView() {
        return inverseView;
    }
}
