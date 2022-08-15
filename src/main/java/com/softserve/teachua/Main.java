package com.softserve.teachua;

import java.util.ArrayList;
import java.util.List;

public class Main<T> {
    public static void main(String[] args) {
        callTestMethod(new ArrayList<Animal>());
    }

    private static void callTestMethod(List<? super Tiger> list) {
        list.add(new Cat());
        Object object = list.get(0);
    }

}
class Animal {

}
class Tiger extends Animal{

}
class Cat extends Tiger{

}
class Kitty extends Cat{

}