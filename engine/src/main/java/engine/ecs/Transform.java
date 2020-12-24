package engine.ecs;

import imgui.ImGui;
import org.joml.Vector2f;

public class Transform {
    public Vector2f position;
    public Vector2f scale;

    public Transform(){
        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position){
        init(position, new Vector2f());
    }
    public Transform(Vector2f position, Vector2f scale){
        init(position, scale);
    }

    public void init(Vector2f p, Vector2f s){
        this.position = p;
        this.scale = s;
    }

    public Transform copy(){
        Transform t = new Transform(new Vector2f(this.position), new Vector2f(this.scale));
        return t;
    }

    public void copy(Transform to){
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Transform)) return false;

        Transform t = (Transform)o;
        return t.position.equals(this.scale) && t.scale.equals(this.scale);
    }

    public void imgui(){
        if(ImGui.collapsingHeader("Transform")){
            float[] arp = {position.x,position.y};
            if(ImGui.dragFloat2("Position", arp)){
               position.set(arp[0],arp[1]);
            }
            float[] ars = {scale.x,scale.y};
            if(ImGui.dragFloat2("Scale", ars)){
                scale.set(ars[0],ars[1]);
            }
        }
        ImGui.spacing();
    }
}
