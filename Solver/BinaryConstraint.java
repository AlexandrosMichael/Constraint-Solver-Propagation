import java.util.*;

public final class BinaryConstraint {
    private int firstVar, secondVar;
    private ArrayList<BinaryTuple> tuples;

    public BinaryConstraint(int fv, int sv, ArrayList<BinaryTuple> t) {
        firstVar = fv;
        secondVar = sv;
        tuples = t;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("c(" + firstVar + ", " + secondVar + ")\n");
        for (BinaryTuple bt : tuples)
            result.append(bt + "\n");
        return result.toString();
    }

    public String toStringHeader() {
        StringBuffer result = new StringBuffer();
        result.append("c(" + firstVar + ", " + secondVar + ")\n");
        return result.toString();
    }

    // SUGGESTION: You will want to add methods here to reason about the constraint


    public ArrayList<BinaryTuple> getTuples() {
        return tuples;
    }

    /**
     * Utility functions which returns whether a constraint exists in the CSP.
     * @param firstVar
     * @param secondVar
     * @return
     */
    public boolean varsMatch(int firstVar, int secondVar) {
        if ((((firstVar == this.firstVar) && (secondVar == this.secondVar))) || (((firstVar == this.secondVar) && (secondVar == this.firstVar)))) {
            return true;
        }
        return false;
    }

    public int getFirstVar() {
        return firstVar;
    }

    public int getSecondVar() {
        return secondVar;
    }
}
