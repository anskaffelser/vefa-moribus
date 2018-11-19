package no.difi.vefa.moribus.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import no.difi.vefa.moribus.util.Arguments;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * @author erlend
 */
@Slf4j
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
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
            log.error("Unable to parse arguments: {}", e.getMessage());
            System.exit(1);
        }

        return arguments;
    }
}
