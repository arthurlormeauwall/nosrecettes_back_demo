package com.arwall.nosrecettes.infra.util;

public class HttpUtils {


    private static final String DEV_PORT = "8091";
    private static final String LAB_PORT = "8090";

    public static String url() {
        return "http://localhost:" + DEV_PORT;
    }

}
