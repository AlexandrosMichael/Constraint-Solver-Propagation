
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


@SuppressWarnings("Duplicates")
/**
 * Class which implements the forward checking algorithm
 */
public class ForwardChecking {


    private BinaryCSP csp;
    // current node under consideration
    private Node currentNode;
    private long startTime;
    private long endTime;
    // flag indicating whether dynamic variable ordering is used
    private boolean dynamicOrdering;


    /**
     * Class constructor.
     * @param csp the instance representing the binary constraint problem.
     * @param dynamicOrdering flag indicating whether dynamic ordering will be used.
     */
    public ForwardChecking(BinaryCSP csp, boolean dynamicOrdering) {
        this.csp = csp;
        HashMap<Integer, ArrayList<Integer>> variablesMap = initVariablesMap();
        ArrayList<Integer> varList = initVarList();
        this.currentNode = new Node(csp, variablesMap, varList);;
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
        System.out.println("Begin solving with Forward Checking solver...\n");
        if (dynamicOrdering) {
            System.out.println("Using dynamic smallest domain heuristic...\n");
        }
        else {
            System.out.println("Using static ascending variable strategy...\n");
        }
        startTime = System.currentTimeMillis();
        forwardChecking(currentNode);
    }


    /**
     * The main recursive procedure of the forward checking algorithm.
     * @param node
     */
    public void forwardChecking(Node node) {
        if (node.completeAssignment()) {
            endTime = System.currentTimeMillis();
            System.out.println("Solution found");
            node.printNodeDetails();
            System.out.println("Number of nodes: " + Node.nodeCount);
            System.out.println("Number of arc revisions: " + Node.revisionCount);
            System.out.println("Time elapsed: " + Long.toString(endTime - startTime) + "ms");
            System.exit(0);
        }

        int var;
        if (dynamicOrdering) {
            var = node.selectVarDynamic();
        }
        else {
            var = node.selectVar();
        }
        int val = node.selectVal(var);
        branchFCLeft(node, var, val);
        branchFCRight(node, var, val);
    }


    /**
     * The method implementing the branch left operation. A variable is assigned and the arcs incident to it and the
     * future variables are revised.
     * @param node essentially the parent node of the node that will result from the assignment
     * @param var selected var
     * @param val value which will be assigned to var
     */
    public void branchFCLeft(Node node, int var, int val) {
        // used in order to undo pruning if necessary
        Node prevNode = node;
        Node leftNode = new Node(csp, node.getVariablesMap(), node.getVarList());

        leftNode.assign(var, val);

        if (leftNode.reviseFutureArcs(var)) {

            currentNode = leftNode;
            forwardChecking(leftNode);
        }

        currentNode = prevNode;
    }


    /**
     * The method implementing the branch right operation. A value is deletec from a variable's domain and the arcs
     * incident to it and the future variables are revised.
     * @param node essentially the parent node of the node that will result from the assignment.
     * @param var selected var.
     * @param val value which will be deleted from var's domain.
     */
    public void branchFCRight(Node node, int var, int val) {
        Node rightNode = new Node(csp, node.getVariablesMap(), node.getVarList());
        // used in order to undo pruning if necessary
        Node prevNode = node;

        rightNode.deleteValue(var, val);

        if (!rightNode.getVariablesMap().get(var).isEmpty()) {
            if (rightNode.reviseFutureArcs(var)) {
                currentNode = rightNode;
                forwardChecking(rightNode);
            }
        }
        currentNode = prevNode;

    }



}
