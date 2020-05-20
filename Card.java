import java.util.*;

public class Card{
    int type;
    String cardName;
    //Constructors
    Card(int type, String cardName){
        this.type=type;
        this.cardName=cardName;
    }
}

class Chance extends Card{

    int position;
    int money;
    String name="Chance";

    Chance(int type,String cardName){
        super(type,cardName);
        this.name="Chance";
    }

    //setters

    public void setPosition(int position){
        this.position=position;
    }
    public void setMoney(int money){
        this.money=money;
    }


    LinkedHashMap info = new LinkedHashMap();
    public LinkedHashMap getInfo(){
        info.put("position",position);
        info.put("money",money);
        info.put("type",type);
        info.put("cardName",cardName);
        info.put("name",name);
        return info;
    }

}

class Community extends Card{

    int position;
    int money;
    String name="Community";

    Community(int type,String cardName){
        super(type,cardName);
    }

    //setters

    public void setPosition(int position){
        this.position=position;
    }
    public void setMoney(int money){
        this.money=money;
    }


    LinkedHashMap info = new LinkedHashMap();
    public LinkedHashMap getInfo(){
        info.put("position",position);
        info.put("money",money);
        info.put("type",type);
        info.put("cardName",cardName);
        info.put("name",name);
        return info;
    }

}
