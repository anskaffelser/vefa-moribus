package no.difi.vefa.moribus.util;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import no.difi.vefa.moribus.guice.MoribusModule;
import no.difi.vefa.moribus.model.Structure;
import org.kohsuke.MetaInfServices;

import java.io.IOException;

/**
 * @author erlend
 */
@MetaInfServices
public class UtilModule extends MoribusModule {

    @Provides
    @Singleton
    public Structure provideStructure(StructureLoader structureLoader) throws IOException {
        return structureLoader.load();
    }
}
