package ca.ubc.ece.cpen221.mp1.utils;

/**
 * <p>Represents a pair of elements. This is a parametrized class,
 * and only stores pairs of elements when the elements implement the
 * HasSimilarity interface. In other words, both elements of the Pair
 * should implement a method named similarity() that returns a double.</p>
 *
 * <p><tt>Pair</tt> implements <tt>Comparable</tt> so that it is convenient
 * for sorting a <tt>List</tt> of <tt>Pair</tt>s and so on.</p>
 *
 * <p>Usage example for creating a Pair:
 * <tt>Pair<SoundWave> myPair = new Pair<SoundWave>(w1, w2);</tt>
 * Note that <tt>SoundWave</tt> should implement the <tt>HasSimilarity</tt>
 * interface.
 * </p>
 *
 * @param <E>
 */
public class

Pair<E extends HasSimilarity> implements Comparable<Pair<E>> {
    private E elem1, elem2;
    private double similarity;

    /**
     * Create a new pair given two elements
     *
     * @param elem1 not null
     * @param elem2 not null
     */
    public Pair(E elem1, E elem2) {
        assert ((elem1 != null) && (elem2 != null));
        this.elem1 = elem1;
        this.elem2 = elem2;
        similarity = elem1.similarity(elem2);
    }

    /**
     * Compare two Pair objects for equality.
     *
     * @param obj is not null
     * @return true if this Pair and the other Pair represent
     * the same two elems and false otherwise.
     */
    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Pair) {
            Pair other = (Pair) obj;
            return ((this.elem1.equals(other.elem1) && this.elem2.equals(other.elem2))
                    || (this.elem1.equals(other.elem2) && (this.elem2.equals(other.elem1))));
        } else {
            return false;
        }

    }

    /**
     * Compute the hashCode for a Pair
     *
     * @return the hashCode for this Pair
     */
    @Override
    public int hashCode() {
        return elem1.hashCode() + elem2.hashCode();
    }

    /**
     * Compare two Pair objects
     *
     * @param other the other Pair to compare this Pair to
     *
     * @return a value less than 0 if this Pair is less similar
     * internally than the other Pair, 0 if the similarity of
     * the two pairs is the same, and a value > 0 if this pair is more
     * similar than the other pair.
     */
    public int compareTo(Pair<E> other) {
        double diff = this.similarity - other.similarity;
        if (diff < 0) {
            return -1;
        } else if (diff > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Return the first element in the Pair.
     * The ordering of E objects in a Pair is arbitrary.
     *
     * @return the first element in the Pair.
     */
    public E getElem1() {
        return elem1;
    }

    /**
     * Return the second element in the Pair.
     * The ordering of E objects in a Pair is arbitrary.
     *
     * @return the second element in the Pair.
     */
    public E getElem2() {
        return elem2;
    }

    /**
     * Return the similarity of the two elements in the Pair.
     *
     * @return the similarity of the two elements in the Pair.
     */
    public double getSimilarity() {
        return similarity;
    }

    /**
     * Return a String representation of the Pair.
     *
     * @return a String representation of the Pair.
     */
    @Override
    public String toString() {
        return (elem1.toString() + " & " + elem2.toString());
    }

}

