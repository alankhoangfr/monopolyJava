
import java.util.*;
import java.lang.*;

public class GamePlay{
    private int numberPlayers;
    private int numberRounds;
    private ArrayList <Object> playerList;
    private Object initial;

    GamePlay(int numberPlayers,int numberRounds,ArrayList <Object> playerList, Object initial){
        this.numberPlayers=numberPlayers;
        this.numberRounds=numberRounds;
        this.playerList=playerList;
        this.initial=initial;
    }
    public void loop(){
        Initalise init = (Initalise) initial;
        //System.out.println(init.getInfoCommunity());
        //System.out.println(init.getInfoChance());
        //System.out.println(init.getInfoOthers());
        //System.out.println(init.getChanceDeck()+": chance deck");
        //System.out.println(init.getCommunityDeck()+": community deck");
        int i =1;
        while(true){
            System.out.println("****************");
            System.out.println("Round: " + i);
            System.out.println("****************");
            for(int p=0;p<numberPlayers;p++){
                Player player = (Player) playerList.get(p);
                if(player.getStatus()==1&&validPlayers(playerList)>1){
                    boolean repeat=true;
                    ArrayList<Boolean> diceList = (ArrayList<Boolean>)player.getDiceHistory();
                    while(repeat==true){
                        System.out.println("");
                        if(diceList.size()>0){
                            System.out.println("Roll Again");
                        }else{
                            System.out.println("++++++++++++++++++++++++++++++++");
                            System.out.println("Turn: " +player.getName());
                            System.out.println("++++++++++++++++++++++++++++++++");
                            playerAction(playerList,player.getPlayerId(),0,-1,player.getCash(),init);
                        }
                        System.out.println("");
                        Dice roll = (Dice) rollDecision(playerList,player.getPlayerId(),init);
                        //System.out.println(init.getChanceDeck()+": chance deck");
                        //System.out.println(init.getCommunityDeck()+": community deck");
                        if(player.getJail()==false&&player.getStatus()==1){
                            int newPossiblePosition = player.rollPlusPosition(1);
                            //Information regarding that position
                            LinkedHashMap information = init.infoPosition(newPossiblePosition);
                            //Move to that position
                            player.setPosition(player.newPosition(1,true));
                            System.out.println("player: "+player.getName()
                                +",cash:"+player.getCash()+" ,position: "+player.getPosition()+",jail: "+player.getJail()
                                +",repeatRoll:"+player.getRepeatRoll());
                            if(information.get("cardName")!=null){
                                System.out.println("position: "+player.getPosition()+", card: "+information.get("name")
                                    + ": "+information.get("cardName"));
                            }else{
                                System.out.println("position: "+player.getPosition()+ ", name: "+information.get("name"));
                            }
                            //System.out.println(init.chanceDeck+": chance deck");
                            //System.out.println(init.communityDeck+": community deck");
                            int type = (Integer) information.get("type");
                            firstResponse(playerList,player.getPlayerId(),information,roll.getTotal(),type,init);
                            if(player.getJail()){
                                System.out.println("");
                                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                                System.out.println(player.getName()+" has rolled 3 doubles and now is in Jail!!");
                                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                                System.out.println("");
                            }
                        }else if(player.getStatus()==1){
                            //Update position which is still in Jail
                            player.setPosition(player.newPosition(roll.getTotal(),roll.getDoub()));
                        }
                        if(player.getStatus()==1){
                            playerAction(playerList,player.getPlayerId(),0,-1,player.getCash(),init);
                        }
                        if(player.getRepeatRoll()==false||player.getStatus()==0||validPlayers(playerList)==1){
                            repeat=false;
                            break;
                        }
                    }
                    if(player.getJail()==false){
                        player.restartDice();
                    }else if(player.getJail()==true && player.getDiceHistory().size()==3){
                        player.restartDice();
                    }
                }
            }
            int vPlayers = validPlayers(playerList);
            if(i==numberRounds||(vPlayers==1)){
                if(vPlayers==1){
                    System.out.println("Game over!. There is a new Champion!!!");
                    System.out.println("The Winner is...");
                }
                if(i==numberRounds){
                    System.out.println("Game over!."+numberRounds+" rounds have been played.");
                    System.out.println("Surviving Players");
                }
                for(int pe=0;pe<numberPlayers;pe++){
                    Player player = (Player) playerList.get(pe);
                    if(player.getStatus()==1){
                        player.printGetInfo();
                    }
                }
                System.out.println("Non surviving Players");
                for(int pe=0;pe<numberPlayers;pe++){
                    Player player = (Player) playerList.get(pe);
                    if(player.getStatus()==0){
                        System.out.println(player.getName());
                    }
                }
                break;
            }
            else{
                i++;
            }
        }
    }
    //Checking how many alive players in the game
    public static int validPlayers(ArrayList<Object> playerList){
        int count=0;
        for (int i = 0;i<playerList.size();i++){
            Player temp = (Player) playerList.get(i);
            if(temp.getStatus()==1){
                count+=1;
            }
        }
        return count;
    }

    public static Object rollDecision(ArrayList<Object> playerList,  int playerId, Object initial){
        Initalise init = (Initalise) initial;
        Random rand = new Random();
        Player player = (Player) playerList.get(playerId);
        Dice roll = new Dice();
        if (player.getJail()==true){
            Boolean decision = false;
            while (decision==false&&player.getStatus()==1){
                int choice;
                if(player.getJailFreeCard()>0){
                    System.out.println(player.getName()+" is in Jail!! Type 1 to Roll for Double. Type 2 to pay $50 bond. "
                        +"Type 3 to use the Get out of jail free card. Decision is FINAL!!!!");
                    ArrayList<Integer> possibleChoice = init.makeAList(1,3);
                    choice = init.userType(possibleChoice,0);
                }else{
                    System.out.println(player.getName()+" is in Jail!! Type 1 to Roll for Double. Type 2 to pay $50 bond. "
                        +"Decision is FINAL!!!!");
                    ArrayList<Integer> possibleChoice = init.makeAList(1,2);
                    choice = init.userType(possibleChoice,0);
                }
                if (player.getDiceHistory().size()==0){
                    System.out.println("You have tried "+(player.getDiceHistory().size())+" time.");
                }else{
                    System.out.println("You have tried "+(player.getDiceHistory().size())+" times.");
                }
                switch (choice){
                    case 1:
                        System.out.println(player.getName()+" has chosen to Roll for Double");
                        System.out.println("dice: total: "+roll.getTotal()+", dice 1 Value: "+roll.getA()
                            +", dice 2 Value: "+roll.getB()+", double: "+roll.getDoub());
                        if(roll.getDoub()==true){
                            System.out.println("Congrulations you have rolled a double. You is now free!");
                            System.out.println(player.getName()+ " is out of jail!");
                            System.out.println("");
                        }else{
                            if(player.getDiceHistory().size()==2){
                                System.out.println(player.getName()+" did not roll a double. You have tried 3 times and "
                                    +"failed and now must pay a $50 bond");
                                if(player.getCash()<50){
                                    System.out.println(player.getName()+" does not have enough money. Consider trading, selling "
                                        +" or mortgaging your property or using a get out of jail card if you have one or "
                                        +" declare Bankruptcy");
                                    playerAction(playerList,player.getPlayerId(),50,-1,player.getCash(),init);
                                    //If bankruptcy
                                }else{
                                    System.out.println(player.getName()+" has paid a $50 Bone");
                                    System.out.println(player.getName()+" has $"+(player.getCash()-50));
                                    System.out.println(player.getName() + " is out of jail!");
                                }
                            }else if (player.getDiceHistory().size()==0){
                                System.out.println(player.getName() + " is still in jail!");
                                System.out.println("");
                            }else{
                                System.out.println(player.getName() + " is still in jail!");
                                System.out.println("");
                            }
                        }
                        decision = true;
                        break;
                    case 2:
                        System.out.println(player.getName()+" has chosen to pay the Bond");
                        if(player.getCash()<50){
                            System.out.println(player.getName()+" does not have enough money. Consider trading, selling or "
                                +" mortgaging your property, rolling for double or using a get out of jail card if you have one or declare Bankruptcy");
                            playerAction(playerList,player.getPlayerId(),50,-1,player.getCash(),init);
                        }else{
                            System.out.println(player.getName()+ " has paid a $50 bond");
                            player.payBond();
                            System.out.println(player.getName()+" has $"+(player.getCash()));
                            System.out.println(player.getName() + " is out of jail!");
                            System.out.println("");
                            System.out.println("dice: total: "+roll.getTotal()+", dice 1 Value: "+roll.getA()+", dice 2: "+roll.getB()+", double: "
                                +roll.getDoub());
                            decision=true;
                       }
                       break;
                    case 3:
                        System.out.println(player.getName()+" has used your Get of Jail free Card");
                        System.out.println(player.getName()+" now has "+player.getJailFreeCard()+" Get of Jail Free Card");
                        String card= player.getTypeCard().get(0);
                        if(card=="Chance"){
                            init.addChanceDeck(8);
                        }else{
                            init.addCommunityDeck(8);
                        }
                        player.useCard();
                        System.out.println("Your Get out of Jail free card has been put back to the botom of the "+card+" deck.");
                        System.out.println(player.getName()+ " is out of jail!");
                        System.out.println("");
                        System.out.println("dice: total: "+roll.getTotal()+", dice 1 Value: "+roll.getA()+", dice 2: "+roll.getB()+", double: "+roll.getDoub());
                        decision = true;
                        break;
                    default:
                        break;

                }
            }
        }else{
            System.out.println("dice: total: "+roll.getTotal()+", dice 1 Value: "+roll.getA()+", dice 2: "+roll.getB()+", double: "+roll.getDoub());
        }
        return roll;
    }

