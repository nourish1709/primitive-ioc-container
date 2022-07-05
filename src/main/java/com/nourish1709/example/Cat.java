package com.nourish1709.example;

import com.nourish1709.context.bean.Bean;

@Bean("catty")
public class Cat implements Animal{

    @Override
    public String sound() {
        return "meow";
    }
}
