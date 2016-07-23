package net.pkhapps.dart.map.importer;

import net.pkhapps.dart.map.database.tables.NlsPaikkatyyppialaryhma;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsPaikkatyyppialaryhmaImporter extends AbstractJaxJooqImporter {

    @Override
    ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsPaikkatyyppialaryhma.NLS_PAIKKATYYPPIALARYHMA);
    }

    public static void main(String[] args) throws Exception {
        new NlsPaikkatyyppialaryhmaImporter().importData();
    }
}
