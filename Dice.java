 import java.util.Random;

 public class Dice{
    Random rand = new Random();
    int a = rand.nextInt(6)+1;
    int b = rand.nextInt(6)+1;
    int total = a+b;
    boolean doub = a == b;

    public int getTotal(){
        return total;
    }
    public boolean getDoub(){
        return doub;
    }
    }
