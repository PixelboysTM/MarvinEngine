package engine.editor.NodeEditor.Programm;

import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public class ActionCommand {
    private Action action;
    private List<Variable> variables;

    public ActionCommand(Action action, List<Variable> variables) {
        this.action = action;
        this.variables = variables;
    }

    public Action getAction() {
        return action;
    }

    public List<Variable> getVariables() {
        if (variables == null)
            return new ArrayList<Variable>();
        return variables;
    }

    public void imgui(boolean isConstant) {
        ImGui.labelText("Command", action.name());
        if (variables == null) return;
        for (int v = 0; v < variables.size(); v++) {
            Variable vs = variables.get(v);
            vs.imgui(isConstant);
        }
    }
}
