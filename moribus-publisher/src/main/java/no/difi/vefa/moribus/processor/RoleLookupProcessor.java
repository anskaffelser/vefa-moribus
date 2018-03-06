package no.difi.vefa.moribus.processor;

import no.difi.vefa.moribus.Arguments;
import no.difi.vefa.moribus.api.Processor;
import no.difi.vefa.moribus.jaxb.domain_1.DomainType;
import no.difi.vefa.moribus.jaxb.lookup_1.RoleLookupType;
import no.difi.vefa.moribus.jaxb.profile_1.ProfileType;
import no.difi.vefa.moribus.jaxb.profile_1.RoleType;
import no.difi.vefa.moribus.lang.MoribusException;
import no.difi.vefa.moribus.model.Structure;
import no.difi.vefa.moribus.util.JaxbHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class RoleLookupProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleLookupProcessor.class);

    @Override
    public void process(Structure structure, Arguments arguments) throws IOException, MoribusException {
        LOGGER.info("Generate role lookup...");

        Path path = arguments.getTargetFolder().toPath().resolve("api/1.0/role");

        if (!Files.exists(path))
            Files.createDirectories(path);

        for (ProfileType profile : structure.getProfiles()) {
            for (RoleType role : profile.getRole()) {

                RoleLookupType roleLookupType = new RoleLookupType();

                roleLookupType.setRole(role);

                DomainType domain = structure.getDomainMap().get(profile.getDomain());
                roleLookupType.setDomain(domain);
                roleLookupType.setProfile(profile);

                try (OutputStream outputStream = Files.newOutputStream(path.resolve(String.format("%s.xml", role.getId())))) {
                    Marshaller marshaller = JaxbHelper.createMarshaller();
                    marshaller.marshal(JaxbHelper.OBJECT_FACTORY.createRoleLookup(roleLookupType), outputStream);
                } catch (JAXBException e) {
                    throw new MoribusException(e.getMessage(), e);
                }
            }
        }
    }
}
