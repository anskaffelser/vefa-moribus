package no.difi.vefa.moribus.util;

import net.sf.saxon.s9api.*;
import no.difi.vefa.moribus.lang.MoribusException;

import javax.inject.Singleton;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
@Singleton
public class SaxonUtil {

    private final Processor PROCESSOR = new Processor(false);

    public XsltTransformer get(String path) throws IOException, MoribusException {
        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            XsltCompiler xsltCompiler = PROCESSOR.newXsltCompiler();
            XsltExecutable xsltExecutable = xsltCompiler.compile(new StreamSource(inputStream));
            return xsltExecutable.load();
        } catch (SaxonApiException e) {
            throw new MoribusException("Unable to load XSLT.", e);
        }
    }

    public void perform(String path, Path source, Path target) throws IOException, MoribusException {
        if (Files.notExists(target.getParent()))
            Files.createDirectories(target.getParent());

        try (InputStream inputStream = Files.newInputStream(source);
             OutputStream outputStream = Files.newOutputStream(target)) {
            perform(path, inputStream, outputStream);
        }
    }

    public void perform(String path, InputStream inputStream, OutputStream outputStream) throws IOException, MoribusException {
        perform(path, new StreamSource(inputStream), PROCESSOR.newSerializer(outputStream));
    }

    public void perform(String path, Source source, Destination destination) throws IOException, MoribusException {
        try {
            XsltTransformer xsltTransformer = get(path);
            xsltTransformer.setSource(source);
            xsltTransformer.setDestination(destination);
            xsltTransformer.transform();
        } catch (SaxonApiException e) {
            throw new MoribusException("Unable to perform transforming.", e);
        }
    }
}
