import java.util.*;
import java.util.Collections;
import java.util.Map.*;

public class Player{

    private int status=1;
    private String name;
    private int playerId;
    private int position =0;
    private int cash = 1500;
    private ArrayList<Integer> mortgage = new ArrayList<>();
    private ArrayList<Integer> mortgagePossible = new ArrayList<>();
    private LinkedHashMap<Integer,Integer> houses = new LinkedHashMap<Integer,Integer>();
    private ArrayList<Integer> hotels = new ArrayList<>();
    private ArrayList<Integer> monopoly = new ArrayList<>();
    private ArrayList<Integer> houseFamily = new ArrayList<>();
    private int jailFreeCard = 0;
    private ArrayList<String> typeCard = new ArrayList<>();
    private ArrayList<Boolean> dice = new ArrayList<>();
    private boolean jail = false;
    private boolean pay = false;
    private boolean repeatRoll = false;

    //Constructors
    Player(String name,int playerId){
        this.name=name;
        this.playerId=playerId;
    }

    //Setters

    public void setPosition(int position){
        this.position=position;
    }
    public void setStatus(int status){
        this.status=status;
    }
    public void setJailFreeCard(int jailFreeCard){
        this.jailFreeCard=jailFreeCard;
    }

    //Getter
    public int getStatus(){
        return status;
    }
    public String getName(){
        return name;
    }

    public int getPlayerId(){
        return playerId;
    }
    public int getPosition(){
        return position;
    }
    public int getCash(){
        return cash;
    }
    public boolean getJail(){
        return jail;
    }
    public boolean getPay(){
        return pay;
    }
    public boolean getRepeatRoll(){
        return repeatRoll;
    }
    public ArrayList<Integer> getMortgage(){
        return mortgage;
    }
    public ArrayList<Integer> getMortgagePossible (){
        return mortgagePossible;
    }
    public LinkedHashMap<Integer,Integer> getHouses (){
        return houses;
    }
    public ArrayList<Integer> getHotels (){
        return hotels;
    }
    public ArrayList<Integer> getMonopoly (){
        return monopoly;
    }
    public ArrayList<Integer> getHouseFamily (){
        return houseFamily;
    }
    public int getJailFreeCard(){
        return jailFreeCard;
    }
    public ArrayList<String> getTypeCard (){
        return typeCard;
    }
    public ArrayList<Boolean> getDiceHistory(){
        return dice;
    }

    //Methods

    public LinkedHashMap<Integer, ArrayList<Integer>> buildingFamilyList(){
        LinkedHashMap<Integer, ArrayList<Integer>> buildingFamily = new LinkedHashMap<Integer, ArrayList<Integer>>();
        ArrayList<Integer> darkPurple = new ArrayList<Integer> (Arrays.asList(1, 3));
        buildingFamily.put(1,darkPurple);
        ArrayList<Integer> lightBlue = new ArrayList<Integer> (Arrays.asList(6, 8,9));
        buildingFamily.put(2,lightBlue);
        ArrayList<Integer> pink = new ArrayList<Integer> (Arrays.asList(11, 13,14));
        buildingFamily.put(3,pink);
        ArrayList<Integer> orange = new ArrayList<Integer> (Arrays.asList(16,18, 19));
        buildingFamily.put(4,orange);
        ArrayList<Integer> red = new ArrayList<Integer> (Arrays.asList(21, 23,24));
        buildingFamily.put(5,red);
        ArrayList<Integer> yellow = new ArrayList<Integer> (Arrays.asList(26,27,29));
        buildingFamily.put(6,yellow);
        ArrayList<Integer> green = new ArrayList<Integer> (Arrays.asList(31, 32,34));
        buildingFamily.put(7,green);
        ArrayList<Integer> darkBlue = new ArrayList<Integer> (Arrays.asList(37, 39));
        buildingFamily.put(8,darkBlue);
        ArrayList<Integer> airport = new ArrayList<Integer> (Arrays.asList(5,15,25,35));
        buildingFamily.put(9,airport);
        ArrayList<Integer> utility = new ArrayList<Integer> (Arrays.asList(12,28));
        buildingFamily.put(10,utility);
        return buildingFamily;
    }


