# Battleship - A simple battleship game in Scala :ship:
<p align="center"><img alt="Battleship" src="https://cdn.pixabay.com/photo/2010/12/06/23/warships-1017_960_720.jpg" width=400></p>

I developed this game during my 5th year at Polytech Montpellier, in CS & Management. Built with [Scala](https://www.scala-lang.org/) (v2.12.6), [SBT](https://www.scala-sbt.org/) and uses [ScalaTest](http://www.scalatest.org/) and [TypeSafe Config](https://github.com/lightbend/config). I tried as much as possible to maintain a [RT](https://en.wikipedia.org/wiki/Referential_transparency)-friendly code, with pure functions in order to test it easily. 

:warning: The user interface is graphically optimised for Linux (usage of console colors, tested), it should be ok for MacOS (need to be tested) but it doesn't work on Windows.

## How to use it?
### With SBT 
Open a terminal, cd to the project folder and then run the *sbt run* command. You wil be asked to chose between 2 main classes: BattleShipGame and TestAI (see below, **"Main classes"** seciton).

### .jar file
You can also generate the .jar file and specify in the MANIFEST.MF file which main class you want to use.

### Config file
You can specify the number of games to play when you are testing AIs in the *application.conf* file (default: 100).

## Main classes
### BattleShipGame :man: :crossed_swords: :woman: :man: :crossed_swords: :robot:
Player a game versus a friend or an AI (low, medium and hard).

### TestAI :ballot_box_with_check:
Launch multiple games (default: 100) between the 3 AI and get the final result printed or written in a CSV file. For example, I got the following results with 20 000 games played between each AI (low VS medium, low VS hard, medium VS hard): 

| AI Name           | Score | AI Name2        | Score2 |
|-------------------|-------|-----------------|--------|
| AI Level Beginner | 5     | AI Level Medium | 19995  |
| AI Level Beginner | 0     | AI Level Hard   | 20000  |
| AI Level Medium   | 274   | AI Level Hard   | 19726  |

## Author
<img src="https://fr.gravatar.com/userimage/96543241/c19533b15b0c4f4071bb389acb5d4d33.jpg?size=200" width=30 style=""><span><i>Clément ROIG</i></span>
