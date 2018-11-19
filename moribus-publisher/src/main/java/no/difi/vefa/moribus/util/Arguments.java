package no.difi.vefa.moribus.util;

import lombok.Getter;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author erlend
 */
@Getter
public class Arguments {

    @Option(name = "-source")
    private Path source = Paths.get("moribus");

    @Option(name = "-target")
    private Path target = Paths.get("target");

    @Argument
    private List<String> steps;

    public Path getSource(String path) {
        return source.resolve(path);
    }

    public Path getTarget(String path) {
        return target.resolve(path);
    }

    public boolean hasStep(String step) {
        return steps == null || steps.isEmpty() || steps.contains(step);
    }
}