    public static void firstResponse (ArrayList<Object> playerList,  int playerId, LinkedHashMap information, int rollTotal,
        int type, Object initial){
        Initalise init = (Initalise) initial;
        Player player = (Player) playerList.get(playerId);
        int prevPosition = player.getPosition();
        //Action when on that position
        //0: Advance to Go or to jail directly
        //1: Receive and pay one off fixed payments
        //3: Receive or pay variable amounts from/to other players
        //4:Advance to utlity and pay variable amount
        //5: Advance to station and pay double amount
        //7: Keep get out of jail free card
        //6:Pay bank based on property
        //8: Go back 3 steps
        //9:Buy/Rent/Auction property
        switch(type){
            case 0:
                player.updateInfo(information,playerList.size(),rollTotal);
                type0(playerList,playerId);
                break;
            case 1:
                int money= (Integer) information.get("money");
                type1(playerList,playerId,money,init,information,rollTotal);
                break;
            case 2:
                player.updateInfo(information,playerList.size(),rollTotal);
                type2(playerList, playerId,init,rollTotal,prevPosition);
                break;
            case 3:
                type3( playerList,playerId,information,init,rollTotal);
                break;
            case 4:
            case 5:
                stationArrival( playerList, player.getPlayerId(),type,init,rollTotal);
                break;
            case 6:
                type6( playerList,playerId,information,init,rollTotal);
                break;
            case 7:
                player.updateInfo(information,playerList.size(),rollTotal);
                type7( playerList,playerId);
                break;
            case 8:
                player.updateInfo(information,playerList.size(),rollTotal);
                type8(playerList, playerId,init,rollTotal,information);
                break;
            case 9:
                player.updateInfo(information,playerList.size(),rollTotal);
                type9(playerList, playerId,init,rollTotal,information);
                break;
            default:
                System.out.println(type);
                System.out.println("error Easy action");
        }

    }

