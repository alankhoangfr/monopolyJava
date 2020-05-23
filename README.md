
# Monopoly game in Java

## Running the Program
In the command line of the source directory\
Run javac *.java -d ../classes\
then in the classes directory  \
java Main


## Overview 

I am currently learning Java and thought it would be fun to create a monopoly game on command line.\
The librarys are very basic and the goal was to familiarise myself with objects, instincts, classes and ect on Java. \
I was hoping to one day create an AI for the game

## Overall Game play

There can be 2 to 6 players. \
The initial amount given to each players is $1500\
There will be 1000 rounds played\
The names and characteristics are based on the Monopoly US version. 

## Difference between the board game

In the game, when provided with a choices, some choices are final as state FINAL.\
This represents the reality of real world. For example, when you have deided to buy a property and do not have enough money to buy, an option of eithering auctioning or commiting to buy by raising fund will be present. This decision is final because before deciding to buy, the liquidation value is state.
Liquidation Value is the possible funds you can raise. Since you have then commited to buy and thus you should up hold that commitment. If the liquidation value is less than the value, then you can raise it through trade but you take the risk in that the trade may fall through.If you do not have the required funds, your only option would be trade or bankruptcy.  

During an auction, in each cycle of bidders, you can only ONCE attempt to raise funds. Note that if you bid a value, you must uphold to it if you win the auction. For example, if your avaliable cash is $150 and you bid $150, then you spent that money before the completition of the auciton, such as trading or unmortgage or building property and you also win the auction, you MUST pay that $150 otherwise you will declare bankruptcy. If you declare bankruptcy, the auction will start again.

The chance and community decks are shuffled once at the beginning of the game. Once a player draw a card and enacts on the commands, it is then placed at the bottom of the deck. If a player draw a Get of out Jail Card, that card is then given to the player. Once the player uses the card, it is then put at the bottom of the respective deck.

## Coding the game






