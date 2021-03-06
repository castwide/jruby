package org.jruby.ir.instructions.defined;

import org.jruby.ir.IRVisitor;
import org.jruby.ir.Operation;
import org.jruby.ir.instructions.FixedArityInstr;
import org.jruby.ir.instructions.Instr;
import org.jruby.ir.operands.Operand;
import org.jruby.ir.transformations.inlining.CloneInfo;
import org.jruby.parser.StaticScope;
import org.jruby.runtime.DynamicScope;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

import java.util.Map;

public class RestoreErrorInfoInstr extends Instr implements FixedArityInstr {
    private Operand arg;

    public RestoreErrorInfoInstr(Operand arg) {
        super(Operation.RESTORE_ERROR_INFO);

        this.arg = arg;
    }

    public Operand getArg() {
        return arg;
    }

    @Override
    public Operand[] getOperands() {
        return new Operand[] { arg };
    }

    @Override
    public String toString() {
        return super.toString() + "(" + arg + ")";
    }

    @Override
    public void simplifyOperands(Map<Operand, Operand> valueMap, boolean force) {
        arg = arg.getSimplifiedOperand(valueMap, force);
    }

    @Override
    public Instr clone(CloneInfo ii) {
        return new RestoreErrorInfoInstr(arg.cloneForInlining(ii));
    }

    @Override
    public Object interpret(ThreadContext context, StaticScope currScope, DynamicScope currDynScope, IRubyObject self, Object[] temp) {
        context.setErrorInfo((IRubyObject) arg.retrieve(context, self, currScope, currDynScope, temp));

        return null;
    }

    @Override
    public void visit(IRVisitor visitor) {
        visitor.RestoreErrorInfoInstr(this);
    }
}
