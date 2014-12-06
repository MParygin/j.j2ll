package j2ll;

import org.objectweb.asm.*;

import java.io.PrintStream;
import java.util.*;
import static j2ll.Internals.*;

/**
 */
public class MV extends MethodVisitor{

    // parent
    private CV cv;

    // state
    String methodName;
    String javaSignature;

    // arguments
    List<String> _argTypes;
    // res
    String _resType;

    // local vars
    Map<Integer, _LocalVar> vars = new HashMap<Integer, _LocalVar>();
    // buffer
    IRBuilder out = new IRBuilder();
    // stack
    _Stack stack = new _Stack();
    // labels
    List<Label> labels = new ArrayList<Label>();
    List<String> usedLabels = new ArrayList<String>();

    int max_local;
    int max_stack;
    int tmp;

    public MV(int i, String methodName, String javaSignature, CV cv) {
        super(i);
        this.methodName = methodName;
        this.javaSignature = javaSignature;
        _JavaSignature s = new _JavaSignature(this.javaSignature);
        _argTypes = s.getArgs();
        _resType = s.getResult();
        this.cv = cv;
    }

    public MV(int i, MethodVisitor methodVisitor) {
        super(i, methodVisitor);
    }

    @Override
    public void visitParameter(String s, int i) {
        System.out.println("VP " + s + " " + i);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return super.visitAnnotationDefault();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        return super.visitAnnotation(s, b);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String s, boolean b) {
        return super.visitTypeAnnotation(i, typePath, s, b);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int i, String s, boolean b) {
        return super.visitParameterAnnotation(i, s, b);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        System.out.println("VA " + attribute);
    }

    @Override
    public void visitCode() {
    }

