package no.difi.vefa.moribus.processor;

import com.google.common.io.ByteStreams;
import no.difi.vefa.moribus.Arguments;
import no.difi.vefa.moribus.api.Processor;
import no.difi.vefa.moribus.lang.MoribusException;
import no.difi.vefa.moribus.model.Structure;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
@Singleton
public class ResourceProcessor implements Processor {

    @Override
    public void process(Structure structure, Arguments arguments) throws IOException, MoribusException {
        Path targetFoldder = arguments.getTargetFolder().toPath();

        // Stylesheet
        copyFromClasspath(
                "/META-INF/resources/webjars/bootstrap/3.3.7-1/css/bootstrap.min.css",
                targetFoldder.resolve("css/bootstrap.css"));

        // JavaScript
        copyFromClasspath(
                "/META-INF/resources/webjars/bootstrap/3.3.7-1/js/bootstrap.min.js",
                targetFoldder.resolve("js/bootstrap.js"));
        copyFromClasspath(
                "/META-INF/resources/webjars/jquery/1.11.1/jquery.min.js",
                targetFoldder.resolve("js/jquery.js"));

        // Fonts
        copyFromClasspath(
                "/META-INF/resources/webjars/bootstrap/3.3.7-1/fonts/glyphicons-halflings-regular.eot",
                targetFoldder.resolve("fonts/glyphicons-halflings-regular.eot"));
        copyFromClasspath(
                "/META-INF/resources/webjars/bootstrap/3.3.7-1/fonts/glyphicons-halflings-regular.svg",
                targetFoldder.resolve("fonts/glyphicons-halflings-regular.svg"));
        copyFromClasspath(
                "/META-INF/resources/webjars/bootstrap/3.3.7-1/fonts/glyphicons-halflings-regular.ttf",
                targetFoldder.resolve("fonts/glyphicons-halflings-regular.ttf"));
        copyFromClasspath(
                "/META-INF/resources/webjars/bootstrap/3.3.7-1/fonts/glyphicons-halflings-regular.woff",
                targetFoldder.resolve("fonts/glyphicons-halflings-regular.woff"));
        copyFromClasspath(
                "/META-INF/resources/webjars/bootstrap/3.3.7-1/fonts/glyphicons-halflings-regular.woff2",
                targetFoldder.resolve("fonts/glyphicons-halflings-regular.woff2"));
    }

    public void copyFromClasspath(String from, Path path) throws IOException {
        if (!Files.exists(path.getParent()))
            Files.createDirectories(path.getParent());

        try (InputStream inputStream = getClass().getResourceAsStream(from);
             OutputStream outputStream = Files.newOutputStream(path)) {
            ByteStreams.copy(inputStream, outputStream);
        }
    }
}
