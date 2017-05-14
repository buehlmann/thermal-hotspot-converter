package tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.valueOf;

public class CsvParser {
    private final Logger logger = LoggerFactory.getLogger(CsvParser.class);

    public List<Record> parse(String id, CsvData data) throws IOException {
        List<Record> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new StringReader(data.getContent()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("lat")) {
                    result.add(parseLine(line));
                }
            }
        }
        logger.info("Parsed {} records for {}...", result.size(), id);
        return result;
    }

    private Record parseLine(String line) {
        String[] args = line.split(",");
        return new Record(
                valueOf(args[0]),
                valueOf(args[1]),
                valueOf(args[2]),
                valueOf(args[3]),
                valueOf(args[4]));
    }
}
