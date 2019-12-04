---
geometry: margin=1in
---
# PROJECT Design Documentation

## Team Information
* Team name: Incognito - 11b
* Team members
  * Jacquelyn Leung
  * Mallory Bridge
  * Anthony Ferraioli
  * Celeste Gambardella
  * Kelly Vo

## Executive Summary

WebCheckers is a webapp to play a game of checkers with other people.
You have the option to watch games live or play games against others.

### Purpose
> The purpose of the project is to work together in an agile team and create a game of WebCheckers.

### Glossary and Acronyms

| Term | Definition |
|:------:|:------------:|
| VO | Value Object |
| UI | User Interface |
| AI | Artificial Intelligence |
| OS | Operating System |
| HW | Hardware |


## Requirements

This section describes the features of the application.

>Sign In
>* When a player is not signed in they have the option to sign in.
>* If a player chooses to sign in they are redirected to a sign in page
>* The sign in page displays two text boxes one for a username and password
>* When the sign in button is clicked we check to see if a valid username and
> password was entered.
>* If there is an invalid username or password an error message will be displayed.
>* When a valid username and password is entered the user will be redirected back to
> the homepage.
>* A user will be able to see other players logged if they are logged in otherwise they
> will just see the number of players available.
>
>Start a Game
>* A user will be able to select a player to start a game if they are logged in.
>* If the player selected is in a game already then the user will see an error message.
>* If a user selects a player not in a game then that player and user will both be redirected
> back to game view.
>* A player will be only be able to drag and drop a piece on valid spaces.
>* A player will only be able to move there pieces.
>
>Simple Move
>* A user will be able to move along the board when it is their turn.
>* A user can backup their move, and submit their turn.
>* A user can also be kinged when at a king spot and move backwards when kinged.
>
>Jump Move
>* A user will be able to jump over an opponent and capture their opponent piece
>* A user will be able to make a single jump or multiple jumps.
>* A user will be able to backup one jump at a time. 
>
>End Game
>* A user will be able to win, lose, or tie a game.
>* A USer will be able to resign a game be lose when resigned
>* A user is only able to resign a game when it is their turn.
>* A user wins by capturing all opponents pieces or making their opponent no longer able to move
>
>AI Enhancement
 >* A user will be able to win, lose, or tie a game.
 >* A USer will be able to resign a game be lose when resigned
 >* A user is only able to resign a game when it is their turn.
>
>Spectator Enhancement
>* A user will be able to win, lose, or tie a game.
>* A USer will be able to resign a game be lose when resigned
>* A user is only able to resign a game when it is their turn.


### Definition of MVP

> Where developers provide the smallest amount of features to help satisfy new customers and
get feedback for new enhancements that can be implemented in future product development.


### MVP Features

>Sign In
>
>>* A player must be able to sign into WebCheckers.
>Start Game
>
>>* A Player must be able to start a game with an opponent.
>Sign Out
>
>>* A Player must be able to sign out of a game.
>Resignation
>
>>* A player must be able to resign from a game when it is their turn.
>Game Play
>
>>* A player must be able to play a game with an opponent.


### Roadmap of Enhancements

>*Spectator Mode*
>
>>* A player is able to spectate a live game.
>>* A player is not able to be put in a game when spectating.
>>* A player is not able to make any moves while spectating.
>>* A player can see the time the last move was made.
>*AI*
>
>>* A player is able to play a game with an AI.
>>* Multiple players can play an AI game.
>>* The AI is able to jump, move, and be kinged on the board.


## Application Domain


![The WebCheckers Domain Model](domain-model.png)

> The domain model provides a common understanding between a customer and the developers
of the scope and major entities that exist in the system.

> We have created our domain model in hopes to help the customer understand
what we was trying to accomplish as we begin our development process. 
Within our domain model we have included the following elements:
>  ***Domain Model Overview***
>   > * Spectator that watches the WebCheckers game
>   > * WebCheckers game that is played on a Board
>   > * Squares that defines a location on the Board
>   > * Piece(s) that are on a Square
>   > * Piece that represents a player
>   > * Player taking turns using a Piece
>   > * Player playing WebCheckers
>   > * Player getting end of game result


## Architecture and Design

### Summary

The following Tiers/Layers model shows a high-level view of the webapp's architecture.

![The Tiers & Layers of the Architecture](architecture-tiers-and-layers.png)

As a web application, the user interacts with the system using a
browser.  The client-side of the UI is composed of HTML pages with
some minimal CSS for styling the page.  There is also some JavaScript
that has been provided to the team by the architect.

The server-side tiers include the UI Tier that is composed of UI Controllers and Views. Controllers are built using the Spark framework and View are built using the FreeMarker framework.  The Application and Model tiers are built using plain-old Java objects (POJOs).

Details of the components within these tiers are supplied below.


### Overview of User Interface

This section describes the web interface flow; this is how the user views and interacts with the WebCheckers application.

![The WebCheckers Web Interface Statechart](web-interface.png)

> Since a web page can only have one state or page at a given time we 
> have designed a flowchart of where each page should go to after
> a certain amount of time or when the user interacts with the web application.
>
> _No HTTP Connection_
> * Begin a connection when the user goes to the web page.
>
> _Home_
> * The user will see the welcome/home page.
> * The number of players that are already signed in are displayed.
>
> _Sign In_
> * They are given an option to sign in.
> * If they choose to sign in a new page will be rendered with text boxes asking for a username
> and password.
> * If the username or password is invalid the user will see an error message appear on the page.
>
> _Home_
> * If a valid username and password the user will be redirected make to the homepage and be able to see the other players logged into the game.
> * Now the user will be able to select another player which will redirect a new game page.
> 
>_Game_
> 
>* The other player selected will also be redirected to the same game.


