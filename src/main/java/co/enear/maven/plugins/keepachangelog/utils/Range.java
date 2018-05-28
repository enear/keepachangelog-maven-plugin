package co.enear.maven.plugins.keepachangelog.utils;

/*-
 * #%L
 * keepachangelog-maven-plugin
 * %%
 * Copyright (C) 2017 - 2018 e.near
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
     * <pre>{@code
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
