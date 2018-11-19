package no.difi.vefa.moribus.generator;

import com.google.inject.Inject;
import no.difi.vefa.moribus.util.Arguments;
import no.difi.vefa.moribus.util.FileExtractor;
import no.difi.vefa.moribus.util.SaxonUtil;
import no.difi.vefa.moribus.api.Generator;
import no.difi.vefa.moribus.lang.MoribusException;

import java.io.IOException;

/**
 * @author erlend
 */
public abstract class AbstractXsltGenerator implements Generator {

    @Inject
    private Arguments arguments;

    @Inject
    private FileExtractor extractor;

    @Inject
    private SaxonUtil saxon;

    protected void generate(String stylesheet, String target) throws IOException, MoribusException {
        saxon.perform(stylesheet,
                arguments.getTarget("all.xml"),
                arguments.getTarget(target));
        extractor.execute(arguments.getTarget(target));
    }
}
