package no.difi.vefa.moribus;

import no.difi.vefa.moribus.api.Processor;
import no.difi.vefa.moribus.lang.MoribusException;
import no.difi.vefa.moribus.model.Structure;
import no.difi.vefa.moribus.util.StructureLoader;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author erlend
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) throws IOException, MoribusException {
        Arguments arguments = new Arguments();

        CmdLineParser cmdLineParser = new CmdLineParser(arguments);
        cmdLineParser.getProperties().withUsageWidth(80);

        try {
            cmdLineParser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            cmdLineParser.printUsage(System.err);
            System.err.println();
            return;
        }

        try {
            Structure structure = StructureLoader.load(arguments);

            List<Processor> processors = StreamSupport.stream(ServiceLoader.load(Processor.class).spliterator(), false)
                    // .sorted(Processor::getWeight)
                    .collect(Collectors.toList());

            for (Processor processor : processors)
                processor.process(structure, arguments);
        } catch (MoribusException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
