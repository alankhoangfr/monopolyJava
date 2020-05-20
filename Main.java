import java.util.*;
import java.lang.*;

public class Main{

    public static void main (String[] args){
        //Parameters
        Initalise init = new Initalise ();
        init.allSetup();
        System.out.println("Welcome to Monopoly!");
        System.out.println("How many players do you want in the game? Type the number. It must be between 2 and 6"
            +" or type -1 to Quit");
        ArrayList<Integer> possibleChoice = init.makeAList(2,6);
        possibleChoice.add(-1);
        int numberPlayers= init.userType(possibleChoice,0);
        if(numberPlayers<2){
            System.out.println("Game Over");
        }else{
            ArrayList <Object> playerList = new ArrayList<Object>();
            ArrayList<String> userNames = new ArrayList<String>();
            Scanner scan = new Scanner(System.in);
            for(int i=0;i<numberPlayers;i++){
                int playerView= i+1;
                System.out.println("Please enter a name for player "+playerView);
                String name = scan.nextLine();
                if(userNames.contains(name.toUpperCase())){
                    System.out.println("Please enter another name as that name has already been registered");
                    i--;
                }else if(name.length()==0){
                    System.out.println("Please type at least one character");
                    i--;
                }else{
                    Player player = new Player(name,i);
                    userNames.add(name.toUpperCase());
                    playerList.add(player);
                }

            }
            int numberRounds = 100;
            //Looping the rounds
            System.out.println("Let the Games Begin!!");
            GamePlay g = new GamePlay(numberPlayers,numberRounds,playerList,init);
            g.loop();
        }

    }


}
