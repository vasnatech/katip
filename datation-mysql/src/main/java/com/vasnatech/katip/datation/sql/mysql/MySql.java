package com.vasnatech.katip.datation.sql.mysql;

import com.vasnatech.datation.schema.Column;
import com.vasnatech.datation.schema.ForeignKey;
import com.vasnatech.datation.schema.Index;
import com.vasnatech.datation.schema.Table;

import java.util.stream.Collectors;

public final class MySql {

    static final MySql INSTANCE = new MySql();

    public static MySql instance() {
        return INSTANCE;
    }

    private MySql() {
    }

    public String getTableName(Table table) {
        return table.getName().toUpperCase();
    }

    public String getColumnName(Column column) {
        return column.getName().toUpperCase();
    }

    public String getColumnType(Column column) {
        if (column.isEnum()) {
            return column.getEnumValues().values().stream().map(value -> "'" + value + "'").collect(Collectors.joining(", ", "ENUM(", ")"));
        }
        return switch (column.getType()) {
            case CHAR -> "CHAR(" + column.getLength() + ")";
            case VARCHAR -> "VARCHAR(" + column.getLength() + ")";
            case BOOL -> "BIT(1)";
            case BYTE -> "TINYINT";
            case INT16 -> "SMALLINT";
            case INT32 -> "INT";
            case INT64 -> "BIGINT";
            case INTEGER -> "DECIMAL(" + column.getLength() + ", 0)";
            case FLOAT -> "FLOAT";
            case DOUBLE -> "DOUBLE";
            case DECIMAL -> "DECIMAL(" + column.getLength() + ", " + column.getLength2() + ")";
            case DATE -> "DATE";
            case TIME -> "TIME";
            case DATETIME -> "DATETIME";
            case TIMESTAMP -> "TIMESTAMP";
            case BLOB -> "BLOB";
            case CLOB -> "TEXT";
        };
    }

    public String getIndexName(Index index) {
        return index.getName().toUpperCase();
    }

    public String getForeignKeyName(ForeignKey foreignKey) {
        return foreignKey.getName().toUpperCase();
    }
}
