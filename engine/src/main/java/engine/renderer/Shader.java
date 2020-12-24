package engine.renderer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int pID;

    private String vSrc, fSrc, filePath;
    private boolean beingUse = false;


    public Shader(String filepath){
        this.filePath = filepath;
        try{
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n",index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")){
                vSrc = splitString[1];
            }else
            if (firstPattern.equals("fragment")){
                fSrc = splitString[1];
            }else{
                throw new IOException("Cant find vertex shader in "+ firstPattern);
            }

            if (secondPattern.equals("vertex")){
                vSrc = splitString[2];
            }else
            if (secondPattern.equals("fragment")){
                fSrc = splitString[2];
            }else{
                throw new IOException("Cant find vertex shader in "+ secondPattern);
            }


        }catch (IOException e){
            e.printStackTrace();
            assert false : "ERROR: Could not open file for shader " + filepath;
        }
    }

    public void compile(){
        int fID, vID;
        vID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vID, vSrc);
        glCompileShader(vID);
        if (glGetShaderi(vID, GL_COMPILE_STATUS) == GL_FALSE){
            int len = glGetShaderi(vID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR '"+ filePath + "'\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vID, len));
            assert false : "";
        }

        fID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fID, fSrc);
        glCompileShader(fID);
        if (glGetShaderi(fID, GL_COMPILE_STATUS) == GL_FALSE){
            int len = glGetShaderi(fID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR '"+ filePath + "'\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fID, len));
            assert false : "";
        }

        pID = glCreateProgram();
        glAttachShader(pID, vID);
        glAttachShader(pID, fID);
        glLinkProgram(pID);
        if (glGetProgrami(pID, GL_LINK_STATUS) == GL_FALSE){
            int len = glGetProgrami(pID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR '"+ filePath + "'\n\tProgramm shader linking failed");
            System.out.println(glGetProgramInfoLog(fID));
            assert false : "";
        }
    }

    public void bind(){
        //Bind shader programm
        if (!beingUse) {
            glUseProgram(pID);
            beingUse = true;
        }
    }

    public void unbind(){


        glUseProgram(0);
        beingUse = false;
    }

    public void uploadMAt4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(pID, varName );
        bind();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec){
        int varLocation = glGetUniformLocation(pID, varName);
        bind();
        glUniform4f(varLocation, vec.x,vec.y,vec.z,vec.w);
    }

    public void uploadFloat(String varName, float data){
        int varLocation = glGetUniformLocation(pID, varName);
        bind();
        glUniform1f(varLocation, data);
    }

    public void uploadInt(String varName, int data){
        int varLocation = glGetUniformLocation(pID,varName);
        bind();
        glUniform1i(varLocation, data);
    }

    public void uploadTexture(String varName, int slot){
        int varLocation = glGetUniformLocation(pID,varName);
        bind();
        glUniform1i(varLocation, slot);
    }
    public void uploadIntArray(String varName, int[] array){
        int varLocation = glGetUniformLocation(pID,varName);
        bind();
        glUniform1iv(varLocation, array);
    }
}
