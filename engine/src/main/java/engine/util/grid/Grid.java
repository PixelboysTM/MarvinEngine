package engine.util.grid;

import engine.Window;
import engine.renderer.Line2D;
import engine.renderer.Shader;
import engine.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Grid {
    private float width, height, ofX, ofY, spacing;
    private Vector3f color;
    private float thickness;
    private Shader shader = AssetPool.getShader("grid.glsl");

    private int vaoID, vboID;

    private int lineNums = 0;


    public Grid(int width, int height, int ofX, int ofY, int spacing, Vector3f color, float thickness) {
        this.width = width;
        this.height = height;
        this.ofX = ofX;
        this.ofY = ofY;
        this.spacing = spacing;
        this.color = color;
        this.thickness = thickness;
        init();
    }

    public void init(){
        List<Line2D> lines = new ArrayList<>();
        for (int i = 0; i < width / spacing; i++) {
            lines.add(new Line2D(new Vector2f( ofX +spacing * i, ofY), new Vector2f(ofX + spacing * i, height), new Vector3f(0), 0));
        }
        for (int i = 0; i < height / spacing; i++) {
            lines.add(new Line2D(new Vector2f(ofX,ofY  + spacing * i), new Vector2f(width,ofY + spacing * i), new Vector3f(0), 0));
        }
        lineNums = lines.size();
        float[] vertexArray = new float[lines.size() * 2 * 6];
        int index = 0;
        for (Line2D line : lines){
            for (int i = 0; i < 2; i++) {
                Vector2f position = i == 0 ? line.getStart() : line.getEnd();
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = this.color.x;
                vertexArray[index + 3] = this.color.y;
                vertexArray[index + 4] = this.color.z;
                vertexArray[index + 5] = -1; // TODO: Fix upload extra data.
                index += 6;
            }

        }


        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, lineNums * 2 * Float.BYTES, GL_STATIC_DRAW); //TODO: To improve performance make Dynamic drawing and not recreate when changing size.

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 6 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glLineWidth(thickness);
        glEnable(GL_LINE_SMOOTH);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray, GL_STATIC_DRAW);
    }

    public void draw(){
        shader.bind();
        shader.uploadMAt4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMAt4f("uView", Window.getScene().camera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glLineWidth(thickness);
        glDrawArrays(GL_LINES, 0, lineNums * 6 * 2);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.unbind();
    }
}
