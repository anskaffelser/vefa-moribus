package no.difi.vefa.moribus.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import no.difi.vefa.moribus.util.Arguments;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * @author erlend
 */
public class ArgumentsModule extends AbstractModule {

    private String[] args;

    public ArgumentsModule(String[] args) {
        this.args = args;
    }

    @Provides
    @Singleton
    protected Arguments getArguments() {
        Arguments arguments = new Arguments();
        CmdLineParser parser = new CmdLineParser(arguments);

        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
            System.exit(1);
        }

        return arguments;
    }
}
