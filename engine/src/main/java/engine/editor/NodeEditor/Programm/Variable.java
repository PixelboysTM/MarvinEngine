package engine.editor.NodeEditor.Programm;

import com.google.gson.JsonPrimitive;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;

public class Variable {
    private String name;
    private JsonPrimitive defaultValue;
    private VarType varType;
    private transient String[] values;
    private transient VarType[] types;
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
    if (!isConstant && !name.equals("_")){
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
               if (ImGui.inputText("Default", v)){
                   defaultValue = new JsonPrimitive(v.get());
               }
               break;
           case BOOL:
               ImBoolean b = new ImBoolean(defaultValue.getAsBoolean());
               if (ImGui.checkbox("Default", b)){
                   defaultValue = new JsonPrimitive(b.get());
               }
               break;
           case INT:
               ImInt i = new ImInt(defaultValue.getAsInt());
               if (ImGui.inputInt("Default", i)){
                   defaultValue = new JsonPrimitive(i.get());
               }
               break;
           default:
               System.out.println("<red>Error: not supported var type: " + varType);
       }
    }


}
