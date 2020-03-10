package com.Gem.gmll;

import java.util.Random;

public class generator {
    int genKey;

    public int getGenKey() {
        return genKey;
    }

    public void setGenKey() {

        int pin_firstform = (int)(Math.random()*99999+1);
//        String pin_secform = convert(pin_firstform);
//
//        genKey = pin_secform;

        this.genKey = pin_firstform;
    }

    String convert(int pin_firstform){

        String regex = String.valueOf(pin_firstform);
        char regex_c = regex.charAt(regex.length());
        int last_val = Character.getNumericValue(regex_c);
        if(last_val<=5){
            regex = regex+"s";
        }else{
            regex = regex+"a";
        }

        return regex;
    }
}