    public static void type0(ArrayList<Object> playerList,  int playerId){
        Player player = (Player) playerList.get(playerId);
        switch(player.getPosition()){
            case 0:
                System.out.println(player.getName()+" has move to GO and collected $200");
                System.out.println(player.getName()+" has $" + player.getCash());
                break;
            case 10:
                System.out.println(player.getName()+" is in Jail");
                break;
            case 20:
                System.out.println(player.getName()+" is in Free parking");
                break;
            default:
                System.out.println("error type0");
        }
    }
    public static void type1(ArrayList<Object> playerList,  int playerId,int money,Object initial,
        LinkedHashMap information,int rollTotal){
        Initalise init = (Initalise) initial;
        Player player = (Player) playerList.get(playerId);
        int prevCash = player.getCash();
        if(money>0){
            player.updateInfo(information,playerList.size(),rollTotal);
            System.out.println(player.getName()+" has received $"+ Math.abs(money)+" from the bank");
        }else{
            if(Math.abs(money)>prevCash){
                playerAction(playerList,player.getPlayerId(),Math.abs(money),-1,prevCash,init);
                if(player.getStatus()==1){
                    player.updateInfo(information,playerList.size(),rollTotal);
                    System.out.println(player.getName()+" has paid $"+ Math.abs(money)+" to the bank");
                }
            }else{
                player.updateInfo(information,playerList.size(),rollTotal);
                System.out.println(player.getName()+" has paid $"+ Math.abs(money)+" to the bank");
            }
        }
        System.out.println(player.getName()+" has $" + player.getCash());
    }
    public static void type2(ArrayList<Object> playerList,  int playerId,Object initial,int rollTotal,int prevPosition){
        Player player = (Player) playerList.get(playerId);
        Initalise init = (Initalise) initial;
        if(prevPosition>player.getPosition()){
            System.out.println(player.getName()+" has pass GO and receives $200");
            System.out.println(player.getName()+" has $" + player.getCash());
        }
        LinkedHashMap newInformation = init.infoPosition(player.getPosition());
        System.out.println("");
        System.out.println(player.getName()+" is walking .............");
        System.out.println(player.getName()+" has arrived at "+newInformation.get("name")+", position: "+player.getPosition());
        type9(playerList, playerId,init,rollTotal,newInformation);
    }
    public static void type3(ArrayList<Object> playerList,  int playerId,LinkedHashMap information,Object initial,int rollTotal){
        Player player = (Player) playerList.get(playerId);
        int amount = (Integer) information.get("money");
        int startNumber = validPlayers(playerList);
        int payment = amount*(startNumber-1);
        int prevCash = player.getCash();
        if(Math.abs(payment)>prevCash){
            if(payment<0){
                playerAction(playerList,player.getPlayerId(),Math.abs(payment),9,player.getCash(),initial);
                player.updateInfo(information,startNumber,rollTotal);
            }
        }else{
            if(payment<0){
                player.updateInfo(information,startNumber,rollTotal);
            }
        }
        if(player.getStatus()==1&&Math.abs(payment)<=player.getCash()){
            for(int newP=0;newP<playerList.size();newP++){
                if(newP!=playerId){
                    Player tempPlayer = (Player) playerList.get(newP);
                    if(tempPlayer.getStatus()==1){
                        if(amount>0){
                            if(amount>tempPlayer.getCash()){
                                playerAction(playerList,tempPlayer.getPlayerId(),Math.abs(amount),player.getPlayerId(),
                                    tempPlayer.getCash(),initial);
                            }
                            if(tempPlayer.getStatus()==1&&tempPlayer.getCash()>=amount){
                                tempPlayer.updateCash(amount*-1);
                                player.updateCash(amount);
                                System.out.println(tempPlayer.getName()+" has paid $"+ Math.abs(amount)+" to "+ player.getName());
                                System.out.println(tempPlayer.getName()+" has $" + tempPlayer.getCash());
                            }

                        }else{
                            System.out.println(tempPlayer.getName()+" has received $"+ Math.abs(amount)+" from "+player.getName());
                        }
                    }
                }
            }
            if(amount*-1>0){
                System.out.println(player.getName()+" has paid money to other players ");
            }else{
                System.out.println(player.getName()+" has received money from other players ");
            }
            System.out.println(player.getName()+" has $" + player.getCash());
        }
    }
    public static void stationArrival(ArrayList<Object> playerList,  int playerId, int type,Object initial, int rollTotal){
        Initalise init = (Initalise) initial;
        Player player = (Player) playerList.get(playerId);
        int newPosition=0;
        if(type==4){
            newPosition = player.closestUtility(player.getPosition());
            if(player.getPosition()>28 && player.getPosition()<40){
                System.out.println(player.getName()+" has passed GO and receives $200 and "+player.getName()+ "'s cash is $"+player.getCash());
            }
        }else if(type==5){
            newPosition = player.closestStation(player.getPosition());
            if(player.getPosition()>35 && player.getPosition()<40){
                System.out.println(player.getName()+" has passed GO and receives $200 and "+player.getName()+ "'s cash is $"+player.getCash());
            }
        }
        player.setPosition(newPosition);
        LinkedHashMap utilityInfo = init.infoPosition(newPosition);
        utilityInfo.remove("type");
        utilityInfo.put("type",type);
        System.out.println("");
        System.out.println(player.getName()+" is walking .............");
        System.out.println(player.getName()+" has arrived at "+utilityInfo.get("name")+", position: "+newPosition);
        LinkedHashMap postInfo=new LinkedHashMap();
        int status =  (Integer) utilityInfo.get("status");
        int owner = (Integer) utilityInfo.get("owner");
        int payment=0;
        int propertyPrice = (Integer) utilityInfo.get("price");
        if(type==4){
            payment = rollTotal*10;
            if (status==0){
                if(propertyPrice>player.getCash()){
                    ArrayList<Integer> possibleChoice1 = new ArrayList<Integer>(Arrays.asList(1,2));
                    System.out.println(player.getName()+" does not have the funds. Type 1 to Auction or Type 2 "
                        +" to raise the funds.  Your decision is FINAL!!!");
                    int userChoice1=init.userType(possibleChoice1,0);
                    if(userChoice1==1){
                        auction(playerList, playerId, utilityInfo, init);
                    }
                    if(player.getStatus()==1&&propertyPrice<=player.getCash()){
                        playerAction(playerList,player.getPlayerId(),propertyPrice,owner,player.getCash(),init);
                        postInfo = (LinkedHashMap)  player.buyRentAuction(utilityInfo,payment,1);
                        init.updatePropertyDetails(player.getPosition(),postInfo);
                        System.out.println(postInfo.get("name")+" was bought for  price $"
                            +postInfo.get("price")+". The new owners is "+player.getName()+". The rent is variable.");
                    }
                }
                else{
                    ArrayList<Integer> possibleChoice = new ArrayList<Integer>(Arrays.asList(1,2));
                    init.printInfoProperty(utilityInfo,playerList);
                    System.out.println(player.getName()+". Type 1 to buy or Type 2 to Auction the property");
                    int userChoice=init.userType(possibleChoice,0);
                    if(userChoice==1){
                        postInfo = (LinkedHashMap)  player.buyRentAuction(utilityInfo,payment,1);
                        init.updatePropertyDetails(player.getPosition(),postInfo);
                        System.out.println(postInfo.get("name")+" was bought for  price $"
                            +postInfo.get("price")+". The new owners is "+player.getName()+". The rent is variable.");
                    }else{
                        System.out.println(player.getName()+" has chosen to start an auction");
                        auction(playerList, playerId, utilityInfo, initial);
                    }
                }
            }else if(status==1){
                if(owner==player.getPlayerId()){
                    System.out.println(player.getName()+" does not have to pay rent because "+player.getName()+" own's the property");
                }else{
                    if(payment>player.getCash()){
                        System.out.println("not enough for rent");
                        playerAction(playerList,player.getPlayerId(),payment,owner,player.getCash(),init);
                    }
                    if(player.getStatus()==1&&player.getCash()>=payment){
                        postInfo = (LinkedHashMap)  player.buyRentAuction(utilityInfo,payment,1);
                    }
                }
            }else{
                System.out.println(player.getName()+" doesn't need to pay rent because the property is mortgaged");
            }
        }else if(type==5){
            if (status==0){
                if(propertyPrice>player.getCash()){
                    ArrayList<Integer> possibleChoice1 = new ArrayList<Integer>(Arrays.asList(1,2));
                    System.out.println(player.getName()+" does not have the funds. Type 1 to Auction or Type 2 "
                        +" to raise the funds.  Your decision is FINAL!!!");
                    int userChoice1=init.userType(possibleChoice1,0);
                    if(userChoice1==1){
                        auction(playerList, playerId, utilityInfo, init);
                    }
                    if(player.getStatus()==1&&propertyPrice<=player.getCash()){
                        playerAction(playerList,player.getPlayerId(),propertyPrice,-1,player.getCash(),init);
                        postInfo = (LinkedHashMap)  player.buyRentAuction(utilityInfo,0,1);
                        init.updatePropertyDetails(player.getPosition(),postInfo);
                        System.out.println(postInfo.get("name")+" was bought for price $"+postInfo.get("price")
                            +". The new owners is "+player.getName()+". The rent is $"+postInfo.get("rentPrice"));
                    }
                }else{
                    ArrayList<Integer> possibleChoice = new ArrayList<Integer>(Arrays.asList(1,2));
                    init.printInfoProperty(utilityInfo,playerList);
                    System.out.println(player.getName()+". Type 1 to buy or Type 2 to Auction the property");
                    int userChoice=init.userType(possibleChoice,0);
                    if(userChoice==1){
                        postInfo = (LinkedHashMap)  player.buyRentAuction(utilityInfo,payment,1);
                        init.updatePropertyDetails(player.getPosition(),postInfo);
                        System.out.println(postInfo.get("name")+" was bought for  price $"
                            +postInfo.get("price")+". The new owners is "+player.getName()+". The rent is variable.");
                    }else{
                        System.out.println(player.getName()+" has chosen to start an auction");
                        auction(playerList, playerId, utilityInfo, initial);
                    }
                }
            }else if(status==1){
                if(owner==player.getPlayerId()){
                    System.out.println(player.getName()+" does not have to pay rent because "+player.getName()+" own's the property");
                }else{
                    payment = (Integer) utilityInfo.get("rentPrice")*2;
                    if(payment>player.getCash()){
                        System.out.println("not enough for rent");
                        playerAction(playerList,player.getPlayerId(),payment,owner,player.getCash(),init);
                    }
                    if(player.getStatus()==1&&player.getCash()>=payment){
                        postInfo = (LinkedHashMap)  player.buyRentAuction(utilityInfo,payment,1);
                    }
                }
            }else{
                System.out.println(player.getName()+" doesn't need to pay rent because the property is mortgaged");
            }
        }
        //Payment for owner of the property
        if(status==1){
            if(owner!=player.getPlayerId()){
                Player temp1 = (Player) playerList.get(owner);
                temp1.updateCash(payment);
                //The rent is normal and not doubled. So it has the same conditins as type 4
                if (type==4||type==0){
                    System.out.println(player.getName()+" will rent "+postInfo.get("name")+" from  "+temp1.getName()
                        +" at rent $"+payment);
                }else if(type==5){
                    System.out.println(player.getName()+" will rent "+postInfo.get("name")+" from  "+temp1.getName()
                        +" at rent double rent of $"+payment/2+ " which is $"+payment);
                }
                System.out.println(player.getName()+"'s cash is $"+player.getCash());
                System.out.println(temp1.getName()+" receives $"+payment +" from "+player.getName()+". "+temp1.getName()
                    +" now has $"+temp1.getCash());
            }
        }else if(status ==2){
            System.out.println("Renting house which is mortgage - name:"+postInfo.get("name")+", owner: "
                +postInfo.get("owner")+", payment: "+postInfo.get("payment"));
        }
    }
    public static void type6(ArrayList<Object> playerList,  int playerId,LinkedHashMap information,Object initial,int rollTotal){
        Player player = (Player) playerList.get(playerId);
        int expense=0;
        int numHouses = player.numberHouses();
        int numHotels = player.numberHotels();
        if((Integer) information.get("money")==40){
            expense=40*numHouses+115*numHotels;
        }else{
            expense= 25*numHouses + 100*numHotels;
        }
        if(expense>player.getCash()){
            playerAction(playerList,player.getPlayerId(),expense,-1,player.getCash(),initial);
        }
        if(player.getStatus()==1&&expense<=player.getCash()){
            player.updateInfo(information,playerList.size(),rollTotal);
            System.out.println(player.getName()+" has "+numHouses + " houses and  "+numHotels+" hotels. It cost $"
                +expense+" to perform the repais" );
            System.out.println(player.getName()+"'s cash is $"+player.getCash());
        }
    }
    public static void type7(ArrayList<Object> playerList,  int playerId){
        Player player = (Player) playerList.get(playerId);
        System.out.println(player.getName()+" has " +player.getJailFreeCard()+" get out of jail free cards");
    }
    public static void type8(ArrayList<Object> playerList,  int playerId,Object initial,int rollTotal,LinkedHashMap information){
        Player player = (Player) playerList.get(playerId);
        Initalise init = (Initalise) initial;
        LinkedHashMap updateInformation = init.infoPosition(player.getPosition());
        if(updateInformation.get("cardName")!=null){
            System.out.println("position: "+player.getPosition()+", card: "+updateInformation.get("name") + ": "
                +updateInformation.get("cardName"));
        }else{
            System.out.println("position: "+player.getPosition()+ ", name: "+updateInformation.get("name"));
        }
        firstResponse(playerList,player.getPlayerId(),updateInformation,rollTotal,(Integer) updateInformation.get("type"),init);
    }
    public static void type9(ArrayList<Object> playerList,  int playerId,Object initial,int rollTotal,LinkedHashMap information){
        Player player = (Player) playerList.get(playerId);
        Initalise init = (Initalise) initial;
        int status= (Integer) information.get("status");
        if(status==0){
            int propertyPrice = (Integer) information.get("price");
            int family = (Integer) information.get("family");
            ArrayList<Integer> possibleChoice = new ArrayList<Integer>(Arrays.asList(1,2));
            init.printInfoProperty(information,playerList);
            System.out.println(player.getName()+". Type 1 to buy or Type 2 to Auction the property");
            int userChoice=init.userType(possibleChoice,0);
            if(userChoice==1){
                if(propertyPrice>player.getCash()){
                    ArrayList<Integer> possibleChoice1 = new ArrayList<Integer>(Arrays.asList(1,2));
                    System.out.println(player.getName()+" does not have the funds. Type 1 to Auction or Type 2 "
                        +" to buy and raise the funds to pay. Your decision is FINAL!!!");
                    int userChoice1=init.userType(possibleChoice1,0);
                    if(userChoice1==1){
                        auction(playerList, playerId, information, initial);
                    }else{
                        playerAction(playerList,player.getPlayerId(),propertyPrice,-1,player.getCash(),init);
                        if(player.getStatus()==0){
                            System.out.println("Since "+player.getName()+" has chosen bankruptcy, it will NOT "
                                +"buy this property and the bank will auction it.");
                            auction(playerList, playerId, information, initial);
                        }else{
                            LinkedHashMap postInfo = (LinkedHashMap)  player.buyRentAuction(information,0,1);
                            init.updatePropertyDetails(player.getPosition(),postInfo);
                            if(family==10){
                                System.out.println(postInfo.get("name")+" was bought for price $"+postInfo.get("price")
                                    +". The new owners are "+player.getName()+". The rent is variable");
                            }else{
                                System.out.println(postInfo.get("name")+" was bought for price $"+postInfo.get("price")
                                    +". The new owners are "+player.getName()+". The rent is $ "+postInfo.get("rentPrice"));
                            }
                        }

                    }
                }else{
                    LinkedHashMap postInfo = (LinkedHashMap)  player.buyRentAuction(information,0,1);
                    init.updatePropertyDetails(player.getPosition(),postInfo);
                    if(family==10){
                        System.out.println(postInfo.get("name")+" was bought for price $"+postInfo.get("price")
                            +". The new owners are "+player.getName()+". The rent is variable");
                    }else{
                        System.out.println(postInfo.get("name")+" was bought for price $"+postInfo.get("price")
                            +". The new owners are "+player.getName()+". The rent is $ "+postInfo.get("rentPrice"));
                    }
                }
            }else{
                System.out.println(player.getName()+" has chosen to start an auction");
                auction(playerList, playerId, information, initial);
            }

        }else if(status ==1){
            if((Integer) information.get("owner")!=player.getPlayerId()){
                int owner = (Integer) information.get("owner");
                int family = (Integer) information.get("family");
                Player temp3 = (Player) playerList.get(owner);
                int rentPrice;
                if(family==10){
                    int numUtlity = Collections.frequency(temp3.getHouseFamily(), 10);
                    switch(numUtlity){
                        case 1:
                            rentPrice = 4*rollTotal;
                            break;
                        case 2:
                            rentPrice = 10*rollTotal;
                            break;
                        default:
                            rentPrice = 0;
                            break;
                    }
                }else{
                    rentPrice = (Integer) information.get("rentPrice");
                }
                if(rentPrice>player.getCash()){
                    System.out.println("not enough for rent");
                    playerAction(playerList,player.getPlayerId(),rentPrice,owner,player.getCash(),init);
                }
                if(player.getStatus()==1&&player.getCash()>=rentPrice){
                    LinkedHashMap postInfo = (LinkedHashMap)  player.buyRentAuction(information,rentPrice,1);
                    temp3.updateCash(rentPrice*1);
                    System.out.println(player.getName()+" will rent "+postInfo.get("name")+" from  "+temp3.getName()
                        +" at rent $"+rentPrice);
                    System.out.println(player.getName()+"'s cash is $"+player.getCash());
                    System.out.println(temp3.getName()+" receives $"+rentPrice +" from "+player.getName()+". "+temp3.getName()
                        +" now has $"+temp3.getCash());
                }

            }else{
                System.out.println(player.getName()+" does not pay rent since he owns the property.");
            }
        }else{
            System.out.println("Renting house which is mortgage - name:"+information.get("name")+", owner: "
                +information.get("owner")+", rentPrice: "+information.get("rentPrice"));
        }
    }

