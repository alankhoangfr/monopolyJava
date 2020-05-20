import java.util.*;
import java.util.Collections;
import java.util.Map.*;

public class Initalise{
    ArrayList<Integer> chanceArray = new ArrayList<>();
    ArrayList<Integer> communityArray = new ArrayList<>();
    ArrayList<Integer> otherArray = new ArrayList<>();
    LinkedHashMap<Integer, LinkedHashMap> infoChance = new LinkedHashMap<Integer, LinkedHashMap>();
    LinkedHashMap<Integer, LinkedHashMap> infoCommunity = new LinkedHashMap<Integer, LinkedHashMap>();
    LinkedHashMap<Integer, LinkedHashMap> infoOthers = new LinkedHashMap<Integer, LinkedHashMap>();
    LinkedHashMap<Integer, LinkedHashMap> infoProperty = new LinkedHashMap<Integer, LinkedHashMap>();
    ArrayList <Integer> chanceDeck= new ArrayList<>();
    ArrayList <Integer> communityDeck= new ArrayList<>();

//Getters
    public LinkedHashMap<Integer, LinkedHashMap> getInfoChance (){
        return infoChance;
    }
    public LinkedHashMap<Integer, LinkedHashMap> getInfoCommunity(){
        return infoCommunity;
    }
    public LinkedHashMap<Integer, LinkedHashMap> getInfoOthers(){
        return infoOthers;
    }
    public LinkedHashMap<Integer, LinkedHashMap> getInfoProperty(){
        return infoProperty;
    }
    public ArrayList <Integer> getChanceDeck(){
        return chanceDeck;
    }
    public ArrayList <Integer> getCommunityDeck (){
        return communityDeck;
    }

    public void printInfoProperty(){
        for(Entry<Integer, LinkedHashMap>entry:infoProperty.entrySet()){
            LinkedHashMap temp = entry.getValue();
            int family= (int) temp.get("family");
            System.out.println(entry.getValue());

        }
    }

    public LinkedHashMap infoPosition (int positionNumber){
        //Type= {0:card,1:building}
        if(chanceArray.contains(positionNumber)){
            int size = chanceDeck.size()-1;
            int card = chanceDeck.get(size);
            chanceDeck.remove(size);
            if(card!=8){
                chanceDeck.add(0,card);
            }
            return infoCommunity.get(card);
        }else if(communityArray.contains(positionNumber)){
            int size = communityDeck.size()-1;
            int card = communityDeck.get(size);
            communityDeck.remove(size);
            if(card!=8){
                communityDeck.add(0,card);
            }
            return infoCommunity.get(card);
        }else if(otherArray.contains(positionNumber)){
            return infoOthers.get(positionNumber);
        }else{
            LinkedHashMap temp = infoProperty.get(positionNumber);
            temp.put("type",9);
            return temp;
        }
    }

    public void updatePropertyDetails(int position,LinkedHashMap updateInformation){
        infoProperty.put(position,(LinkedHashMap) updateInformation);
    }
    //mortgageOrUnmortgage ==1 -> mortgage, mortgageOrUnmortgage==2->unmortgage
    public ArrayList<LinkedHashMap> getMortgageInfo(ArrayList<Integer> properties,int mortgageOrUnmortgage){
        ArrayList<LinkedHashMap> mortgage = new ArrayList<>();
        for (int num: properties) {
            LinkedHashMap temp = new LinkedHashMap();
            LinkedHashMap infoProp = infoProperty.get(num);
            temp.put("Position",num);
            temp.put("Name",(String) infoProp.get("name"));
            if(mortgageOrUnmortgage==1){
                temp.put("Mortgage Value", (Integer) infoProp.get("mortgage"));
            }else{
                temp.put("Interest",(int) Math.round((Integer) infoProp.get("mortgage")*0.1));
                temp.put("Mortgage Value",(Integer) infoProp.get("mortgage"));
            }
            mortgage.add(temp);
        }
        return mortgage;
    }

