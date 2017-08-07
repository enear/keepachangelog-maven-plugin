package org.enear.changelog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A range.
 *
 * @param <N> the type of range.
 */
public class Range<N> {
    private N begin;
    private N end;

    /**
     * Creates a new range.
     *
     * @param begin the first element of the range.
     * @param end   the last element of the range.
     */
    public Range(N begin, N end) {
        this.begin = begin;
        this.end = end;
    }

    /**
     * Returns the first element of the range.
     *
     * @return the first element of the range.
     */
    public N getBegin() {
        return begin;
    }

    /**
     * Returns the last element of the range.
     *
     * @return the last element of the range.
     */
    public N getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return String.format("Range(%s,%s)", begin, end);
    }

    /**
     * Returns a list of ranges given two lists. If one list is short, excess elements of the larger list are discarded.
     * <p>
     * Example:
     * pre>{@code
     * List<Integer> xs = Arrays.asList(1, 2, 3);
     * List<Integer> ys = Arrays.asList(2, 4, 5, 6);
     * Range.zip(xs, ys); // [Range(1,2), Range(2,4), Range(3,5)]
     * }
     * </pre>
     *
     * @param xs  first list
     * @param ys  second list
     * @param <N> the type of the lists
     * @return a list of ranges.
     */
    public static <N> List<Range<N>> zip(List<N> xs, List<N> ys) {
        List<Range<N>> list = new ArrayList<>(xs.size());
        Iterator<N> itXs = ys.iterator();
        for (N x : xs) {
            if (itXs.hasNext()) {
                N y = itXs.next();
                Range<N> range = new Range<N>(x, y);
                list.add(range);
            } else {
                break;
            }
        }
        return list;
    }
}
