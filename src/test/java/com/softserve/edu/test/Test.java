package com.softserve.edu.test;

import com.softserve.edu.data.Locations;
import com.softserve.edu.utils.ConfigPropertiesReader;

public class Test {

    private static final ConfigPropertiesReader config = new ConfigPropertiesReader();

    public static void main(String[] args) {
        System.out.println(Locations.KYIV);
    }

}
