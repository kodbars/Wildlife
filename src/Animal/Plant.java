package Animal;

import Interfase.Panel;

import java.util.concurrent.ThreadLocalRandom;

public class Plant {
    private int x = ThreadLocalRandom.current().nextInt(Panel.X);
    private int y = ThreadLocalRandom.current().nextInt(Panel.Y);
    private double weight = 1d;
    private int plantMax = 200;
    private volatile static int size;
    private final String icon = "plant.png"; //трава

    public int getSize() {return size;}
    public void setSize(int size) {Plant.size = size;}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getWeight() {
        return weight;
    }

    public int getPlantMax() {
        return plantMax;
    }

    public String getIcon() {
        return icon;
    }
}
