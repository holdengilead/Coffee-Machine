import java.util.Scanner;

enum CupOfCoffee {
    ESPRESSO(250, 0, 16, 4), LATTE(350, 75, 20, 7), CAPPUCCINO(200, 100, 12, 6);

    private final int water;
    private final int milk;
    private final int coffeeBeans;
    private final int money;

    CupOfCoffee(int water, int milk, int coffeeBeans, int money) {
        this.water = water;
        this.milk = milk;
        this.coffeeBeans = coffeeBeans;
        this.money = money;
    }

    public int water() {
        return water;
    }

    public int milk() {
        return milk;
    }

    public int coffeeBeans() {
        return coffeeBeans;
    }

    public int money() {
        return money;
    }

}

enum State {
    WAITING_FOR_ORDER, WAITING_FOR_WATER, WAITING_FOR_MILK, WAITING_FOR_COFFEE_BEANS, WAITING_FOR_CUPS,
    WAITING_FOR_TYPE_CUP, STOPPED;
}

public class CoffeeMachine {

    private final static int INITIAL_WATER = 400;
    private final static int INITIAL_MILK = 540;
    private final static int INITIAL_COFFEE_BEANS = 120;
    private final static int INITIAL_DISPOSABLE_CUPS = 9;
    private final static int INITIAL_MONEY = 550;

    private int water;
    private int milk;
    private int coffeeBeans;
    private int disposableCups;
    private int money;
    private State state;

    public CoffeeMachine() {
        this.water = INITIAL_WATER;
        this.milk = INITIAL_MILK;
        this.coffeeBeans = INITIAL_COFFEE_BEANS;
        this.disposableCups = INITIAL_DISPOSABLE_CUPS;
        this.money = INITIAL_MONEY;
        setWaitingForOrder();
    }

    public void setWaitingForOrder() {
        this.state = State.WAITING_FOR_ORDER;
        System.out.println("Write action (buy, fill, take, remaining, exit):");
    }

    public void listSupplies() {
        System.out.println("\nThe coffee machine has:");
        System.out.println(this.water + " of water");
        System.out.println(this.milk + " of milk");
        System.out.println(this.coffeeBeans + " of coffee beans");
        System.out.println(this.disposableCups + " of disposable cups");
        System.out.println("$" + this.money + " of money\n");
    }

    public void takeMoney() {
        System.out.println("\nI gave you $" + this.money + "\n");
        this.money = 0;
    }

    public boolean checkResources(CupOfCoffee cup) {
        if (this.disposableCups == 0) {
            System.out.println("Sorry, not enough disposable cups!");
            System.out.println("");
            return false;
        } else if (cup.water() > this.water) {
            System.out.println("Sorry, not enough water!");
            System.out.println("");
            return false;
        } else if (cup.milk() > this.milk) {
            System.out.println("Sorry, not enough milk!");
            System.out.println("");
            return false;
        } else if (cup.coffeeBeans() > this.coffeeBeans) {
            System.out.println("Sorry, not enough coffee beans!");
            System.out.println("");
            return false;
        }
        System.out.println("I have enough resources, making you a coffee!");
        System.out.println("");
        return true;
    }

    public void makeCup(String action) {
        CupOfCoffee cup;
        switch (action) {
            case "back":
                return;
            case "1":
                cup = CupOfCoffee.ESPRESSO;
                break;
            case "2":
                cup = CupOfCoffee.LATTE;
                break;
            case "3":
                cup = CupOfCoffee.CAPPUCCINO;
                break;
            default:
                return;
        }
        if (checkResources(cup)) {
            this.water -= cup.water();
            this.milk -= cup.milk();
            this.coffeeBeans -= cup.coffeeBeans();
            this.disposableCups--;
            this.money += cup.money();
        }
    }

    public void setState(String action) {
        switch (action) {
            case "remaining":
                listSupplies();
                setWaitingForOrder();
                break;
            case "take":
                takeMoney();
                setWaitingForOrder();
                break;
            case "exit":
                this.state = State.STOPPED;
                break;
            case "fill":
                System.out.println("\nWrite how many ml of water do you want to add:");
                this.state = State.WAITING_FOR_WATER;
                break;
            case "buy":
                System.out.println(
                        "\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
                this.state = State.WAITING_FOR_TYPE_CUP;
                break;
            default:
                break;
        }
    }

    public void processAction(String action) {
        switch (this.state) {
            case WAITING_FOR_ORDER:
                setState(action);
                break;
            case WAITING_FOR_TYPE_CUP:
                makeCup(action);
                setWaitingForOrder();
                break;
            case WAITING_FOR_WATER:
                this.water += Integer.parseInt(action);
                System.out.println("Write how many ml of milk do you want to add: ");
                this.state = State.WAITING_FOR_MILK;
                break;
            case WAITING_FOR_MILK:
                this.milk += Integer.parseInt(action);
                System.out.println("Write how many grams of coffee beans do you want to add:");
                this.state = State.WAITING_FOR_COFFEE_BEANS;
                break;
            case WAITING_FOR_COFFEE_BEANS:
                this.coffeeBeans += Integer.parseInt(action);
                System.out.println("Write how many disposable cups of coffee do you want to add:");
                this.state = State.WAITING_FOR_CUPS;
                break;
            case WAITING_FOR_CUPS:
                this.disposableCups += Integer.parseInt(action);
                System.out.println("");
                setWaitingForOrder();
                break;
            default:
                break;
        }
    }

    public boolean isOn() {
        return state != State.STOPPED;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CoffeeMachine cm = new CoffeeMachine();

        while (cm.isOn()) {
            cm.processAction(scanner.next());
        }
    }
}
