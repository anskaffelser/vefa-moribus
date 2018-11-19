package no.difi.vefa.moribus.generator;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.difi.vefa.moribus.annotation.Step;
import no.difi.vefa.moribus.annotation.Order;
import no.difi.vefa.moribus.api.Generator;
import no.difi.vefa.moribus.util.Arguments;

import java.io.IOException;
import java.nio.file.Files;

/**
 * @author erlend
 */
@Singleton
@Order(-10000)
@Step("clean")
public class CleanGenerator implements Generator {

    @Inject
    private Arguments arguments;

    @Override
    public void perform() throws IOException {
        if (Files.exists(arguments.getTarget()))
            MoreFiles.deleteDirectoryContents(arguments.getTarget(), RecursiveDeleteOption.ALLOW_INSECURE);
        else
            Files.createDirectories(arguments.getTarget());
    }
}