    LinkedHashMap info = new LinkedHashMap();
    public LinkedHashMap getInfo (int cashNow){
        info.put("Name",name);
        info.put("status",status);
        info.put("Cash",cashNow);
        info.put("Get Out of Jail Free Card",jailFreeCard);
        info.put("Mortgage",mortgage);
        info.put("Mortgage Free",mortgagePossible);
        info.put("Houses",houses);
        info.put("Hotels",hotels);
        return info;
    }
    public void printGetInfo (Object initial){
        Initalise init = (Initalise) initial;
        int playerView= playerId+1;
        int liquidationValue = liquidateAll(init, 1,0);
        System.out.println("");
        System.out.println("----------------------------------------------------------");
        System.out.println("id: "+playerView+ ", Name: "+name+ ", Cash: $"+cash +", Liquidation Value: $"
            +liquidationValue+", Get Out of Jail Free Card: "+jailFreeCard);
        System.out.println("Mortgage Property");
        for(int deed:mortgage){
            int family = getFamilyList(deed);
            String familyName = init.familyNames(family);
            System.out.println("Property: "+deed+", Propery Colour: "+familyName);
        }
        System.out.println("Mortgage Free");
        for(int deed:mortgagePossible){
            int family = getFamilyList(deed);
            String familyName = init.familyNames(family);
            System.out.println("Property: "+deed+", Propery Colour: "+familyName);
        }
        System.out.println("Hotels");
        for(int deed:hotels){
            int family = getFamilyList(deed);
            String familyName = init.familyNames(family);
            System.out.println("Property: "+deed+", Propery Colour: "+familyName);
        }
        System.out.println("Houses");
        for(Entry<Integer, Integer> entry: houses.entrySet()){
            int deed = (Integer) entry.getKey();
            int num =(Integer) entry.getValue();
            int family = getFamilyList(deed);
            String familyName = init.familyNames(family);
            System.out.println("Property: "+deed+", Propery Colour: "+familyName+", Number of Houses: "+ num);
        }
        System.out.println("----------------------------------------------------------");
        System.out.println("");
    }
    public LinkedHashMap getTradeInfo (){
        info.put("id",playerId+1);
        info.put("Name",name);
        info.put("Cash",cash);
        info.put("Mortgage property: ",mortgage);
        info.put("Mortgage Free",mortgagePossible);
        info.put("Get Out of Jail Free Card",jailFreeCard);
        return info;
    }
    public void printTradeInfo (){
        Initalise init = new Initalise ();
        int playerView= playerId+1;
        System.out.println("");
        System.out.println("id: "+playerView+ ", Name: "+name+ ", Cash: "+cash+ ", Get Out of Jail Free Card: "
            +jailFreeCard);
        for(int deed:mortgage){
            int family = getFamilyList(deed);
            String familyName = init.familyNames(family);

            System.out.println("Property: "+deed+", Propery Colour: "+familyName+" ---- Mortgaged");
        }
        for(int deed:mortgagePossible){
            int family = getFamilyList(deed);
            String familyName = init.familyNames(family);
            System.out.println("Property: "+deed+", Propery Colour: "+familyName+" ---- Mortgage Free");
        }
        System.out.println("");
    }
    public int getFamilyList(int propertyPosition){
        LinkedHashMap<Integer, ArrayList<Integer>> familyList=buildingFamilyList();
        for (Entry<Integer, ArrayList<Integer>> entry: familyList.entrySet()){
            if(entry.getValue().contains(propertyPosition)){
                return entry.getKey();
            }
        }
        return -1;
    }

