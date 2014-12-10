package j2ll;

import org.objectweb.asm.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
        tmp.append(type); // IR of op1 ???
        tmp.append(' ');
        tmp.append(op1);
        tmp.append(", ");
        tmp.append(op2);
        add(tmp.toString());
    }

    public void _new(_Stack stack, Resolver resolver, String name) {
        String struc = resolver.resolveStruct(name);
        String object = resolver.resolve(name);
        String res = stack.pushObjRef(object);

        add("; " + resolver.resolveStruct(name));
        add("%_" + tmp + " = call i8* @malloc(" + Internals.structSize(struc) + ")");
        add(res + " = bitcast i8* %_" + tmp + " to " + object);
        tmp++;
    }

    public void neg(_Stack stack, String type) {
        StackValue value = stack.pop();
        String res = stack.push(type);
        if (Internals.INT.equals(type) || Internals.LONG.equals(type)) {
            add(res + " = sub " + value.getIR() + " 0, " + value);
        } else {
            add(res + " = fsub " + value.getIR() + " 0.0, " + value);
        }
    }


    public void branch(_Stack stack, Stack<String> commands, Label label, int op) {
        if (commands.size() > 0) {
            System.err.println("Unknown prefix: " + commands.pop());
        }

        //  %cond = icmp eq i32 %a, %b
        // br i1 %cond, label %IfEqual, label %IfUnequal
        // IfEqual:
        StackValue op2 = stack.pop();
        StackValue op1 = stack.pop();
        add("%__tmpc" + tmp + " = icmp " + IR.ICMP[op] + " i32 " + op1 + ", " + op2);
        add("br i1 %__tmpc" + tmp + ", label %" + label + ", label %__tmpl" + tmp);
        add("__tmpl" + tmp + ":");
        tmp++;

    }

    public void branch0(_Stack stack, Stack<String> commands, Label label, int op) {
        if (commands.size() == 0) {
            //  %cond = icmp eq i32 %a, 0
            // br i1 %cond, label %IfEqual, label %IfUnequal
            // IfEqual:
            StackValue op1 = stack.pop();
            add("%__tmpc" + tmp + " = icmp " + IR.ICMP[op] + " i32 " + op1 + ", 0");
            add("br i1 %__tmpc" + tmp + ", label %" + label + ", label %__tmpl" + tmp);
            add("__tmpl" + tmp + ":");
            tmp++;
        } else {
            // POP prefix
            String cmd = commands.pop();
            if (Prefix.FCMPL.equals(cmd) || Prefix.FCMPG.equals(cmd) || Prefix.DCMPL.equals(cmd) || Prefix.DCMPG.equals(cmd)) {
                // double compare
                StackValue value2 = stack.pop();
                StackValue value1 = stack.pop();
                add("%__tmpc" + tmp + " = fcmp " + IR.FCMP[op] + " " + value1.fullName() + ", " + value2); // ordered compare
            } else {
                System.err.println("Unknown prefix: " + commands.pop());
            }
            add("br i1 %__tmpc" + tmp + ", label %" + label + ", label %__tmpl" + tmp);
            add("__tmpl" + tmp + ":");
            tmp++;
        }
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

    public void putfield(_Stack stack, Resolver resolver, String className, String name, String signature) {
        StackValue value = stack.pop();
        StackValue inst = stack.pop();
        String res = Util.javaSignature2irType(resolver, signature);

        add("; putfield " + className + " " + name + " " + signature + " ( " + inst.fullName() + " := " + value.fullName() + " )");

        add("%__tmp" + tmp + " = getelementptr " + inst.fullName() + ", i32 0, i32 " + Util.class2ptr(className, name));
        add("store " + value.fullName() + ", " + res + "* %__tmp" + tmp);
        tmp++;
    }

    public void getfield(_Stack stack, Resolver resolver, String className, String name, String signature) {
        StackValue inst = stack.pop();
        String res = Util.javaSignature2irType(resolver, signature);
        String value = stack.push(res);

        add("; getfield " + className + " " + name + " " + signature + " ( " + inst.fullName() + " )");

        add("%__tmp" + tmp + " = getelementptr " + inst.fullName() + ", i32 0, i32 " + Util.class2ptr(className, name));
        add(value + " = load " + res + "* %__tmp" + tmp);
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
