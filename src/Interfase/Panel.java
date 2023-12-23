package Interfase;

import Animal.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Panel extends JFrame {

    private static final JFrame frame = new JFrame("Остров");;
    private static final JFrame frameStatistics = new JFrame("Статистика");
    private static final JLabel[][] labelsStatistics = new JLabel[6][2];
    public static final int X = 42;
    public static final int Y = 65;
    public static final int Z = 1000;
    private static final JLabel[][] labels = new JLabel[X][Y];
    private static int x;
    private static int y;
    private static double weight;
    private static int cellMax;
    private static int movingMax;
    private static int movingMaxSave;
    private static int eatenIteration;
    private static int hungerIteration;
    private static int plantIteration;
    private static int reproductionIteration;
    private static double animalSatietyKg;
    private static String icon;
    private static String iconPrevious;
    private static final String FIELD_ICON = "whitesquare.png";
    private static final String PLANT_ICON = "plant.png";
    private static Animal animal;
    private static Plant plant;
    private static ArrayList<Object> animalList;
    private static ArrayList<Object> plantCaterpillar;
    private static Object[][][] animals = new Object[X][Y][Z];


    public Panel() {
        animalList = SetAnimal.fillAnimal();
        plantCaterpillar = SetAnimal.fillPlantCaterpillar();
        panel();
        filling();
    }
    private static void panel(){
        frame.setBounds(0, 0, 1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("island.png")));
        frame.setLayout(new GridBagLayout());
        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                labels[i][j] = new JLabel(new ImageIcon(Objects.requireNonNull(Panel.class.getResource(FIELD_ICON))));
                frame.add(labels[i][j], new GridBagConstraints(j, i, 1, 1, 1, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(1, 1, 1, 1), 0, 0));
            }
        }
    }

    private static void initialization(ArrayList<Object> list, int i) {
        if (list.get(i) instanceof Animal) {
            animal = (Animal) list.get(i);
            x = animal.getX();
            y = animal.getY();
            weight = animal.getWeight();
            cellMax = animal.getAnimalMax();
            movingMax = animal.getMovingMax();
            animalSatietyKg = animal.getAnimalSatietyKg();
            icon = animal.getICON();
        } else if (list.get(i) instanceof Plant) {
            plant = (Plant) list.get(i);
            x = plant.getX();
            y = plant.getY();
            weight = plant.getWeight();
            cellMax = plant.getPlantMax();
            icon = plant.getIcon();
        }
    }

    public static synchronized void movement() {
            for (int i = 0; i < animalList.size(); i++) {
                initialization(animalList, i);
                movingMaxSave = 1 + ThreadLocalRandom.current().nextInt(movingMax);
                int r = 1 + ThreadLocalRandom.current().nextInt(4);
                if (r == 1 && ((x + movingMaxSave) < labels.length)) {
                    stepXY(movingMaxSave, r);
                } else if (r == 2 && ((x - movingMaxSave) >= 0)) {
                    stepXY(movingMaxSave, r);
                } else if (r == 3 && ((y + movingMaxSave) < labels[0].length)) {
                    stepXY(movingMaxSave, r);
                } else if (r == 4 && ((y - movingMaxSave) >= 0)) {
                    stepXY(movingMaxSave, r);
                }
                putXY(animal);
                reproduction();
                hungerCheck();
                shift();
                animalList.removeAll(Collections.singleton(null));
                grassGrowth();
                visible();
            }
    }
    private static void removeContainer() {
        frame.remove(labels[x][y]);
        labels[x][y] = new JLabel(new ImageIcon(Objects.requireNonNull(Panel.class.getResource(iconPrevious))));
        frame.add(labels[x][y], new GridBagConstraints(y, x, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));
    }
    private static void addContainer() {
        frame.remove(labels[x][y]);
        labels[x][y] = new JLabel(new ImageIcon(Objects.requireNonNull(Panel.class.getResource(icon))));
        frame.add(labels[x][y], new GridBagConstraints(y, x, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));
    }
    private static void previousIcon(){
        for (int i = 0; i < Z; i++) {
            if (animals[x][y][i] != null) {
                if (animals[x][y][i] instanceof Animal) {
                    iconPrevious = ((Animal) animals[x][y][i]).getICON();
                    break;
                } else if (animals[x][y][i] instanceof Plant) {
                    iconPrevious = PLANT_ICON;
                    break;
                }
            } else if ((animals[x][y][i] == null) && (animals[x][y][i + 1] == null) && ((i+1) < Z)){
                iconPrevious = FIELD_ICON;
                break;
            }
        }
    }
    private static void stepXY(int movingMaxSave, int r) {
        for (int i = 0; i < animals[x][y].length; i++) {
            if (animals[x][y][i] == animal) {
                animals[x][y][i] = null;
                break;
            }
        }
        shift();
        previousIcon();
        removeContainer();
        if (r == 1)
            x += movingMaxSave;
        else if (r == 2)
            x -= movingMaxSave;
        else if (r == 3)
            y += movingMaxSave;
        else if (r == 4)
            y -= movingMaxSave;

        for (int i = 0; true; i++) {
            if (animals[x][y][i] == null) {
                animals[x][y][i] = animal;
                break;
            }
        }
        addContainer();
        visible();
    }
    private static void putXY(Animal animal){
        animal.setX(x);
        animal.setY(y);
    }
    private void filling() {
        animalCellCheck();
        visible();
    }
    private void animalCellCheck() {
        for(int i = 0; i < animalList.size(); i++) {
            initialization(animalList, i);
            for (int j = 0; true; j++) {
                if (animal != null) {
                    if ((animals[x][y][j] == null) && (j == 0)) {
                        addContainer();
                        animals[x][y][j] = animal;
                        animal = null;
                        break;
                    } else if ((animals[x][y][j] == null)) {
                        animals[x][y][j] = animal;
                        animal = null;
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < plantCaterpillar.size(); i++) {
            initialization(plantCaterpillar, i);
            for (int j = 0; true; j++) {
                if (animal != null) {
                    if ((animals[x][y][j] == null) && (j == 0)) {
                        addContainer();
                        animals[x][y][j] = animal;
                        animal = null;
                        break;
                    } else if ((animals[x][y][j] == null)) {
                        animals[x][y][j] = animal;
                        animal = null;
                        break;
                    }
                } else if (plant != null) {
                    if ((animals[x][y][j] == null) && (j == 0)) {
                        addContainer();
                        animals[x][y][j] = plant;
                        plant = null;
                        break;
                    } else if ((animals[x][y][j] == null)) {
                        animals[x][y][j] = plant;
                        plant = null;
                        break;
                    }
                }
            }
        }
    }

    private static void visible() {
        frame.setVisible(true);
        frame.pack();
    }
    private static void hungerCheck(){
        if (!animal.getSatiety() && animal.getEatMovement() < 20){
            animal.setEatMovement(animal.getEatMovement() + 1);
            eat();
        } else if (animal.getEatenKg() >= animal.getAnimalSatietyKg()) {
            animal.setEatMovement(animal.getEatMovement() + 1);
            if (animal.getEatMovement() > 5) {
                animal.setEatMovement(0);
                animal.setEatenKg(0);
                animal.setSatiety(false);
            }
        } else {
            for (int j = 0; j < animalList.size(); j++) {
                if (animalList.get(j) == animal) {
                    for (int k = 0; k < animals[x][y].length; k++) {
                        if (animals[x][y][k] == animal) {
                            animals[x][y][k] = null;
                            animalList.set(j, null);
                            previousIcon();
                            removeContainer();
                            hungerIteration++;
                            System.out.println("Умерли от голода: " + hungerIteration);
                            break;
                        }
                    }
                }
            }
        }
    }
    private static void eat() {
        for(int i = 0; true; i++) {
            if ((animals[x][y][i] != null) && (animals[x][y][i] instanceof Predator)) {
                for (int j = i+1; j < Z; j++) {
                    if ((animals[x][y][j] != null) && (animals[x][y][j] instanceof Herbivores)) {
                        if (animals[x][y][i] == null)
                            break;
                        eatPH((Animal)animals[x][y][i], (Animal)animals[x][y][j], j);
                    } else if ((animals[x][y][j] != null) && (animals[x][y][j] instanceof Predator)) {
                        if (animals[x][y][i] == null)
                            break;
                        eatPP((Animal)animals[x][y][i],(Animal) animals[x][y][j], i, j);
                    } else if ((animals[x][y][j] == null) && (animals[x][y][j + 1] == null) && ((j + 1) < Z)) {
                        break;
                    }
                }
            } else if ((animals[x][y][i] != null) && (animals[x][y][i] instanceof Herbivores)){
                for (int j = i+1; j < Z; j++) {
                    if ((animals[x][y][j] != null) && (animals[x][y][j] instanceof Predator)) {
                        if (animals[x][y][i] == null)
                            break;
                        eatPH((Animal)animals[x][y][j],(Animal) animals[x][y][i], i);
                    } else if ((animals[x][y][j] != null) && (animals[x][y][j] instanceof Herbivores)) {
                        if (animals[x][y][i] == null)
                            break;
                        eatHH((Animal)animals[x][y][i],(Animal) animals[x][y][j], i, j);
                    } else if ((animals[x][y][j] != null) && (animals[x][y][j] instanceof Plant)) {
                        eatHPlant((Animal)animals[x][y][i], j);
                    } else if ((animals[x][y][j] == null) && (animals[x][y][j + 1] == null) && ((j + 1) < Z)) {
                        break;
                    }
                }
            } else if ((animals[x][y][i] != null) && (animals[x][y][i] instanceof Plant)) {
                for (int j = i+1; j < Z; j++) {
                    if ((animals[x][y][j] != null) && (animals[x][y][j] instanceof Herbivores)) {
                        if (animals[x][y][i] == null)
                            break;
                        eatHPlant((Animal)animals[x][y][j], i);
                    } else if ((animals[x][y][j] == null) && (animals[x][y][j + 1] == null) && ((j + 1) < Z)) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }
    private static void eatPercent(Animal animalEat, int percent, int i) {
        if ((1 + ThreadLocalRandom.current().nextInt(100)) <= percent) {
            for (int j = 0; j < animalList.size(); j++) {
                if (animalList.get(j) == null)
                    continue;
                else if (animalList.get(j) == animals[x][y][i]) {
                    if (animals[x][y][i] instanceof Animal) {
                        animalEat.setEatenKg(animalEat.getEatenKg() + ((Animal) animals[x][y][i]).getWeight());
                        animalEat.setEatMovement(0);
                    }
                    animalList.set(j, null);
                    break;
                }
                if ((animals[x][y][i] instanceof Plant) && (animalEat != null)) {
                    animalEat.setEatenKg(animalEat.getEatenKg() + ((Plant) animals[x][y][i]).getWeight());
                    animalEat.setEatMovement(0);
                    break;
                }
            }
            eatenIteration++;
            System.out.println("Съели: " + eatenIteration);
            animals[x][y][i] = null;
            if ((animalEat.getEatenKg() >= animalEat.getAnimalSatietyKg()) && (animalEat != null))
                animalEat.setSatiety(true);
        } else {
            animalEat.setEatMovement(animalEat.getEatMovement() + 1);
        }
    }
    private static void shift() {
        for (int i = 0; i < animals[x][y].length; i++) {
            for (int j = i + 1; j < animals[x][y].length; j++) {
                if (animals[x][y][i] == null) {
                    if ((animals[x][y][j] == null) && (animals[x][y][j - 1] != null)) {
                        animals[x][y][i] = animals[x][y][j - 1];
                        animals[x][y][j - 1] = null;
                    } else if ((animals[x][y][j] == null) && (animals[x][y][j - 1] == null)) {
                        break;
                    }
                } else if ((animals[x][y][j] == null) && (animals[x][y][j + 1] == null) && ((i+1) < animals[x][y].length)) {
                    break;
                }
            }
            if ((animals[x][y][i] == null) && (animals[x][y][i + 1] == null) && ((i+1) < animals[x][y].length)) {
                break;
            }
        }
    }
    private static void eatPH(Animal predator, Animal herbivores, int i){
        if (predator instanceof Wolf) {
            if ((herbivores instanceof Horse) || (herbivores instanceof Buffalo)) {
                eatPercent(predator, 10, i);
            } else if ((herbivores instanceof Deer) || (herbivores instanceof Hog)) {
                eatPercent(predator, 15, i);
            } else if ((herbivores instanceof Rabbit) || (herbivores instanceof Goat)) {
                eatPercent(predator, 60, i);
            } else if (herbivores instanceof Mouse) {
                eatPercent(predator, 80, i);
            } else if (herbivores instanceof Sheep) {
                eatPercent(predator, 70, i);
            } else if (herbivores instanceof Duck) {
                eatPercent(predator, 40, i);
            }
        } else if (predator instanceof Boa) {
            if (herbivores instanceof Rabbit) {
                eatPercent(predator, 20, i);
            } else if (herbivores instanceof Mouse) {
                eatPercent(predator, 40, i);
            } else if (herbivores instanceof Duck) {
                eatPercent(predator, 10, i);
            }
        } else if (predator instanceof Fox) {
            if (herbivores instanceof Rabbit) {
                eatPercent(predator, 70, i);
            } else if (herbivores instanceof Mouse) {
                eatPercent(predator, 90, i);
            } else if (herbivores instanceof Duck) {
                eatPercent(predator, 60, i);
            } else if (herbivores instanceof Caterpillar) {
                eatPercent(predator, 40, i);
            }
        } else if (predator instanceof Bear) {
            if (herbivores instanceof Horse) {
                eatPercent(predator, 40, i);
            } else if ((herbivores instanceof Deer) || (herbivores instanceof Rabbit)) {
                eatPercent(predator, 80, i);
            } else if (herbivores instanceof Mouse) {
                eatPercent(predator, 90, i);
            } else if ((herbivores instanceof Sheep) || (herbivores instanceof Goat)) {
                eatPercent(predator, 70, i);
            } else if (herbivores instanceof Hog) {
                eatPercent(predator, 50, i);
            } else if (herbivores instanceof Buffalo) {
                eatPercent(predator, 20, i);
            } else if (herbivores instanceof Duck) {
                eatPercent(predator, 10, i);
            }
        } else if (predator instanceof Eagle) {
            if ((herbivores instanceof Rabbit) || (herbivores instanceof Mouse)) {
                eatPercent(predator, 90, i);
            } else if (herbivores instanceof Duck) {
                eatPercent(predator, 80, i);
            }
        }
    }
    private static void eatPP(Animal predator1, Animal predator2, int i, int j) {
        if ((predator1 instanceof Boa) || (predator2 instanceof Boa)) {
            if (predator2 instanceof Fox) {
                eatPercent(predator1, 15, j);
            } else if (predator1 instanceof Fox) {
                eatPercent(predator2, 15, i);
            } else if (predator2 instanceof Bear) {
                eatPercent(predator2, 80, i);
            } else if (predator1 instanceof Bear) {
                eatPercent(predator1, 80, j);
            }
        } else if ((predator1 instanceof Eagle) || (predator2 instanceof Eagle)) {
            if (predator2 instanceof Fox) {
                eatPercent(predator1, 10, j);
            } else if (predator1 instanceof Fox) {
                eatPercent(predator2, 10, i);
            }
        }
    }
    private static void eatHH(Animal herbivore1, Animal herbivore2, int i, int j) {
        if ((herbivore1 instanceof Caterpillar) || (herbivore2 instanceof Caterpillar)) {
            if ((herbivore2 instanceof Mouse) || (herbivore2 instanceof Hog) || (herbivore2 instanceof Duck)) {
                eatPercent(herbivore1, 90, i);
            } else if ((herbivore1 instanceof Mouse) || (herbivore1 instanceof Hog) || (herbivore1 instanceof Duck)) {
                eatPercent(herbivore2, 90, j);
            }
        } else if ((herbivore1 instanceof Hog) || (herbivore2 instanceof Hog)) {
            if (herbivore2 instanceof Mouse) {
                eatPercent(herbivore1, 50, j);
            } else if (herbivore1 instanceof Hog) {
                eatPercent(herbivore2, 50, i);
            }
        }
    }
    private static void eatHPlant(Animal herbivores, int i) {
        eatPercent(herbivores, 100, i);
    }
    private static void reproduction() {
        for (int i = 0; i < Z; i++) {
            for (int j = i + 1; j < Z; j++) {
                if ((animals[x][y][i] instanceof Bear) && (animals[x][y][j] instanceof Bear)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Bear animal = new Bear();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][i] instanceof Boa) && (animals[x][y][j] instanceof Boa)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Boa animal = new Boa();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][i] instanceof Buffalo) && (animals[x][y][j] instanceof Buffalo)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Buffalo animal = new Buffalo();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][i] instanceof Deer) && (animals[x][y][j] instanceof Deer)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Deer animal = new Deer();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][i] instanceof Duck) && (animals[x][y][j] instanceof Duck)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Duck animal = new Duck();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][i] instanceof Eagle) && (animals[x][y][j] instanceof Eagle)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Eagle animal = new Eagle();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][i] instanceof Fox) && (animals[x][y][j] instanceof Fox)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Fox animal = new Fox();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][i] instanceof Goat) && (animals[x][y][j] instanceof Goat)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Goat animal = new Goat();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][i] instanceof Hog) && (animals[x][y][j] instanceof Hog)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Hog animal = new Hog();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][i] instanceof Horse) && (animals[x][y][j] instanceof Horse)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Horse animal = new Horse();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][i] instanceof Mouse) && (animals[x][y][j] instanceof Mouse)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Mouse animal = new Mouse();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][i] instanceof Rabbit) && (animals[x][y][j] instanceof Rabbit)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Rabbit animal = new Rabbit();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][i] instanceof Sheep) && (animals[x][y][j] instanceof Sheep)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Sheep animal = new Sheep();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][i] instanceof Wolf) && (animals[x][y][j] instanceof Wolf)
                        && (!(((Animal) animals[x][y][i]).getReproduce())) && (!(((Animal) animals[x][y][j]).getReproduce()))) {
                    Wolf animal = new Wolf();
                    cellReproduction(animal, j);
                    break;
                } else if ((animals[x][y][j] == null) && (animals[x][y][j+1] == null) && ((j+1) < Z)) {
                    break;
                }
            }
            if ((animals[x][y][i] == null) && (animals[x][y][i+1] == null) && ((i+1) < Z))
                break;
        }
    }
    private static void cellReproduction(Animal animal, int i) {
        animalList.add(animal);
        animals[x][y][i+1] = animal;
        putXY(animal);
        ((Animal) animals[x][y][i]).setReproduce(true);
        reproductionIteration++;
        System.out.println("Родилось: " + reproductionIteration);
    }
    public static synchronized void grassGrowth (){
        int scorePlant = 0;
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                for (int k = 0; k < Z; k++) {
                    if (animals[i][j][k] instanceof Plant){
                        scorePlant++;
                    } else if ((animals[i][j][k]) == null && (k == 0)) {
                        break;
                    } else if ((animals[i][j][k+1] == null) && (animals[i][j][k+2] == null) && ((k+2) < Z))
                        break;
                }
            }
        }
        if (scorePlant < 450) {
            for (int i = 0; i < 50; i++) {
                Plant plant = new Plant();
                int x = plant.getX();
                int y = plant.getY();
                if (animals[x][y][0] == null){
                    animals[x][y][0] = plant;
                    String iconSave = icon;
                    icon = PLANT_ICON;
                    addContainer();
                    icon = iconSave;
                } else {
                    for (int j = 0; j < Z; j++) {
                        if(animals[x][y][j] == null) {
                            animals[x][y][j] = plant;
                            break;
                        }
                    }
                }
            }
            plantIteration+=50;
            System.out.println("Трава: " + plantIteration);
        }
    }
    public static synchronized void panelStatistics(){ //syn
        frameStatistics.setBounds(1000, 750, 400, 200);
        frameStatistics.setIconImage(Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("diagram.png")));
        frameStatistics.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = frameStatistics.getContentPane();
        container.setLayout(new GridBagLayout());
        for (int i = 0; i < labelsStatistics.length; i++) {
            for (int j = 0; j < labelsStatistics[i].length; j++) {
                if (i == 0 && j == 0){
                    constraintStatistics("Всего животных: ", i, j);
                } else if (i == 1 && j == 0) {
                    constraintStatistics("Кол-во съеденных животных/травы: ", i, j);
                } else if (i == 2 && j == 0) {
                    constraintStatistics("Кол-во рождаемости: ", i, j);
                } else if (i == 3 && j == 0) {
                    constraintStatistics("Кол-во смертей от голода: ", i, j);
                } else if (i == 4 && j == 0) {
                    constraintStatistics("Кол-во выращенной травы: ", i, j);
                } else if (i == 5 && j == 0) {
                    constraintStatistics("Среднее кол-во животных/травы на одно поле: ", i, j);
                } else {
                    constraintStatistics("", i, j);
                }
            }
        }
    }
    private static void constraintStatistics(String s, int i, int j) {
        labelsStatistics[i][j] = new JLabel(s);
        frameStatistics.add(labelsStatistics[i][j], new GridBagConstraints(j, i, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));
    }
    public static synchronized void statistics() { //syn
        double totalAnimals = 1;
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                for (int k = 0; k < Z; k++) {
                    if (animals[i][j][k] instanceof Animal) {
                        totalAnimals++;
                        if ((animals[i][j][k+1] == null) && (animals[i][j][k+2] == null) && ((k+2) < Z))
                            break;
                    }
                }
            }
        }
        for (int i = 0; i < labelsStatistics.length; i++) {
            for (int j = 0; j < labelsStatistics[i].length; j++) {
                if (i == 0 && j == 1){
                    container(("" + (int) totalAnimals), i, j);
                } else if (i == 1 && j == 1) {
                    container(("" + eatenIteration), i, j);
                } else if (i == 2 && j == 1) {
                    container(("" + reproductionIteration), i, j);
                } else if (i == 3 && j == 1) {
                    container(("" + hungerIteration), i, j);
                } else if (i == 4 && j == 1) {
                    container(("" + plantIteration), i, j);
                } else if (i == 5 && j == 1) {
                    container((String.format("%.3f", totalAnimals/(X*Y))), i, j);;
                } else {
                    continue;
                }
            }
        }
        frameStatistics.setVisible(true);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private static void container(String s, int i, int j) {
        frameStatistics.remove(labelsStatistics[i][j]);
        labelsStatistics[i][j] = new JLabel(s);
        frameStatistics.add(labelsStatistics[i][j], new GridBagConstraints(j, i, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));
    }
}
