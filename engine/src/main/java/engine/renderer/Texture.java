package engine.renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    private String filepath;
    private transient int texID;
    private int width, height, channels;

    public Texture() {
        texID = -1;
        width = -1;
        height = -1;
    }

    public Texture(int w, int h) {
        this.filepath = "Generated";

        //Gen texture
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);

    }

    public void init(String filepath) {
        this.filepath = filepath;

        //Gen texture
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        //Set tex parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if (image != null) {
            this.width = width.get(0);
            this.height = height.get(0);

            if (channels.get(0) == 3)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            else if (channels.get(0) == 4)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            else
                assert false : "ERROR: Unknown number of channles " + channels.get(0);
        } else {
            assert false : "ERROR: Could not load image '" + filepath + "'";
        }

        stbi_image_free(image);
        //glBindTexture(GL_TEXTURE_2D,0);

    }

    public void init(ByteBuffer data,int w, int h, int c, String id) {
        this.filepath = "custom_" + id;


        //Gen texture
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        //Set tex parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        if (data != null) {
            this.width = w;
            this.height = h;
            this.channels = c;

            if (c == 3)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
            else if (c == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
                int i = glGetError();
            } else
                assert false : "ERROR: Unknown number of channles " + c;
        } else {
            assert false : "ERROR: Could not load image '" + filepath + "'";
        }

        stbi_image_free(data);
        //glBindTexture(GL_TEXTURE_2D,0);

    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getId() {
        return texID;
    }

    public String getFilepath() {
        return this.filepath;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Texture)) return false;
        Texture texture = (Texture) o;
        return texture.getWidth() == this.width &&
                texture.getHeight() == this.height &&
                texture.getId() == this.texID &&
                texture.getFilepath().equals(this.filepath);
    }


    public int getChannels() {
        return channels;
    }
}
