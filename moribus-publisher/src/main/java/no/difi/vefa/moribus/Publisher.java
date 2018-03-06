package no.difi.vefa.moribus;

import no.difi.vefa.moribus.api.Processor;
import no.difi.vefa.moribus.lang.MoribusException;
import no.difi.vefa.moribus.model.Structure;
import no.difi.vefa.moribus.util.StructureLoader;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Set;

/**
 * @author erlend
 */
public class Publisher {

    @Inject
    private Set<Processor> processors;

    @Inject
    private StructureLoader structureLoader;

    public void perform(Arguments arguments) throws IOException, MoribusException {
        Structure structure = structureLoader.load(arguments);

        for (Processor processor : processors)
            processor.process(structure, arguments);
    }
}
