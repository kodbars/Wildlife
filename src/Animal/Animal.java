package Animal;

public interface Animal {

    boolean getReproduce();
    void setReproduce(boolean reproduce);
    int getX();
    void setX(int x);
    int getY();
    void setY(int y);
    int getMovingMax();
    String getICON();
    double getWeight();
    int getAnimalMax();
    double getAnimalSatietyKg();
    int getEatMovement();
    void setEatMovement(int size);
    void setSatiety(boolean satiety);
    boolean getSatiety();
    double getEatenKg();
    void setEatenKg(double eatenKg);
}