    //Game Play - Movement
    public void restartDice (){
        dice.clear();
    }
    public void adjust(int money, int change){
        cash+=money;
        position=rollPlusPosition(change);
    }
    public List<Boolean> updateDice (boolean doub){
        int histDice = dice.size();
        if(histDice<4){
            dice.add(doub);
        }else{
            dice.remove(0);
            dice.add(doub);
        }
        return dice;
    }
    public int rollPlusPosition (int num){
        int newTotal = num + position;
        if (newTotal>=40){
            newTotal = newTotal-40;
        }
        return newTotal;
    }
    public int newPosition (int num,boolean doub){
        int newTotal = rollPlusPosition(num);
        if(jail==true){
            repeatRoll = false;
            if(doub==false){
                updateDice(doub);
                boolean triple = checkJail(dice,"Fine");
                if (triple==true){
                    pay=true;
                    updateCash(-50);
                    jail=false;
                    return newTotal;
                }else{
                    return 10;
                }
            }else{
                jail=false;
                return newTotal;
            }
        }else{
            pay=false;
            updateDice(doub);
            boolean goToJail = checkJail(dice,"toJail");
            if (goToJail == true){
                jail = true;
                repeatRoll=false;
                return 10;
            }else if(doub==true){
                repeatRoll = true;
                return newTotal;
            }else{
                repeatRoll=false;
                return newTotal;
            }
        }
    }
    public boolean checkJail (List<Boolean> listDice, String type){
        boolean result = true;
        if(listDice.size()<3){
            return false;
        }else{
            if(type=="toJail"){
                int i = 0;
                while(i<3){
                    int index = listDice.size()-i-1;
                    if(listDice.get(index)==false){
                        result = false;
                        break;
                    }
                    i++;
                }
                return result;
            }else{
                int i = 0;
                while(i<3){
                    int index = listDice.size()-i-1;
                    if(listDice.get(index)==true){
                        result = false;
                        break;
                    }
                    i++;
                }
                return result;
            }

        }
    }

    //Game Play impact to player
    //Subtract or add amount to plyaer
    public void updateCash (int amount){
        cash+=amount;
    }
    public int closestStation (int currPosition){
        int stationPosition;
        if(currPosition<5 || currPosition>35){
            stationPosition = 5;
            //Pass GO to collect 200
            if(currPosition>35 && currPosition<40){
                updateCash(200);
            }
        }else if(currPosition>6  && currPosition<15){
            stationPosition = 15;
        }else if(currPosition>16  && currPosition<25){
            stationPosition = 25;
        }else if(currPosition>26  && currPosition<35){
            stationPosition = 35;
        }else{
            stationPosition = currPosition;
        }
        return stationPosition;
    }
    public int closestUtility (int currPosition){
        int stationPosition;
        if(currPosition<12 || currPosition>28){
            stationPosition = 12;
            //Pass GO to collect 200
            if(currPosition>28 && currPosition<40){
                updateCash(200);
            }
        }else if(currPosition>12  && currPosition<28){
            stationPosition = 28;
        }else{
            stationPosition = currPosition;
        }
        return stationPosition;
    }
    public void payBond (){
        updateCash(-50);
        jail=false;
        restartDice();
    }
    public void useCard (){
        jailFreeCard -=1;
        jail=false;
        restartDice();
        typeCard.remove(0);
    }
    public int numberHouses(){
        if(houses.size()==0){
            return 0;
        }else{
            int count = 0;
            for (Entry<Integer, Integer> comb : houses.entrySet()) {
                count+=comb.getValue();
            }
            return count;
        }
    }
    public int numberHotels(){
        return hotels.size();
    }