    //winner -- -1: bank or other playerid. If no winner, -1, Collected players: 9.
    public static void playerAction(ArrayList<Object> playerList,  int playerId,int minCash,int winner,
        int prevCash,Object initial){
        Initalise init = (Initalise) initial;
        Player player = (Player) playerList.get(playerId);
        System.out.println("");
        if(player.getStatus()==1){
           if(prevCash>=minCash||minCash==0){
                System.out.println(player.getName()+". Type 0 to Continue or Type 1 to Build Houses and Hotels, Type 2 to Mortgage Property,"
                    +" Type 3 to Sell Property,"+" Type 4 to Trade or Type 9 for player information");
                ArrayList<Integer> possibleChoice = init.makeAList(1,4);
                possibleChoice.add(9);
                possibleChoice.add(0);
                int userChoice  = init.userType(possibleChoice,0);
                if(userChoice==9){
                    player.printGetInfo();
                    playerAction( playerList, playerId, minCash,winner, prevCash, init);
                }else{
                    playersChoice(playerList,playerId,winner,init,userChoice);
                }
           }else{
                System.out.println(player.getName()+" has $"+prevCash+" but needs $"+minCash+". There is a $"+(prevCash-minCash)+
                    " difference.");
                System.out.println(player.getName()+". Type -1 to Declare Bankruptcy or Type 1 to Build Houses and Hotels, "
                    +" Type 2 to Mortgage Property, "
                    +"Type 3 to Sell Property,"+" Type 4 to Trade and Type or Type 9 for player information");
                ArrayList<Integer> possibleChoice = init.makeAList(1,4);
                possibleChoice.add(9);
                possibleChoice.add(-1);
                int userChoice  = init.userType(possibleChoice,0);
                if(userChoice==9){
                    player.printGetInfo();
                    playerAction( playerList,playerId,minCash,winner,prevCash, init);
                }else if(userChoice==-1){
                    ArrayList<Integer> possibleChoice1 = new ArrayList<Integer>(Arrays.asList(1,2));
                    System.out.println(player.getName()+" has chosen Bankruptcy. Once you start the process, it will be final"
                        +" and you will exit the game");
                    System.out.println("The bank will liquidise all assets. After liquidisation, if "+player.getName()
                        +" has enough cash to pay its creditors, then the mortgaged assets and other assets will be auctioned");
                    System.out.println("If it is not enough to pay creditors, then all the mortgage assests and oher assests"
                        +" will be transfered to the creditor");
                    System.out.println("");
                    System.out.println("Running simiulation!!");
                    int total = player.liquidateAll(init,1);
                    System.out.println("");
                    if(total>=minCash){
                        System.out.println("Since the total avaliable funds is greater than the current payment."
                            +   " Bankruptcy may not be the best option!");
                    }else{
                        System.out.println("The total avaliable funds is NOT greater than the current payment."
                            +   " Try trading or declare Bankruptcy!!");
                    }
                    System.out.println("");
                    System.out.println(player.getName()+". Are you sure want to start the process. Type 1 to start or type 2 to find"
                        +   " other options");
                    int userChoice1=init.userType(possibleChoice1,0);
                    if(userChoice1==2){
                        playerAction( playerList,playerId,minCash,winner,player.getCash(), init);
                    }else{
                        bankruptcy(playerList,playerId,winner,minCash, initial);
                    }
                }else{
                    playersChoice(playerList,playerId,winner,init,userChoice);
                }
                if(player.getCash()<=minCash&&player.getStatus()==1){
                    playerAction( playerList,playerId,minCash,winner,player.getCash(), init);
                }

            }

            init.updateMonopolyPrices();
         }
    }

