package net.pkhapps.dart.map.importer.maastotiedot;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.map.importer.AbstractJooqImporter;
import net.pkhapps.dart.map.importer.CSR;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.xml.AppSchemaConfiguration;
import org.geotools.xml.PullParser;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaResolver;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO Document me!
 *
 * @see <a href="http://www.maanmittauslaitos.fi/en/">NLS</a>
 */
public abstract class AbstractNlsMaastotiedotImporter<R extends UpdatableRecord<R>> extends AbstractJooqImporter {

    /**
     * Namespace URI for the GML used by the NLS terrain database ("maastotietojärjestelmä").
     */
    protected static final String NAMESPACE_URI = "http://xml.nls.fi/XML/Namespace/Maastotietojarjestelma/SiirtotiedostonMalli/2011-02";

    private final AppSchemaConfiguration configuration;

    /**
     * Creates the importer, resolving the GML schema and storing the downloaded XSD files in a temporary directory.
     * The actual importing must be done by invoking {@link #importData()}.
     *
     * @throws Exception if something goes wrong.
     */
    protected AbstractNlsMaastotiedotImporter() throws Exception {
        File tempFile = File.createTempFile("DART-NLS-IMPORTER", ".txt");
        SchemaCache schemaCache = new SchemaCache(tempFile.getParentFile(), true);
        SchemaResolver resolver = new SchemaResolver(schemaCache);
        configuration = new AppSchemaConfiguration(NAMESPACE_URI,
                "http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Maastotiedot.xsd",
                resolver);
        // Intentionally left out the GMLConfiguration, forcing the parser to return
        // maps instead. This is because some important information was left out from
        // the SimpleFeature, but included in the map representation.
    }

    @Override
    protected void importData(DSLContext dslContext) throws Exception {
        // TODO Replace with system property or similar
        File dataDirectory = new File("/Users/petterprivate/Google Drive/Maps/maastotietokanta_tiesto");
        if (!dataDirectory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + dataDirectory.getCanonicalPath());
        }
        try {
            Files.list(dataDirectory.toPath())
                    .filter(path -> path.toString().toLowerCase().endsWith(".xml"))
                    .forEach(path -> {
                        try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
                            System.out.printf("Importing from %s%n", path.toAbsolutePath());
                            importData(is, dslContext);
                        } catch (Exception ex) {
                            throw new ImportException(ex);
                        }
                    });
        } catch (ImportException ex) {
            throw ex.getCause();
        }
    }

    private static class ImportException extends RuntimeException {

        public ImportException(Exception cause) {
            super(cause);
        }

        @Override
        public synchronized Exception getCause() {
            return (Exception) super.getCause();
        }
    }

    /**
     * @param inputStream
     * @param dslContext
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    protected void importData(InputStream inputStream, DSLContext dslContext) throws Exception {
        PullParser parser = new PullParser(configuration, inputStream, getFeatureQName());
        List<R> records = new ArrayList<>();
        int count = 0;
        Map<String, Object> feature = (Map<String, Object>) parser.parse();
        while (feature != null) {
            records.add(createRecord(feature, dslContext));
            feature = (Map<String, Object>) parser.parse();
            ++count;
            if (count % 100 == 0) {
                runBatch(records, dslContext);
            }
        }
        if (records.size() > 0) {
            runBatch(records, dslContext);
        }
        System.out.printf("Imported %d record(s)%n", count);
    }

    /**
     * @return
     */
    protected abstract QName getFeatureQName();

    /**
     * @param feature
     * @param dslContext
     * @return
     * @throws Exception
     */
    protected abstract R createRecord(Map<String, Object> feature, DSLContext dslContext) throws Exception;

    /**
     * @param addressData
     * @return
     */
    protected static String addressDataToString(Object addressData) {
        if (addressData instanceof Map) {
            return (String) ((Map) addressData).get(null);
        } else if (addressData instanceof String) {
            return (String) addressData;
        } else {
            return "";
        }
    }

    /**
     * @param x
     * @param y
     * @return
     * @throws Exception
     */
    protected Coordinates fromTM35FINtoWGS84(double x, double y) throws Exception {
        DirectPosition2D source = new DirectPosition2D(x, y);
        DirectPosition2D destination = new DirectPosition2D();
        CSR.transform.transform(source, destination);
        return new Coordinates(new BigDecimal(destination.getY()), new BigDecimal(destination.getX()));
    }
}