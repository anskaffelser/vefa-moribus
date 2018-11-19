package no.difi.vefa.moribus.util;

import lombok.Getter;
import org.kohsuke.args4j.Option;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author erlend
 */
@Getter
public class Arguments {

    @Option(name = "-source")
    private Path source = Paths.get("moribus");

    @Option(name = "-target")
    private Path target = Paths.get("target");

    public Path getSource(String path) {
        return source.resolve(path);
    }

    public Path getTarget(String path) {
        return target.resolve(path);
    }
}
