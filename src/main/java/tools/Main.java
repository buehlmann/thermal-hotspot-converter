package tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Main {
    private final Logger logger = LoggerFactory.getLogger(Main.class);
    private final String path;

    private DataSource dataSource;
    private CsvParser parser;
    private KmlConverter converter;
    private FileWriter writer;

    public Main(String path) {
        this.path = path;
    }

    public static void main(String[] args) {
        String path = "/tmp/";
        if (args.length > 0) {
            path = args[0];
        }
        new Main(path).run();
    }

    public void run() {
        init();
        process();
    }

    private void init() {
        dataSource = new DataSource();
        parser = new CsvParser();
        converter = new KmlConverter();
        writer = new FileWriter();
    }

    private void process() {
        try {
            for (CsvData data : dataSource.fetch()) {
                List<Record> parsedData = parser.parse(data.getId(), data);
                String kml = converter.toKml(data.getId(), parsedData);
                writer.writeToFile(kml, path + data.getId() + ".kml");
            }
        } catch (IOException e) {
            logger.error("IOException occurred", e);
        }
    }
}
