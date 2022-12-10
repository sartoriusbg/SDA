package bg.sofia.uni.fmi.mjt.netflix;

import bg.sofia.uni.fmi.mjt.netflix.Content;
import bg.sofia.uni.fmi.mjt.netflix.ContentType;
import bg.sofia.uni.fmi.mjt.netflix.NetflixRecommender;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class NetflixRecommenderTest {

    private NetflixRecommender test;

    @Test
    void getAllContentTest() {
        test = new NetflixRecommender(new StringReader("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes\n" +
                "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0"));
        LinkedList<Content> expected = new LinkedList<>();
        expected.add(Content.of("tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0"));
        assertIterableEquals(expected, test.getAllContent(), "Should be the same");
    }

    @Test
    void getAllGenresTest() {
        test = new NetflixRecommender(new StringReader("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes\n" +
                "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0\n" +
                "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0"));
        LinkedList<String> expected = new LinkedList<>();
        expected.add("drama");
        expected.add("crime");
        expected.add("action");
        expected.add("thriller");
        expected.add("european");
        assertIterableEquals(expected, test.getAllGenres(), "Should be the same");

    }

    @Test
    void getTheLongestMovieTest() {
        test = new NetflixRecommender(new StringReader("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes\n" +
                "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0\n" +
                "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0"));
        assertEquals(Content.of("tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0\n"), test.getTheLongestMovie(), "Should be same");
    }

    @Test
    void getTopNRatedContentTest() {
        test = new NetflixRecommender(new StringReader("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes\n" +
                "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0\n" +
                "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0"));
        LinkedList<Content> expected = new LinkedList<>();
        expected.add(Content.of("tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0"));
        assertIterableEquals(expected, test.getTopNRatedContent(1), "Should be the same");
    }

    @Test
    void getSimilarContentTest() {
        test = new NetflixRecommender(new StringReader("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes\n" +
                "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0\n" +
                "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0"));
        LinkedList<Content> expected = new LinkedList<>();
        expected.add(Content.of("tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0"));
        expected.add(Content.of("tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0"));
        assertIterableEquals(expected, test.getSimilarContent(Content.of("tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0")), "Should be same");
    }

    @Test
    void getContentByKeywordsTest() {
        test = new NetflixRecommender(new StringReader("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes\n" +
                "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0\n" +
                "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0"));
        HashSet<Content> expected = new HashSet<>();
        expected.add(Content.of("tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0"));
        assertTrue(expected.equals(test.getContentByKeywords("Vietnam")), "Should be true");
    }

    /*@Test
    void groupContentByTypeTest() {
        test = new NetflixRecommender(new StringReader("tm120801,The Dirty Dozen,MOVIE,12 American military prisoners in World War II are ordered to infiltrate a well-guarded enemy château and kill the Nazi officers vacationing there. The soldiers; most of whom are facing death sentences for a variety of violent crimes; agree to the mission and the possible commuting of their sentences.,1967,150,['war'; 'action'],-1,tt0061578,7.7,72662.0\n" +
                "ts22164,Monty Python's Flying Circus,SHOW,A British sketch comedy series with the shows being composed of surreality; risqué or innuendo-laden humour; sight gags and observational sketches without punchlines.,1969,30,['comedy'; 'european'],1,tt0063929,8.8,73424.0"));
        HashMap<ContentType, HashSet<Content>> expected = new HashMap<>();
        HashSet<Content> s = new HashSet<>();
        s.add(Content.of("ts22164,Monty Python's Flying Circus,SHOW,A British sketch comedy series with the shows being composed of surreality; risqué or innuendo-laden humour; sight gags and observational sketches without punchlines.,1969,30,['comedy'; 'european'],1,tt0063929,8.8,73424.0"));
        HashSet<Content> m = new HashSet<>();
        m.add(Content.of("tm120801,The Dirty Dozen,MOVIE,12 American military prisoners in World War II are ordered to infiltrate a well-guarded enemy château and kill the Nazi officers vacationing there. The soldiers; most of whom are facing death sentences for a variety of violent crimes; agree to the mission and the possible commuting of their sentences.,1967,150,['war'; 'action'],-1,tt0061578,7.7,72662.0"));
        expected.put(ContentType.SHOW, s);
        expected.put(ContentType.MOVIE, m);
        assertEquals(expected, test.groupContentByType(), "Should be same");

    }*/

}

