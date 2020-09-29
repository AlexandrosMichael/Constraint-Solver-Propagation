import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

@SuppressWarnings("Duplicates")
/**
 * Class implementing the node data structure
 */
public class Node {


    public static int nodeCount = 0;
    public static int revisionCount = 0;
    public static long revisionTime = 0;
    private BinaryCSP csp;
    // maps the variable to its domain
    private HashMap<Integer, ArrayList<Integer>> variablesMap;
    // holds unassigned variables
    private ArrayList<Integer> varList;


    /**
     * Class constructor
     *
     * @param csp          instance of the binary CSP
     * @param variablesMap initially it will be the same as the parent node's map.
     * @param varList      initially it will be the same as the parent node's list.
     */
    public Node(BinaryCSP csp, HashMap<Integer, ArrayList<Integer>> variablesMap, ArrayList<Integer> varList) {
        this.csp = csp;

        HashMap<Integer, ArrayList<Integer>> varsMap = new HashMap<>();
        for (int key : variablesMap.keySet()) {
            ArrayList<Integer> vals = (ArrayList<Integer>) variablesMap.get(key).clone();
            varsMap.put(key, vals);
        }
        this.variablesMap = varsMap;
        this.varList = (ArrayList<Integer>) varList.clone();

        nodeCount++;

    }


    /**
     * Checks whether complete assignment has been reached
     *
     * @return
     */
    public boolean completeAssignment() {
        if (varList.isEmpty()) {
            return true;
        }
        return false;
    }


    /**
     * Static ordering
     *
     * @return variable selected
     */
    public int selectVar() {
        if (!varList.isEmpty()) {
            Collections.sort(varList);
            return varList.get(0);
        }
        return -1;
    }


    /**
     * Dynamic smallest-domain first variable ordering. Iterates the unassigned variables and returns the one with the
     * smallest domain
     *
     * @return variable selected
     */
    public int selectVarDynamic() {
        if (!varList.isEmpty()) {
            Collections.sort(varList);
            int smallestDomainVar = varList.get(0);
            for (int key : varList) {
                ArrayList<Integer> domain = variablesMap.get(key);
                int domainSize = domain.size();
                if ((domainSize < variablesMap.get(smallestDomainVar).size()) && (domain.size() > 0)) {
                    smallestDomainVar = key;
                }
            }
            return smallestDomainVar;
        }
        return -1;
    }


    /**
     * Select a value from a variable's domain
     *
     * @param var from whose domain a value will be selected
     * @return the value that will be assigned to var
     */
    public int selectVal(int var) {
        ArrayList<Integer> domain = variablesMap.get(var);
        Collections.sort(domain);
        return variablesMap.get(var).get(0);
    }


    /**
     * Method which assigns a value to a variable.
     *
     * @param var variable being assigned
     * @param val value assigned to the variable.
     */
    public void assign(int var, int val) {
        ArrayList<Integer> assignedDomain = new ArrayList<>();
        assignedDomain.add(val);
        varList.remove(varList.indexOf(var));
        variablesMap.put(var, assignedDomain);
    }


    /**
     * Method which deleted a value from a variable's domain.
     *
     * @param var variable from whose domain the value will be deleted
     * @param val value to be deleted from the domain of var.
     */
    public void deleteValue(int var, int val) {
        ArrayList<Integer> domain = variablesMap.get(var);
        domain.remove(domain.indexOf(val));
        variablesMap.put(var, domain);
    }


