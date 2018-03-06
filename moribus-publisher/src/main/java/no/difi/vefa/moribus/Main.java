package no.difi.vefa.moribus;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import no.difi.vefa.moribus.guice.MoribusModule;
import no.difi.vefa.moribus.lang.MoribusException;
import no.difi.vefa.moribus.util.Arguments;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author erlend
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) {
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

        List<Module> modules = Lists.newArrayList(ServiceLoader.load(MoribusModule.class).iterator());
        modules.add(arguments);

        Injector injector = Guice.createInjector(modules);

        try {
            injector.getInstance(Publisher.class).perform();
        } catch (MoribusException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
