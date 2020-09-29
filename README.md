# Constraint Solver Propagation - Constraint Programming

In this practical, I have designed, implemented, and empirically tested a binary constraint solver. The solver employs 2-way branching and it implements the Forward Checking and Maintaining Arc Consistency algorithms. Furthermore, it supports two variable ordering strategies, namely, a static and ascending variable strategy and a dynamic, smaller-domain first strategy.

## Navigate to the source directory
```bash
cd Solver
```

## Compiling the source code

Run the following in the command line:

```bash
javac *.java
```

## Usage
You can run the program with a number of command-line arguments depending on the algorithm and the variable ordering strategy you wish to use. 

```bash
java Main <csp_filename> [-d] [-m]
```
Where:

-d: instructs the solver to use a dynamic, smaller-domain first variable ordering. In its absence, a static ascending variable ordering is used.

-m: instantiates and runs the solver using the Maintaining Arc Consistency algorithm. In its absence, the solver uses the Forward Checking algorithm.

## Collecting empirical evidence
In order to automate the process of collecting empirical evidence, I have provided a script which runs the solver with all available algorithm and variable ordering strategy for every one of the available constraint satisfaction problems. The script creates files containing the textual output of each invocation of the program, including the time taken to find a solution, the number of nodes used in search and the number of arc revisions. These files will be located in one of the subdirectories within the results directory. To run the script, simply enter the following in the command line:

```bash
./empirical.sh
```

## Author
160004864