### UI Tier
> This level of the application contains everything the user will interact with.
It will interact with the Application and Model tiers. The following UML diagram shows the 
classes used in the UI 

![The WebCheckers UI tier](ui-uml.png)


> A Sequence diagram of the start a game and sign in was created to showcase 
a better understanding of the functionality of how start a game and sign in works.

![The WebCheckers Sign_In SD](Sign-in-SD.png)
>
![The WebCheckers Start_Game SD](Start-Game-SD.png)

>These diagrams breaks down what each class handles or is expecting and how they connect to each other.
You are able to follow the arrows to see how GetGameRoute uses other aspects to get a game and show it to the user.


### Application Tier
> The Application Tier provides support and functionality for the game. 
It is designed to handle the logic of game and act as the middle man between the Model Tier and the UI Tier. The classes currently stored within the Application Tier in the UML diagram below. 
![The WebCheckers Application tier](appl-uml.png)

### Model Tier
> The model tier is the basic structure for the game.
We use a Board View, Checkers Game, Move, Piece, Player, Position, Row, Space and Turn in our model tier.
The classes that are within the Model tier are shown in the UML Diagram below. 
![The WebCheckers_Model tier](model-uml.png)

### Design Improvements
> To improve our design we should adhere to Object-Oriented design principles more. To adhere to the controller principle we should ensure that we do not make multiple unnecessary controllers and try to combine controllers together. For example keeping Signing in and out in the same controller.
To adhere to the polymorphism principle we should use method overloading to have different arguments within the parameters. 
We currently do not adhere to the Liskov Principle and to do this we can add in pre and post conditions that will help improve our adherence as well as having subclasses properly extending super classes will allow our team to have more adherence to the principle.   
To improve our usage of the open/closed principle, we can create more classes as the base for other classes that share common properties. This would help limit the code in each class and lower cohesion. We also currently do not follow pure fabrication. We can use pure fabrication in the future for our design to lower cohesion and clean up the overall readability with our code. Classes can be made to calculate or examine movement options, or just test the possibility of decisions made by a player in the game. They would have no physical representation on the UI, but they would be referenceable in multiple scenarios and aid future class codings. 

## Testing

### Acceptance Testing
> We had acceptance criteria that we used to create acceptance tests in Spring 1 and 2. During Sprint 1, we were unable to finish the acceptance criteria that stated that a user would be able to be automatically directed to start a game. We were able to pass
all other acceptance criteria tests in Sprint 1. In Sprint 2, we were able to pass all acceptance criteria from Sprint 1 as well as new acceptance
criteria from other user stories added to the game. 
![The WebCheckers Acceptance Testing](acceptance-test-plan-1.png)

![The WebCheckers Acceptance Testing](acceptance-test-plan-2.png)

### Unit Testing and Code Coverage
> Each team member created unit tests to test various parts of the program.
Initially each member tested an item from the UI and something from the application or model tier. Afterwards, team members created more tests to try to gain high code coverage.
We aimed for above 95% code coverage in the application model for the lines and methods.
For the model tier we aimed for 95% code coverage and for the UI Tier we aimed for 90% code coverage.
The figures shown below are the Jacoco analysis from the tests. 

![The WebCheckers Unit Testing](overall-code-coverage.png)

![The WebCheckers Unit Testing](code-coverage-appl.png)

![The WebCheckers Unit Testing](model-code-coverage.png)

![The WebCheckers Unit Testing](ui-code-coverage.png)

### Code Metric Analysis
> Code metrics produces a numerical measurement of a characteristic. This allows decisions to be made within the code. 
Our current code metrics is showcased below. Almost all metrics were in the preset range and there were hardly any measurements that
did not meet the target. On the Complexity metrics, there were 7 functions that went over the target. The following were the functions that did not meet the target: 

> * Model.MoveValidator.checkMove
> > * The ev(G) and v(G) were out of bounds
> *  Model.MoveValidator.isInMoves
> >  * The ev(G) was out of bounds
> * Model.Tree.makeTree
> > * the v(G) was out of bounds
> * UI.GetGameRoute.handleNewGame
> > * The ev(G) was out of bounds
> * UI.GetHomeRoute.handle
> > * The ev(G) was out of bounds
> * UI.PostCheckTurn.handle
> > * The ev(G) was out of bounds
> * UI.PostSubmitTurn.processTurn
> > * The ev(G), iv(G), and v(G) were out of bounds

> Looking into these metrics, these functions had high complexity due to their use within the code. To improve the complexity, we should use more helper functions and create different functions
to help lesson the use of the functions that had high complexity. Some functions like handle in the UI cannot be improved due to their
use being needed for the UI and unable to replicate or use other functions.  


![The WebCheckers Code Metrics Analysis](Chidamber-Kemerer%20metrics.png)

![The WebCheckers Code Metrics Analysis](Complexity-Metrics.png)

![The WebCheckers Code Metrics Analysis](Javadoc-Coverage-Metrics.png)

![The WebCheckers Code Metrics Analysis](Lines-of-Code-metrics.png)

![The WebCheckers Code Metrics Analysis](Martin-Package-Metrics.png)