    public static void playersChoice(ArrayList<Object> playerList,  int playerId,int winner, Object initial, int userChoice){
        Player player = (Player) playerList.get(playerId);
        switch (userChoice){
            case 1:
                houseAction(playerList,playerId,initial,1);
                break;
            case 2:
                mortgage(playerList,playerId,initial);
                break;
            case 3:
                houseAction(playerList,playerId,initial,-1);
                break;
            case 4:
                trade(playerList,playerId,initial);
                break;
            case 0:
                break;
            default:
                System.out.println("Error User choice");
        }
    }

    //buildOrSell - build : 1 or sell:-1 or 2: no funds
    public static void houseAction(ArrayList<Object> playerList,  int playerId,Object initial, int buildOrSell){
        Player player = (Player) playerList.get(playerId);
        Initalise init = (Initalise) initial;
        Boolean propertiesAvaliable=false ;
        if(((player.numberHouses()>0||player.numberHotels()>0)&&buildOrSell==-1)|| (player.getMonopoly().size()>0&&buildOrSell==1)){
            propertiesAvaliable=true;
        }
        if(propertiesAvaliable==true){
            System.out.println("");
            System.out.println(player.getName()+ " has $"+player.getCash());
            System.out.println("These are the avaliable properties");
            System.out.println("");
            ArrayList<Integer> buildAvaliable=new ArrayList<Integer>();
            for (Integer key : player.getHouses().keySet()) {
                buildAvaliable.add(key);
                LinkedHashMap tempInfo = init.infoPosition(key);
                int buildCost = (Integer) tempInfo.get("buildCost");
                int sellValue = (Integer) Math.round(buildCost/2);
                int buildingPosition = (Integer) tempInfo.get("position");
                String colour = (String) tempInfo.get("colour");
                int numberHotel=0;
                if(player.getHotels().contains(buildingPosition)){
                    numberHotel=1;
                }
                System.out.println("Position: "+buildingPosition+", Name: "+tempInfo.get("name")+", Property Group Colour: "
                    +colour+", Building Cost: "+buildCost+ ", Selling Value: "+ sellValue+ ", Number of Houses: "
                    + player.getHouses().get(buildingPosition)+", Number of Hotels: "+ numberHotel);
            }
            System.out.println("");
            if(buildOrSell==1){
                System.out.println(player.getName()+" has chosen to build houses and hotel. Type position number of the "
                    +"property that will be built or -1 to Return to menu");

            }else{
                System.out.println(player.getName()+" has chosen to sell houses and hotel. Type position number of the "
                    +"property that will be sold or -1 to Return to menu");
            }
            System.out.println("");
            //System.out.println(player.getHouses());
            buildAvaliable.add(-1);
            int buildChoice  = init.userType(buildAvaliable,0);
            if(buildChoice>0){
                LinkedHashMap buildingInfo = init.infoPosition(buildChoice);
                int buildCost =(Integer) buildingInfo.get("buildCost");
                Boolean rightToBuild;
                if(buildOrSell==1){
                    if(player.getCash()>=buildCost){
                        rightToBuild = player.buildAction(buildingInfo,1);
                    }else{
                        System.out.println(player.getName()+" does not have the fund to build property. Consider mortgaging or trading"
                            +" or selling property to raise funds.");
                        rightToBuild=false;
                        buildOrSell=2;
                    }
                }else{
                    rightToBuild = player.buildAction(buildingInfo,-1);
                }
                LinkedHashMap<Integer,Integer> housePortfolio = (LinkedHashMap<Integer,Integer>) player.getHouses();
                int numberHouses = (Integer) housePortfolio.get(buildChoice);
                if(rightToBuild==true&&buildOrSell==1){
                    int hotel = 0;
                    if(player.getHotels().contains(buildChoice)){
                        hotel=1;
                    }
                    init.updateHousePrices(buildChoice,numberHouses,hotel);
                    System.out.println(player.getName()+" has built a property at "+buildingInfo.get("name")+" at a cost of "
                        +buildCost+". "+player.getName()+" has "+player.getHouses().get(buildChoice)+" houses"
                        + " and "+hotel+" hotel at "+buildingInfo.get("name"));
                }else if(rightToBuild==true&&buildOrSell==-1){
                    int hotel = 0;
                    if(player.getHotels().contains(buildChoice)){
                        hotel=1;
                    }
                    init.updateHousePrices(buildChoice,numberHouses,hotel);
                    System.out.println(player.getName()+" has sold a property at "+buildingInfo.get("name")+" at a value of "
                        +(Integer) Math.round((Integer)buildCost/2)+". "+player.getName()+" has "
                        +player.getHouses().get(buildChoice)+" houses"+ " and "+hotel+" hotel at "+buildingInfo.get("name"));
                }else if(rightToBuild==false&&buildOrSell==1){
                    System.out.println(player.getName()+" was unable to build a property at "+buildingInfo.get("name")
                        +". Please remember to build evenly or you have too many property at "+buildingInfo.get("name"));
                }else if(rightToBuild==false&&buildOrSell==-1){
                    System.out.println(player.getName()+" was unable to sell a property at "+buildingInfo.get("name")
                        +". Please remember the even rule or you have no property at  "+buildingInfo.get("name"));
                }else{
                    playerAction( playerList,   playerId,  0,-1,player.getCash(), init);
                }
            }

        }else{
            if(buildOrSell==1){
                System.out.println(player.getName()+" has no property monopoly to build");
            }else{
                System.out.println(player.getName()+" has no property monopoly to sell");
            }

        }
        playerAction( playerList,   playerId,  0,-1,player.getCash(), init);
    }

