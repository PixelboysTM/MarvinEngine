package engine.editor.NodeEditor;

import com.google.gson.JsonPrimitive;
import engine.editor.NodeEditor.Programm.Action;
import engine.editor.NodeEditor.Programm.ActionCommand;
import engine.editor.NodeEditor.Programm.VarType;
import engine.editor.NodeEditor.Programm.Variable;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.Collections;

public class NodeHolder {

   public static Node getNode(String name){
       if (name.equals("Start Node")){
           return new Node(
                   0,
                   0,
                   150,
                   100,
                   new Vector3f(1, 0.2f, 0.2f),
                   "Start",
                   new InputHandle[]{},
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.FLOW, "Run"),
                           new OutputHandle(HandleDataType.STRING, "args"),
                   },
                   new ActionCommand(Action.START, Collections.singletonList(new Variable("_", new JsonPrimitive("Hello World!"), VarType.STRING)))

            );
       }
       if (name.equals("Int Variable get")){
           return new Node(
                   0,
                   0,
                   100,
                   80,
                   new Vector3f(0.2f, 0.2f, 1f),
                   "get Int",
                   new InputHandle[]{},
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.INT, "value"),
                   },
                   new ActionCommand(Action.GET_VAR, Collections.singletonList(new Variable("test_int", new JsonPrimitive(42), VarType.INT)))

           );
       }
       if (name.equals("Int Variable set")){
           return new Node(
                   0,
                   0,
                   100,
                   100,
                   new Vector3f(0.2f, 0.2f, 1f),
                   "set Int",
                   new InputHandle[]{
                           new InputHandle(HandleDataType.FLOW, "set"),
                           new InputHandle(HandleDataType.INT, "value")
                   },
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.FLOW, "con"),
                           new OutputHandle(HandleDataType.INT, "out"),
                   },
                   new ActionCommand(Action.SET_VAR, Collections.singletonList(new Variable("test_int", new JsonPrimitive(128), VarType.INT)))

           );
       }
       if (name.equals("Print")){
           return new Node(
                   0,
                   0,
                   100,
                   100,
                   new Vector3f(0.9f, 0.9f, .9f),
                   "Print",
                   new InputHandle[]{
                           new InputHandle(HandleDataType.FLOW, "In"),
                           new InputHandle(HandleDataType.STRING, "data")
                   },
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.FLOW, "Out"),
                   },
                   new ActionCommand(Action.PRINT, Collections.singletonList(new Variable("_", new JsonPrimitive("Hello World!"), VarType.STRING)))

           );
       }
       if (name.equals("Int to String")){
           return new Node(
                   0,
                   0,
                   100,
                   80,
                   new Vector3f(0.2f, 0.2f, 1f),
                   "Int to String",
                   new InputHandle[]{
                           new InputHandle(HandleDataType.INT, "data")
                   },
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.STRING, "out"),
                   },
                   new ActionCommand(Action.CONVERT, null)

           );
       }
       if (name.equals("Int constant")){
           return new Node(
                   0,
                   0,
                   100,
                   80,
                   new Vector3f(0.2f, 0.2f, 1f),
                   "int",
                   new InputHandle[]{},
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.INT, "value"),
                   },
                   new ActionCommand(Action.GET_CONSTANT, Collections.singletonList(new Variable("_", new JsonPrimitive(12), VarType.INT)))

           );
       }
       if (name.equals("Set x Position")){
           return new Node(
                   0,
                   0,
                   120,
                   100,
                   new Vector3f(204/255f, 125/255f, 6/255f), //204, 125, 6
                   "Set X Position",
                   new InputHandle[]{
                           new InputHandle(HandleDataType.FLOW, "in"),
                           new InputHandle(HandleDataType.INT, "amount")
                   },
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.FLOW, "out")
                   },
                   new ActionCommand(Action.SET_X, Collections.singletonList(new Variable("_", new JsonPrimitive(10), VarType.INT)))//TODO: Change to float

           );
       }
       if (name.equals("Set y Position")){
           return new Node(
                   0,
                   0,
                   120,
                   100,
                   new Vector3f(204/255f, 125/255f, 6/255f),
                   "Set Y Position",
                   new InputHandle[]{
                           new InputHandle(HandleDataType.FLOW, "in"),
                           new InputHandle(HandleDataType.INT, "amount")
                   },
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.FLOW, "out")
                   },
                   new ActionCommand(Action.SET_Y, Collections.singletonList(new Variable("_", new JsonPrimitive(10), VarType.INT)))//TODO: Change to float

           );
       }
       if (name.equals("Set Position")){
           return new Node(
                   0,
                   0,
                   120,
                   120,
                   new Vector3f(204/255f, 125/255f, 6/255f),
                   "Set Position",
                   new InputHandle[]{
                           new InputHandle(HandleDataType.FLOW, "in"),
                           new InputHandle(HandleDataType.INT, "X amount"),
                           new InputHandle(HandleDataType.INT, "Y amount"),
                   },
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.FLOW, "out")
                   },
                   new ActionCommand(Action.SET_P, Arrays.asList(
                           new Variable("_input1",new JsonPrimitive(10), VarType.INT), //TODO: Change to float
                           new Variable("_input2",new JsonPrimitive(10), VarType.INT)))

           );
       }
       if (name.equals("Move x Position")){
           return new Node(
                   0,
                   0,
                   120,
                   100,
                   new Vector3f(204/255f, 125/255f, 6/255f), //204, 125, 6
                   "Move X Position",
                   new InputHandle[]{
                           new InputHandle(HandleDataType.FLOW, "in"),
                           new InputHandle(HandleDataType.INT, "amount")
                   },
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.FLOW, "out")
                   },
                   new ActionCommand(Action.MOVE_X, Collections.singletonList(new Variable("_", new JsonPrimitive(10), VarType.INT)))//TODO: Change to float

           );
       }
       if (name.equals("Move y Position")){
           return new Node(
                   0,
                   0,
                   120,
                   100,
                   new Vector3f(204/255f, 125/255f, 6/255f),
                   "Move Y Position",
                   new InputHandle[]{
                           new InputHandle(HandleDataType.FLOW, "in"),
                           new InputHandle(HandleDataType.INT, "amount")
                   },
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.FLOW, "out")
                   },
                   new ActionCommand(Action.MOVE_Y, Collections.singletonList(new Variable("_", new JsonPrimitive(10), VarType.INT)))//TODO: Change to float

           );
       }
       if (name.equals("Move Position")){
           return new Node(
                   0,
                   0,
                   120,
                   120,
                   new Vector3f(204/255f, 125/255f, 6/255f),
                   "Move Position",
                   new InputHandle[]{
                           new InputHandle(HandleDataType.FLOW, "in"),
                           new InputHandle(HandleDataType.INT, "X amount"),
                           new InputHandle(HandleDataType.INT, "Y amount"),
                   },
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.FLOW, "out")
                   },
                   new ActionCommand(Action.MOVE_P, Arrays.asList(
                           new Variable("_input1",new JsonPrimitive(10), VarType.INT), //TODO: Change to float
                           new Variable("_input2",new JsonPrimitive(10), VarType.INT)))

           );
       }
       if (name.equals("Key Pressed")){
           return new Node(
                   0,
                   0,
                   120,
                   80,
                   new Vector3f(1f, 0.2f, 0.2f),
                   "Key Pressed",
                   new InputHandle[]{},
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.FLOW, "action")
                   },
                   new ActionCommand(Action.KEY_DOWN,
                           Collections.singletonList(new Variable("_KeyCode", new JsonPrimitive("Space"), VarType.KEY)))

           );
       }
       if (name.equals("Int Add")){
           return new Node(
                   0,0,80, 100,
                   new Vector3f(0.2f,1,0.2f),
                   "+ (Add)",
                   new InputHandle[]{
                           new InputHandle(HandleDataType.INT, "in1"),
                   new InputHandle(HandleDataType.INT, "in2")
                   },
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.INT, "out")
                   },
                   new ActionCommand(Action.ADD, Arrays.asList(
                           new Variable("_in1", new JsonPrimitive(42), VarType.INT),
                           new Variable("_in2", new JsonPrimitive(42), VarType.INT)
                   ))
           );
       }
       if (name.equals("Int Subtract")){
           return new Node(
                   0,0,80, 100,
                   new Vector3f(0.2f,1,0.2f),
                   "- (Sub)",
                   new InputHandle[]{
                           new InputHandle(HandleDataType.INT, "in1"),
                           new InputHandle(HandleDataType.INT, "in2")
                   },
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.INT, "out")
                   },
                   new ActionCommand(Action.SUB, Arrays.asList(
                           new Variable("_in1", new JsonPrimitive(42), VarType.INT),
                           new Variable("_in2", new JsonPrimitive(42), VarType.INT)
                   ))
           );
       }
       if (name.equals("Int Multiply")){
           return new Node(
                   0,0,80, 100,
                   new Vector3f(0.2f,1,0.2f),
                   "* (Mul)",
                   new InputHandle[]{
                           new InputHandle(HandleDataType.INT, "in1"),
                           new InputHandle(HandleDataType.INT, "in2")
                   },
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.INT, "out")
                   },
                   new ActionCommand(Action.MUL, Arrays.asList(
                           new Variable("_in1", new JsonPrimitive(42), VarType.INT),
                           new Variable("_in2", new JsonPrimitive(42), VarType.INT)
                   ))
           );
       }
       if (name.equals("Int Divide")){
           return new Node(
                   0,0,80, 100,
                   new Vector3f(0.2f,1,0.2f),
                   "/ (Div)",
                   new InputHandle[]{
                           new InputHandle(HandleDataType.INT, "in1"),
                           new InputHandle(HandleDataType.INT, "in2")
                   },
                   new OutputHandle[]{
                           new OutputHandle(HandleDataType.INT, "out")
                   },
                   new ActionCommand(Action.DIV, Arrays.asList(
                           new Variable("_in1", new JsonPrimitive(42), VarType.INT),
                           new Variable("_in2", new JsonPrimitive(42), VarType.INT)
                   ))
           );
       }
       assert false : "Unavailable Node";
       return null;
   }
}
