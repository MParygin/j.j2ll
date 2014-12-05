package j2ll;

import java.util.ArrayList;
import java.util.List;

/**
 * IR Builder
 */
public class IRBuilder {

    private int tmp;

    private List<String> strings = new ArrayList<String>();

    public void add(String str) {
        strings.add(str);
    }

    public void in2out1(_Stack stack, String op, String type) {
        StackValue op2 = stack.pop();
        StackValue op1 = stack.pop();
        String res = stack.push(type);
        StringBuilder tmp = new StringBuilder();
        tmp.append(res);
        tmp.append(" = ");
        tmp.append(op);
        tmp.append(' ');
        tmp.append(type);
        tmp.append(' ');
        tmp.append(op1);
        tmp.append(", ");
        tmp.append(op2);
        add(tmp.toString());
    }

    public void newArray(_Stack stack, String arrayType) {
        StackValue op = stack.pop();
        String res = stack.pushObjRef(arrayType);
        add("%_" + tmp + " = call i8* @malloc(i32 " + op + ")");
        add(res + " = bitcast i8* %_" + tmp + " to " + arrayType);
        tmp++;
    }

    public void astore(_Stack stack, String type) {
        StackValue value = stack.pop();
        StackValue index = stack.pop();
        StackValue arrayRef = stack.pop(); // todo
        //add("%__tmp" + tmp + " = getelementptr " + arrayRef.fullName() + ", i32 0, i32 1, i32 " + index); // todo type index
        add("%__tmp" + tmp + " = getelementptr " + arrayRef.fullName() + ", i32 0, i32 1, " + index.fullName());
//        add("store i32 " + value + ", i32* %__tmp" + tmp);
        add("store " + value.fullName() + ", " + type + "* %__tmp" + tmp);
        tmp++;
    }

    public void aload(_Stack stack, String type) {
        StackValue index = stack.pop();
        StackValue arrayRef = stack.pop();
        String value = stack.push(type);
        add("%__tmp" + tmp + " = getelementptr " + arrayRef.fullName() + ", i32 0, i32 1, " + index.fullName());
        add(value + " = load " + type + "* %__tmp" + tmp);
        tmp++;
    }




    public List<String> getStrings() {
        return strings;
    }
}