    public static void auction(ArrayList<Object> playerList,  int playerId,LinkedHashMap information,Object initial){
        Initalise init = (Initalise) initial;
        int numberOfPlayers = playerList.size();
        ArrayList<Integer> auctionOrder = new ArrayList<Integer>();
        for(int i=0;i<numberOfPlayers;i++){
            int order = i+playerId;
            if(order>=numberOfPlayers){
                order=i+playerId-numberOfPlayers;
            }
            auctionOrder.add(order);
        }
        int auctionValue=0;
        System.out.println("");
        System.out.println("Auctioning ");
        init.printInfoProperty(information,playerList);
        int winnerId;
        if(validPlayers(playerList)==1){
            int tempWinner=-1;
            for(int pe=0;pe<numberOfPlayers;pe++){
                Player finalWinner = (Player) playerList.get(pe);
                if(finalWinner.getStatus()==1){
                    tempWinner=finalWinner.getPlayerId();
                }
            }
            winnerId=tempWinner;
        }else{
            while (auctionOrder.size()>1){
                for(int i=0;i<auctionOrder.size();i++){
                    int id = auctionOrder.get(i);
                    Player temp = (Player) playerList.get(id);
                    if(temp.getStatus()==1){
                        int maxCash = temp.getCash();
                        ArrayList<Integer> possibleChoice = init.makeAList(auctionValue+1,maxCash);
                        possibleChoice.add(-1);
                        System.out.println("Current Auction Value: "+ auctionValue);
                        System.out.println(temp.getName()+ ": Register a price!."+" Current cash: $"+temp.getCash());
                        System.out.println("It must be above the current auction."+" Type -1 to drop out of the auction");
                        int userChoice = init.userType(possibleChoice,1);
                        if(userChoice==-1){
                            System.out.println(temp.getName()+" has dropped out of the auction!!");
                            auctionOrder.remove(new Integer(id));
                            i--;
                            if(auctionOrder.size()==1){
                                break;
                            }
                        }else{
                            auctionValue=userChoice;
                        }
                        System.out.println("");
                    }else{
                        auctionOrder.remove(new Integer(id));
                        i--;
                    }
                }
                System.out.println("..............................");
            }
            winnerId = auctionOrder.get(0);
        }
        int position = (Integer) information.get("position");
        int status = (Integer) information.get("status");
        Player winner = (Player) playerList.get(winnerId);
        String name = (String) information.get("name");
        System.out.println("");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(name+" was auction for $"+auctionValue+". The winner is "+winner.getName());
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("");
        if(status==2){
            ArrayList<Integer> removeMortgageList= new ArrayList<Integer>();
            ArrayList<Integer> MortgageList= new ArrayList<Integer>(Arrays.asList(position));
            LinkedHashMap tradeList = new LinkedHashMap();
            System.out.println("Transfering Money..."+ winner.getName()+" has transfered $"+auctionValue+" to the Bank");
            winner.updateCash(-auctionValue);
            tradeList.put("Mortgage",MortgageList);
            tradeMortgage(playerList, winnerId, init,tradeList, removeMortgageList);

        }else{
            LinkedHashMap postInfo = (LinkedHashMap)  winner.buyRentAuction(information,auctionValue,0);
            System.out.println("Transfering Money..."+ winner.getName()+" has transfered $"+auctionValue+" to the Bank");
            init.updatePropertyDetails(position,postInfo);
            System.out.println(winner.getName()+" is now owners of "+name);
            init.printInfoProperty(postInfo,playerList);
        }

    }
    public static void mortgage(ArrayList<Object> playerList,  int playerId,Object initial){

        Player player = (Player) playerList.get(playerId);
        Initalise init = (Initalise) initial;
        System.out.println("");
        if(player.getMortgagePossible().size()==0){
            System.out.println(player.getName()+" does not have any property to mortgage");
            playerAction( playerList,   playerId,  0,-1,player.getCash(),  init);
        }else{
            System.out.println(player.getName()+ ". Type 1 to mortgage property, type 2 to unmortgage property or "
                +"type -1 to return back to menu");
            System.out.println("");
            ArrayList<Integer> possibleChoice = init.makeAList(1,2);
            possibleChoice.add(-1);
            int choice1= init.userType(possibleChoice,0);
            if (choice1==1){
                ArrayList<Integer> propertiesAvaliable = (ArrayList)player.getMortgagePossible().clone();
                if(propertiesAvaliable.size()>0){
                    System.out.println(player.getName()+" has chosen to mortgage property. Type position number of the "
                        +"property that will be mortgaged or -1 to Return to menu");
                    for(LinkedHashMap i:init.getMortgageInfo(propertiesAvaliable,1)){
                        System.out.println(i);
                    }
                    //System.out.println(init.getMortgageInfo(propertiesAvaliable,1));
                    propertiesAvaliable.add(-1);
                    System.out.println("");
                    int mortgageChoice  = init.userType(propertiesAvaliable,0);
                    if(mortgageChoice>0){
                        LinkedHashMap mortgageInfo = init.infoPosition(mortgageChoice);
                        LinkedHashMap postInfo = (LinkedHashMap)  player.mortgageAction(mortgageInfo);
                        System.out.println(mortgageInfo);
                        System.out.println(player.getName()+" has chosen "+ mortgageInfo.get("name")+" to mortgage and has raised $"
                            +mortgageInfo.get("mortgage"));
                        init.updatePropertyDetails(mortgageChoice,postInfo);
                        LinkedHashMap mortgageInfo1 = init.infoPosition(mortgageChoice);
                        player.printGetInfo();
                    }

                }else{
                    System.out.println(player.getName()+" has no property avaliable to mortgage");
                    playerAction( playerList,   playerId,  0,-1,player.getCash(), init);
                }
                playerAction( playerList,   playerId,  0,-1,player.getCash(), init);
            }else if(choice1==2){
                ArrayList<Integer> mortgageProperty = (ArrayList)player.getMortgage().clone();
                if(mortgageProperty.size()>0){
                    System.out.println(player.getName()+" has chosen to umortgage property. Type position number of the property that "
                        +"will be unmortgaged or type -1 to return to menu");
                    //System.out.println(init.getMortgageInfo(mortgageProperty,2));
                    mortgageProperty.add(-1);
                    int mortgageChoice  = init.userType(mortgageProperty,0);
                    if(mortgageChoice>0){
                        LinkedHashMap mortgageInfo = init.infoPosition(mortgageChoice);
                        int interestPayment =(int) Math.round((Integer) mortgageInfo.get("price")*0.1);
                        int mortgage =(Integer) mortgageInfo.get("mortgage");
                        if(interestPayment+mortgage>player.getCash()){
                            System.out.println("");
                            System.out.println(player.getName()+" does not have enough cash. Consider mortgaging other properties, trade "
                                +"or selling houses/hotels");
                        }else{
                            LinkedHashMap postInfo = (LinkedHashMap)  player.mortgageAction(mortgageInfo);
                            System.out.println("");
                            System.out.println(player.getName()+" has chosen "+ mortgageInfo.get("name")+" to unmortgage and has paided $"
                                +mortgageInfo.get("mortgage") + " plus $"+interestPayment + " interest");
                            init.updatePropertyDetails(mortgageChoice,postInfo);
                            LinkedHashMap mortgageInfo1 = init.infoPosition(mortgageChoice);
                            player.printGetInfo();
                        }
                    }
                }else{
                    System.out.println(player.getName()+" has no property avaliable to unmortgage");
                }
                playerAction( playerList,   playerId,  0,-1,player.getCash(),  init);
            }else if(choice1==-1){
                playerAction( playerList,   playerId,  0,-1,player.getCash(),  init);
            }
        }
    }

