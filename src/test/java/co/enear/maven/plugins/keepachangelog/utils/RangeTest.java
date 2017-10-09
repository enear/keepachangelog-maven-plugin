package co.enear.maven.plugins.keepachangelog.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RangeTest {

    private Range range;

    @Before
    public void setup() {
        range = new Range<String>("1", "12");
    }

    @Test
    public void getBeginTest() {
        assertEquals("1", range.getBegin());
    }

    @Test
    public void getEndTest() {
        assertEquals("12", range.getEnd());
    }

    @Test
    public void zipTest() {

        List<Range<String>> actual = Range.zip(Arrays.asList("1", "2", "3"), Arrays.asList("4", "5", "6", "7", "8"));

        assertEquals(3, actual.size());
        assertEquals("1", actual.get(0).getBegin());
        assertEquals("4", actual.get(0).getEnd());

        assertEquals("2", actual.get(1).getBegin());
        assertEquals("5", actual.get(1).getEnd());

        assertEquals("3", actual.get(2).getBegin());
        assertEquals("6", actual.get(2).getEnd());

    }

}
