package Animal;

import Interfase.Panel;

import java.util.concurrent.ThreadLocalRandom;

public class Mouse extends Herbivores{
    private int x = ThreadLocalRandom.current().nextInt(Panel.X);
    private int y = ThreadLocalRandom.current().nextInt(Panel.Y);
    private double weight = 0.05d;
    private int animalMax = 500;
    private int movingMax = 1;
    private double animalSatietyKg = 0.01d;
    private double eatenKg;
    private volatile int size;
    private final String ICON = "mouse.png"; //мышь
    private boolean satiety = false;
    private boolean reproduce = false;

    @Override
    public boolean getReproduce() {return reproduce;}
    @Override
    public void setReproduce(boolean reproduce) {this.reproduce = reproduce;}
    @Override
    public int getX() {
        return x;
    }
    @Override
    public int getY() {
        return y;
    }
    @Override
    public void setX(int x) {this.x = x;}
    @Override
    public void setY(int y) {this.y = y;}
    @Override
    public int getMovingMax() {
        return movingMax;
    }
    @Override
    public String getICON() {
        return ICON;
    }
    @Override
    public double getWeight() {
        return weight;
    }
    @Override
    public int getAnimalMax() {
        return animalMax;
    }
    @Override
    public double getAnimalSatietyKg() {
        return animalSatietyKg;
    }
    @Override
    public int getEatMovement() {return size;}
    @Override
    public void setEatMovement(int size) {this.size = size;}
    @Override
    public void setSatiety(boolean satiety) {this.satiety = satiety;}
    @Override
    public boolean getSatiety() {return satiety;}    @Override
    public double getEatenKg() {return eatenKg;}
    @Override
    public void setEatenKg(double eatenKg) {this.eatenKg = eatenKg;}
}
