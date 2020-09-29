#!/usr/bin/env bash

#compile all java files
javac *.java

#run all problems with forward checking.

#nQueens problems with static variable ordering
java Main 4Queens.csp > results/forwardChecking/static/nQueens/4Queens.txt
java Main 6Queens.csp > results/forwardChecking/static/nQueens/6Queens.txt
java Main 8Queens.csp > results/forwardChecking/static/nQueens/8Queens.txt
java Main 10Queens.csp > results/forwardChecking/static/nQueens/10Queens.txt
java Main 14Queens.csp > results/forwardChecking/static/nQueens/14Queens.txt
java Main 20Queens.csp > results/forwardChecking/static/nQueens/20Queens.txt

#nQueens problems with dynamic smallest domain first ordering
java Main 4Queens.csp -d > results/forwardChecking/dynamic/nQueens/4Queens.txt
java Main 6Queens.csp -d > results/forwardChecking/dynamic/nQueens/6Queens.txt
java Main 8Queens.csp -d > results/forwardChecking/dynamic/nQueens/8Queens.txt
java Main 10Queens.csp -d > results/forwardChecking/dynamic/nQueens/10Queens.txt
java Main 14Queens.csp -d > results/forwardChecking/dynamic/nQueens/14Queens.txt
java Main 20Queens.csp -d > results/forwardChecking/dynamic/nQueens/20Queens.txt


#langfords problems with static variable ordering
java Main langfords2_3.csp > results/forwardChecking/static/langfords/langfords2_3.txt
java Main langfords2_4.csp > results/forwardChecking/static/langfords/langfords2_4.txt
java Main langfords3_9.csp > results/forwardChecking/static/langfords/langfords3_9.txt
java Main langfords3_10.csp > results/forwardChecking/static/langfords/langfords3_10.txt
#langfords problems with dynamic smallest domain first ordering
java Main langfords2_3.csp -d > results/forwardChecking/dynamic/langfords/langfords2_3.txt
java Main langfords2_4.csp -d > results/forwardChecking/dynamic/langfords/langfords2_4.txt
java Main langfords3_9.csp -d > results/forwardChecking/dynamic/langfords/langfords3_9.txt
java Main langfords3_10.csp -d > results/forwardChecking/dynamic/langfords/langfords3_10.txt


#sudoku problems with static variable ordering
java Main FinnishSudoku.csp > results/forwardChecking/static/sudoku/FinnishSudoku.txt
java Main SimonisSudoku.csp > results/forwardChecking/static/sudoku/SimonisSudoku.txt
#sudoku problems with dynamic smallest domain first ordering
java Main FinnishSudoku.csp -d > results/forwardChecking/dynamic/sudoku/FinnishSudoku.txt
java Main SimonisSudoku.csp -d > results/forwardChecking/dynamic/sudoku/SimonisSudoku.txt


#run all problems with mac solver.

#nQueens problems with static variable ordering
java Main 4Queens.csp -m > results/mac3/static/nQueens/4Queens.txt
java Main 6Queens.csp -m > results/mac3/static/nQueens/6Queens.txt
java Main 8Queens.csp -m > results/mac3/static/nQueens/8Queens.txt
java Main 10Queens.csp -m > results/mac3/static/nQueens/10Queens.txt
java Main 14Queens.csp -m > results/mac3/static/nQueens/14Queens.txt
java Main 20Queens.csp -m > results/mac3/static/nQueens/20Queens.txt

#nQueens problems with dynamic smallest domain first ordering
java Main 4Queens.csp -d -m > results/mac3/dynamic/nQueens/4Queens.txt
java Main 6Queens.csp -d -m > results/mac3/dynamic/nQueens/6Queens.txt
java Main 8Queens.csp -d -m > results/mac3/dynamic/nQueens/8Queens.txt
java Main 10Queens.csp -d -m > results/mac3/dynamic/nQueens/10Queens.txt
java Main 14Queens.csp -d -m > results/mac3/dynamic/nQueens/14Queens.txt
java Main 20Queens.csp -d -m > results/mac3/dynamic/nQueens/20Queens.txt



#langfords problems with static variable ordering
java Main langfords2_3.csp -m > results/mac3/static/langfords/langfords2_3.txt
java Main langfords2_4.csp -m > results/mac3/static/langfords/langfords2_4.txt
java Main langfords3_9.csp -m > results/mac3/static/langfords/langfords3_9.txt
java Main langfords3_10.csp -m > results/mac3/static/langfords/langfords3_10.txt
#langfords problems with dynamic smallest domain first ordering
java Main langfords2_3.csp -d -m > results/mac3/dynamic/langfords/langfords2_3.txt
java Main langfords2_4.csp -d -m > results/mac3/dynamic/langfords/langfords2_4.txt
java Main langfords3_9.csp -d -m > results/mac3/dynamic/langfords/langfords3_9.txt
java Main langfords3_10.csp -d -m > results/mac3/dynamic/langfords/langfords3_10.txt


#sudoku problems with static variable ordering
java Main FinnishSudoku.csp -m > results/mac3/static/sudoku/FinnishSudoku.txt
java Main SimonisSudoku.csp -m > results/mac3/static/sudoku/SimonisSudoku.txt
#sudoku problems with dynamic smallest domain first ordering
java Main FinnishSudoku.csp -d -m > results/mac3/dynamic/sudoku/FinnishSudoku.txt
java Main SimonisSudoku.csp -d -m > results/mac3/dynamic/sudoku/SimonisSudoku.txt

