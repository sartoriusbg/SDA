package bg.sofia.uni.fmi.mjt.markdown;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarkdownConverterTest {

    private MarkdownConverter test;

    private Path path1, path2;
    private File file1, file2;

    @TempDir
    private Path srcDir;

    @TempDir
    private Path wDir;

    @BeforeEach
    void setup() {
        test = new MarkdownConverter();
        try {
            path1 = srcDir.resolve( "mdfile.md" );
            path2 = wDir.resolve( "htmlfile.html" );
        }
        catch( InvalidPathException ipe ) {
            System.err.println(
                    "error creating temporary test file in " +
                            this.getClass().getSimpleName() );
        }
    }
    @Test
    void convertMarkdownTest() {
        StringReader reader = new StringReader("# Heading level 1\n## Heading level 2\n### Heading level 3\n#### Heading level 4\n##### Heading level 5\n###### Heading level 6\nI just love **bold text**.\nLove**is**bold\nItalicized text is the *cat's meow*.\nAlways `.close()` your streams\n`.close()` *your* **eyes**");
        StringWriter writer = new StringWriter();
        test.convertMarkdown(reader,writer);
        assertEquals("<html>" + System.lineSeparator() +
                "<body>" + System.lineSeparator()+
                "<h1>Heading level 1</h1>" + System.lineSeparator() +
                "<h2>Heading level 2</h2>" + System.lineSeparator() +
                "<h3>Heading level 3</h3>" + System.lineSeparator() +
                "<h4>Heading level 4</h4>" + System.lineSeparator() +
                "<h5>Heading level 5</h5>" + System.lineSeparator() +
                "<h6>Heading level 6</h6>" + System.lineSeparator() +
                "I just love <strong>bold text</strong>." + System.lineSeparator() +
                "Love<strong>is</strong>bold" + System.lineSeparator() +
                "Italicized text is the <em>cat's meow</em>." + System.lineSeparator() +
                "Always <code>.close()</code> your streams" + System.lineSeparator() +
                "<code>.close()</code> <em>your</em> <strong>eyes</strong>" + System.lineSeparator() +
                "</body>" + System.lineSeparator() +
                "</html>", writer.toString());
    }

    @Test
    void convertMarkdownPathTest() {
        try(BufferedWriter bw1 = Files.newBufferedWriter(path1); BufferedWriter bw2 = Files.newBufferedWriter(path2); BufferedReader br1 = Files.newBufferedReader(path1); BufferedReader br2 = Files.newBufferedReader(path2)) {
            bw1.write("# Heading level 1\n## Heading level 2\n### Heading level 3\n#### Heading level 4\n##### Heading level 5\n###### Heading level 6\nI just love **bold text**.\nLove**is**bold\nItalicized text is the *cat's meow*.\nAlways `.close()` your streams\n`.close()` *your* **eyes**");
            bw1.flush();
            String line;
            String result = "";
            while ((line = br1.readLine()) != null) {
                result += line + System.lineSeparator();
            }
            System.out.println(line);
            test.convertMarkdown(path1, path2);
            result = "";
            while ((line = br2.readLine()) != null) {
                result += line + System.lineSeparator();
            }
            assertEquals("<html>" + System.lineSeparator() +
                    "<body>" + System.lineSeparator()+
                    "<h1>Heading level 1</h1>" + System.lineSeparator() +
                    "<h2>Heading level 2</h2>" + System.lineSeparator() +
                    "<h3>Heading level 3</h3>" + System.lineSeparator() +
                    "<h4>Heading level 4</h4>" + System.lineSeparator() +
                    "<h5>Heading level 5</h5>" + System.lineSeparator() +
                    "<h6>Heading level 6</h6>" + System.lineSeparator() +
                    "I just love <strong>bold text</strong>." + System.lineSeparator() +
                    "Love<strong>is</strong>bold" + System.lineSeparator() +
                    "Italicized text is the <em>cat's meow</em>." + System.lineSeparator() +
                    "Always <code>.close()</code> your streams" + System.lineSeparator() +
                    "<code>.close()</code> <em>your</em> <strong>eyes</strong>" + System.lineSeparator() +
                    "</body>" + System.lineSeparator() +
                    "</html>",result);
        }
        catch (IOException e) {}
    }

    @Test
    void convertMarkdownDirTest() {

    }
}