    public static void trade(ArrayList<Object> playerList,  int playerId,Object initial){
        Initalise init = (Initalise) initial;
        Player player = (Player) playerList.get(playerId);
        System.out.println("");
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        System.out.println(player.getName()+ " wants to trade. Below are its trading information");
        player.printTradeInfo();
        System.out.println("-----------------------------------------------------------------------");
        ArrayList<Integer> possibleChoice = new ArrayList<Integer> ();
        for(int newP=0;newP<playerList.size();newP++){
            if(newP!=playerId){
                Player tempPlayer = (Player) playerList.get(newP);
                if(tempPlayer.getStatus()==1){
                    int adjustedplayerId = tempPlayer.getPlayerId()+1;
                    tempPlayer.printTradeInfo();
                    System.out.println("-----------------------------------------------------------------------");
                    possibleChoice.add(adjustedplayerId);
                }
            }
        }
        System.out.println("");
        System.out.println("Type the playerId in which you wish the trade would occur or type -1 to return to menu.");
        System.out.println("");

        possibleChoice.add(-1);
        int userChoice = init.userType(possibleChoice,0);
        if(userChoice==-1){
            playerAction( playerList,   playerId,  0,-1,player.getCash(), init);
        }else{
            Player tradePlayer = (Player) playerList.get(userChoice-1);
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(player.getName()+" will propose a trae with "+tradePlayer.getName());
            player.printTradeInfo();
            System.out.println("-----------------------------------------------------------------------");
            tradePlayer.printTradeInfo();
            System.out.println("");
            LinkedHashMap tradeList1 = new LinkedHashMap();
            LinkedHashMap tradeList2 = new LinkedHashMap();
            ArrayList<Integer> emptyList = new ArrayList<>();

            //Player
            tradeList1.put("id",player.getPlayerId());
            tradeList1.put("Cash",0);
            ArrayList<String> typeCard1 = new ArrayList<String>();
            tradeList1.put("jailFreeCard",typeCard1);
            tradeList1.put("Deeds",emptyList);
            tradeList1.put("Mortgage",emptyList);

            if(player.getMortgagePossible().size()>0){
                tradeList1.put("id",player.getPlayerId());
                System.out.println(player.getName()+". Type the deed you wish to trade or type -1 to continue ");
                System.out.println("");
                for(int d: player.getMortgagePossible()){
                    LinkedHashMap infoDeed = (LinkedHashMap)init.getInfoProperty().get(d);
                    int family = (Integer) infoDeed.get("family");
                    String familyName = init.familyNames(family);
                    System.out.println("Position: "+d+", Name: "+infoDeed.get("name")+", Building Group: "+familyName);
                }
                ArrayList<Integer> deedList=new ArrayList<Integer>();
                ArrayList<Integer> tempMortgagePossible = (ArrayList<Integer>)player.getMortgagePossible().clone();
                tempMortgagePossible.add(-1);
                while(tempMortgagePossible.size()>1){
                    int userChoice1 = init.userType(tempMortgagePossible,0);
                    if(userChoice1==-1){
                        break;
                    }else{
                        tempMortgagePossible.remove(new Integer(userChoice1));
                        deedList.add(userChoice1);
                    }
                }

                tradeList1.put("Deeds",deedList);
            }
            if(player.getMortgage().size()>0){
                System.out.println("");
                System.out.println(player.getName()+". Type the mortgage you wish to trade or type -1 to continue ");
                System.out.println("");
                for(int d: player.getMortgage()){
                    LinkedHashMap infoDeed = (LinkedHashMap)init.getInfoProperty().get(d);
                    int family = (Integer) infoDeed.get("family");
                    String familyName = init.familyNames(family);
                    System.out.println("Position: "+d+", Name: "+infoDeed.get("name")+", Building Group: "+familyName);
                }
                ArrayList<Integer> mortgageList=new ArrayList<Integer>();
                ArrayList<Integer> tempMortgage = (ArrayList<Integer>) player.getMortgage().clone();
                tempMortgage.add(-1);
                while(tempMortgage.size()>1){
                    int userChoice2 = init.userType(tempMortgage,0);
                    if(userChoice2==-1){
                        break;
                    }else{
                        tempMortgage.remove(new Integer(userChoice2));
                        mortgageList.add(userChoice2);
                    }
                }
                tradeList1.put("Mortgage",mortgageList);
            }if(player.getJailFreeCard()>0){
                ArrayList<String> tempTypeCard = (ArrayList<String>) player.getTypeCard();
                System.out.println("");
                System.out.println(player.getName()+". Type the number of Get out of Jail Card you wish to receive from "
                    +player.getName() );
                System.out.println("");
                int numberJailFreeCard = (int) player.getJailFreeCard();
                ArrayList<Integer> possibleChoice2 = init.makeAList(0,numberJailFreeCard);
                int userChoice1 = init.userType(possibleChoice2,0);
                for(int i=0; i<userChoice1;i++){
                    String card = tempTypeCard.get(i);
                    typeCard1.add(card);
                }
                tradeList1.put("jailFreeCard",typeCard1);
            }if(player.getCash()>0){
                System.out.println(player.getName()+". Type the amount of cash you wish to exchange. "
                    +". "+player.getName()+" has available cash $"+player.getCash());
                System.out.println("");
                ArrayList<Integer> possibleChoice3 = init.makeAList(0,player.getCash());
                int userChoice3 = init.userType(possibleChoice3,0);
                tradeList1.put("Cash",userChoice3);
            }

            //Trader
            tradeList2.put("id",tradePlayer.getPlayerId());
            tradeList2.put("Cash",0);
            ArrayList<String> typeCard2 = new ArrayList<String>();
            tradeList2.put("jailFreeCard",typeCard2);
            tradeList2.put("Deeds",emptyList);
            tradeList2.put("Mortgage",emptyList);

            if(tradePlayer.getMortgagePossible().size()>0){
                System.out.println("");
                System.out.println(player.getName()+". Type the deed you wish to receive from "+tradePlayer.getName()
                    +" or type -1 to continue ");
                System.out.println("");
                for(int d: tradePlayer.getMortgagePossible()){
                    LinkedHashMap infoDeed = (LinkedHashMap)init.getInfoProperty().get(d);
                    int family = (Integer) infoDeed.get("family");
                    String familyName = init.familyNames(family);
                    System.out.println("Position: "+d+", Name: "+infoDeed.get("name")+", Building Group: "+familyName);
                }
                ArrayList<Integer> deedList=new ArrayList<Integer>();
                ArrayList<Integer> tempMortgagePossible = (ArrayList<Integer>)tradePlayer.getMortgagePossible().clone();
                tempMortgagePossible.add(-1);
                while(tempMortgagePossible.size()>1){
                    int userChoice1 = init.userType(tempMortgagePossible,0);
                    if(userChoice1==-1){
                        break;
                    }else{
                        tempMortgagePossible.remove(new Integer(userChoice1));
                        deedList.add(userChoice1);
                    }
                }

                tradeList2.put("Deeds",deedList);
            }
            if(tradePlayer.getMortgage().size()>0){
                System.out.println("");
                System.out.println(player.getName()+". Type the mortgage you wish to receive from "+tradePlayer.getName()+
                    "or type -1 to continue ");
                System.out.println("");
                for(int d: tradePlayer.getMortgage()){
                    LinkedHashMap infoDeed = (LinkedHashMap)init.getInfoProperty().get(d);
                    int family = (Integer) infoDeed.get("family");
                    String familyName = init.familyNames(family);
                    System.out.println("Position: "+d+", Name: "+infoDeed.get("name")+", Building Group: "+familyName);
                }
                ArrayList<Integer> mortgageList=new ArrayList<Integer>();
                ArrayList<Integer> tempMortgage = (ArrayList<Integer>) tradePlayer.getMortgage().clone();
                tempMortgage.add(-1);
                while(tempMortgage.size()>1){
                    int userChoice2 = init.userType(tempMortgage,0);
                    if(userChoice2==-1){
                        break;
                    }else{
                        tempMortgage.remove(new Integer(userChoice2));
                        mortgageList.add(userChoice2);
                    }
                }
                tradeList2.put("Mortgage",mortgageList);
            }
            if(tradePlayer.getTypeCard().size()>0){
                ArrayList<String> tempTypeCard = (ArrayList<String>) tradePlayer.getTypeCard();
                System.out.println("");
                System.out.println(player.getName()+". Type the number of Get out of Jail Card you wish to receive from "
                    +tradePlayer.getName() );
                System.out.println("");
                int numberJailFreeCard = (int) tradePlayer.getJailFreeCard();
                ArrayList<Integer> possibleChoice2 = init.makeAList(0,numberJailFreeCard);
                int userChoice2 = init.userType(possibleChoice2,0);
                for(int i=0; i<userChoice2;i++){
                    String card = tempTypeCard.get(i);
                    typeCard2.add(card);
                }
                tradeList2.put("jailFreeCard",typeCard2);
            }if(tradePlayer.getCash()>0){
                System.out.println(player.getName()+". Type the cash amount you wish to receive from "+tradePlayer.getName()
                    +". "+tradePlayer.getName() +" has avaliable cash: $"+tradePlayer.getCash());
                System.out.println("");
                ArrayList<Integer> possibleChoice3 = init.makeAList(0,tradePlayer.getCash());
                int userChoice3 = init.userType(possibleChoice3,0);
                tradeList2.put("Cash",userChoice3);
            }
            System.out.println("");
            System.out.println("Trading: "+tradeList1);
            System.out.println("Trading: "+tradeList2);
            System.out.println("");
            System.out.println(player.getName()+". Do you accept the terms? Type 1 for Yes "
                    +" or type 0 for No ");
            System.out.println("");
            ArrayList<Integer> tradeOptions = init.makeAList(0,1);
            int trade1Choice = init.userType(tradeOptions,0);
            if(trade1Choice==1){
                System.out.println(tradePlayer.getName()+". Do you accept the terms? Type 1 for Yes "
                        +" or type 0 for No ");
                int trade2Choice = init.userType(tradeOptions,0);
                if(trade2Choice==1){
                    System.out.println("Trade between "+player.getName()+ " and "+tradePlayer.getName()
                        +" has been successful!");
                    tradeTransaction(playerList,init,tradeList1,tradeList2);
                }else{
                   System.out.println("Trade has been declined by "+tradePlayer.getName());
                   playerAction( playerList,   playerId,  0,-1,player.getCash(), init);
                }

            }else{
                System.out.println("Trade has been declined by "+player.getName());
                playerAction( playerList,   playerId,  0,-1,player.getCash(), init);
            }

        }

    }

    public static void tradeTransaction (ArrayList<Object> playerList, Object initial,
        LinkedHashMap tradeList1, LinkedHashMap tradeList2){
        Initalise init = (Initalise) initial;
        int playerId = (Integer) tradeList1.get("id");
        int tradePlayerId = (Integer) tradeList2.get("id");
        Player player = (Player) playerList.get(playerId);
        Player tradePlayer = (Player) playerList.get(tradePlayerId);
        //Mortgage Free + Cash + JailFreeCard
        System.out.println("");
        System.out.println("Transacting Deeds, JailFreeCard and Cash now....");
        System.out.println("");
        player.tradeCashDeedsJail(tradeList1,tradeList2);
        tradePlayer.tradeCashDeedsJail(tradeList2,tradeList1);
        for(int deedNumber: (ArrayList<Integer>) tradeList1.get("Deeds")){
            LinkedHashMap buildingInfo = init.infoPosition(deedNumber);
            buildingInfo.put("owner",tradePlayerId);
            init.updatePropertyDetails(deedNumber,buildingInfo);
        }
        for (int deedNumber: (ArrayList<Integer>) tradeList2.get("Deeds")){
            LinkedHashMap buildingInfo = init.infoPosition(deedNumber);
            buildingInfo.put("owner",playerId);
            init.updatePropertyDetails(deedNumber,buildingInfo);
        }
        System.out.println("Cash, Deeds and Get of jail free card Transaction Completed!");
        //Mortgage
        System.out.println("");
        System.out.println("Transacting Mortgages now....");
        System.out.println("");
        ArrayList<Integer> removeMortgageList1 = (ArrayList<Integer>) tradeList1.get("Mortgage") ;
        ArrayList<Integer> removeMortgageList2 = (ArrayList<Integer>) tradeList2.get("Mortgage") ;
        tradeMortgage(playerList,playerId,init,tradeList2,removeMortgageList1);
        tradeMortgage(playerList,tradePlayerId,init,tradeList1,removeMortgageList2);
        System.out.println("Mortgage Transaction Completed!");


        player.printGetInfo();
        tradePlayer.printGetInfo();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        playerAction( playerList,   playerId,  0,-1,player.getCash(), init);

    }

