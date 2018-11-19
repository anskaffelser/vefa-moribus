package no.difi.vefa.moribus.util;

import lombok.extern.slf4j.Slf4j;
import no.difi.vefa.moribus.jaxb.v2.files.FileType;
import no.difi.vefa.moribus.jaxb.v2.files.FilesType;
import no.difi.vefa.moribus.lang.MoribusException;
import org.w3c.dom.Node;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
@Slf4j
@Singleton
public class FileExtractor {

    private static final JAXBContext JAXB_CONTEXT;

    private static final TransformerFactory transFactory = TransformerFactory.newInstance();

    private Path targetFolder;

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(FileType.class, FilesType.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Inject
    public FileExtractor(Arguments arguments) throws IOException {
        this.targetFolder = arguments.getTarget("site");

        if (Files.notExists(targetFolder))
            Files.createDirectories(targetFolder);
    }

    public void execute(Path sourceFile) throws IOException, MoribusException {
        try (InputStream inputStream = Files.newInputStream(sourceFile)) {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            FilesType filesType = unmarshaller.unmarshal(new StreamSource(inputStream), FilesType.class).getValue();

            for (FileType fileType : filesType.getFile())
                perform(fileType);
        } catch (JAXBException e) {
            throw new MoribusException("Error during parsing of source file.", e);
        }
    }

    private void perform(FileType fileType) throws IOException, MoribusException {
        log.debug("Extracting file '{}'.", fileType.getFilename());

        Path filename = targetFolder.resolve(fileType.getFilename());

        if (!Files.exists(filename.getParent()))
            Files.createDirectories(filename.getParent());

        try (OutputStream outputStream = Files.newOutputStream(filename)) {
            Transformer transformer = transFactory.newTransformer();
            transformer.transform(new DOMSource(((Node) fileType.getAny())), new StreamResult(outputStream));
        } catch (TransformerException e) {
            throw new MoribusException(String.format("Unable to write file '%s'.", fileType.getFilename()), e);
        }
    }
}
