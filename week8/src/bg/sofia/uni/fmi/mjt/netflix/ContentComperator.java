package bg.sofia.uni.fmi.mjt.netflix;

import java.util.Comparator;

public class ContentComperator implements Comparator<Content> {
    /** The weighed rating is calculated by the following formula:
            * Weighted Rating (WR) = (v ÷ (v + m)) × R + (m ÷ (v + m)) × C
     * where
     * R is the content's own average rating across all votes. If it has no votes, its R is 0.
            * C is the average rating of content across the dataset
     * v is the number of votes for a content
     * m is a tunable parameter: sensitivity threshold. In our algorithm, it's a constant equal to 10_000.*/
    @Override
    public int compare(Content o1, Content o2) {

    }

    private static double calc( )
}
