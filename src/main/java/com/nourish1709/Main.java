package com.nourish1709;

import com.nourish1709.context.ApplicationContext;
import com.nourish1709.context.PrimitiveApplicationContext;
import com.nourish1709.example.Animal;
import com.nourish1709.example.Dog;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new PrimitiveApplicationContext("com.nourish1709");

        System.out.println(applicationContext.getAllBeans(Animal.class));
        final Animal cat = applicationContext.getBean("catty", Animal.class);
        System.out.println(cat.sound());
        final Dog dog = applicationContext.getBean(Dog.class);
        System.out.println(dog.sound());
    }
}
