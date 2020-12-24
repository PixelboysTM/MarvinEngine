package engine.editor.NodeEditor.Programm;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VirtualMachine {
    private List<JsonArray> programms; //TODO: Add gameObject reference interface
    private ListWrapper[] callQueue;
    private RuntimeVarHandler varHandler;

    public VirtualMachine(List<JsonArray> programms) {
        this.programms = programms;
        this.varHandler = new RuntimeVarHandler();
        callQueue = new ListWrapper[programms.size()];
        for (int i = 0; i < callQueue.length; i++) {
            callQueue[i] = new ListWrapper();
        }
        for (int i = 0; i < programms.size(); i++) {
            JsonArray a = programms.get(i);
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

                JsonObject obj = programms.get(i).get(in).getAsJsonObject();
                String cmd = obj.get("cmd").getAsString();
                JsonArray data = obj.getAsJsonArray("data");
                JsonArray vars = obj.getAsJsonArray("vars");
                performCmd(i, cmd, data, vars);
                JsonArray next = obj.getAsJsonArray("flows");
                for (int j = 0; j < next.size(); j++) {
                    w.list.add(next.get(j).getAsJsonObject().get("value").getAsInt());
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
                System.out.println("<green>VM_LOG: Starting");
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
            default:
                System.out.println("<red>VM_ERROR: Unknown command.");
                break;
        }
    }

    private String getStringParam(int p, int n, String identifier) {
        JsonObject obj = programms.get(p).getAsJsonArray().get(n).getAsJsonObject();
        switch (obj.get("cmd").getAsString()) {
            case "START":
                return obj.get("vars").getAsJsonArray().get(0).getAsJsonObject().get("default").getAsString();
            case "CONVERT":
                int no = obj.get("data").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsInt();
                int i = getIntegerParam(p, no, obj.get("data").getAsJsonArray().get(0).getAsJsonObject().get("identifier").getAsString());
                return String.valueOf(i);
        }
        return "Not finished";
    }

    /**
     * Gets an Integer Param from handle
     * @param p the current programm
     * @param n the node index to acess
     * @param asString identifier to specify handle
     * @return the value
     */
    private int getIntegerParam(int p, int n, String asString) {
        JsonObject obj = programms.get(p).getAsJsonArray().get(n).getAsJsonObject();
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
        }
        return -1;
    }

    class ListWrapper {
        public List<Integer> list;

        public ListWrapper() {
            list = new ArrayList<Integer>();
        }
    }
}
