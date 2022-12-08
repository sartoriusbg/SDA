package bg.sofia.uni.fmi.mjt.netflix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Content(String id, String title, ContentType type, String description, int releaseYear, int runtime, List<String> genres, int seasons, String imdbId, double imdbScore, double imdbVotes) {

    private static final String PEAK_ATTRIBUTE_DELIMITER = ",";
    private static final int ID = 0;
    private static final int TITLE = 1;
    private static final int TYPE = 2;
    private static final int DESCRIPTION = 3;
    private static final int RELEASE_YEAR = 4;
    private static final int RUNTIME = 5;
    private static final int GENRES = 6;
    private static final int SEASONS = 7;
    private static final int IMDB_ID = 8;
    private static final int IMDB_SCORE = 9;
    private static final int IMDB_VOTES = 10;
    public static Content of(String line) {
        final String[] tokens = line.strip().split(PEAK_ATTRIBUTE_DELIMITER);
        return new Content(tokens[ID], tokens[TITLE], readType(tokens[TYPE]),
                tokens[DESCRIPTION], Integer.parseInt(tokens[RELEASE_YEAR]), Integer.parseInt(tokens[RUNTIME]), readGenres(tokens[GENRES]),
                Integer.parseInt(tokens[SEASONS]), tokens[IMDB_ID], Double.parseDouble(tokens[IMDB_SCORE]), Double.parseDouble(tokens[IMDB_VOTES]));
    }

    private static List<String> readGenres(String line) {
        String[] genres = line.replaceAll("\'", "").replace("[", "").replace("]", "").replaceAll(" ", "").split(";");
        return Arrays.stream(genres).toList();
    }
    private static ContentType readType(String line) {
        if(line == "MOVIE") {
            return ContentType.MOVIE;
        }
        return ContentType.SHOW;
    }
}
