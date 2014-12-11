package j2ll;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * Vars Collector
 */
public final class MethodVarsCollector extends MethodVisitor {

    // local vars
    LocalVars vars;
    Resolver resolver;

    public MethodVarsCollector(int i, LocalVars vars, Resolver resolver) {
        super(i);
        this.vars = vars;
        this.resolver = resolver;
    }

    @Override
    public void visitLocalVariable(String name, String sign, String s2, Label label, Label label1, int slot) {
        vars.put(slot, new _LocalVar(name, sign));
        Util.javaSignature2irType(this.resolver, sign);
    }

}