    public void updateInfo(LinkedHashMap infoData, int numberPlayers,int rollTotal){
        int type = (Integer) infoData.get("type");
        switch (type){
            // type 0: change position only and if necessary +200 when passing GO
            case 0:
                int prevPos = position;
                position = (Integer) infoData.get("position");
                String cardName = (String) infoData.get("name");
                if(cardName == "Go to Jail"){
                    jail = true;
                    dice.clear();
                }else if(position==0){
                    updateCash(200);
                }
                break;
            //type 1: receive or pay standard amount
            case 1:
                updateCash((Integer) infoData.get("money"));
                break;
            //type 2: change position and maybe must pass through GO for 200 and pay or buy at property
            case 2:
                int finalPosition = (Integer) infoData.get("position");
                if (position>finalPosition){
                    updateCash(200);
                }
                position = finalPosition;
                break;
            //type 3 : pay to x amounts of players.
            case 3:
                int amount = (Integer) infoData.get("money");
                int totalAmount = amount*(numberPlayers-1);
                updateCash(totalAmount);
                break;
            //Type 4: pay 10 times dice roll for uyility.
            case 4:
                int factor = 10;
                if(position<12 && position>28){
                    position = 12;
                }else{
                    position = 28;
                }
                updateCash(rollTotal*factor);
                break;
            //Type 5 : pay 2 times the fee for railroad
            case 5:
                 int factor1 =2;
                 int price = (Integer) infoData.get("rentPrice");
                 if ((Integer) infoData.get("status")==0){
                    buyRentAuction(infoData,0,1);
                 }else{
                    updateCash(-1*price*factor1);
                 }
                 break;
            // Type 6: pay x amount for each house and y amount for each hotel
            case 6:
                int numberHouse = numberHouses();
                int numberHotel = numberHotels();
                if((Integer) infoData.get("money")==40){
                    cash-=40*numberHouse+115*numberHotel;
                }else{
                    cash-= 25*numberHouse + 100*numberHotel;
                }
                break;
            case 7:
                jailFreeCard+=1;
                typeCard.add((String) infoData.get("card"));
                break;
            case 8:
                position = newPosition(-3,false);
                break;
        }

    }
    public ArrayList<Integer> checkMonopoly (){
        ArrayList<Integer> monopolyList = new ArrayList<Integer>();
        for(int i:houseFamily){
            int b=Collections.frequency(houseFamily, i);
            if(i==1 || i==8){
                if(b==2 && monopolyList.contains(i)==false){
                    monopolyList.add(i);
                    if(!monopoly.contains(i)){
                        LinkedHashMap<Integer, ArrayList<Integer>>buildingFamily=buildingFamilyList();
                        ArrayList<Integer> bFamily = buildingFamily.get(i);
                        for (int h:bFamily){
                            houses.put(h,0);
                        }
                    }
                }
            }else if(i>1 && i <8){
                if(b==3 && monopolyList.contains(i)==false){
                    monopolyList.add(i);
                    if(!monopoly.contains(i)){
                        LinkedHashMap<Integer, ArrayList<Integer>>buildingFamily=buildingFamilyList();
                        ArrayList<Integer> bFamily = buildingFamily.get(i);
                        for (int h:bFamily){
                            houses.put(h,0);
                        }
                    }
                }
            }
        }
        return monopolyList;
    }
    //Trade
    public void tradeCashDeedsJail(LinkedHashMap tradeList1, LinkedHashMap tradeList2){
        //tradeList1 - current player outflow of property and money
        //tradeList2 - current player inflow of property and money
        ArrayList<Integer> outflowDeeds = (ArrayList<Integer>) tradeList1.get("Deeds");
        ArrayList<Integer> inflowDeeds = (ArrayList<Integer>) tradeList2.get("Deeds");
        int outflowCash = (Integer) tradeList1.get("Cash");
        int inflowCash = (Integer) tradeList2.get("Cash");
        ArrayList<String> outflowJail = (ArrayList<String>) tradeList1.get("jailFreeCard");
        ArrayList<String> inflowJail = (ArrayList<String>) tradeList2.get("jailFreeCard");
        if (outflowJail.size()>0){
            for(int i=0;i<outflowJail.size();i++){
                typeCard.remove(0);
            }
        }
        if(inflowJail.size()>0){
            for(int i=0;i<inflowJail.size();i++){
                typeCard.add(inflowJail.get(i));
            }
        }
        updateCash(inflowCash-outflowCash);
        jailFreeCard= jailFreeCard-outflowJail.size()+inflowJail.size();
        for(int deed: outflowDeeds){
            int family = getFamilyList(deed);
            mortgagePossible.remove(new Integer(deed));
            houseFamily.remove(new Integer(family));
        }
        for (int deed: inflowDeeds){
            int family = getFamilyList(deed);
            mortgagePossible.add(deed);
            houseFamily.add(family);
        }
        Collections.sort(mortgagePossible);
        Collections.sort(houseFamily);
        monopoly=checkMonopoly();
    }

