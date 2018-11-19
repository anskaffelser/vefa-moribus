package no.difi.vefa.moribus;

import com.google.common.collect.Lists;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import no.difi.vefa.moribus.api.Generator;
import no.difi.vefa.moribus.lang.MoribusException;
import no.difi.vefa.moribus.module.ArgumentsModule;
import no.difi.vefa.moribus.util.Arguments;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author erlend
 */
@Slf4j
@Singleton
public class Main {

    @Inject
    private Arguments arguments;

    @Inject
    private Set<Generator> generators;

    public static void main(String... args) throws IOException, MoribusException {
        List<Module> modules = Lists.newArrayList(ServiceLoader.load(Module.class));
        modules.add(new ArgumentsModule(args));

        Guice.createInjector(modules)
                .getInstance(Main.class)
                .run();
    }

    public void run() throws IOException, MoribusException {
        log.info("Preparing target");
        if (Files.exists(arguments.getTarget()))
            MoreFiles.deleteRecursively(arguments.getTarget(), RecursiveDeleteOption.ALLOW_INSECURE);
        Files.createDirectories(arguments.getTarget());

        for (Generator generator : generators.stream()
                .sorted(Comparator.comparing(Generator::getOrder))
                .collect(Collectors.toList()))
            generator.perform();
    }
}
