package engine.editor.NodeEditor.Programm;

import com.google.gson.JsonPrimitive;
import engine.input.KeyCodes;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;

public class Variable {
    private String name;
    private JsonPrimitive defaultValue;
    private VarType varType;
    private transient String[] values;
    private transient VarType[] types;
    private static final transient String[] keyCodes = KeyCodes.KEY_CODES.keySet().toArray(new String[0]);
    public Variable(String name, JsonPrimitive defaultValue, VarType type) {
        this.defaultValue = defaultValue;
        this.name = name;
        this.varType = type;
        types = VarType.values();
        values = new String[types.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = types[i].name();
        }
    }

    public JsonPrimitive getDefaultValue() {
        return defaultValue;
    }
    public String getDefaultValueAsString(){
        return String.valueOf(getDefaultValue());
    }

    public VarType getVarType() {
        return varType;
    }

    public String getName() {
        return name;
    }

    public void imgui(boolean isConstant) {
        if (values == null){
            types = VarType.values();
            values = new String[types.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = types[i].name();
            }
        }
    if (!isConstant && !name.startsWith("_")){
        ImString IMname = new ImString(name, 256);
        if(ImGui.inputText("Variable name", IMname)){
            name = IMname.get();
        }
    }
//        ImInt sel = new ImInt(varType.ordinal());
//       if(ImGui.combo("Variable Type", sel, values, values.length)){
//           if (varType.ordinal() != sel.get()){
//               varType = (types[ sel.get()]);
//               switch (varType){
//                   case STRING:
//                       defaultValue = new JsonPrimitive("");
//                       break;
//                   case BOOL:
//                       defaultValue = new JsonPrimitive(false);
//                       break;
//                   case INT:
//                       defaultValue = new JsonPrimitive(-1);
//                       break;
//               }
//           }
//       }
       switch (varType){
           case STRING:
               ImString v = new ImString(defaultValue.getAsString(), 256);
               ImGui.pushID(ImGui.getID(name));
               if (ImGui.inputText("Default", v)){
                   defaultValue = new JsonPrimitive(v.get());
               }
               ImGui.popID();
               break;
           case BOOL:
               ImBoolean b = new ImBoolean(defaultValue.getAsBoolean());
               ImGui.pushID(ImGui.getID(name));
               if (ImGui.checkbox("Default", b)){
                   defaultValue = new JsonPrimitive(b.get());
               }
               ImGui.popID();
               break;
           case INT:
               ImInt i = new ImInt(defaultValue.getAsInt());
               ImGui.pushID(ImGui.getID(name));
               if (ImGui.inputInt("Default", i)){
                   defaultValue = new JsonPrimitive(i.get());
               }
               ImGui.popID();
               break;
           case KEY:
               ImInt selected = new ImInt(indexOf(keyCodes, defaultValue.getAsString()));
               ImGui.pushID(ImGui.getID(name));
               if (ImGui.combo("Key to Listen to", selected, keyCodes, keyCodes.length)){
                   defaultValue = new JsonPrimitive(keyCodes[selected.get()]);
               }
               ImGui.popID();
               break;
           case FLOAT:
               ImFloat f = new ImFloat(defaultValue.getAsFloat());
               ImGui.pushID(ImGui.getID(name));
               if (ImGui.inputFloat("Default", f)){
                   defaultValue = new JsonPrimitive(f.get());
               }
               ImGui.popID();
               break;
           default:
               System.out.println("<red>Error: not supported var type: " + varType);
       }

    //ImGui.labelText("debug_name", name);
    }

    private static <T> int indexOf(T[] list, T value){
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(value)){
                return i;
            }
        }
        return -1;
    }

}