    //Remove Mortgage after trade

    public void changeMortgage(int deedPosition,int addMinus){
        if(addMinus==1){
            mortgage.add(deedPosition);
        }else{
            mortgage.remove(new Integer(deedPosition));
        }
    }

    // mortgage
    public LinkedHashMap mortgageAction(LinkedHashMap propertyInfo){
        LinkedHashMap tempPropertyInfo = (LinkedHashMap) propertyInfo;
        int status = (Integer) propertyInfo.get("status");
        int family=(Integer) propertyInfo.get("family");
        if(status!=2){
            int mortgageValue = (Integer) propertyInfo.get("mortgage");
            int propertyLocation = (Integer) propertyInfo.get("position");
            tempPropertyInfo.put("status",2);
            tempPropertyInfo.put("rentPrice",0);
            tempPropertyInfo.put("owner",playerId);
            updateCash(mortgageValue);
            mortgagePossible.remove(new Integer(propertyLocation));
            houseFamily.remove(new Integer(family));
            mortgage.add(propertyLocation);

        }else if(status==2){
            int mortgageValue = (int) Math.round((Integer) propertyInfo.get("mortgage"));
            int interest = (int) Math.round((Integer) propertyInfo.get("price")*0.10);
            int propertyLocation = (Integer) propertyInfo.get("position");
            int landRent = (Integer) propertyInfo.get("landRent");
            tempPropertyInfo.put("owner",playerId);
            tempPropertyInfo.put("status",1);
            tempPropertyInfo.put("rentPrice",landRent);
            updateCash(-mortgageValue-interest);
            mortgage.remove(new Integer(propertyLocation));
            houseFamily.add(family);
            mortgagePossible.add(propertyLocation);

        }
        Collections.sort(houseFamily);
        Collections.sort(mortgagePossible);
        monopoly=checkMonopoly();
        return tempPropertyInfo;
    }

