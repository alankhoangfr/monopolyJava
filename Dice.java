 import java.util.Random;

 public class Dice{
    Random rand = new Random();
    private int a = rand.nextInt(6)+1;
    private int b = rand.nextInt(6)+1;
    private int total = a+b;
    private boolean doub = a == b;

    public int getA(){
        return a;
    }
    public int getB(){
        return b;
    }
    public int getTotal(){
        return total;
    }
    public boolean getDouble(){
        return doub;
    }

    }
