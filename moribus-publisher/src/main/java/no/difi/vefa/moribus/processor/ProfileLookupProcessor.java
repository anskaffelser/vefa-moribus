package no.difi.vefa.moribus.processor;

import no.difi.vefa.moribus.api.Processor;
import no.difi.vefa.moribus.jaxb.domain_1.DomainType;
import no.difi.vefa.moribus.jaxb.lookup_1.ProfileLookupType;
import no.difi.vefa.moribus.jaxb.profile_1.ProfileType;
import no.difi.vefa.moribus.lang.MoribusException;
import no.difi.vefa.moribus.model.Structure;
import no.difi.vefa.moribus.util.JaxbHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
@Singleton
public class ProfileLookupProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileLookupProcessor.class);

    @Inject
    @Named("target")
    private Path targetFolder;

    @Override
    public void process(Structure structure) throws IOException, MoribusException {
        LOGGER.info("Generate profile lookup...");

        Path path = targetFolder.resolve("api/1.0/profile");

        if (!Files.exists(path))
            Files.createDirectories(path);

        for (ProfileType profile : structure.getProfiles()) {
            ProfileLookupType profileLookupType = new ProfileLookupType();

            DomainType domain = structure.getDomainMap().get(profile.getDomain());
            profileLookupType.setDomain(domain);
            profileLookupType.setProfile(profile);

            try (OutputStream outputStream = Files.newOutputStream(path.resolve(String.format("%s.xml", profile.getId())))) {
                Marshaller marshaller = JaxbHelper.createMarshaller();
                marshaller.marshal(JaxbHelper.OBJECT_FACTORY.createProfileLookup(profileLookupType), outputStream);
            } catch (JAXBException e) {
                throw new MoribusException(e.getMessage(), e);
            }
        }
    }
}