    /**
     * Method which calls the revise operation on the future variables.
     *
     * @param var
     * @return
     */
    public boolean reviseFutureArcs(int var) {

        for (int futureVar : varList) {
            if (futureVar != var) {
                try {
                    revise(futureVar, var);
                } catch (ReviseException e) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Revise procedure. Revises the arc represented by constraint c(iVar, jVar).
     * It will remove the domain values of iVar that are not supported by the domain values of jVar.
     *
     * @param iVar
     * @param jVar
     * @return whether the revision has caused any changes in the domain of iVar
     */
    public boolean revise(int iVar, int jVar) throws ReviseException {
        long start = System.currentTimeMillis();
        revisionCount++;
        boolean changed = false;

        ArrayList<Integer> iDomain = variablesMap.get(iVar);
        ArrayList<Integer> jDomain = variablesMap.get(jVar);

        ArrayList<Integer> unsupportedValues = new ArrayList<>();

        for (int i : iDomain) {
            // holds whether domain value is supported by some domain value of jVar
            boolean supported = false;
            for (int j : jDomain) {
                if (satisfies(iVar, i, jVar, j)) {
                    supported = true;
                }
            }
            if (!supported) {
                unsupportedValues.add(i);
                changed = true;
                if (unsupportedValues.size() == iDomain.size()) {
                    break;
                }
            }
        }
        for (int unsupportedValue : unsupportedValues) {
            deleteValue(iVar, unsupportedValue);
        }

        if (iDomain.isEmpty()) {
            long end = System.currentTimeMillis();
            revisionTime += (end - start);
            throw new ReviseException("Domain is detected. Exit AC3 early.");
        }
        long end = System.currentTimeMillis();
        revisionTime += (end - start);
        return changed;

    }


    /**
     * Method which checks revises the arcs and checks for global consistency. If a domain empties, revise will throw a
     * ReviseException so that ac3 returns early.
     *
     * @return true if assignment globally consistent.
     */
    public boolean ac3(int var) {
        ArrayList<ArcPair> arcQueue = initialiseArcQueue(var);
        while (!arcQueue.isEmpty()) {
            ArcPair arcPair = arcQueue.remove(0);
            try {
                if (revise(arcPair.getFirstVar(), arcPair.getSecondVar())) {
                    ArrayList<ArcPair> subsequentArcs = getSubsequentArcs(arcPair.getFirstVar());
                    for (ArcPair subsequentArc : subsequentArcs) {
                        if (!arcQueue.contains(subsequentArc)) {
                            arcQueue.add(subsequentArc);
                        }
                    }
                }
            } catch (ReviseException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method which initialises the arc queue employed by AC3.
     *
     * @param var variable being examined.
     * @return a list of ArcPair, the initial queue for AC3.
     */
    public ArrayList<ArcPair> initialiseArcQueue(int var) {

        ArrayList<ArcPair> arcQueue = new ArrayList<>();
        for (BinaryConstraint c : csp.getConstraints()) {
            if (c.getFirstVar() == var) {
                if (varList.contains(c.getSecondVar())) {
                    arcQueue.add(new ArcPair(c.getSecondVar(), c.getFirstVar()));
                }
            } else if (c.getSecondVar() == var) {
                if (varList.contains(c.getFirstVar())) {
                    arcQueue.add(new ArcPair(c.getFirstVar(), c.getSecondVar()));
                }
            }
        }
        return arcQueue;
    }


    /**
     * Method which adds the appropriate arcs to AC3's arc queue, once an arc has been revised.
     *
     * @param var variable being examined
     * @return
     */
    public ArrayList<ArcPair> getSubsequentArcs(int var) {
        ArrayList<ArcPair> arcPairs = new ArrayList<>();

        for (BinaryConstraint c : csp.getConstraints()) {
            if (c.getFirstVar() == var) {
                if (varList.contains(c.getSecondVar())) {
                    arcPairs.add(new ArcPair(c.getSecondVar(), c.getFirstVar()));
                }
            } else if (c.getSecondVar() == var) {
                if (varList.contains(c.getFirstVar())) {
                    arcPairs.add(new ArcPair(c.getFirstVar(), c.getSecondVar()));
                }
            }
        }

        return arcPairs;
    }


    /**
     * Method which returns whether a variable assignment satisfies the binary constraint c(iVar, jVar)
     *
     * @param iVar
     * @param iVal
     * @param jVar
     * @param jVal
     * @return
     */
    public boolean satisfies(int iVar, int iVal, int jVar, int jVal) {
        boolean constraintExists = false;
        for (BinaryConstraint c : csp.getConstraints()) {
            if (c.varsMatch(iVar, jVar)) {
                constraintExists = true;
                for (BinaryTuple t : c.getTuples()) {
                    // since c(2,1) and c(1,2) are both represented c(1,2), must check which one is greater
                    // left sides of tuple is possible values and right sides is possible j values
                    if (iVar < jVar) {
                        // if the tuple matches the values passed as parameters
                        if (t.getFirstVal() == iVal && t.getSecondVal() == jVal) {
                            return true;
                        }
                    } else {
                        if (t.getSecondVal() == iVal && t.getFirstVal() == jVal) {
                            return true;
                        }
                    }
                }
            }
        }
        if (!constraintExists) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Utility method which prints the details of the node
     */
    public void printNodeDetails() {
        System.out.println("-------------------------");
        System.out.println("Node details");
        printList("Unassigned vars", varList);
        printMap();
    }


    /**
     * Method which prints the variables of the  CSP and their domain set.
     */
    private void printMap() {
        System.out.println("Node variables map: ");
        for (int key : variablesMap.keySet()) {
            ArrayList<Integer> domain = variablesMap.get(key);
            System.out.print("var: " + key + " with domain: ");
            for (int i = 0; i < domain.size(); i++) {
                System.out.print(" " + domain.get(i) + " ");
            }
            System.out.println('\n');
        }
    }


    /**
     * Method which prints the unassigned variables.
     */
    private void printList(String listName, ArrayList<Integer> varList) {
        System.out.println("Details for: " + listName);
        System.out.println("size of list : " + varList.size());
        for (int i = 0; i < varList.size(); i++) {
            System.out.print(" " + varList.get(i) + " ");
        }
        System.out.print("\n\n");
    }


    public HashMap<Integer, ArrayList<Integer>> getVariablesMap() {
        return variablesMap;
    }

    public ArrayList<Integer> getVarList() {
        return varList;
    }


}
