import bg.sofia.uni.fmi.mjt.markdown.MarkdownConverter;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        MarkdownConverter m = new MarkdownConverter();
        Path first = Paths.get("test.md");
        Path second = Paths.get("restest.html");
        m.convertMarkdown(first, second);
    }
}