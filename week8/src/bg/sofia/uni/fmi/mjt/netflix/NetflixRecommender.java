package bg.sofia.uni.fmi.mjt.netflix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NetflixRecommender {

    private List<Content> content;
    /**
     * Loads the dataset from the given {@code reader}.
     *
     * @param reader Reader from which the dataset can be read.
     */
    public NetflixRecommender(Reader reader) {
        content = new LinkedList<>();
        String line = "";
        try(BufferedReader br = new BufferedReader(reader)) {
            content = br.lines().map(Content::of).filter(c -> c != null).toList();
        }
        catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from the file", e);
        }
    }

    /**
     * Returns all movies and shows from the dataset in undefined order as an unmodifiable List.
     * If the dataset is empty, returns an empty List.
     *
     * @return the list of all movies and shows.
     */
    public List<Content> getAllContent() {
        return content;
    }

    /**
     * Returns a list of all unique genres of movies and shows in the dataset in undefined order.
     * If the dataset is empty, returns an empty List.
     *
     * @return the list of all genres
     */
    private static List<String> add (List first, List second) {
        first.addAll(second);
        return first;
    }
    public List<String> getAllGenres() {
        return  content.stream()
                .map(Content::genres)
                .reduce(new LinkedList<>(), (current, next) -> add(current,next)).stream()
                .distinct().toList();

    }

    /**
     * Returns the movie with the longest duration / run time. If there are two or more movies
     * with equal maximum run time, returns any of them. Shows in the dataset are not considered by this method.
     *
     * @return the movie with the longest run time
     * @throws NoSuchElementException in case there are no movies in the dataset.
     */
    public Content getTheLongestMovie() {
        return content.stream().max(Comparator.comparingInt(Content::runtime))
                .orElseThrow(() -> new NoSuchElementException("No movies in the dataset"));
    }

    /**
     * Returns a breakdown of content by type (movie or show).
     *
     * @return a Map with key: a ContentType and value: the set of movies or shows on the dataset, in undefined order.
     */

    private Map<ContentType, Set<Content>> add(Map<ContentType, Set<Content>> map, Content toAdd) {
        if(toAdd.type()==ContentType.MOVIE){
            map.get(ContentType.MOVIE).add(toAdd);
        }
        else {
            map.get(ContentType.SHOW).add(toAdd);
        }
        return map;
    }
    public Map<ContentType, Set<Content>> groupContentByType() {
        EnumMap<ContentType, Set<Content>> res = new EnumMap<>(ContentType.class);
        return res;
    }

    /**
     * Returns the top N movies and shows sorted by weighed IMDB rating in descending order.
     * If there are fewer movies and shows than {@code n} in the dataset, return all of them.
     * If {@code n} is zero, returns an empty list.
     *
     * The weighed rating is calculated by the following formula:
     * Weighted Rating (WR) = (v ÷ (v + m)) × R + (m ÷ (v + m)) × C
     * where
     * R is the content's own average rating across all votes. If it has no votes, its R is 0.
     * C is the average rating of content across the dataset
     * v is the number of votes for a content
     * m is a tunable parameter: sensitivity threshold. In our algorithm, it's a constant equal to 10_000.
     *
     * Check https://stackoverflow.com/questions/1411199/what-is-a-better-way-to-sort-by-a-5-star-rating for details.
     *
     * @param n the number of the top-rated movies and shows to return
     * @return the list of the top-rated movies and shows
     * @throws IllegalArgumentException if {@code n} is negative.
     */
    public List<Content> getTopNRatedContent(int n) {
       return content.stream().sorted(new ContentComparator(content.stream().mapToDouble(Content::imdbScore).average().getAsDouble())).limit(n).toList();
    }

    /**
     * Returns a list of content similar to the specified one sorted by similarity is descending order.
     * Two contents are considered similar, only if they are of the same type (movie or show).
     * The used measure of similarity is the number of genres two contents share.
     * If two contents have equal number of common genres with the specified one, their mutual oder
     * in the result is undefined.
     *
     * @param content the specified movie or show.
     * @return the sorted list of content similar to the specified one.
     */

    public List<Content> getSimilarContent(Content content) {
       return this.content.stream().filter(c -> c.type() == content.type()).sorted(new SimComparator(content)).toList();
    }

    /**
     * Searches content by keywords in the description (case-insensitive).
     *
     * @param keywords the keywords to search for
     * @return an unmodifiable set of movies and shows whose description contains all specified keywords.
     */
    private boolean containsKWords (Content c, String... keywords) {
        return Arrays.stream(keywords).allMatch(curr ->c.description().contains(curr));
    }
    public Set<Content> getContentByKeywords(String... keywords) {
        return content.stream().filter(c -> containsKWords(c,keywords)).collect(Collectors.toUnmodifiableSet());
    }

}
