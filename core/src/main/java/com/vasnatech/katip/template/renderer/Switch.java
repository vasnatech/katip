package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Part;
import com.vasnatech.katip.template.document.Tag;
import org.springframework.expression.Expression;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Switch extends ContainerRenderer {

    @Override
    public String name() {
        return "switch";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
        validateAllAttributesExist(tag, "key");

        int caseCount = 0;
        int elseCount = 0;
        for (Part childPart : tag.children()) {
            if (childPart instanceof Tag childTag) {
                if (childTag.renderer() instanceof Case) {
                    ++caseCount;
                } else if (childTag.renderer() instanceof Else) {
                    ++elseCount;
                    if (elseCount > 1) {
                        throw new RenderException(childTag.path(), childTag.line(), "switch tag can contain at most one else tag.");
                    }
                } else {
                    throw new RenderException(childTag.path(), childTag.line(), "switch tag can only contain case or else tags.");
                }
            } else {
                throw new RenderException(childPart.path(), childPart.line(), "switch tag can only contain case or else tags.");
            }
        }
        if (caseCount < 1) {
            throw new RenderException(tag.path(), tag.line(), "switch tag must contain at least one case tag.");
        }
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        Expression keyExpr = tag.attributes().get("key");
        Object switchValue = getValue(tag, renderContext, keyExpr);
        debug(tag, renderContext, Map.of(keyExpr.getExpressionString(), switchValue == null ? "null" : switchValue));

        Tag elseTag = null;
        for (Part childPart : tag.children()) {
            Tag childTag = (Tag) childPart;
            if (childTag.renderer() instanceof Else) {
                elseTag = childTag;
                continue;
            }
            Expression valueExp = childTag.attributes().get("value");
            Object caseValue = getValue(tag, renderContext, valueExp);
            if (equals(switchValue, caseValue)) {
                debug(childTag, renderContext, Map.of(valueExp.getExpressionString(), caseValue == null ? "null" : caseValue));
                childTag.render(output, renderContext);
                return;
            }
        }
        if (elseTag != null) {
            debug(elseTag, renderContext, Map.of());
            elseTag.render(output, renderContext);
        }
    }

    boolean equals(Object switchValue, Object caseValue) {
        if (switchValue == caseValue) {
            return true;
        }
        if (switchValue == null || caseValue == null) {
            return false;
        }
        if (switchValue.equals(caseValue)) {
            return true;
        }
        if (switchValue instanceof Number switchNumber && caseValue instanceof Number caseNumber) {
            return Math.abs(switchNumber.doubleValue() - caseNumber.doubleValue()) < 0.01;
        }
        return false;
    }
}
