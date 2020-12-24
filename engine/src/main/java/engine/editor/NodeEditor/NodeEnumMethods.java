package engine.editor.NodeEditor;

import org.joml.Vector3f;

public class NodeEnumMethods {
    public static Vector3f getColor( HandleDataType dataType){
        if (dataType == null){
            return new Vector3f(28/255.0f, 22/255.0f, 58/255.0f);
        }

        switch (dataType){
            case INT:
                return new Vector3f(0,0,1);
            case FLOW:
                return new Vector3f(1);
            case STRING:
                return new Vector3f(198/255.0f, 3/255.0f, 252/255.0f);
            default:
                return new Vector3f(28/255.0f, 22/255.0f, 58/255.0f);
        }
    }
}
