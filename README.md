# Battleship - A simple battleship game in Scala
<p align="center"><img alt="Battleship" src="https://cdn.pixabay.com/photo/2010/12/06/23/warships-1017_960_720.jpg" width=400></p>
I developed this game during my 5th year at Polytech Montpellier, in CS & Management. It build with **Scala**, **SBT** and uses **ScalaTest** and **assembly** library. I tried as much as possible to maintain a code RT-friendly, with pure functions in order to test it easily. 

## How to? 
### With SBT 
Open a terminal, cd to the project folder and then run the *sbt run* command. You wil be asked to chose between 2 main classes: BattleShipGame and TestAI. 

### .jar file
You cna also generate the .jar file and specify in the MANIFEST.MF file which main class you want to use (see below)

## Main classes
### BattleShipGame
Player a game versus a friend or an AI (low, medium and hard).

### TestAI
Launch multiple games (default: 100) between the 3 AI and get the final result printed or written in a CSV file. 
