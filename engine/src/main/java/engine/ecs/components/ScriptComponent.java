package engine.ecs.components;

import engine.ecs.Component;
import engine.editor.NodeEditor.NodeEditor;

public class ScriptComponent extends Component {

    private transient NodeEditor editor;

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {
        //gameObject.transform.position.x += 10 * dt;

    }

    @Override
    public void imgui() {
        editor.imgui();
    }
}
