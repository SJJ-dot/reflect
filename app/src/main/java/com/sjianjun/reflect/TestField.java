package com.sjianjun.reflect;

import sjj.alog.Log;

public class TestField {
    public int field;
    public int field2;
    public static String staticField = "staticField value";

    public void testM(Integer field, int i) {

    }

    public static void testStatic(String aaaa, String bbbbb) {
        Log.e(aaaa + "   " + bbbbb);
    }
}
