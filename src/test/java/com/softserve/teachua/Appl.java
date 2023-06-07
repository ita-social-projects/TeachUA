package com.softserve.teachua;

import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class A {
    String res;

    public A(String res) {
        this.res = res;
    }

    public void aMethod() {
        System.out.println("Class A");
    }

    @Override
    public String toString() {
        return "class A {" +
                "res='" + res + '\'' +
                '}';
    }
}

class B {
    String res;

    public B(String res) {
        this.res = res;
    }

    public void bMethod() {
        System.out.println("Class B");
    }

    @Override
    public String toString() {
        return "class B {" +
                "res='" + res + '\'' +
                '}';
    }
}

public class Appl {

    public <T> T work(String s, Class<T> clazz) {
        T result = null;
        try {
            result = clazz.getConstructor(String.class).newInstance(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public <T> T work(String s, String className) {
        T result = null;
        try {
            Class<?> clazz = Class.forName(className);
            result = (T) clazz.getConstructor(String.class).newInstance(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void main(String[] args) {
        Appl appl = new Appl();
        /*
        Gson gson = new Gson();
        A a = new A("AA");
        String ser = gson.toJson(a);
        System.out.println("ser = " + ser);
        A a2 = gson.fromJson(ser, A.class);
        System.out.println("A = " + a2);
        */
        //
        /*
        A a3 = appl.work("11_A", A.class);
        System.out.println("a3 = " + a3);
        B b3 = appl.work("22_B", B.class);
        System.out.println("b3 = " + b3);
        //
        appl.work("11_A", A.class).aMethod();
        */
        // /*
        A a4 = appl.work("33_A", "com.softserve.teachua.A");
        System.out.println("a4 = " + a4);
        B b4 = appl.work("44_B", "com.softserve.teachua.B");
        System.out.println("b4 = " + b4);
        //
        System.out.println("Res = "
                + appl.work("33_A", "com.softserve.teachua.A").getClass().getName());
        // */
    }
}
