package org.example;

import java.util.Map;

public class Person {

    public String name;
    public Map<String,Double> films;

    public Person(String name, Map<String, Double> films) {
        this.name = name;
        this.films = films;
    }
}
