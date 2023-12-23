package Interfase;

import Animal.*;

import java.util.ArrayList;


public class SetAnimal {
    private static ArrayList<Object> animals = new ArrayList<>();
    private static ArrayList<Object> plantCaterpillar = new ArrayList<>();

    public static ArrayList<Object> fillAnimal() {
        for (int i = 0; i < 5; i++) {
            animals.add(new Bear());
        }
        for (int i = 0; i < 10; i++) {
            animals.add(new Buffalo());
        }
        for (int i = 0; i < 10; i++) {
            animals.add(new Deer());
        }
        for (int i = 0; i < 10; i++) {
            animals.add(new Eagle());
        }
        for (int i = 0; i < 10; i++) {
            animals.add(new Horse());
        }
        for (int i = 0; i < 15; i++) {
            animals.add(new Fox());
        }
        for (int i = 0; i < 15; i++) {
            animals.add(new Wolf());
        }
        for (int i = 0; i < 15; i++) {
            animals.add(new Boa());
        }
        for (int i = 0; i < 25; i++) {
            animals.add(new Hog());
        }
        for (int i = 0; i < 70; i++) {
            animals.add(new Goat());
        }
        for (int i = 0; i < 75; i++) {
            animals.add(new Rabbit());
        }
        for (int i = 0; i < 75; i++) {
            animals.add(new Sheep());
        }
        for (int i = 0; i < 100; i++) {
            animals.add(new Duck());
        }
        for (int i = 0; i < 150; i++) {
            animals.add(new Mouse());
        }

        return animals;
    }

    public static ArrayList<Object> fillPlantCaterpillar() {
        for (int i = 0; i < 500; i++) {
            plantCaterpillar.add(new Plant());
        }
        for (int i = 0; i < 250; i++) {
            plantCaterpillar.add(new Caterpillar());
        }
        return plantCaterpillar;
    }
}
