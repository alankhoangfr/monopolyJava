import java.util.*;
import java.util.Collections;
import java.util.Map.*;
public class test{
    public static void main (String[] args){
        HashMap<Integer,Integer> temp = new HashMap<Integer,Integer>();
        temp.put(21,0);
        temp.put(23,0);
        temp.put(50,3);
        if(temp.size()==0){
            System.out.print(0);
        }else{
            int count = 0;
            for (int value : temp.values()) {
                count+=value;
            }
            System.out.print(count);
            System.out.print(temp);
        }

    }
}
