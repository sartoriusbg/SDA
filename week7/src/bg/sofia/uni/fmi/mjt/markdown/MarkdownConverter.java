package bg.sofia.uni.fmi.mjt.markdown;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownConverter implements MarkdownConverterAPI {

    private static final int MAX = 6;

    public MarkdownConverter() {
    }

    @Override
    public void convertMarkdown(Reader source, Writer output) {
        String line;
        String toWrite;
        try (BufferedReader br = new BufferedReader(source); BufferedWriter bw = new BufferedWriter(output)) {
            start(bw);
            while ((line = br.readLine()) != null) {
                toWrite = lineHandler(line) + System.lineSeparator();
                bw.write(toWrite);
                bw.flush();
            }
            end(bw);
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }

    }

    @Override
    public void convertMarkdown(Path from, Path to) {
        try (BufferedReader br = Files.newBufferedReader(from); BufferedWriter bw = Files.newBufferedWriter(to)) {
            convertMarkdown(br, bw);

        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }
    }

    @Override
    public void convertAllMarkdownFiles(Path sourceDir, Path targetDir) {
        try (DirectoryStream<Path> files = Files.newDirectoryStream(sourceDir)) {
            for (Path toConvert : files) {
                if (toConvert.getFileName().toString().endsWith(".md")) {
                    String newFileName = toConvert.getFileName().toString().replace(".md", ".html");
                    Path toCreate = targetDir.resolve(newFileName);
                    convertMarkdown(toConvert, toCreate);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a dir", e);
        }
    }

    private void start(Writer output) throws IOException {
        String tag1 = "<html>" + System.lineSeparator();
        String tag2 = "<body>" + System.lineSeparator();

        output.write(tag1);
        output.flush();
        output.write(tag2);
        output.flush();
    }

    private void end(Writer output) throws IOException {
        String tag1 = "</html>";
        String tag2 = "</body>" + System.lineSeparator();

        output.write(tag2);
        output.flush();
        output.write(tag1);
        output.flush();
    }

    private String lineHandler(String line) {
        String result;
        result = codeHandler(italicHandler(boldHandler(headingHandler(line))));
        return result;
    }

    private String headingHandler(String toChange) {
        String result = toChange;
        Pattern pattern;
        Matcher matcher;
        StringBuilder p = new StringBuilder("# ");
        StringBuilder step = new StringBuilder("#");
        for (Integer i = 1; i <= MAX; i++) {
            pattern = Pattern.compile("^" + p.toString());
            matcher = pattern.matcher(toChange);
            if (matcher.find()) {
                result = toChange.replace(p, "<h" + i.toString() + ">");
                result += "</h" + i.toString() + ">";
                return result;
            }
            p = new StringBuilder(step.toString() + p.toString());
        }
        return result;
    }

    private String boldHandler(String toChange) {
        String result = toChange;
        Pattern pattern = Pattern.compile("\\*\\*" + ".*" + "\\*\\*");
        Matcher matcher = pattern.matcher(toChange);
        if (matcher.find()) {
            result = toChange.replace("**", "</strong>");
            result = result.replaceFirst("</strong>", "<strong>");
            result = result.replace("<strong>*", "*</strong>");
        }
        return result;
    }

    private String italicHandler(String toChange) {
        String result = toChange;
        Pattern pattern = Pattern.compile("\\*" + ".*" + "\\*");
        Matcher matcher = pattern.matcher(toChange);
        if (matcher.find()) {
            result = toChange.replace("*", "</em>");
            result = result.replaceFirst("</em>", "<em>");
        }
        return result;
    }

    private String codeHandler(String toChange) {
        String result = toChange;
        Pattern pattern = Pattern.compile("`" + ".*" + "`");
        Matcher matcher = pattern.matcher(toChange);
        if (matcher.find()) {
            result = toChange.replace("`", "</code>");
            result = result.replaceFirst("</code>", "<code>");
        }
        return result;
    }

}
