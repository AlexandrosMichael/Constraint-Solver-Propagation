/**
 * Class which represents an arc(x_i, c_j)
 */
public class ArcPair {

    private int firstVar;
    private int secondVar;

    public ArcPair(int firstVar, int secondVar) {
        this.firstVar = firstVar;
        this.secondVar = secondVar;
    }

    public int getFirstVar() {
        return firstVar;
    }


    public int getSecondVar() {
        return secondVar;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArcPair arcPair = (ArcPair) o;
        return ((firstVar == arcPair.firstVar && secondVar == arcPair.secondVar)) || ((firstVar ==arcPair.secondVar) && (secondVar == arcPair.firstVar));
    }
}
