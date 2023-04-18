package com.vasnatech.katip.datation.sql.mysql;


import com.vasnatech.katip.datation.Generator;
import com.vasnatech.katip.datation.GeneratorFactory;

public class MySqlGeneratorFactory extends GeneratorFactory {

    @Override
    public Generator create() {
        return new MySqlGenerator();
    }
}
