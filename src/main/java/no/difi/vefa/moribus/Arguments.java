package no.difi.vefa.moribus;

import lombok.Getter;
import org.kohsuke.args4j.Option;

import java.io.File;

/**
 * Object holding arguments provided for runtime.
 *
 * @author erlend
 */
@Getter
public class Arguments {

    @Option(name = "-source", usage = "Source folder")
    private File sourceFolder = new File(".");

    @Option(name = "-target", usage = "Target folder")
    private File targetFolder = new File("target");

}