    public ArrayList<Integer> shuffle(){
        ArrayList<Integer> deck = new ArrayList<>();
        for(int i=1;i<17;i++){
            deck.add(i);
        }
        Collections.shuffle(deck);
        return deck;
    }
    //Auction text - 1 else 0
    public static int userType(ArrayList<Integer> possibleChoices,int auction){
        ArrayList<Integer> array =  (ArrayList<Integer>) possibleChoices.clone();
        Scanner sc = new Scanner(System.in);
        if(array.size()<40){
            System.out.println(array);
        }
        int option = -99;
        do {
            while (!sc.hasNextInt()) {
                sc.nextLine(); // Clears the invalid input
                System.out.println("Please enter the option. Enter an Integer");
                break;
            }
            if(sc.hasNextInt()){
                option = sc.nextInt();
                if(!(array.contains(option))){
                    if(auction==1){
                        System.out.println("Please enter a value above the auction value");
                    }else{
                        System.out.println("Please enter an integer in the options");
                    }
                }

            }else{
                sc.nextLine();
            }
        } while (!(array.contains(option)));

        return option;
    }
    public static ArrayList<Integer> makeAList(int lower,int upper){
        ArrayList<Integer> possibleChoice =new ArrayList<Integer>();
        for (int i=lower; i<=upper; i++) {
            possibleChoice.add(i);
        }
        return possibleChoice;
    }

    public void updateHousePrices(int deed, int numberOfHouses, int hotel){
        LinkedHashMap info = (LinkedHashMap) infoProperty.get(deed);
        int rentPrice=(Integer) info.get("rentPrice");
        int numberProp;
        if(hotel>0){
            numberProp=5;
        }else{
            numberProp=numberOfHouses;
        }
        switch(numberProp){
            case 0:
                rentPrice=(Integer) info.get("monopoly");
                break;
            case 1:
                rentPrice=(Integer) info.get("oneHouse");
                break;
            case 2:
                rentPrice=(Integer) info.get("twoHouse");
                break;
            case 3:
                rentPrice=(Integer) info.get("threeHouse");
                break;
            case 4:
                rentPrice=(Integer) info.get("fourHouse");
                break;
            case 5:
                rentPrice=(Integer) info.get("hotel");
                break;
            default:
                System.out.println("error pricing");
        }
        info.put("rentPrice",rentPrice);
        updatePropertyDetails(deed,info);
    }

    public void updateMonopolyPrices(){
        Player p = new Player("temp",-1);
        LinkedHashMap<Integer, ArrayList<Integer>> familyList = p.buildingFamilyList();
        for (Entry<Integer, ArrayList<Integer>> entry: familyList.entrySet()){
            LinkedHashMap<Integer,ArrayList<Integer>> ownerGroup = new LinkedHashMap<Integer,ArrayList<Integer>>();
            LinkedHashMap<Integer,Integer> count = new LinkedHashMap<Integer,Integer>();
            for(int d: entry.getValue()){
                LinkedHashMap info = (LinkedHashMap) infoProperty.get(d);
                int owner = (Integer) info.get("owner");
                if(ownerGroup.containsKey(owner)){
                    ArrayList <Integer> temp = ownerGroup.get(owner);
                    temp.add(d); //Pointer
                    count.put((Integer) ownerGroup.get(owner).size(),owner);
                }else if(owner!=-1){
                    ArrayList <Integer> temp = new ArrayList<Integer>(Arrays.asList(d));
                    ownerGroup.put(owner,temp);
                }
            }
            if(entry.getKey()<9){
                for(int d: entry.getValue()){
                    LinkedHashMap info = (LinkedHashMap) infoProperty.get(d);
                    int landRent = (Integer) info.get("landRent");
                    int rentPrice = (Integer) info.get("rentPrice");
                    int monopolyPrice = (Integer) info.get("monopoly");
                    int owner = (Integer) info.get("owner");
                    boolean monoStatus;
                    if(entry.getKey()==1 || entry.getKey()==8){
                        monoStatus=count.containsKey(2);
                    }else{
                        monoStatus=count.containsKey(3);
                    }
                    if(rentPrice==landRent&&owner!=-1&&monoStatus){
                        info.put("rentPrice",monopolyPrice);
                        updatePropertyDetails(d,info);
                    }
                }
            }if(entry.getKey()==9){
                for (Entry<Integer, ArrayList<Integer>> ownerNumber: ownerGroup.entrySet()){
                    int numProperty = ownerNumber.getValue().size();
                    int rentPrice;
                    switch(numProperty){
                        case 1:
                            rentPrice=25;
                            break;
                        case 2:
                            rentPrice = 50;
                            break;
                        case 3:
                            rentPrice = 100;
                            break;
                        case 4:
                            rentPrice = 200;
                            break;
                        default:
                            rentPrice = 0;
                            break;
                    }
                    for(int pN : ownerNumber.getValue()){
                        LinkedHashMap info = (LinkedHashMap) infoProperty.get(pN);
                        //System.out.print(ownerNumber);
                        int owner = (Integer) info.get("owner");
                        if(owner!=-1){
                            info.put("rentPrice",rentPrice);
                            updatePropertyDetails(pN,info);
                        }
                    }
                }
            }
        }
    }