    public static void tradeMortgage(ArrayList<Object> playerList,int playerId, Object initial,
        LinkedHashMap tradeList, ArrayList<Integer> removeMortgageList){
        Initalise init = (Initalise) initial;
        Player player = (Player) playerList.get(playerId);
        ArrayList<Integer> mortgageList = (ArrayList<Integer>)tradeList.get("Mortgage");
        if(mortgageList.size()>0&&player.getStatus()==1&&validPlayers(playerList)>1){
            for(int deed: (ArrayList<Integer>) tradeList.get("Mortgage")){
                ArrayList<Integer> possibleChoice=new ArrayList<Integer>(Arrays.asList(1,2));
                System.out.println(player.getName()+". Type 1 to pay off Mortgage now or Type 2 to pay 10% valuation now"
                    +" and pay off mortgage at a later date. Your Decision is FINAL!!!");
                System.out.println(player.getName()+" has $"+player.getCash());
                int userChoice = init.userType(possibleChoice,0);
                LinkedHashMap buildingInfo = init.infoPosition(deed);
                int interest = (int) Math.round((Integer) buildingInfo.get("price")*0.10);
                int mortgage = (Integer) buildingInfo.get("mortgage");
                if(userChoice==1){
                    if(mortgage+interest>player.getCash()){
                        System.out.println(player.getName()+ " does not have enough cash. Consider"+
                            " paying 10% valuation now or raise funding");
                        playerAction( playerList,   playerId,  mortgage+interest,-1,player.getCash(), init);

                    }
                    if(player.getStatus()==0){
                        System.out.println("");
                        System.out.println("The property will go back on Auction!");
                        System.out.println("");
                        auction(playerList, playerId, buildingInfo, init);
                    }
                    else{
                        LinkedHashMap propertyInfo = player.mortgageAction(buildingInfo);
                        init.updatePropertyDetails(deed,propertyInfo);
                        for(int removeMortgage: removeMortgageList){
                            player.changeMortgage(removeMortgage,-1);
                        }
                        System.out.println(player.getName()+" has chosen "+ buildingInfo.get("name")+" to unmortgage"
                            +" and has paided $"+mortgage + " plus $"+interest + " interest");
                    }
                }else if(userChoice==2){
                    if(interest>player.getCash()){
                        System.out.println(player.getName()+ " does not have enough cash. Consider raising funds");
                        playerAction( playerList,   playerId,  interest,-1,player.getCash(), init);
                        //tradeMortgage(playerList,playerId,init,tradeList,removeMortgageList);
                    }
                    if(player.getStatus()==0){
                        System.out.println("");
                        System.out.println("The property will go back on Auction!");
                        System.out.println("");
                        auction(playerList, playerId, buildingInfo, init);
                    }
                    else{
                        buildingInfo.put("owner",playerId);
                        init.updatePropertyDetails(deed,buildingInfo);
                        player.updateCash(-interest);
                         player.changeMortgage(deed,-1);
                        System.out.println(player.getName()+" has chosen not to unmortgage "+ buildingInfo.get("name")
                            +"."+player.getName()+" has paided the transaction fee which is 10% valuation -$"+interest);
                        for(int removeMortgage: removeMortgageList){
                            player.changeMortgage(removeMortgage,-1);
                        }
                    }
                }
            }
        }

    }

    public static void bankruptcy(ArrayList<Object> playerList,int victim,int winner, int minCash,Object initial){
        Initalise init = (Initalise) initial;
        Player vict = (Player) playerList.get(victim);
        System.out.println("");
        System.out.println("Liquidatng all assets...");
        //Get of jail card
        ArrayList<String> cards= vict.getTypeCard();
        for(String card: cards){
            if(card=="Community"){
                System.out.println("Put back Get of jail card into Community Deck");
                init.addCommunityDeck(8);
            }else{
                init.addChanceDeck(8);
                System.out.println("Put back Get of jail card into ChanceDeck");
            }
        }
        vict.setJailFreeCard(0);
        init.getChanceDeck();
        init.getCommunityDeck();
        int victCash = vict.liquidateAll(init,0);
        //Enough to pay creditor
        if(victCash>=minCash){
            System.out.println("Liqudated all assets. Able to raise $"+victCash);
            System.out.println("");
            if(winner>=0&&winner<9){
                //Exact Player
                Player win = (Player) playerList.get(winner);
                win.updateCash(minCash);
                vict.setStatus(0);
                System.out.println(vict.getName()+" was able to pay off ($"+minCash+") its creditor "+win.getName());
            }else if(winner==9){
                //Many people
                vict.setStatus(0);
                int numOfPlayers= validPlayers(playerList);
                int split = (int) Math.round(minCash/numOfPlayers);
                for(int i=0; i<playerList.size();i++){
                    Player win = (Player) playerList.get(i);
                    if(win.getStatus()==1){
                        win.updateCash(victCash);
                        System.out.println(win.getInfo(win.getCash()));
                    }
                }
                System.out.println(vict.getName()+" was able to pay off ($"+minCash+") to all creditors ");
            }else{
                System.out.println("The bank has received all of "+ vict.getName()+"'s cash.");
                vict.setStatus(0);
            }
            //if there is remaining money - taken away - given to bank
            vict.updateCash(-vict.getCash());
            //then auction all mortgage property
            auctionAll(playerList,victim,initial);

        }else{
            //Not enough for creditor
            System.out.println("Liqudated all assets. Able to raise $"+victCash);
            if(winner>=0&&winner<9){
                //Pay exact player-> winner gets all mortgage property
                Player win = (Player) playerList.get(winner);
                win.updateCash(victCash);
                System.out.println(vict.getName()+" was unable to pay off ($"+minCash+") its creditor "+win.getName());
                System.out.println(win.getName()+" will receive all of "+vict.getName()+"'s mortgages");
                System.out.println("Transfering all mortgages...");
                ArrayList<Integer> removeMortgageList =new ArrayList<Integer>() ;
                LinkedHashMap tradeList = new LinkedHashMap();
                ArrayList<Integer> MortgageList= (ArrayList<Integer>) vict.getMortgage();
                tradeList.put("Mortgage",MortgageList);
                tradeMortgage(playerList, winner, init, tradeList, removeMortgageList);
                System.out.println("Mortgage transfer complete.");
                vict.setStatus(0);

            }else if(winner==9){
                //split each player with what the victim has
                //Then auction the victims mortgages
                System.out.println(vict.getName()+" was unable to pay off ($"+minCash+") all creditor");
                vict.setStatus(0);
                int numOfPlayers= validPlayers(playerList);
                int split = (int) Math.round(victCash/numOfPlayers);
                System.out.println("");
                System.out.println(vict.getName()+" was only able to pay off ($"+split+") to all creditors ");
                if(victCash%2!=0){
                    System.out.println("Since the actual split is a float, the bank will keep the remaining dollar");
                }
                for(int i=0; i<playerList.size();i++){
                    Player win = (Player) playerList.get(i);
                    if(win.getStatus()==1){
                        win.updateCash(split);
                        System.out.println(win.getName()+" has $"+win.getCash());
                    }
                }
                auctionAll(playerList,victim,initial);
            }else{
                //Bank will take all the money
                vict.updateCash(-vict.getCash());
                vict.setStatus(0);
                auctionAll(playerList,victim,initial);
            }
        }
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        System.out.println(vict.getName()+" has left the game.");
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        System.out.println("");

    }

    public static void auctionAll (ArrayList<Object> playerList,int victim,Object initial){
        Initalise init = (Initalise) initial;
        Player vict = (Player) playerList.get(victim);
        int numPlayers= validPlayers(playerList);
        System.out.println("The bank will now auction all its mortgages");
        for(int mortgageDeeds:vict.getMortgage()){
            LinkedHashMap information = init.infoPosition(mortgageDeeds);
            information.put("status",2);
            information.put("rentPrice",0);
            init.updatePropertyDetails(mortgageDeeds,information);
            auction(playerList, victim, information, init);
        }
        vict.clearEverything();

    }
}
