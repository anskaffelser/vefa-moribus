package no.difi.vefa.moribus.processor;

import no.difi.vefa.moribus.Arguments;
import no.difi.vefa.moribus.api.Processor;
import no.difi.vefa.moribus.jaxb.domain_1.DomainType;
import no.difi.vefa.moribus.jaxb.lookup_1.DomainLookupType;
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
import java.util.stream.Collectors;

/**
 * @author erlend
 */
@MetaInfServices
public class DomainLookupProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainLookupProcessor.class);

    @Override
    public void process(Structure structure, Arguments arguments) throws IOException, MoribusException {
        LOGGER.info("Generate domain lookup...");

        Path path = arguments.getTargetFolder().toPath().resolve("api/1.0/domain");

        if (!Files.exists(path))
            Files.createDirectories(path);

        for (DomainType domain : structure.getDomains()) {
            DomainLookupType domainLookupType = new DomainLookupType();
            domainLookupType.getDomain().add(domain);
            domainLookupType.getProfile().addAll(structure.getProfiles().stream()
                    .filter(p -> domain.getId().equals(p.getDomain()))
                    .collect(Collectors.toList()));

            try (OutputStream outputStream = Files.newOutputStream(path.resolve(String.format("%s.xml", domain.getId())))) {
                Marshaller marshaller = JaxbHelper.createMarshaller();
                marshaller.marshal(JaxbHelper.OBJECT_FACTORY.createDomainLookup(domainLookupType), outputStream);
            } catch (JAXBException e) {
                throw new MoribusException(e.getMessage(), e);
            }
        }
    }
}
