package j2ll;

import org.objectweb.asm.*;

import java.io.PrintStream;
import java.util.*;

public class CV extends ClassVisitor {

    // out
    private PrintStream ps;
    
    // state
    String className;
    String superName;
    private List<_Field> staticFields = new ArrayList<_Field>();
    private List<_Field> fields = new ArrayList<_Field>();

    private List<MV> methods = new ArrayList<MV>();
    // shared states
    Set<String> declares = new HashSet<String>();


    public CV(PrintStream ps) {
        super(Opcodes.ASM5);
        this.ps = ps;
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
        this.superName = superName;
    }

    public void visitSource(String source, String debug) {
//        this.ps.println("src " + source);
    }

    public void visitOuterClass(String owner, String name, String desc) {
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    public void visitAttribute(Attribute attr) {
        this.ps.println(" attr " + attr);
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if ((access & Opcodes.ACC_STATIC) != 0) {
            this.staticFields.add(new _Field(name, desc));
        } else {
            this.fields.add(new _Field(name, desc));
//            this.ps.println("  f  " + desc + " " + name + " " + signature + " " + value);
        }
        return null;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        // skip constructor
        if ("<init>".equals(name)) return null;
        // parse
        MV mv = new MV(Opcodes.ASM5, name, desc, this);
        this.methods.add(mv);
        return mv;
    }

    public void visitEnd() {
        this.ps.println("; CLASS: " + this.className + " extends " + this.superName);
        this.ps.println();
        this.ps.println("declare noalias i8* @malloc(i32)");
        this.ps.println("declare void @free(i8*)");
        this.ps.println();

        // declares
        for (String name : declares) {
            this.ps.println("declare " + name);
        }
        this.ps.println();

        // out fields
        for (_Field field : staticFields) {
            String ir = Util.javaSignature2irType(field.javaSignature);
            this.ps.println("@" + this.className + "." +field.name + " = external global " + ir);
        }
        this.ps.println();

        // out class
        StringJoiner joiner = new StringJoiner(", ", "%\"" + this.className + "\" = type {", "}");
        for (_Field field : fields) {
            String ir = Util.javaSignature2irType(field.javaSignature);
            joiner.add(ir);
        }
        this.ps.println(joiner);
        this.ps.println();

        // out methods
        for (MV method : this.methods) {
            method.out(this.ps);
        }

    }
}