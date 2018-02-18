package no.difi.vefa.moribus.processor;

import no.difi.vefa.moribus.Arguments;
import no.difi.vefa.moribus.api.Processor;
import no.difi.vefa.moribus.jaxb.lookup_1.DownloadType;
import no.difi.vefa.moribus.lang.MoribusException;
import no.difi.vefa.moribus.model.Structure;
import no.difi.vefa.moribus.util.JaxbHelper;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
@MetaInfServices
public class DownloadProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadProcessor.class);

    @Override
    public void process(Structure structure, Arguments arguments) throws IOException, MoribusException {
        LOGGER.info("Generate download...");

        Path path = arguments.getTargetFolder().toPath().resolve("api/1.0/domains.xml");

        if (!Files.exists(path.getParent()))
            Files.createDirectories(path.getParent());

        DownloadType downloadType = new DownloadType();
        downloadType.getDomain().addAll(structure.getDomains());
        downloadType.getProfile().addAll(structure.getProfiles());

        try (OutputStream outputStream = Files.newOutputStream(path)) {
            Marshaller marshaller = JaxbHelper.createMarshaller();
            marshaller.marshal(JaxbHelper.OBJECT_FACTORY.createDownload(downloadType), outputStream);
        } catch (JAXBException e) {
            throw new MoribusException(e.getMessage(), e);
        }
    }
}
