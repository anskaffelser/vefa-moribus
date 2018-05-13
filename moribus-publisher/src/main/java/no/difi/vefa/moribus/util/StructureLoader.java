package no.difi.vefa.moribus.util;

import no.difi.vefa.moribus.jaxb.domain_1.DomainType;
import no.difi.vefa.moribus.jaxb.domain_1.DomainsType;
import no.difi.vefa.moribus.jaxb.profile_1.ProfileType;
import no.difi.vefa.moribus.jaxb.profile_1.ProfilesType;
import no.difi.vefa.moribus.jaxb.transportprofile_1.TransportProfileType;
import no.difi.vefa.moribus.jaxb.transportprofile_1.TransportProfilesType;
import no.difi.vefa.moribus.model.Structure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Loads metadata from files found in source folder provided as argument.
 *
 * @author erlend
 */
@Singleton
public class StructureLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(StructureLoader.class);

    @Inject
    @Named("source")
    private Path sourceFolder;

    @Inject
    @Named("target")
    private Path targetFolder;

    public Structure load() throws IOException {
        List<Path> paths = Files.walk(sourceFolder)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".xml"))
                .map(Path::toAbsolutePath)
                .filter(p -> !p.toString().startsWith(targetFolder.toAbsolutePath().toString()))
                .collect(Collectors.toList());

        List<Object> objects = new ArrayList<>();
        for (Path path : paths) {
            Object o = parse(path);
            if (o != null)
                objects.add(o);
        }

        // Extract profiles from lists of profiles.
        new ArrayList<>(objects).stream()
                .filter(ProfilesType.class::isInstance)
                .map(ProfilesType.class::cast)
                .forEach(profiles -> {
                    objects.remove(profiles);
                    objects.addAll(profiles.getProfile());
                });

        // Extract domains from lists of domains.
        new ArrayList<>(objects).stream()
                .filter(DomainsType.class::isInstance)
                .map(DomainsType.class::cast)
                .forEach(domains -> {
                    objects.remove(domains);
                    objects.addAll(domains.getDomain());
                });

        // Extract transport profiles from lists of transport profiles.
        new ArrayList<>(objects).stream()
                .filter(TransportProfilesType.class::isInstance)
                .map(TransportProfilesType.class::cast)
                .forEach(transportProfiles -> {
                    objects.remove(transportProfiles);
                    objects.addAll(transportProfiles.getTransportProfile());
                });

        return new Structure(
                objects.stream()
                        .filter(DomainType.class::isInstance)
                        .map(DomainType.class::cast)
                        .sorted(Comparator.comparing(DomainType::getTitle))
                        .collect(Collectors.toList()),
                objects.stream().
                        filter(ProfileType.class::isInstance)
                        .map(ProfileType.class::cast)
                        .sorted(Comparator.comparing(ProfileType::getTitle))
                        .collect(Collectors.toList()),
                objects.stream().
                        filter(TransportProfileType.class::isInstance)
                        .map(TransportProfileType.class::cast)
                        .sorted(Comparator.comparing(TransportProfileType::getTitle))
                        .collect(Collectors.toList())
        );
    }

    /**
     * Parsing of a single provided file.
     *
     * @param path Provided file for parsing.
     * @return Object parsed from file or null if unexpected content where found.
     * @throws IOException Thrown on problems related to reading file.
     */
    private Object parse(Path path) throws IOException {
        LOGGER.info("Parsing '{}'.", path);

        try (InputStream inputStream = Files.newInputStream(path)) {
            Unmarshaller unmarshaller = JaxbHelper.createUnmarshaller();
            Element element = (Element) unmarshaller.unmarshal(new StreamSource(inputStream), Object.class).getValue();

            switch (element.getTagName()) {
                case "Domains":
                    return unmarshaller.unmarshal(element, DomainsType.class).getValue();
                case "Domain":
                    return unmarshaller.unmarshal(element, DomainType.class).getValue();
                case "Profiles":
                    return unmarshaller.unmarshal(element, ProfilesType.class).getValue();
                case "Profile":
                    return unmarshaller.unmarshal(element, ProfileType.class).getValue();
                case "TransportProfiles":
                    return unmarshaller.unmarshal(element, TransportProfilesType.class).getValue();
                case "TransportProfile":
                    return unmarshaller.unmarshal(element, TransportProfileType.class).getValue();
            }
        } catch (JAXBException e) {
            LOGGER.warn(e.getMessage(), e);
        }

        return null;
    }
}
