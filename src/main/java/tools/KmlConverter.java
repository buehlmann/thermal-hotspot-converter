package tools;

import de.micromata.opengis.kml.v_2_2_0.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static de.micromata.opengis.kml.v_2_2_0.AltitudeMode.ABSOLUTE;
import static java.lang.String.format;

public class KmlConverter {
    private final Logger logger = LoggerFactory.getLogger(KmlConverter.class);


    public String toKml(String id, List<Record> data) {

        Kml kml = KmlFactory.createKml();
        Document doc = kml.createAndSetDocument();
        doc.withName(id).withVisibility(true).withOpen(true);

        for (int i = 1; i < 6; i++)
            appendStyle(i, doc);

        Folder folder = doc.createAndAddFolder()
                .withVisibility(true)
                .withOpen(false);

        for (Record record : data) {
            Placemark mark = folder.createAndAddPlacemark()
                    .withVisibility(true)
                    .withOpen(false)
                    .withDescription(
                            "<p>Probability: " + record.getProbability() + "</p>" +
                            "<p>Height: " + record.getHeight() + "</p>" +
                            "<p>Rank: " + record.getRank() + "</p>")
                    .withStyleUrl("#" + getStyleForProbability(record.getProbability()));

            mark.createAndSetPoint()
                    .withAltitudeMode(ABSOLUTE)
                    .addToCoordinates(format("%f,%f,%.0f",
                            record.getLongitude(),
                            record.getLatitude(),
                            record.getHeight()));
        }

        try (StringWriter writer = new StringWriter()) {
            JAXBContext context = JAXBContext.newInstance(Kml.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new KmlNamespaceMapper());
            marshaller.marshal(kml, writer);
            return writer.toString();

        } catch (PropertyException e) {
            logger.error("Could not register custom namespace mapper", e);
        } catch (JAXBException e) {
            logger.error("Error during serializing to kml model", e);
        } catch (IOException e) {
            logger.error("IO Error occurred during serializing to kml model", e);
        }
        return null;
    }

    private void appendStyle(int i, Document doc) {
        doc.createAndAddStyle()
                .withId("hotspot-" + i)
                .createAndSetIconStyle()
                .withScale(0.5)
                .createAndSetIcon()
                    .setHref("https://github.com/buehlmann/map-data/raw/master/icons/icon-" + i + ".png");
    }

    private String getStyleForProbability(double probability) {
        if(probability >= 0.0 && probability < 0.2) {
            return "hotspot-1";
        } else if(probability >= 0.2 && probability < 0.4) {
            return "hotspot-2";
        } else if(probability >= 0.4 && probability < 0.6) {
            return "hotspot-3";
        } else if(probability >= 0.6 && probability < 0.8) {
            return "hotspot-4";
        } else if(probability >= 0.8 && probability <= 1.0) {
            return "hotspot-5";
        } else {
            throw new RuntimeException("Probability must lie between 0 and 1. Found " + probability);
        }
    }
}
