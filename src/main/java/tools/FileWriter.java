package tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class FileWriter {
    private final Logger logger = LoggerFactory.getLogger(KmlConverter.class);

    public void writeToFile(String content, String file) {
        try (PrintWriter out = new PrintWriter(file, "UTF-8")) {
            out.println(content);
            out.flush();
            logger.info("Successfully written file {}", file);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            logger.error("Error during writing file... ", e);
        }
    }
}
