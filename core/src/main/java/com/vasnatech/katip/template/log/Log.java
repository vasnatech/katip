package com.vasnatech.katip.template.log;

public class Log {

    static Logger logger = DoNothingLogger.instance();

    public static Logger logger() {
        return logger;
    }

    public static void logger(Logger logger) {
        Log.logger = logger == null ? DoNothingLogger.instance() : logger;
    }

    public static void debug(CharSequence message) {
        Log.logger().debug(message);
    }
    public static void info(CharSequence message) {
        Log.logger().info(message);
    }
    public static void warn(CharSequence message) {
        Log.logger().warn(message);
    }
    public static void error(CharSequence message) {
        Log.logger().error(message);
    }


    public static void debug(Object message) {
        Log.logger().debug(message == null ? "" : message.toString());
    }
    public static void info(Object message) {
        Log.logger().info(message == null ? "" : message.toString());
    }
    public static void warn(Object message) {
        Log.logger().warn(message == null ? "" : message.toString());
    }
    public static void error(Object message) {
        Log.logger().error(message == null ? "" : message.toString());
    }
}
