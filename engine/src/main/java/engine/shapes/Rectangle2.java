package engine.shapes;

import engine.Window;
import engine.renderer.Shader;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.GL_TRIANGLES;
import static org.lwjgl.opengl.GL20.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL20.glDrawElements;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Rectangle2 {
    float x,y,w,h;

    private Vector4f color;
    private Vector4f outline;

    private Shader shader;
    private int vaoID, vboID, eboID;

    private float[] vertexArray;
    private int[] elementArray = {
            2,1,0,
            0,1,3
    };

    public Rectangle2(float x, float y, float w, float h, Vector4f color, Vector4f outline) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
        this.outline = outline;
    }

    public void create(){
        vertexArray = new float[]{
                //position             //color                                            //Outline
                x+w,   y,0.0f,        color.x,color.y,color.z,color.w, outline.x,          outline.y, outline.z, outline.w, //Bottom Right 0
                x, y+h,0.0f,          color.x,color.y,color.z,color.w, outline.x,          outline.y, outline.z, outline.w,//Top Left     1
                x+w, y+h,0.0f,        color.x,color.y,color.z,color.w, outline.x,          outline.y, outline.z, outline.w,//Top Right    2
                x,   y,0.0f,          color.x,color.y,color.z,color.w, outline.x,          outline.y, outline.z, outline.w,//Bottom Left  3
        };


        shader = new Shader("assets/shaders/primitive2.glsl");
        shader.compile();

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);


        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);


        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int outlineSIze = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize + outlineSIze) * floatSizeBytes;
        glVertexAttribPointer(0,positionSize,  GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, outlineSIze, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * floatSizeBytes);
        glEnableVertexAttribArray(2);
    }

    public void draw(){
        //Bind shader programm
        shader.bind();
        shader.uploadMAt4f("uProj", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMAt4f("uView", Window.getScene().camera().getViewMatrix());
        //Bind vao
        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        shader.unbind();
    }
}
