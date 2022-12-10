import bg.sofia.uni.fmi.mjt.netflix.Content;
import bg.sofia.uni.fmi.mjt.netflix.NetflixRecommender;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.StringReader;
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


}
