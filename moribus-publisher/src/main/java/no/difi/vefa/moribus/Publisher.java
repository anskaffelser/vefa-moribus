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
    private Structure structure;

    public void perform() throws IOException, MoribusException {
        for (Processor processor : processors)
            processor.process(structure);
    }
}
