package Extra;

public class PairInt implements Comparable<PairInt> {
    private final Integer a1;
    private final Integer a2;
    public PairInt(Integer a1, Integer a2) {
        this.a1 = a1;
        this.a2 = a2;
    }
    public Integer get_first() {
        return a1;
    }
    public Integer get_second() {
        return a2;
    }

    @Override
    public String toString() {
        return "Perechea: " + "(" + a1 + "," + a2 + ") ";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            PairInt that = (PairInt) obj;
            return a1.equals(that.a1) && a2.equals(that.a2);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h1 = (a1 != null ? a1.hashCode() : 0);
        int h2 = (a2 != null ? a2.hashCode() : 0);
        return 63 * h1 + h2;
    }

    @Override
    public int compareTo(PairInt other){
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal
        int sgn = a1.compareTo(other.a1);
        if(sgn != 0)
            return sgn;
        sgn = this.a2.compareTo(other.a2);
        return sgn;
    }
}
