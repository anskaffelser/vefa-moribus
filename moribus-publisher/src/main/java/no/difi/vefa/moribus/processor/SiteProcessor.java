package no.difi.vefa.moribus.processor;

import no.difi.vefa.moribus.Arguments;
import no.difi.vefa.moribus.api.Processor;
import no.difi.vefa.moribus.jaxb.domain_1.DomainType;
import no.difi.vefa.moribus.jaxb.profile_1.ProfileType;
import no.difi.vefa.moribus.model.Structure;
import no.difi.vefa.moribus.util.PathGenerator;
import no.difi.vefa.moribus.util.Thymeleaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author erlend
 */
@Singleton
public class SiteProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteProcessor.class);

    @Inject
    private PathGenerator pathGenerator;

    @Inject
    private Thymeleaf thymeleaf;

    @Override
    public void process(Structure structure, Arguments arguments) throws IOException {
        LOGGER.info("Generate site...");

        homepage(structure, arguments);
        domains(structure, arguments);
        domain(structure, arguments);
        profile(structure, arguments);
    }

    private void homepage(Structure structure, Arguments arguments) throws IOException {
        Context context = new Context();
        context.setVariable("root", "./");
        context.setVariable("pathgen", pathGenerator);

        context.setVariable("domains", structure.getDomains().stream()
                .collect(Collectors.groupingBy(DomainType::getQualifier)));
        thymeleaf.publish("home", arguments.getTargetFolder().toPath().resolve("index.html"), context);
    }

    private void domains(Structure structure, Arguments arguments) throws IOException {
        Context context = new Context();
        context.setVariable("root", "../");
        context.setVariable("pathgen", pathGenerator);

        context.setVariable("domains", structure.getDomains().stream()
                .collect(Collectors.groupingBy(DomainType::getQualifier)));
        thymeleaf.publish("domains", arguments.getTargetFolder().toPath().resolve("domain/index.html"), context);
    }

    private void domain(Structure structure, Arguments arguments) throws IOException {
        Context context = new Context();
        context.setVariable("root", "../../");
        context.setVariable("pathgen", pathGenerator);

        for (DomainType domain : structure.getDomains()) {
            context.setVariable("domain", domain);
            context.setVariable("profiles", structure.getProfiles().stream()
                    .filter(p -> domain.getId().equals(p.getDomain()))
                    .collect(Collectors.toList()));

            thymeleaf.publish("domain", arguments.getTargetFolder().toPath().resolve(pathGenerator.getIndex(domain)), context);
        }
    }

    private void profile(Structure structure, Arguments arguments) throws IOException {
        Context context = new Context();
        context.setVariable("root", "../../../");
        context.setVariable("pathgen", pathGenerator);

        for (ProfileType profile : structure.getProfiles()) {
            DomainType domain = structure.getDomainMap().get(profile.getDomain());

            context.setVariable("profile", profile);
            context.setVariable("domain", domain);

            thymeleaf.publish("profile", arguments.getTargetFolder().toPath().resolve(pathGenerator.getIndex(domain, profile)), context);
        }
    }
}
