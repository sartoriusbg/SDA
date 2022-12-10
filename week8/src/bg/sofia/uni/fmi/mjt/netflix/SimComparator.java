package bg.sofia.uni.fmi.mjt.netflix;

import java.util.Comparator;

public class SimComparator implements Comparator<Content> {

    private Content template;

    public SimComparator(Content template) {
        this.template = template;
    }
    @Override
    public int compare(Content o1, Content o2) {
        return Integer.compare(calc(o2), calc(o1));
    }

    private int calc (Content c) {
       return (int) c.genres().stream().filter(s -> template.genres().contains(s)).count();
    }
}
