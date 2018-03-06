package no.difi.vefa.moribus.api;

import no.difi.vefa.moribus.Arguments;
import no.difi.vefa.moribus.lang.MoribusException;
import no.difi.vefa.moribus.model.Structure;

import java.io.IOException;

/**
 * @author erlend
 */
public interface Processor {

    /**
     * Weight of processor. Those processors with high weight are executed before those with low weight.
     *
     * @return Weight of processor for sorting.
     */
    default int getWeight() {
        return 0;
    }

    void process(Structure structure, Arguments arguments) throws IOException, MoribusException;

}
