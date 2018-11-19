package no.difi.vefa.moribus.generator;

import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.difi.vefa.moribus.annotation.Order;
import no.difi.vefa.moribus.annotation.Step;
import no.difi.vefa.moribus.api.Generator;
import no.difi.vefa.moribus.util.Arguments;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
@Singleton
@Order(10000)
@Step("bootstrap")
public class BootstrapGenerator implements Generator {

    private Path target;

    @Inject
    public BootstrapGenerator(Arguments arguments) {
        this.target = arguments.getTarget("site");
    }

    @Override
    public void perform() throws IOException {
        copy("css/bootstrap.css");
        copy("js/bootstrap.js");
    }

    private void copy(String filename) throws IOException {
        Path targetFile = target.resolve(filename);
        Files.createDirectories(targetFile.getParent());

        try (InputStream inputStream = getClass().getResourceAsStream("/META-INF/resources/webjars/bootstrap/4.1.3/" + filename);
             OutputStream outputStream = Files.newOutputStream(targetFile)) {
            ByteStreams.copy(inputStream, outputStream);
        }
    }
}
