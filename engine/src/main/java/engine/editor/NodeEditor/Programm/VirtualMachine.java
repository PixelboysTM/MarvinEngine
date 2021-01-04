package engine.editor.NodeEditor.Programm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.ecs.GameObject;
import engine.input.KeyCodes;
import engine.util.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VirtualMachine {
    private List<Tuple<JsonArray, GameObject>> programms; //TODO: Add gameObject reference interface
    private ListWrapper[] callQueue;
    private RuntimeVarHandler varHandler;

    public VirtualMachine(List<Tuple<JsonArray, GameObject>> programms) {
        this.programms = programms;
        this.varHandler = new RuntimeVarHandler();
        callQueue = new ListWrapper[programms.size()];
        for (int i = 0; i < callQueue.length; i++) {
            callQueue[i] = new ListWrapper();
        }
        for (int i = 0; i < programms.size(); i++) {
            JsonArray a = programms.get(i).x;
            for (int j = 0; j < a.size(); j++) {
                JsonObject o = a.get(j).getAsJsonObject();
                if (o.get("cmd").getAsString().equals("START")) {
                    callQueue[i].list.add(j);
                }
            }
        }
    }

    public void step() {
        for (int i = 0; i < callQueue.length; i++) {
            ListWrapper w = new ListWrapper();
            for (int in :
                    callQueue[i].list) {

                JsonObject obj = getCmdWithId(i, in);
                String cmd = obj.get("cmd").getAsString();
                JsonArray data = obj.getAsJsonArray("data");
                JsonArray vars = obj.getAsJsonArray("vars");
                performCmd(i, cmd, data, vars);
                JsonArray next = obj.getAsJsonArray("flows");
                for (int j = 0; j < next.size(); j++) {
                    JsonArray ar = next.get(j).getAsJsonObject().get("value").getAsJsonArray();
                    for (int k = 0; k < ar.size(); k++) {
                        w.list.add(ar.get(k).getAsInt());
                    }
                }
            }
            callQueue[i] = w;
        }
    }

    public boolean hasStepsLeft() {
        for (ListWrapper w : callQueue) {
            if (w.list.size() > 0)
                return true;
        }
        return false;
    }

    private void performCmd(int p, String cmd, JsonArray data, JsonArray vars) {
        switch (cmd) {
            case "START":
                System.out.println("<green>VM_LOG: Starting code for GameObject: " + programms.get(p).y.getName());
                break;
            case "PRINT":
                System.out.println("<green>VM_LOG: Printing");
                if (data.size() > 0) {
                    JsonObject aObj = data.get(0).getAsJsonObject();
                    int n = aObj.get("value").getAsInt();
                    String value = getStringParam(p, n, aObj.get("identifier").getAsString());
                    System.out.println("<yellow>VM_OUT: " + value);
                } else {
                    String value = vars.get(0).getAsJsonObject().get("default").getAsString();
                    System.out.println("<yellow>VM_OUT: " + value);
                }
                break;
            case "SET_VAR":
                String varName = vars.get(0).getAsJsonObject().get("name").getAsString();
                if (data.size() > 0) {
                    switch (vars.get(0).getAsJsonObject().get("type").getAsString()) {
                        case "STRING":
                            System.out.println("<red>VM_ERROR: STRING vars not supportet yet add to VarHandler.");
                            break;
                        case "INT":
                            int value = getIntegerParam(p, data.get(0).getAsJsonObject().get("value").getAsInt(), data.get(0).getAsJsonObject().get("identifier").getAsString() );
                            varHandler.setValue(varName, value, VarType.INT);
                            break;
                    }
                } else {
                    switch (vars.get(0).getAsJsonObject().get("type").getAsString()) {
                        case "STRING":
                            System.out.println("<red>VM_ERROR: STRING vars not supportet yet add to VarHandler.");
                            break;
                        case "INT":
                            String si = vars.get(0).getAsJsonObject().get("default").getAsString();
                            si = si.replace(".0", "");
                            int value = Integer.parseInt(si);
                            varHandler.setValue(varName, value, VarType.INT);
                            break;
                    }
                }
                break;
            case "SET_X":
                if (data.size() > 0) {
                    JsonObject aObj = data.get(0).getAsJsonObject();
                    int n = aObj.get("value").getAsInt();
                    int value = getIntegerParam(p, n, aObj.get("identifier").getAsString());
                    GameObject g = programms.get(p).y;
                    g.transform.position.x = value;
                } else {
                    int value = vars.get(0).getAsJsonObject().get("default").getAsInt();
                    GameObject g = programms.get(p).y;
                    g.transform.position.x = value;
                }
                break;
            case "SET_Y":
                if (data.size() > 0) {
                    JsonObject aObj = data.get(0).getAsJsonObject();
                    int n = aObj.get("value").getAsInt();
                    int value = getIntegerParam(p, n, aObj.get("identifier").getAsString());
                    GameObject g = programms.get(p).y;
                    g.transform.position.y = value;
                } else {
                    int value = vars.get(0).getAsJsonObject().get("default").getAsInt();
                    GameObject g = programms.get(p).y;
                    g.transform.position.y = value;
                }
                break;
            case "SET_P":
                if (data.size() > 1) {
                    JsonObject aObj_x = data.get(0).getAsJsonObject();
                    int n_x = aObj_x.get("value").getAsInt();
                    int value_x = getIntegerParam(p, n_x, aObj_x.get("identifier").getAsString());
                    JsonObject aObj_y = data.get(1).getAsJsonObject();
                    int n_y = aObj_y.get("value").getAsInt();
                    int value_y = getIntegerParam(p, n_y, aObj_y.get("identifier").getAsString());
                    GameObject g = programms.get(p).y;
                    g.transform.position.set(value_x, value_y);
                } else if (data.size() > 0){
                    JsonObject aObj = data.get(0).getAsJsonObject();
                    int n = aObj.get("value").getAsInt();
                    int value = getIntegerParam(p, n, aObj.get("identifier").getAsString());;
                    String key = aObj.get("key").getAsString();
                    GameObject g = programms.get(p).y;
                    if (key.toUpperCase().startsWith("Y")){
                        int vx = vars.get(0).getAsJsonObject().get("default").getAsInt();
                        g.transform.position.set(vx, value);
                    }else if (key.toUpperCase().startsWith("X")){
                        int vy = vars.get(1).getAsJsonObject().get("default").getAsInt();
                        g.transform.position.set(value, vy);
                    }else {
                        System.out.println("<red>VM_ERROR: Error cant resolve pluck situation!");
                    }
                }else {
                    int value_x = vars.get(0).getAsJsonObject().get("default").getAsInt();
                    int value_y = vars.get(1).getAsJsonObject().get("default").getAsInt();
                    GameObject g = programms.get(p).y;
                    g.transform.position.set(value_x, value_y);

                }
                break;
            case "MOVE_X":
                if (data.size() > 0) {
                    JsonObject aObj = data.get(0).getAsJsonObject();
                    int n = aObj.get("value").getAsInt();
                    int value = getIntegerParam(p, n, aObj.get("identifier").getAsString());
                    GameObject g = programms.get(p).y;
                    g.transform.position.x += value;
                    System.out.println("<yellow>VM_OUT: Moving" + value);
                } else {
                    int value = vars.get(0).getAsJsonObject().get("default").getAsInt();
                    GameObject g = programms.get(p).y;
                    g.transform.position.x += value;
                    System.out.println("<yellow>VM_OUT: Moving" + value);
                }
                break;
            case "MOVE_Y":
                if (data.size() > 0) {
                    JsonObject aObj = data.get(0).getAsJsonObject();
                    int n = aObj.get("value").getAsInt();
                    int value = getIntegerParam(p, n, aObj.get("identifier").getAsString());
                    GameObject g = programms.get(p).y;
                    g.transform.position.y += value;
                } else {
                    int value = vars.get(0).getAsJsonObject().get("default").getAsInt();
                    GameObject g = programms.get(p).y;
                    g.transform.position.y += value;
                }
                break;
            case "MOVE_P":
                if (data.size() > 1) {
                    JsonObject aObj_x = data.get(0).getAsJsonObject();
                    int n_x = aObj_x.get("value").getAsInt();
                    int value_x = getIntegerParam(p, n_x, aObj_x.get("identifier").getAsString());
                    JsonObject aObj_y = data.get(1).getAsJsonObject();
                    int n_y = aObj_y.get("value").getAsInt();
                    int value_y = getIntegerParam(p, n_y, aObj_y.get("identifier").getAsString());
                    GameObject g = programms.get(p).y;
                    g.transform.position.add(value_x, value_y);
                } else if (data.size() > 0){
                    JsonObject aObj = data.get(0).getAsJsonObject();
                    int n = aObj.get("value").getAsInt();
                    int value = getIntegerParam(p, n, aObj.get("identifier").getAsString());;
                    String key = aObj.get("key").getAsString();
                    GameObject g = programms.get(p).y;
                    if (key.toUpperCase().startsWith("Y")){
                        int vx = vars.get(0).getAsJsonObject().get("default").getAsInt();
                        g.transform.position.add(vx, value);
                    }else if (key.toUpperCase().startsWith("X")){
                        int vy = vars.get(1).getAsJsonObject().get("default").getAsInt();
                        g.transform.position.add(value, vy);
                    }else {
                        System.out.println("<red>VM_ERROR: Error cant resolve pluck situation!");
                    }
                }else {
                    int value_x = vars.get(0).getAsJsonObject().get("default").getAsInt();
                    int value_y = vars.get(1).getAsJsonObject().get("default").getAsInt();
                    GameObject g = programms.get(p).y;
                    g.transform.position.add(value_x, value_y);

                }
                break;
            case "KEY_DOWN":
                System.out.println("<green>VM_LOG: Key Pressed");
                break;
                default:
                System.out.println("<red>VM_ERROR: Unknown command.");
                break;
        }
    }

    private String getStringParam(int p, int n, String identifier) {
        JsonObject obj = getCmdWithId(p,n);
        switch (obj.get("cmd").getAsString()) {
            case "START":
                return obj.get("vars").getAsJsonArray().get(0).getAsJsonObject().get("default").getAsString();
            case "CONVERT":
                int no = obj.get("data").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsInt();
                int i = getIntegerParam(p, no, obj.get("data").getAsJsonArray().get(0).getAsJsonObject().get("identifier").getAsString());
                return String.valueOf(i);
        }
        return "Not finished cmd: " + obj.get("cmd").getAsString();
    }

    /**
     * Gets an Integer Param from handle
     * @param p the current programm
     * @param n the node index to acess
     * @param identifier identifier to specify handle
     * @return the value
     */
    private int getIntegerParam(int p, int n, String identifier) {
        JsonObject obj = getCmdWithId(p, n);
        switch (obj.get("cmd").getAsString()) {
            case "SET_VAR":
            case "GET_VAR":
                JsonObject var = obj.get("vars").getAsJsonArray().get(0).getAsJsonObject();
                String si = var.get("default").getAsString();
                si = si.replace(".0", "");
                int i = varHandler.getValueOrDefault(var.get("name").getAsString(), Integer.parseInt(si), VarType.valueOf(var.get("type").getAsString()));
                return i;
            case "GET_CONSTANT":
                var = obj.get("vars").getAsJsonArray().get(0).getAsJsonObject();
                si = var.get("default").getAsString();
                si = si.replace(".0", "");
                return Integer.parseInt(si);

            //MAth TODO: Refactor to one with less code
            case "ADD":
                JsonArray conns = obj.get("data").getAsJsonArray();
                if (conns.size() == 0){
                    String var1s = obj.get("vars").getAsJsonArray().get(0).getAsJsonObject().get("default").getAsString();
                    var1s = var1s.replace(".0", "");
                    int var1 = Integer.parseInt(var1s);

                    String var2s = obj.get("vars").getAsJsonArray().get(1).getAsJsonObject().get("default").getAsString();
                    var2s = var2s.replace(".0", "");
                    int var2 = Integer.parseInt(var2s);

                    return var1 + var2;
                } else if( conns.size() == 1){
                    JsonObject handle = obj.get("data").getAsJsonArray().get(0).getAsJsonObject();
                    String hName = handle.get("key").getAsString();
                    int Ivar = Integer.MIN_VALUE;
                    if (hName.endsWith("1")){
                        String vars = obj.get("vars").getAsJsonArray().get(1).getAsJsonObject().get("default").getAsString();
                        vars = vars.replace(".0", "");
                        Ivar = Integer.parseInt(vars);

                    }else {
                        String vars = obj.get("vars").getAsJsonArray().get(0).getAsJsonObject().get("default").getAsString();
                        vars = vars.replace(".0", "");
                        Ivar = Integer.parseInt(vars);

                    }
                    int value2 = getIntegerParam(p, handle.get("value").getAsInt(), handle.get("identifier").getAsString());
                    return Ivar + value2;
                }else{
                    JsonObject handle1 = obj.get("data").getAsJsonArray().get(0).getAsJsonObject();
                    int v1 = getIntegerParam(p, handle1.get("value").getAsInt(), handle1.get("identifier").getAsString());

                    JsonObject handle2 = obj.get("data").getAsJsonArray().get(1).getAsJsonObject();
                    int v2 = getIntegerParam(p, handle2.get("value").getAsInt(), handle1.get("identifier").getAsString());

                    return v1 + v2;
                }
            case "SUB":
                JsonArray conn = obj.get("data").getAsJsonArray();
                if (conn.size() == 0){
                    String var1s = obj.get("vars").getAsJsonArray().get(0).getAsJsonObject().get("default").getAsString();
                    var1s = var1s.replace(".0", "");
                    int var1 = Integer.parseInt(var1s);

                    String var2s = obj.get("vars").getAsJsonArray().get(1).getAsJsonObject().get("default").getAsString();
                    var2s = var2s.replace(".0", "");
                    int var2 = Integer.parseInt(var2s);

                    return var1 - var2;
                } else if( conn.size() == 1){
                    JsonObject handle = obj.get("data").getAsJsonArray().get(0).getAsJsonObject();
                    String hName = handle.get("key").getAsString();
                    int Ivar = Integer.MIN_VALUE;
                    int value2 = getIntegerParam(p, handle.get("value").getAsInt(), handle.get("identifier").getAsString());
                    if (hName.endsWith("1")){
                        String vars = obj.get("vars").getAsJsonArray().get(1).getAsJsonObject().get("default").getAsString();
                        vars = vars.replace(".0", "");
                        Ivar = Integer.parseInt(vars);
                        return value2 - Ivar;
                    }else {
                        String vars = obj.get("vars").getAsJsonArray().get(0).getAsJsonObject().get("default").getAsString();
                        vars = vars.replace(".0", "");
                        Ivar = Integer.parseInt(vars);
                        return Ivar - value2;
                    }
                }else{
                    JsonObject handle1 = obj.get("data").getAsJsonArray().get(0).getAsJsonObject();
                    int v1 = getIntegerParam(p, handle1.get("value").getAsInt(), handle1.get("identifier").getAsString());

                    JsonObject handle2 = obj.get("data").getAsJsonArray().get(1).getAsJsonObject();
                    int v2 = getIntegerParam(p, handle2.get("value").getAsInt(), handle1.get("identifier").getAsString());

                    return v1 - v2;
                }
            case "MUL":
                JsonArray conns2 = obj.get("data").getAsJsonArray();
                if (conns2.size() == 0){
                    String var1s = obj.get("vars").getAsJsonArray().get(0).getAsJsonObject().get("default").getAsString();
                    var1s = var1s.replace(".0", "");
                    int var1 = Integer.parseInt(var1s);

                    String var2s = obj.get("vars").getAsJsonArray().get(1).getAsJsonObject().get("default").getAsString();
                    var2s = var2s.replace(".0", "");
                    int var2 = Integer.parseInt(var2s);

                    return var1 * var2;
                } else if( conns2.size() == 1){
                    JsonObject handle = obj.get("data").getAsJsonArray().get(0).getAsJsonObject();
                    String hName = handle.get("key").getAsString();
                    int Ivar = Integer.MIN_VALUE;
                    if (hName.endsWith("1")){
                        String vars = obj.get("vars").getAsJsonArray().get(1).getAsJsonObject().get("default").getAsString();
                        vars = vars.replace(".0", "");
                        Ivar = Integer.parseInt(vars);

                    }else {
                        String vars = obj.get("vars").getAsJsonArray().get(0).getAsJsonObject().get("default").getAsString();
                        vars = vars.replace(".0", "");
                        Ivar = Integer.parseInt(vars);

                    }
                    int value2 = getIntegerParam(p, handle.get("value").getAsInt(), handle.get("identifier").getAsString());
                    return Ivar * value2;
                }else{
                    JsonObject handle1 = obj.get("data").getAsJsonArray().get(0).getAsJsonObject();
                    int v1 = getIntegerParam(p, handle1.get("value").getAsInt(), handle1.get("identifier").getAsString());

                    JsonObject handle2 = obj.get("data").getAsJsonArray().get(1).getAsJsonObject();
                    int v2 = getIntegerParam(p, handle2.get("value").getAsInt(), handle1.get("identifier").getAsString());

                    return v1 * v2;
                }
            case "DIV":
                JsonArray conn3 = obj.get("data").getAsJsonArray();
                if (conn3.size() == 0){
                    String var1s = obj.get("vars").getAsJsonArray().get(0).getAsJsonObject().get("default").getAsString();
                    var1s = var1s.replace(".0", "");
                    int var1 = Integer.parseInt(var1s);

                    String var2s = obj.get("vars").getAsJsonArray().get(1).getAsJsonObject().get("default").getAsString();
                    var2s = var2s.replace(".0", "");
                    int var2 = Integer.parseInt(var2s);

                    return var1 / var2;
                } else if( conn3.size() == 1){
                    JsonObject handle = obj.get("data").getAsJsonArray().get(0).getAsJsonObject();
                    String hName = handle.get("key").getAsString();
                    int Ivar = Integer.MIN_VALUE;
                    int value2 = getIntegerParam(p, handle.get("value").getAsInt(), handle.get("identifier").getAsString());
                    if (hName.endsWith("1")){
                        String vars = obj.get("vars").getAsJsonArray().get(1).getAsJsonObject().get("default").getAsString();
                        vars = vars.replace(".0", "");
                        Ivar = Integer.parseInt(vars);
                        return value2 / Ivar;
                    }else {
                        String vars = obj.get("vars").getAsJsonArray().get(0).getAsJsonObject().get("default").getAsString();
                        vars = vars.replace(".0", "");
                        Ivar = Integer.parseInt(vars);
                        return Ivar / value2;
                    }
                }else{
                    JsonObject handle1 = obj.get("data").getAsJsonArray().get(0).getAsJsonObject();
                    int v1 = getIntegerParam(p, handle1.get("value").getAsInt(), handle1.get("identifier").getAsString());

                    JsonObject handle2 = obj.get("data").getAsJsonArray().get(1).getAsJsonObject();
                    int v2 = getIntegerParam(p, handle2.get("value").getAsInt(), handle1.get("identifier").getAsString());

                    return v1 / v2;
                }
        }
        return -1;
    }

    private JsonObject getCmdWithId(int p, int id){
        for (int i = 0; i < programms.get(p).x.size(); i++){
            JsonObject o = programms.get(p).x.get(i).getAsJsonObject();
            if (o.get("debug_key_id").getAsInt() == id)
                return o;
        }
        assert false : "Index not in Program please check compile version! | Index: " + id ;
        return null;
    }

    public void addKeyEvent(int glfwKey) {
        for (int i = 0; i < programms.size(); i++) {
            JsonArray a = programms.get(i).x;
            for (int j = 0; j < a.size(); j++) {
                JsonObject o = a.get(j).getAsJsonObject();
                if (!o.get("cmd").getAsString().equals("KEY_DOWN"))
                    continue;
                JsonArray vars = o.get("vars").getAsJsonArray();
                JsonObject var = vars.get(0).getAsJsonObject();
                String k = var.get("default").getAsString().replace("\"", "");
                if (!KeyCodes.KEY_CODES.containsKey(k))
                    continue;
                int key = KeyCodes.KEY_CODES.get(k);
                if (key == glfwKey ) {
                    callQueue[i].list.add(j);
                    System.out.println("<green>VM_LOG: Added Key event at index: " + j + " with name" + o.get("cmd"));
                }
            }
        }
    }

    class ListWrapper {
        public List<Integer> list;

        public ListWrapper() {
            list = new ArrayList<Integer>();
        }
    }
}
