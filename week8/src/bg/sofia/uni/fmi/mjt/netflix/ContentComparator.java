package bg.sofia.uni.fmi.mjt.netflix;

import java.util.Comparator;

public class ContentComparator implements Comparator<Content> {

    private static final double M = 10000;
    private double C;

    public ContentComparator(double C) {
        this.C = C;
    }

    /**
     * The weighed rating is calculated by the following formula:
     * Weighted Rating (WR) = (v ÷ (v + m)) × R + (m ÷ (v + m)) × C
     * where
     * R is the content's own average rating across all votes. If it has no votes, its R is 0.
     * C is the average rating of content across the dataset
     * v is the number of votes for a content
     * m is a tunable parameter: sensitivity threshold. In our algorithm, it's a constant equal to 10_000.
     */
    @Override
    public int compare(Content o1, Content o2) {
        return Double.compare(calc(o2), calc(o1));
    }

    private double calc(Content c) {
        return (c.imdbVotes() / (c.imdbVotes() + M)) * c.imdbScore() + (c.imdbVotes() / (c.imdbVotes() + M)) * C;
    }
}
