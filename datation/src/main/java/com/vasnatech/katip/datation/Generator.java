package com.vasnatech.katip.datation;

import java.util.Map;

public interface Generator {

    void generate(Map<String, ?> config) throws GeneratorException;
}