    //Buy, auction and rent
    //action Buy or rent == 1 and auction ==0;
    public LinkedHashMap buyRentAuction (LinkedHashMap propertyInfo,int payment,int action){
        LinkedHashMap tempPropertyInfo = (LinkedHashMap) propertyInfo;
        int status = (Integer) propertyInfo.get("status");
        int owner = (Integer) propertyInfo.get("owner");
        int rentPrice=(Integer) propertyInfo.get("rentPrice");
        int housePrice=(Integer) propertyInfo.get("price");
        int family=(Integer) propertyInfo.get("family");
        int landRent = (Integer) propertyInfo.get("landRent");
        int propertyPosition = (Integer) propertyInfo.get("position");
        if (action==0){
            if(status==0){
                tempPropertyInfo.put("owner",playerId);
                tempPropertyInfo.put("status",1);
                tempPropertyInfo.put("rentPrice",landRent);
                updateCash(-payment);
                houseFamily.add(family);
                mortgagePossible.add(propertyPosition);
            }else if(status==1){
                tempPropertyInfo.put("owner",playerId);
                updateCash(-payment);
                houseFamily.add(family);
                mortgage.add(propertyPosition);
            }else if (status==2){
                tempPropertyInfo.put("owner",playerId);
                updateCash(-payment);
                houseFamily.add(family);
                mortgage.add(propertyPosition);
            }
        }else if (action==1){
            if(status ==0){
                tempPropertyInfo.put("owner",playerId);
                tempPropertyInfo.put("status",1);
                if(family==9){
                    ArrayList<Integer> trainStations = new ArrayList<>();
                    int numStations = Collections.frequency(houseFamily, 9);
                    updateCash(-200);
                    switch(numStations){
                        case 0:
                            tempPropertyInfo.put("rentPrice",25);
                            break;
                        case 1:
                            tempPropertyInfo.put("rentPrice",50);
                            break;
                        case 2:
                            tempPropertyInfo.put("rentPrice",100);
                            break;
                        case 3:
                            tempPropertyInfo.put("rentPrice",200);
                            break;
                    }
                }else{
                    tempPropertyInfo.put("rentPrice",landRent);
                    updateCash(-housePrice);
                }
                houseFamily.add(family);
                mortgagePossible.add(propertyPosition);
            }else if(status==1){//Renting
                int type = (Integer) propertyInfo.get("type");
                if(owner!=playerId){
                    if(family==10){
                        updateCash(payment*-1);
                    }else if(type==5){
                        updateCash(-rentPrice*2);
                    }
                    else{
                        updateCash(-rentPrice);
                    }
                }
            }
        }

        Collections.sort(houseFamily);
        Collections.sort(mortgagePossible);
        monopoly=checkMonopoly();
        return tempPropertyInfo;
    }
    //build or sell
    public boolean buildAction (LinkedHashMap propertyInfo,int buildSell){
        int family = (Integer) propertyInfo.get("family");
        int buildingPosition = (Integer) propertyInfo.get("position");
        int buildCost = (Integer)propertyInfo.get("buildCost");
        LinkedHashMap<Integer, ArrayList<Integer>> familyList= buildingFamilyList();
        if(!monopoly.contains(family)){
            return false;
        }else{
            ArrayList<Integer> groupList = new ArrayList<Integer>();
            for(Integer i: familyList.get(family)){
                if(hotels.contains(i)){
                    groupList.add(5);
                }else{
                    groupList.add((Integer) houses.get(i));
                }
            }
            int max = Collections.max(groupList);
            int min = Collections.min(groupList);
            int currentNumber;
            if(hotels.contains(buildingPosition)){
                currentNumber = 5;
            }else{
                currentNumber = houses.get(buildingPosition);
            }
            //Build
            if(buildSell==1){
                if(max!=min){
                    if(currentNumber+1>max||currentNumber+1>5){
                        return (false);
                    }else{
                        if(currentNumber+1>5){
                            return (false);
                        }else{
                            if(currentNumber<4){
                                if(currentNumber==0){
                                    mortgagePossible.remove(new Integer(buildingPosition));
                                }
                                houses.put(buildingPosition,currentNumber+1);
                            }else{
                                houses.put(buildingPosition,0);
                                hotels.add(buildingPosition);
                            }
                            updateCash(-buildCost);
                            return (true);
                        }
                    }
                }else{
                    if(currentNumber+1>5){
                        return (false);
                    }else{
                        if(currentNumber<4){
                            if(currentNumber==0){
                                mortgagePossible.remove(new Integer(buildingPosition));
                            }
                            houses.put(buildingPosition,currentNumber+1);
                        }else{
                            houses.put(buildingPosition,0);
                            hotels.add(buildingPosition);
                        }
                        updateCash(-buildCost);
                        return (true);
                    }
                }
            }else{
            //Sell
                int sellValue = (Integer) Math.round(buildCost/2);
                if(max!=min){
                    if(currentNumber-1<min||currentNumber-1<0){
                        return (false);
                    }else{
                        if(currentNumber-1<0){
                            return (false);
                        }else{
                            if(currentNumber==5){
                                houses.put(buildingPosition,4);
                                hotels.remove(new Integer(buildingPosition));
                            }else{
                                if(currentNumber==1){
                                    mortgagePossible.add(buildingPosition);
                                }
                                houses.put(buildingPosition,currentNumber-1);
                            }
                            updateCash(sellValue);
                            return (true);
                        }
                    }
                }else{
                    if(currentNumber-1<0){
                        return (false);
                    }else{
                        if(currentNumber==5){
                            houses.put(buildingPosition,4);
                            hotels.remove(new Integer(buildingPosition));
                        }else{
                            if(currentNumber==1){
                                mortgagePossible.add(buildingPosition);
                            }
                            houses.put(buildingPosition,currentNumber-1);
                        }
                        updateCash(sellValue);
                        return (true);
                    }
                }
            }


        }
    }
    //simulate==1 -> simulation, simulate ==0 -> not simulation
    public int liquidateAll(Object initial, int simulate,int print){
        Initalise init = (Initalise) initial;
        if(print==1){
            System.out.println("starting Cash: "+cash);
        }
        ArrayList<Integer>hotels1;
        LinkedHashMap<Integer,Integer>houses1;
        ArrayList<Integer>mortgagePossible1;
        if(simulate==1){
            hotels1=(ArrayList<Integer>) hotels.clone();
            houses1=(LinkedHashMap<Integer,Integer>)houses.clone();
            mortgagePossible1=(ArrayList<Integer>) mortgagePossible.clone();
        }else{
            hotels1=(ArrayList<Integer>) hotels;
            houses1=(LinkedHashMap<Integer,Integer>)houses;
            mortgagePossible1=(ArrayList<Integer>) mortgagePossible;
        }
        //liqudate hotels
        if(print==1){
            if(hotels1.size()==1){
                System.out.println("Liquadating All Hotels - "+name+" has "+hotels.size()+" hotel");
            }else{
                System.out.println("Liquadating All Hotels - "+name+" has "+hotels.size()+" hotels");
            }
        }
        int totalHotelValue= 0;
        for(int propertyPosition: hotels1){
            LinkedHashMap info = (LinkedHashMap) init.infoPosition(propertyPosition);
            int buildValue = (Integer) info.get("buildCost");
            int sellValue = (int) Math.round(buildValue/2);
            int totalSellValue = sellValue*5;

            totalHotelValue+=totalSellValue;
            if(simulate==0){
                mortgagePossible.add(propertyPosition);
                updateCash(totalSellValue);
            }
        }
        hotels1.clear();
        if(print==1){
            System.out.println("Able to raise $"+totalHotelValue);
            if(simulate==0){
                System.out.println("Current Cash: $"+cash);
            }
            //liqudate houses
            if(numberHouses()==1){
            System.out.println("Liquadating All hosues - "+name+" has "+numberHouses()+" houses");
            }else{
                System.out.println("Liquadating All hosues - "+name+" has "+numberHouses()+" houses");
            }
        }

        int totalHouseValue= 0;
        for (Entry<Integer, Integer> comb : houses1.entrySet()){
            int propertyPosition =  comb.getKey();
            LinkedHashMap info = (LinkedHashMap) init.infoPosition(propertyPosition);
            int buildValue = (Integer) info.get("buildCost");
            int sellValue = (int) Math.round(buildValue/2);
            int totalSellValue = sellValue*comb.getValue();

            totalHouseValue+=totalSellValue;
            if(simulate==0){
                if(comb.getValue()>0){
                   mortgagePossible.add(propertyPosition);
                }
                updateCash(totalSellValue);
            }
        }
        houses1.clear();
        if(print==1){
            System.out.println("Able to raise $"+totalHouseValue);
            if(simulate==0){
                System.out.println("Current Cash: $"+cash);
            }
            //Mortgage all deeds
            if(mortgagePossible1.size()==1){
                System.out.println("Mortgaging all properties - "+name+" has "+mortgagePossible1.size()+" properties");
            }else{
                System.out.println("Mortgaging all properties - "+name+" has "+mortgagePossible1.size()+" properties");
            }
        }
        int totalMortgageValue= 0;
        for (int propertyPosition: mortgagePossible1){
            LinkedHashMap info = (LinkedHashMap) init.infoPosition(propertyPosition);
            int mortgageValue = (Integer) info.get("mortgage");
            totalMortgageValue+=mortgageValue;
            if(simulate==0){
                updateCash(mortgageValue);
                mortgage.add(propertyPosition);
            }
        }
        mortgagePossible1.clear();
        if(print==1){
            System.out.println("Able to raise $"+totalMortgageValue);
        }
        if(simulate==0){
            if(print==1){
                System.out.println("Current Cash: $"+cash);
            }
            return cash;
        }else{
            int simulateCash = cash+totalMortgageValue+totalHouseValue+totalHotelValue;
            if(print==1){
                System.out.println("Possible Cash: $"+simulateCash);
            }
            return simulateCash;
        }
    }
    public void clearEverything(){
        mortgage.clear();
        mortgage.clear();
        mortgagePossible.clear();
        houses.clear();
        hotels.clear();
        monopoly.clear();
        houseFamily.clear();
        typeCard.clear();
    }

}
