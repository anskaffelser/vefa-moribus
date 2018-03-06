package no.difi.vefa.moribus.util;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.kohsuke.args4j.Option;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.nio.file.Path;

/**
 * Object holding arguments provided for runtime.
 *
 * @author erlend
 */
public class Arguments extends AbstractModule {

    @Option(name = "-source", usage = "Source folder")
    private File sourceFolder = new File("");

    @Option(name = "-target", usage = "Target folder")
    private File targetFolder = new File("target");

    @Provides
    @Named("source")
    @Singleton
    public Path provideSourceFolder() {
        return sourceFolder.toPath();
    }

    @Provides
    @Named("target")
    @Singleton
    public Path provideTargetFolder() {
        return targetFolder.toPath();
    }
}
