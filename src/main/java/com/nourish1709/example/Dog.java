package com.nourish1709.example;

import com.nourish1709.context.bean.Bean;

@Bean
public class Dog implements Animal{

    @Override
    public String sound() {
        return "bark";
    }
}
