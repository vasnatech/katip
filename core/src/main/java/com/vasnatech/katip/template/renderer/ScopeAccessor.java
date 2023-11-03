package com.vasnatech.katip.template.renderer;

import com.vasnatech.commons.type.Scope;
import org.springframework.asm.MethodVisitor;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.CodeFlow;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class ScopeAccessor extends MapAccessor {

    @Override
    public Class<?>[] getSpecificTargetClasses() {
        return new Class<?>[] {Scope.class};
    }

    @Override
    public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
        return target instanceof Scope scope && scope.containsKey(name);
    }

    @Override
    public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
        Assert.state(target instanceof Scope, "Target must be of type Scope");
        Scope scope = (Scope) target;
        Object value = scope.get(name);
        if (value == null && !scope.containsKey(name)) {
            throw new AccessException("Scope does not contain a value for key '" + name + "'");
        }
        return new TypedValue(value);
    }

    @Override
    public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
        return true;
    }

    @Override
    public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) throws AccessException {
        Assert.state(target instanceof Scope, "Target must be a Scope");
        Scope scope = (Scope) target;
        scope.put(name, newValue);
    }

    @Override
    public boolean isCompilable() {
        return true;
    }

    @Override
    public Class<?> getPropertyType() {
        return Object.class;
    }

    @Override
    public void generateCode(String propertyName, MethodVisitor mv, CodeFlow cf) {
        String descriptor = cf.lastDescriptor();
        if (descriptor == null || !descriptor.equals("Lcom/vasnatech/commons/type/Scope")) {
            if (descriptor == null) {
                cf.loadTarget(mv);
            }
            CodeFlow.insertCheckCast(mv, "Lcom/vasnatech/commons/type/Scope");
        }
        mv.visitLdcInsn(propertyName);
        mv.visitMethodInsn(INVOKESPECIAL, "com/vasnatech/commons/type/Scope", "get","(Ljava/lang/String;)Ljava/lang/Object;",true);
    }
}
