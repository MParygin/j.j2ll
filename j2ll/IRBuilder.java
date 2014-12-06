package j2ll;

import org.objectweb.asm.Label;

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

    public void branch(_Stack stack, Label label, String op) {
        //  %cond = icmp eq i32 %a, %b
        // br i1 %cond, label %IfEqual, label %IfUnequal
        // IfEqual:
        StackValue op2 = stack.pop();
        StackValue op1 = stack.pop();
        add("%__tmpc" + tmp + " = icmp " + op + " i32 " + op1 + ", " + op2);
        add("br i1 %__tmpc" + tmp + ", label %" + label + ", label %__tmpl" + tmp);
        add("__tmpl" + tmp + ":");
        tmp++;

    }

    public void branch0(_Stack stack, Label label, String op) {
        //  %cond = icmp eq i32 %a, 0
        // br i1 %cond, label %IfEqual, label %IfUnequal
        // IfEqual:
        StackValue op1 = stack.pop();
        add("%__tmpc" + tmp + " = icmp " + op + " i32 " + op1 + ", 0");
        add("br i1 %__tmpc" + tmp + ", label %" + label + ", label %__tmpl" + tmp);
        add("__tmpl" + tmp + ":");
        tmp++;
    }

    public void newArray(_Stack stack, String arrayType) {
        StackValue op = stack.pop();
        String res = stack.pushObjRef(arrayType);
        // size array in bytes
        int bytes = Internals.sizeOf(Internals.LONG); //todo
        if (bytes != 1) {
            add("%_" + tmp + " = shl " + op.fullName() + ", " + bytes);
            tmp++;
        }
        add("%_" + tmp + " = add i32 %_" + (tmp-1) + ", 4");
        tmp++;
        add("%_" + tmp + " = call i8* @malloc(i32 %_" + (tmp - 1) + ")");
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

    public void fptosi(_Stack stack, String type) {
        operationto(stack, "fptosi", type);
    }

    public void sitofp(_Stack stack, String type) {
        operationto(stack, "sitofp", type);
    }

    public void operationto(_Stack stack, String op, String type) {
        StackValue f = stack.pop();
        String res = stack.push(type);
        add(res + " = " + op + " " + f.fullName() + " to " + type);
    }


    public List<String> getStrings() {
        return strings;
    }
}