    @Override
    public void visitFrame(int i, int i1, Object[] objects, int i2, Object[] objects1) {
        out.add("; TODO FRame: " + i + " " + i1 + " " + i2);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == Opcodes.NOP) {
        } else if (opcode == Opcodes.ACONST_NULL) {
            out.add("const null"); //todo
        } else if (opcode >= Opcodes.ICONST_M1 && opcode <= Opcodes.ICONST_5) { // [2..8]
            int value = opcode - Opcodes.ICONST_0;
            stack.push(new StackValue(StackValue.TYPE_IMM, value, INT));
            out.add("; push " + value);
        } else if (opcode >= Opcodes.LCONST_0 && opcode <= Opcodes.LCONST_1) { // [9..10]
            int value = opcode - Opcodes.LCONST_0;
            stack.push(new StackValue(StackValue.TYPE_IMM, (long)value, LONG));
            out.add("; push " + value);
        } else if (opcode == Opcodes.DCONST_0) { // 14
            stack.push(new StackValue(StackValue.TYPE_IMM, 0.0d, DOUBLE));
            out.add("; push 0.0d");
        } else if (opcode == Opcodes.DCONST_1) { // 15
            stack.push(new StackValue(StackValue.TYPE_IMM, 1.0d, DOUBLE));
            out.add("; push 1.0d");
        } else if (opcode == Opcodes.IALOAD) { // 46
            out.aload(stack, "i32");
        } else if (opcode == Opcodes.LALOAD) { // 47
            out.aload(stack, "i64");
        } else if (opcode == Opcodes.FALOAD) { // 48
            out.aload(stack, "float");
        } else if (opcode == Opcodes.DALOAD) { // 49
            out.aload(stack, "double");

        } else if (opcode == Opcodes.AALOAD) { // 50
            out.add("aaload"); //todo pop
        } else if (opcode == Opcodes.BALOAD) { // 51
            out.add("baload"); //todo pop
        } else if (opcode == Opcodes.CALOAD) { // 52
            out.add("caload"); //todo pop

        } else if (opcode == Opcodes.IASTORE) { // 79
            out.astore(stack, "i32");
        } else if (opcode == Opcodes.LASTORE) { // 80
            out.astore(stack, "i64");
        } else if (opcode == Opcodes.FASTORE) { // 81
            out.astore(stack, "float");
        } else if (opcode == Opcodes.DASTORE) { // 82
            out.astore(stack, "double");
        } else if (opcode == Opcodes.AASTORE) { // 83
            out.astore(stack, "s32"); //todo char
        } else if (opcode == Opcodes.BASTORE) { // 84
            out.astore(stack, BYTE); //todo char
        } else if (opcode == Opcodes.CASTORE) { // 85
            out.astore(stack, CHAR); //todo char

        } else if (opcode == Opcodes.POP) { // 87
            stack.pop();
            out.add("pop"); //todo pop
        } else if (opcode == Opcodes.POP2) { // 88
            stack.pop();
            stack.pop();
            out.add("pop2"); //todo pop2
        } else if (opcode == Opcodes.DUP) { // 89 todo check ?
            StackValue op = stack.pop();
            stack.push(op);
            stack.push(op);
        } else if (opcode == Opcodes.DUP2) { // 92 todo check runtime depth ? for long & double
            StackValue op = stack.pop();
            stack.push(op);
            stack.push(op);
        } else if (opcode == Opcodes.IADD) { // 96
            out.in2out1(stack, "add", "i32");
        } else if (opcode == Opcodes.LADD) { // 97
            out.in2out1(stack, "add", "i64");
        } else if (opcode == Opcodes.FADD) { // 98
            out.in2out1(stack, "fadd", "float");
        } else if (opcode == Opcodes.DADD) { // 99
            out.in2out1(stack, "fadd", "double");
        } else if (opcode == Opcodes.ISUB) { // 100
            out.in2out1(stack, "sub", "i32");
        } else if (opcode == Opcodes.LSUB) { // 101
            out.in2out1(stack, "sub", "i64");
        } else if (opcode == Opcodes.FSUB) { // 102
            out.in2out1(stack, "fsub", "float");
        } else if (opcode == Opcodes.DSUB) { // 103
            out.in2out1(stack, "fsub", "double");
        } else if (opcode == Opcodes.IMUL) { // 104
            out.in2out1(stack, "mul", "i32");
        } else if (opcode == Opcodes.LMUL) { // 105
            out.in2out1(stack, "mul", "i64");
        } else if (opcode == Opcodes.FMUL) { // 106
            out.in2out1(stack, "fmul", "float");
        } else if (opcode == Opcodes.DMUL) { // 107
            out.in2out1(stack, "fmul", "double");
        } else if (opcode == Opcodes.IDIV) { // 108
            out.in2out1(stack, "sdiv", "i32");
        } else if (opcode == Opcodes.LDIV) { // 109
            out.in2out1(stack, "sdiv", "i64");
        } else if (opcode == Opcodes.FDIV) { // 110
            out.in2out1(stack, "fdiv", "float");
        } else if (opcode == Opcodes.DDIV) { // 111
            out.in2out1(stack, "fdiv", "double");
        } else if (opcode == Opcodes.IREM) { // 112
            out.in2out1(stack, "srem", "i32");
        } else if (opcode == Opcodes.LREM) { // 113
            out.in2out1(stack, "srem", "i64");
        } else if (opcode == Opcodes.FREM) { // 114
            out.in2out1(stack, "frem", "float");
        } else if (opcode == Opcodes.DREM) { // 115
            out.in2out1(stack, "frem", "double");


            // =============================================== negs ==
        } else if (opcode == Opcodes.INEG) { // 116
            out.add("neg"); //todo
        } else if (opcode == Opcodes.DNEG) { // 119
            out.add("dneg"); //todo

            // =============================================== shifts ==
        } else if (opcode == Opcodes.ISHL) { // 120
            out.in2out1(stack, "shl", INT);
        } else if (opcode == Opcodes.LSHL) { // 121
            out.operationto(stack, "sext", LONG); // extend stack to long (!)
            out.in2out1(stack, "shl", LONG);
        } else if (opcode == Opcodes.ISHR) { // 122
            out.in2out1(stack, "ashr", INT);
        } else if (opcode == Opcodes.LSHR) { // 123
            out.operationto(stack, "sext", LONG); // extend stack to long (!)
            out.in2out1(stack, "ashr", LONG);
        } else if (opcode == Opcodes.IUSHR) { // 124
            out.add("ushr"); //todo

            // =============================================== bits ==
        } else if (opcode == Opcodes.IAND) { // 126
            out.add("and"); //todo
        } else if (opcode == Opcodes.LAND) { // 127
            out.add("longand"); //todo
        } else if (opcode == Opcodes.IOR) { // 128
            out.add("ior"); //todo
        } else if (opcode == Opcodes.IXOR) { // 130
            out.add("ixor"); //todo


            // =============================================== converts ==
        } else if (opcode == Opcodes.I2L) { // 133
            out.operationto(stack, "sext", "i64");
        } else if (opcode == Opcodes.I2F) { // 134
            out.sitofp(stack, "float");
        } else if (opcode == Opcodes.I2D) { // 135
            out.sitofp(stack, "double");
        } else if (opcode == Opcodes.L2I) { // 136
            out.operationto(stack, "trunc", "i32");
        } else if (opcode == Opcodes.L2F) { // 137
            out.sitofp(stack, "float");
        } else if (opcode == Opcodes.L2D) { // 138
            out.sitofp(stack, "double");
        } else if (opcode == Opcodes.F2I) { // 139
            out.fptosi(stack, INT);
        } else if (opcode == Opcodes.F2L) { // 140
            out.fptosi(stack, LONG);
        } else if (opcode == Opcodes.F2D) { // 141
            out.operationto(stack, "fpext", "double");
        } else if (opcode == Opcodes.D2I) { // 142
            out.fptosi(stack, INT);
        } else if (opcode == Opcodes.D2L) { // 143
            out.fptosi(stack, LONG);
        } else if (opcode == Opcodes.D2F) { // 144
            out.operationto(stack, "fptrunc", "float");

        } else if (opcode == Opcodes.I2B) { // 145
            out.operationto(stack, "strunc", BYTE); //todo ?
        } else if (opcode == Opcodes.I2C) { // 146
            out.operationto(stack, "utrunc", CHAR); //todo ? strunc ?
            // todo
        } else if (opcode == Opcodes.I2S) { // 147
            StackValue op = stack.pop();
            out.add("i2s " + op);
            // =============================================== compares ==
        } else if (opcode == Opcodes.LCMP) { // 148
            StackValue op = stack.pop();
            out.add("lcmp " + op); //todo
        } else if (opcode == Opcodes.DCMPL) { // 151
            StackValue op = stack.pop();
            StackValue op2 = stack.pop();
            String op3 = stack.push("i32");
            out.add("dcmpl " + op); //todo


            // =============================================== returns ==
        } else if (opcode == Opcodes.IRETURN) { // 172
            StackValue op = stack.pop();
            out.add("ret i32 " + op);
        } else if (opcode == Opcodes.LRETURN) { // 173
            StackValue op = stack.pop();
            out.add("ret i64 " + op);
        } else if (opcode == Opcodes.FRETURN) { // 174
            StackValue op = stack.pop();
            out.add("ret float " + op);
        } else if (opcode == Opcodes.DRETURN) { // 175
            StackValue op = stack.pop();
            out.add("ret double " + op);
        } else if (opcode == Opcodes.ARETURN) { // 176
            StackValue op = stack.pop();
            out.add("ret A " + op); //todo
        } else if (opcode == Opcodes.RETURN) { // 177
            out.add("ret void");



        } else if (opcode == Opcodes.ARRAYLENGTH) { // 190
            out.add("arraylength");
        } else if (opcode == Opcodes.ATHROW) { // 191
            out.add("athrow");
        } else if (opcode == Opcodes.MONITORENTER) { // 194
            out.add("monitorenter");
        } else if (opcode == Opcodes.MONITOREXIT) { // 195
            out.add("monitorexit");
        } else {
            System.out.println("IN " + opcode);
        }
    }

    @Override
    public void visitIntInsn(int opcode, int value) {
        if (opcode == Opcodes.BIPUSH) {  // 16
            stack.push(new StackValue(StackValue.TYPE_IMM, value, "i32"));
            out.add("; push " + value);
        } else if (opcode == Opcodes.SIPUSH) { // 17
            stack.push(new StackValue(StackValue.TYPE_IMM, value, "i32"));
            out.add("; push " + value);
        } else if (opcode == Opcodes.NEWARRAY) { // 188
            out.newArray(stack, Internals.newJVMArray(value));
        } else {
            System.out.println("INI " + opcode + " " + value);
        }
    }

    @Override
    public void visitVarInsn(int opcode, int slot) {
        if (opcode == Opcodes.ILOAD) { // 21
            // copy from local to stack
            String op = stack.push("i32");
            out.add(op + " = load slot-pointer:" + slot);
        } else if (opcode == Opcodes.LLOAD) { // 22
            // copy from local to stack TODO
            String op = stack.push("i64");
            out.add(op + " = load slot-pointer:" + slot);
        } else if (opcode == Opcodes.FLOAD) { // 23
            // copy from local to stack TODO
            String op = stack.push("float");
            out.add(op + " = load slot-pointer:" + slot);
        } else if (opcode == Opcodes.DLOAD) { // 24
            // copy from local to stack TODO
            String op = stack.push("double");
            out.add(op + " = load slot-pointer:" + slot);
        } else if (opcode == Opcodes.ALOAD) { // 25
            // copy from local to stack TODO
            String op = stack.push("slot-type:" + slot);
            out.add(op + " = load slot-pointer:" + slot);
        } else if (opcode >= Opcodes.ISTORE && opcode <= Opcodes.ASTORE) { // [54..58] Store stack into local variable
            StackValue op = stack.pop();
            out.add("store " + op.fullName() + ", slot-pointer:" + slot);
        } else {
            System.out.println("VVI " + opcode + " " + slot);
        }
    }

    @Override
    public void visitTypeInsn(int opcode, String s) {
        if (opcode == Opcodes.NEW) { // 187
            out.add("new " + s);
        } else if (opcode == Opcodes.ANEWARRAY) { // 189
            out.add("new array " + s);
        } else if (opcode == Opcodes.CHECKCAST) { // 192
            out.add("checkcast " + s);
        } else if (opcode == Opcodes.INSTANCEOF) { // 193
            out.add("instanceof " + s);
        } else {
            System.out.println("VTI " + opcode + " " + s);
        }
    }

    @Override
    public void visitFieldInsn(int opcode, String s, String s1, String s2) {
        if (opcode == Opcodes.GETSTATIC) { // 178 todo
            out.add("getstatic " + s + " " + s1 + " " + s2);
        } else if (opcode == Opcodes.PUTSTATIC) { // 179 todo
            out.add("putstatic " + s + " " + s1 + " " + s2);
        } else if (opcode == Opcodes.GETFIELD) { // 180 todo
            out.add("getfield " + s + " " + s1 + " " + s2);
        } else if (opcode == Opcodes.PUTFIELD) { // 181 todo
            out.add("putfield " + s + " " + s1 + " " + s2);
        } else {
            System.out.println("VFI " + opcode + " " + s);
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String s, String s1, String s2) {
        System.out.println("VMI old " + opcode + " " + s);
    }

    @Override
    public void visitMethodInsn(int opcode, String className, String methodName, String signature, boolean b) {
        if (opcode == Opcodes.INVOKESPECIAL) { // 183 todo
            _JavaSignature s = new _JavaSignature(signature);
            String call = s.getSignatureCall(className, methodName, this.stack);
            if ("void".equals(s.getResult())) {
                out.add("call " + call);
            } else {
                String op = stack.push(s.getResult());
                out.add(op + " = call " + call);
            }

            // declare
            if (!this.cv.className.equals(className)) {
                this.cv.declares.add(s.getSignatureDeclare(className, methodName));
            }

        } else if (opcode == Opcodes.INVOKESTATIC) { // 184
            _JavaSignature s = new _JavaSignature(signature);
            String call = s.getSignatureCall(className, methodName, this.stack);
            if ("void".equals(s.getResult())) {
                out.add("call " + call);
            } else {
                String op = stack.push(s.getResult());
                out.add(op + " = call " + call);
            }

            // declare
            if (!this.cv.className.equals(className)) {
                this.cv.declares.add(s.getSignatureDeclare(className, methodName));
            }

        } else if (opcode == Opcodes.INVOKEVIRTUAL) { // 182
            _JavaSignature s = new _JavaSignature(signature);
            String call = s.getSignatureCall(className, methodName, this.stack);
            if ("void".equals(s.getResult())) {
                out.add("call virt  " + call);
            } else {
                String op = stack.push(s.getResult());
                out.add(op + " = call virt " + call);
            }

            // declare
            if (!this.cv.className.equals(className)) {
                this.cv.declares.add(s.getSignatureDeclare(className, methodName));
            }

        } else if (opcode == Opcodes.INVOKEINTERFACE) { // 185
            _JavaSignature s = new _JavaSignature(signature);
            String call = s.getSignatureCall(className, methodName, this.stack);
            if ("void".equals(s.getResult())) {
                out.add("call int  " + call);
            } else {
                String op = stack.push(s.getResult());
                out.add(op + " = call int " + call);
            }

            // declare
            if (!this.cv.className.equals(className)) {
                this.cv.declares.add(s.getSignatureDeclare(className, methodName));
            }

        } else {
            System.out.println("VMI " + opcode);
        }
    }

    @Override
    public void visitInvokeDynamicInsn(String s, String s1, Handle handle, Object... objects) {
        System.out.println("VIDI " + s + " " + s1);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        if (opcode == Opcodes.GOTO) {
            usedLabels.add(label.toString());
            out.add("br label %" + label);
        } else if (opcode >= Opcodes.IFEQ && opcode <= Opcodes.IFLE) { // [153..158]
            usedLabels.add(label.toString());
            int pos = opcode - Opcodes.IFEQ;
            String[] types = {"eq", "ne", "slt", "sge", "sgt", "sle"};
            out.branch0(stack, label, types[pos]);
        } else if (opcode >= Opcodes.IF_ICMPEQ && opcode <= Opcodes.IF_ICMPLE) { // [159..164]
            usedLabels.add(label.toString());
            int pos = opcode - Opcodes.IF_ICMPEQ;
            String[] types = {"eq", "ne", "slt", "sge", "sgt", "sle"};
            out.branch(stack, label, types[pos]);
        } else {
            out.add("GOTO " + opcode + " " + label.toString());
        }
    }

    @Override
    public void visitLabel(Label label) {
        labels.add(label);
        out.add("label:" + label.toString());
    }

    @Override
    public void visitLdcInsn(Object o) {
        if (o instanceof String) {
            // const
            stack.pushImm(10, "i32"); // todo
            out.add("VLDC " + o);
        } else if (o instanceof Integer) {
            Integer value = (Integer) o;
            stack.pushImm(value, INT);
            out.add("; push " + value);
        } else if (o instanceof Long) {
            Long value = (Long) o;
            stack.pushImm(value, LONG);
            out.add("; push " + value);
        } else if (o instanceof Float) {
            Float value = (Float) o;
            stack.pushImm(value, FLOAT);
            out.add("; push " + value);
        } else if (o instanceof Double) {
            Double value = (Double) o;
            stack.pushImm(value, DOUBLE);
            out.add("; push " + value);
        } else {
            out.add("; todo add const " + o);
        }
    }

    @Override
    public void visitIincInsn(int slot, int value) {
        out.add("inc:" + slot + ":" + value);
    }

    @Override
    public void visitTableSwitchInsn(int i, int i1, Label label, Label... labels) {
        super.visitTableSwitchInsn(i, i1, label, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label label, int[] ints, Label[] labels) {
        super.visitLookupSwitchInsn(label, ints, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String s, int i) {
        super.visitMultiANewArrayInsn(s, i);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int i, TypePath typePath, String s, boolean b) {
        return super.visitInsnAnnotation(i, typePath, s, b);
    }

    @Override
    public void visitTryCatchBlock(Label label, Label label1, Label label2, String s) {
        super.visitTryCatchBlock(label, label1, label2, s);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int i, TypePath typePath, String s, boolean b) {
        return super.visitTryCatchAnnotation(i, typePath, s, b);
    }

    @Override
    public void visitLocalVariable(String name, String sign, String s2, Label label, Label label1, int slot) {
        //System.out.println("VLV + " + name + " / " + sign + " " + s2 + " " + slot);
        vars.put(slot, new _LocalVar(name, sign));
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int i, TypePath typePath, Label[] labels, Label[] labels1, int[] ints, String s, boolean b) {
        return super.visitLocalVariableAnnotation(i, typePath, labels, labels1, ints, s, b);
    }

    @Override
    public void visitLineNumber(int i, Label label) {
        //System.out.println("LN " + i + " " + label);
        //super.visitLineNumber(i, label);
    }

    public void visitMaxs(int stack, int local) {
        // Maximums
        this.max_stack = stack;
        this.max_local = local;
    }

    @Override
    public void visitEnd() {
    }


    public void out(PrintStream ps) {
        // 0) info
        _JavaSignature ss = new _JavaSignature(this.javaSignature);

        ps.println("define " + this._resType +  " @" + Util.classMethodSignature2id(this.cv.className, this.methodName, ss) + "(" + Util.enumArgs(this._argTypes, "%s") + ") {");

        ps.print("    ; locals: ");
        ps.println(max_local);
        ps.print("    ; stack: ");
        ps.println(max_stack);
        ps.print("    ; args: ");
        ps.println(this._argTypes.size());

        // 1) local vars & args
        int cntSlot = 0;
        for (Integer i : vars.keySet()) {
            _LocalVar lv = vars.get(i);
            cntSlot++;
            // local var
            ps.print("    ");
            ps.println("%" + lv.name + " = alloca " + Util.javaSignature2irType(lv.signature) +  "\t\t; slot " + i + " = " + lv.signature);
            // init from arg (!)
            if (this._argTypes.size() < cntSlot) continue;
            String argType = this._argTypes.get(cntSlot-1);
            ps.println("    store "+argType+" %s" + i + ", "+argType+"* %" + lv.name); //todo
        }
        // 2) text
        for (String str : out.getStrings()) {
            int p = str.indexOf("slot-pointer");
            if (p != -1) {
                for (Integer slot : vars.keySet()) {
                    _LocalVar lv = vars.get(slot);
                    String s = "slot-pointer:" + slot;
                    String r = Util.javaSignature2irType(lv.signature) + "* %" + lv.name; // todo
                    str = str.replace(s, r);
                }
            }

            p = str.indexOf("slot-type");
            if (p != -1) {
                for (Integer slot : vars.keySet()) {
                    _LocalVar lv = vars.get(slot);
                    String s = "slot-type:" + slot;
                    String r = Util.javaSignature2irType(lv.signature);
                    if (r == null) System.out.println("CF TYPE " + lv.signature);
                    str = str.replace(s, r);
                }
            }

            // label
            p = str.indexOf("label:");
            if (p != -1) {
                str = str.substring(6);
                if (usedLabels.contains(str)) {
                    ps.print("br label %");
                    ps.println(str);
                    str = str + ":";
                } else {
                    continue;
                }
            }
            // inc
            p = str.indexOf("inc:");
            if (p != -1) {
                str = str.substring(4);
                p = str.indexOf(":");
                int slot = Integer.parseInt(str.substring(0, p));
                int value = Integer.parseInt(str.substring(p + 1));
                _LocalVar var = this.vars.get(slot);
                if (var != null) {
                    //%x = load i32* %x.ptr        ; загрузили значение типа i32 по указателю %x.ptr
                    //%tmp = add i32 %x, 5         ; прибавили 5
                    //store i32 %tmp, i32* %x.ptr
                    ps.println("    %__tmpv" + tmp + " = load i32* %" + var.name); tmp++;
                    ps.println("    %__tmpv" + tmp + " = add i32 %__tmpv" + (tmp - 1) + ", " + value); tmp++;
                    ps.println("    store i32 %__tmpv" + (tmp - 1) + ", i32* %" + var.name);
                    continue;
                }
            }
            ps.print("    ");
            ps.println(str);
        }
        // 3) end
        ps.println("}");
        ps.println("");
    }
}
