package tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class DataSource {
    private final Logger logger = LoggerFactory.getLogger(DataSource.class);
    private String[] datas = {"Jan_04", "Jan_07", "Jan_10", "Apr_04", "Apr_07", "Apr_10", "Jul_04", "Jul_07", "Jul_10", "Oct_04", "Oct_07", "Oct_10"};

    public List<CsvData> fetch() throws IOException {
        logger.info("Fetching available Data from remote...");
        List<CsvData> result = new ArrayList<>();
        for (String dataId : datas) {
            URLConnection connection = new URL(format("http://thermal.kk7.ch/php/hotspots.php?t=%s&typ=csv&bbox=5.955870%%2C45.818020%%2C10.492030%%2C47.808380&limit=10000", dataId)).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");

            try (InputStream in = connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                logger.info("Fetching {} from {}...", dataId, connection.getURL());
                String content = reader.lines().parallel().collect(joining("\n"));
                result.add(new CsvData(dataId, content));
            }
        }
        logger.info("Fetched successfully {} files from remote...", datas.length);
        return result;
    }
}