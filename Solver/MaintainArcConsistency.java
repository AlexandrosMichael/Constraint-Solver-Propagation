import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


@SuppressWarnings("Duplicates")
/**
 * Class which implements the MAC3 algorithm
 */
public class MaintainArcConsistency {

    private BinaryCSP csp;
    // current node under consideration
    private Node currentNode;
    private long startTime;
    private long endTime;
    private boolean dynamicOrdering;


    /**
     * Class constructor
     * @param csp the instance representing the binary constraint problem.
     * @param dynamicOrdering flag indicating whether dynamic ordering will be used.
     */
    public MaintainArcConsistency(BinaryCSP csp, boolean dynamicOrdering) {
        this.csp = csp;
        HashMap<Integer, ArrayList<Integer>> variablesMap = initVariablesMap();
        ArrayList<Integer> varList = initVarList();
        this.currentNode = new Node(csp, variablesMap, varList);
        this.dynamicOrdering = dynamicOrdering;
    }


    /**
     * Method which initialises the list containing all unassigned variables.
     * Essentially, the varList of the root node.
     */
    public ArrayList<Integer> initVarList() {
        System.out.println("Initialising variables list...\n");
        ArrayList<Integer> varList = new ArrayList<>();
        int nVars = csp.getNoVariables();
        // vars will be 0, 1, 2 ... (nVars - 1)
        for (int i = 0; i < nVars; i++) {
            varList.add(i);
        }
        return varList;
    }


    /**
     * Method which initialises the data structure which maps a variable to the values in its domain
     */
    public HashMap<Integer, ArrayList<Integer>> initVariablesMap() {
        System.out.println("Initialising variables map...\n");
        HashMap<Integer, ArrayList<Integer>> variablesMap = new HashMap<>();
        int nVars = csp.getNoVariables();
        // vars will be 0, 1, 2 ... (nVars - 1)
        for (int i = 0; i < nVars; i++) {
            // find lower and upper bound of variable's domain
            int lowerBound = csp.getLB(i);
            int upperBound = csp.getUB(i);
            // populate domain of variable
            ArrayList<Integer> domainSet = new ArrayList<>();
            for (int j = lowerBound; j <= upperBound; j++) {
                domainSet.add(j);
            }
            Collections.sort(domainSet);
            variablesMap.put(i, domainSet);
        }
        return variablesMap;
    }


    /**
     * Method called from Main to initiate the algorithm.
     */
    public void solve() {
        System.out.println("Begin solving with MAC solver...\n");
        if (dynamicOrdering) {
            System.out.println("Using dynamic, smallest domain heuristic...\n");
        }
        else {
            System.out.println("Using ascending variable strategy...\n");
        }
        startTime = System.currentTimeMillis();
        mac3(currentNode);
        System.out.println("exit normally");
    }


    /**
     * The main recursive procedure of the mac3 algorithm
     * @param node
     */
    public void mac3(Node node) {
        int var;
        // selecting variable
        if (dynamicOrdering) {
            var = node.selectVarDynamic();
        }
        else {
            var = node.selectVar();
        }
        // select value
        int val = node.selectVal(var);

        // create copy of current node to be used in order to undo pruning if necessary
        Node prevNode = new Node(csp, currentNode.getVariablesMap(), currentNode.getVarList());

        Node leftNode = new Node(csp, prevNode.getVariablesMap(), prevNode.getVarList());

        leftNode.assign(var, val);

        if (leftNode.completeAssignment()) {
            endTime = System.currentTimeMillis();
            System.out.println("Solution found");
            leftNode.printNodeDetails();
            System.out.println("Number of nodes: " + Node.nodeCount);
            System.out.println("Number of arc revisions: " + Node.revisionCount);
            System.out.println("Time elapsed: " + Long.toString(endTime - startTime) + "ms");
            System.exit(0);
        } else if (leftNode.ac3(var)) {
            currentNode = leftNode;
            mac3(leftNode);
        }

        // undo pruning of ac3 above (also unassigns the value)
        currentNode = prevNode;

        // since it didn't work out, re-create next node and delete val from its domain
        Node rightNode = new Node(csp, currentNode.getVariablesMap(), currentNode.getVarList());
        rightNode.deleteValue(var, val);

        // if its domain is not empty
        if (!rightNode.getVariablesMap().get(var).isEmpty()) {
            // if it's arc consistent
            if (rightNode.ac3(var)) {
                currentNode = rightNode;
                mac3(rightNode);
            }
            currentNode = prevNode;
        }
    }

}

































