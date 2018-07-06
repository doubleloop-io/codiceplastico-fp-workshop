# Text-based RPG

## Refactoring TODO
- remove multable var in Postion case class
- remove mutable var world in Game
- remove mutable var executing in Game
- split command parsing from execution
    - separate "program" construction from evaluation
- handle side effect with IO monad

## Features TODO
- every cell can be land or sea
- if the player ends up in the sea, he dies and the game ends
- a land can contains an enemy
- add the command 'fight', in response the player suffers damage for a constant value eg: -10
- produce a random damage value
- a land can contains potion that gives life to the player for a constant value of eg: 20