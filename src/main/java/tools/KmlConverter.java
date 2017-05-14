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

import static java.lang.String.format;

public class KmlConverter {
    private final Logger logger = LoggerFactory.getLogger(KmlConverter.class);

    public String toKml(String id, List<Record> data) {

        Kml kml = KmlFactory.createKml();
        Document doc = kml.createAndSetDocument();
        doc.withName(id).withVisibility(true).withOpen(true);

        Folder folder = doc.createAndAddFolder()
                .withVisibility(true)
                .withOpen(false);

        for (Record record : data) {
            Placemark mark = folder.createAndAddPlacemark()
                    .withVisibility(true)
                    .withOpen(false)
                    .withDescription("<p>Probability: " + record.getProbability() + "</p><p>Rank: " + record.getRank() + "</p>");

                    mark.createAndAddStyle()
                        .createAndSetIconStyle().withScale(record.getProbability())
                    .createAndSetIcon().setHref("http://maps.google.com/mapfiles/kml/pal2/icon18.png");

                    mark.createAndSetPoint()
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
}
