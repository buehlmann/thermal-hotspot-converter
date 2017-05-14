package tools;


import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class KmlNamespaceMapper extends NamespacePrefixMapper {
    private static final String KML_PREFIX = "";
    private static final String KML_URI = "http://www.opengis.net/kml/2.2";

    private static final String GX_PREFIX = "gx";
    private static final String GX_URI = "http://www.google.com/kml/ext/2.2";

    private static final String ATOM_PREFIX = "atom";
    private static final String ATOM_URI = "http://www.w3.org/2005/Atom";

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        switch (namespaceUri) {
            case KML_URI:
                return KML_PREFIX;
            case GX_URI:
                return GX_PREFIX;
            case ATOM_URI:
                return ATOM_PREFIX;
            default:
                return null;
        }
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[]{KML_URI, GX_URI, ATOM_URI};
    }
}
