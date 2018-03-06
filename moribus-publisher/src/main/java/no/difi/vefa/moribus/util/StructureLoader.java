package no.difi.vefa.moribus.util;

import no.difi.vefa.moribus.Arguments;
import no.difi.vefa.moribus.jaxb.domain_1.DomainType;
import no.difi.vefa.moribus.jaxb.domain_1.DomainsType;
import no.difi.vefa.moribus.jaxb.profile_1.ProfileType;
import no.difi.vefa.moribus.jaxb.profile_1.ProfilesType;
import no.difi.vefa.moribus.model.Structure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

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

    public Structure load(Arguments arguments) throws IOException {
        List<Path> paths = Files.walk(arguments.getSourceFolder().toPath())
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".xml"))
                .map(Path::toAbsolutePath)
                .filter(p -> !p.toString().startsWith(arguments.getTargetFolder().toPath().toAbsolutePath().toString()))
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
            }
        } catch (JAXBException e) {
            LOGGER.warn(e.getMessage(), e);
        }

        return null;
    }
}