    public void allSetup(){;
        chanceDeck =shuffle();
        communityDeck =shuffle();
        chanceArray.add(7);
        chanceArray.add(22);
        chanceArray.add(36);
        communityArray.add(2);
        communityArray.add(17);
        communityArray.add(33);
        otherArray.add(0);
        otherArray.add(4);
        otherArray.add(10);
        otherArray.add(20);
        otherArray.add(30);
        otherArray.add(38);

        Chance c1= new Chance(0,"Advance to Go. (Collect $200) ");
        c1.setPosition(0);
        infoChance.put(1,c1.getInfo());

        Chance c2= new Chance(2,"Advance to St. Charles Place. If you pass Go, collect $200.");
        c2.setPosition(16);
        c2.setMoney(200);
        infoChance.put(2,c2.getInfo());

        Chance c3= new Chance(2,"Advance to Illinois Ave. If you pass Go, collect $200");
        c3.setPosition(24);
        c3.setMoney(200);
        infoChance.put(3,c3.getInfo());

        Chance c4= new Chance(4,"Advance token to nearest Utility. If unowned, you may buy it from the Bank. If owned, "
            +"throw dice and pay owner a total 10 times the amount thrown.");
        infoChance.put(4,c4.getInfo());

        Chance c5= new Chance(5,"Advance token to the nearest Railroad and pay owner twice the rental to which"
            +" he/she is otherwise entitled. If Railroad is unowned, you may buy it from the Bank.");
        infoChance.put(5,c5.getInfo());

        Chance c6= new Chance(5,"Advance token to the nearest Railroad and pay owner twice the rental to which "
            +"he/she is otherwise entitled. If Railroad is unowned, you may buy it from the Bank.");
        infoChance.put(6,c6.getInfo());

        Chance c7= new Chance(1,"Bank pays you dividend of $50.");
        c7.setMoney(50);
        infoChance.put(7,c7.getInfo());

        Chance c8= new Chance(7,"Get out of Jail Free. This card may be kept until needed, or traded/sold.");
        infoChance.put(8,c8.getInfo());

        Chance c9= new Chance(8,"Go Back Three Spaces.");
        c9.setPosition(-3);
        infoChance.put(9,c9.getInfo());

        Chance c10= new Chance(0,"Go to Jail. Go directly to Jail. Do not pass GO, do not collect $200.");
        c9.setPosition(10);
        c10.position=10;
        infoChance.put(10,c10.getInfo());

        Chance c11= new Chance(6,"Make general repairs on all your property: For each house pay $25, For each hotel pay $100.");
        infoChance.put(11,c11.getInfo());

        Chance c12= new Chance(1,"Pay poor tax of $15");
        c12.setMoney(-15);
        infoChance.put(12,c12.getInfo());

        Chance c13= new Chance(2,"Take a trip to Reading Railroad.");
        c13.setMoney(200);
        c13.setPosition(5);
        infoChance.put(13,c13.getInfo());

        Chance c14= new Chance(2,"Take a walk on the Boardwalk. Advance token to Boardwalk.");
        c14.setPosition(39);
        infoChance.put(14,c14.getInfo());

        Chance c15= new Chance(3,"You have been elected Chairman of the Board. Pay each player $50.");
        c15.setMoney(-50);
        infoChance.put(15,c15.getInfo());

        Chance c16= new Chance(1,"Your building loan matures. Receive $150.");
        c16.setMoney(150);
        infoChance.put(16,c16.getInfo());

        Community com1= new Community(0,"Advance to Go. (Collect $200) ");
        com1.setMoney(200);
        com1.setPosition(0);
        infoCommunity.put(1,com1.getInfo());

        Community com2= new Community(1,"Bank error in your favor. Collect $200.");
        com2.setMoney(200);
        infoCommunity.put(2,com2.getInfo());

        Community com3= new Community(1,"Doctor's fees. Pay $50.");
        com3.setMoney(-50);
        infoCommunity.put(3,com3.getInfo());

        Community com4= new Community(1,"From sale of stock you get $50.");
        com4.setMoney(50);
        infoCommunity.put(4,com4.getInfo());

        Community com5= new Community(1,"You inherit $100.");
        com5.setMoney(100);
        infoCommunity.put(5,com5.getInfo());

        Community com6= new Community(1,"Holiday Fund matures. Receive $100.");
        com6.setMoney(100);
        infoCommunity.put(6,com6.getInfo());

        Community com7= new Community(1,"Income tax refund. Collect $20");
        com7.setMoney(20);
        infoCommunity.put(7,com7.getInfo());

        Community com8= new Community(7,"Get out of Jail Free. This card may be kept until needed, or traded/sold.");
        infoCommunity.put(8,com8.getInfo());

        Community com9= new Community(3,"It is your birthday. Collect $10 from every player.");
        com9.setMoney(10);
        infoCommunity.put(9,com9.getInfo());

        Community com10= new Community(0,"Go to Jail. Go directly to Jail. Do not pass GO, do not collect $200.");
        com10.setPosition(10);
        infoCommunity.put(10,com10.getInfo());

        Community com11= new Community(6,"You are assessed for street repairs: Pay $40 per house and $115 per hotel you own.");
        com11.setMoney(40);
        infoCommunity.put(11,com11.getInfo());

        Community com12= new Community(1,"Hospital Fees. Pay $50.");
        com12.setMoney(-50);
        infoCommunity.put(12,com12.getInfo());

        Community com13= new Community(1,"School fees. Pay $50.");
        com13.setMoney(-50);
        infoCommunity.put(13,com13.getInfo());

        Community com14= new Community(1,"Receive $25 consultancy fee.");
        com14.setMoney(25);
        infoCommunity.put(14,com14.getInfo());

        Community com15= new Community(1,"Life insurance matures – Collect $100");
        com15.setMoney(100);
        infoCommunity.put(15,com15.getInfo());

        Community com16= new Community(1,"You have won second prize in a beauty contest. Collect $10. ");
        com16.setMoney(10);
        infoCommunity.put(16,com16.getInfo());

        OtherPosition op1= new OtherPosition.PropertyBuilder()
            .with(build ->{
                build.position=0;
                build.type=1;
                build.name="Go, Collect $200";
                build.money=200;
            })
            .createBuilding();
        infoOthers.put(0,op1.getInfo());

        OtherPosition op2= new OtherPosition.PropertyBuilder()
            .with(build ->{
                build.position=4;
                build.type=1;
                build.name="Income Tax. Pay $200";
                build.money=-200;
            })
            .createBuilding();
        infoOthers.put(4,op2.getInfo());

        OtherPosition op3= new OtherPosition.PropertyBuilder()
            .with(build ->{
                build.position=10;
                build.type=0;
                build.name="Just Visiting Jail";
                build.money=0;
            })
            .createBuilding();
        infoOthers.put(10,op3.getInfo());

        OtherPosition op4= new OtherPosition.PropertyBuilder()
            .with(build ->{
                build.position=20;
                build.type=0;
                build.name="Free Parking";
            })
            .createBuilding();
        infoOthers.put(20,op4.getInfo());

        OtherPosition op5= new OtherPosition.PropertyBuilder()
            .with(build ->{
                build.position=10;
                build.type=0;
                build.name="Go to Jail";
            })
            .createBuilding();
        infoOthers.put(30,op5.getInfo());

        OtherPosition op6= new OtherPosition.PropertyBuilder()
            .with(build ->{
                build.position=38;
                build.type=1;
                build.name="Luxury Tax. Pay $75";
                build.money=-75;
            })
            .createBuilding();
        infoOthers.put(38,op6.getInfo());

                IndividualProperty id1 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Mediterranean Avenue";
                build.family=1;
                build.colour="Brown";
                build.price=60;
                build.landRent=2;
                build.monopoly=4;
                build.oneHouse=10;
                build.twoHouse=30;
                build.threeHouse=90;
                build.fourHouse=160;
                build.hotel=250;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=30;
                build.buildCost=50;
                build.position=1;
            }).createBuilding();
        infoProperty.put(1,id1.getInfo());

        IndividualProperty id3 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Baltic Avenue";
                build.family=1;
                build.colour="Brown";
                build.price=60;
                build.landRent=4;
                build.monopoly=8;
                build.oneHouse=20;
                build.twoHouse=60;
                build.threeHouse=180;
                build.fourHouse=320;
                build.hotel=450;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=30;
                build.buildCost=50;
                build.position=3;
            }).createBuilding();
        infoProperty.put(3,id3.getInfo());

        IndividualProperty id6 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Oriental Avenue";
                build.family=2;
                build.colour="Light Blues";
                build.price=100;
                build.landRent=6;
                build.monopoly=12;
                build.oneHouse=30;
                build.twoHouse=90;
                build.threeHouse=270;
                build.fourHouse=400;
                build.hotel=550;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=50;
                build.buildCost=50;
                build.position=6;
            }).createBuilding();
        infoProperty.put(6,id6.getInfo());

        IndividualProperty id8 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Vermont Avenue";
                build.family=2;
                build.colour="Light Blues";
                build.price=100;
                build.landRent=6;
                build.monopoly=12;
                build.oneHouse=30;
                build.twoHouse=90;
                build.threeHouse=270;
                build.fourHouse=400;
                build.hotel=550;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=50;
                build.buildCost=50;
                build.position=8;
            }).createBuilding();
        infoProperty.put(8,id8.getInfo());

        IndividualProperty id9 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Connecticut  Avenue";
                build.family=2;
                build.colour="Light Blues";
                build.price=100;
                build.landRent=8;
                build.monopoly=16;
                build.oneHouse=40;
                build.twoHouse=100;
                build.threeHouse=300;
                build.fourHouse=450;
                build.hotel=600;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=60;
                build.buildCost=50;
                build.position=9;
            }).createBuilding();
        infoProperty.put(9,id9.getInfo());

        IndividualProperty id11 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="St. Charles Place";
                build.family=3;
                build.colour="Pinks";
                build.price=140;
                build.landRent=10;
                build.monopoly=20;
                build.oneHouse=50;
                build.twoHouse=150;
                build.threeHouse=450;
                build.fourHouse=625;
                build.hotel=750;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=70;
                build.buildCost=100;
                build.position=11;
            }).createBuilding();
        infoProperty.put(11,id11.getInfo());

        IndividualProperty id13 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="States Avenue";
                build.family=3;
                build.colour="Pinks";
                build.price=140;
                build.landRent=10;
                build.monopoly=20;
                build.oneHouse=50;
                build.twoHouse=150;
                build.threeHouse=450;
                build.fourHouse=625;
                build.hotel=750;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=70;
                build.buildCost=100;
                build.position=13;
            }).createBuilding();
        infoProperty.put(13,id13.getInfo());

        IndividualProperty id14 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Virginia  Avenue";
                build.family=3;
                build.colour="Pinks";
                build.price=160;
                build.landRent=12;
                build.monopoly=24;
                build.oneHouse=60;
                build.twoHouse=180;
                build.threeHouse=500;
                build.fourHouse=700;
                build.hotel=900;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=80;
                build.buildCost=100;
                build.position=14;
            }).createBuilding();
            infoProperty.put(14,id14.getInfo());

        IndividualProperty id16 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="St. James Place";
                build.family=4;
                build.colour="Oranges";
                build.price=180;
                build.landRent=14;
                build.monopoly=28;
                build.oneHouse=70;
                build.twoHouse=200;
                build.threeHouse=550;
                build.fourHouse=750;
                build.hotel=950;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=90;
                build.buildCost=100;
                build.position=16;
            }).createBuilding();
        infoProperty.put(16,id16.getInfo());

        IndividualProperty id18 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Tennessee Avenue";
                build.family=4;
                build.colour="Oranges";
                build.price=180;
                build.landRent=14;
                build.monopoly=28;
                build.oneHouse=70;
                build.twoHouse=200;
                build.threeHouse=550;
                build.fourHouse=750;
                build.hotel=950;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=90;
                build.buildCost=100;
                build.position=18;
            }).createBuilding();
        infoProperty.put(18,id18.getInfo());

        IndividualProperty id19 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="New York Avenue";
                build.family=4;
                build.colour="Oranges";
                build.price=200;
                build.landRent=16;
                build.monopoly=32;
                build.oneHouse=80;
                build.twoHouse=220;
                build.threeHouse=600;
                build.fourHouse=800;
                build.hotel=1000;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=100;
                build.buildCost=100;
                build.position=19;
            }).createBuilding();
        infoProperty.put(19,id19.getInfo());

        IndividualProperty id21 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Kentucky Avenue";
                build.family=5;
                build.colour="Red";
                build.price=220;
                build.landRent=18;
                build.monopoly=36;
                build.oneHouse=90;
                build.twoHouse=250;
                build.threeHouse=700;
                build.fourHouse=875;
                build.hotel=1050;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=110;
                build.buildCost=150;
                build.position=21;
            }).createBuilding();
        infoProperty.put(21,id21.getInfo());

        IndividualProperty id23 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Indiana Avenue";
                build.family=5;
                build.colour="Red";
                build.price=220;
                build.landRent=18;
                build.monopoly=36;
                build.oneHouse=90;
                build.twoHouse=250;
                build.threeHouse=700;
                build.fourHouse=875;
                build.hotel=1050;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=110;
                build.buildCost=150;
                build.position=23;
            }).createBuilding();
        infoProperty.put(23,id23.getInfo());

        IndividualProperty id24 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Illinois Avenue";
                build.family=5;
                build.colour="Red";
                build.price=240;
                build.landRent=20;
                build.monopoly=40;
                build.oneHouse=100;
                build.twoHouse=300;
                build.threeHouse=750;
                build.fourHouse=925;
                build.hotel=1100;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=120;
                build.buildCost=150;
                build.position=24;
            }).createBuilding();
        infoProperty.put(24,id24.getInfo());

        IndividualProperty id26 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Atlantic Avenue";
                build.family=6;
                build.colour="Yellow";
                build.price=260;
                build.landRent=22;
                build.monopoly=44;
                build.oneHouse=110;
                build.twoHouse=330;
                build.threeHouse=800;
                build.fourHouse=975;
                build.hotel=1150;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=130;
                build.buildCost=150;
                build.position=26;
            }).createBuilding();
        infoProperty.put(26,id26.getInfo());

        IndividualProperty id7 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Ventnor  Avenue";
                build.family=6;
                build.colour="Yellow";
                build.price=260;
                build.landRent=22;
                build.monopoly=44;
                build.oneHouse=110;
                build.twoHouse=330;
                build.threeHouse=800;
                build.fourHouse=975;
                build.hotel=1150;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=130;
                build.buildCost=150;
                build.position=27;
            }).createBuilding();
        infoProperty.put(27,id7.getInfo());

        IndividualProperty id29 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Marvin Gardens";
                build.family=6;
                build.colour="Yellow";
                build.price=280;
                build.landRent=24;
                build.monopoly=48;
                build.oneHouse=120;
                build.twoHouse=360;
                build.threeHouse=850;
                build.fourHouse=1025;
                build.hotel=1200;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=140;
                build.buildCost=150;
                build.position=29;
            }).createBuilding();
        infoProperty.put(29,id29.getInfo());

        IndividualProperty id31 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Pacific  Avenue";
                build.family=7;
                build.colour="Green";
                build.price=300;
                build.landRent=26;
                build.monopoly=52;
                build.oneHouse=130;
                build.twoHouse=390;
                build.threeHouse=900;
                build.fourHouse=1110;
                build.hotel=1275;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=150;
                build.buildCost=200;
                build.position=31;
            }).createBuilding();
        infoProperty.put(31,id31.getInfo());

        IndividualProperty id32 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="North Carolina Avenue";
                build.family=7;
                build.colour="Green";
                build.price=300;
                build.landRent=26;
                build.monopoly=52;
                build.oneHouse=130;
                build.twoHouse=390;
                build.threeHouse=900;
                build.fourHouse=1110;
                build.hotel=1275;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=150;
                build.buildCost=200;
                build.position=32;
            }).createBuilding();
        infoProperty.put(32,id32.getInfo());

        IndividualProperty id34 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Pennsylvania  Avenue";
                build.family=7;
                build.colour="Green";
                build.price=320;
                build.landRent=28;
                build.monopoly=56;
                build.oneHouse=150;
                build.twoHouse=450;
                build.threeHouse=1000;
                build.fourHouse=1200;
                build.hotel=1400;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=160;
                build.buildCost=200;
                build.position=34;
                }).createBuilding();
        infoProperty.put(34,id34.getInfo());

        IndividualProperty id37 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Park Place";
                build.family=8;
                build.colour="Blue";
                build.price=350;
                build.landRent=35;
                build.monopoly=70;
                build.oneHouse=175;
                build.twoHouse=500;
                build.threeHouse=1100;
                build.fourHouse=1300;
                build.hotel=1500;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=175;
                build.buildCost=200;
                build.position=37;
        }).createBuilding();
        infoProperty.put(37,id37.getInfo());

        IndividualProperty id39 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Boardwalk";
                build.family=8;
                build.colour="Blue";
                build.price=400;
                build.landRent=50;
                build.monopoly=100;
                build.oneHouse=200;
                build.twoHouse=600;
                build.threeHouse=1400;
                build.fourHouse=1700;
                build.hotel=2000;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=200;
                build.buildCost=200;
                build.position=39;
            }).createBuilding();
        infoProperty.put(39,id39.getInfo());

        IndividualProperty id5 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Reading Railroad";
                build.family=9;
                build.price=200;
                build.oneHouse=25;
                build.twoHouse=50;
                build.threeHouse=100;
                build.fourHouse=200;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=100;
                build.position=5;
                build.colour="Railway";
            }).createBuilding();
        infoProperty.put(5,id5.getInfo());

        IndividualProperty id15 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Pennsylvania Railroad";
                build.family=9;
                build.price=200;
                build.oneHouse=25;
                build.twoHouse=50;
                build.threeHouse=100;
                build.fourHouse=200;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=100;
                build.position=15;
                build.colour="Railway";
            }).createBuilding();
        infoProperty.put(15,id15.getInfo());

        IndividualProperty id25 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="B. & O. Railroad";
                build.family=9;
                build.price=200;
                build.oneHouse=25;
                build.twoHouse=50;
                build.threeHouse=100;
                build.fourHouse=200;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=100;
                build.position=25;
                build.colour="Railway";
            }).createBuilding();
        infoProperty.put(25,id25.getInfo());

        IndividualProperty id35 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Short Line";
                build.family=9;
                build.price=200;
                build.oneHouse=25;
                build.twoHouse=50;
                build.threeHouse=100;
                build.fourHouse=200;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=100;
                build.position=35;
                build.colour="Railway";
            }).createBuilding();
        infoProperty.put(35,id35.getInfo());

        IndividualProperty id12 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Electric Company";
                build.family=10;
                build.price=150;
                build.oneHouse=4;
                build.twoHouse=10;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=75;
                build.position=12;
                build.colour="Utlity";
            }).createBuilding();
        infoProperty.put(12,id12.getInfo());

        IndividualProperty id28 = new IndividualProperty.PropertyBuilder()
            .with(build->{
                build.name="Water Works";
                build.colour="Utlity";
                build.family=10;
                build.price=150;
                build.oneHouse=4;
                build.twoHouse=10;
                build.status=0;
                build.rentPrice=0;
                build.mortgage=75;
                build.position=28;
            }).createBuilding();
        infoProperty.put(28,id28.getInfo());



    }

}
