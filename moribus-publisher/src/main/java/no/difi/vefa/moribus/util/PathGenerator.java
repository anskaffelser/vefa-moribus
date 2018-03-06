package no.difi.vefa.moribus.util;

import com.google.inject.Singleton;
import no.difi.vefa.moribus.jaxb.domain_1.DomainType;
import no.difi.vefa.moribus.jaxb.profile_1.ProfileType;

/**
 * @author erlend
 */
@Singleton
public class PathGenerator {

    public String get(DomainType domain) {
        return String.format("domain/%s/", part(domain));
    }

    public String get(DomainType domain, ProfileType profile) {
        return String.format("domain/%s/%s/", part(domain), part(profile));
    }

    public String getIndex(DomainType domain) {
        return String.format("domain/%s/index.html", part(domain));
    }

    public String getIndex(DomainType domain, ProfileType profile) {
        return String.format("domain/%s/%s/index.html", part(domain), part(profile));
    }

    private String part(DomainType domain) {
        return String.format("%s-%s",
                domain.getQualifier().toLowerCase().replace(" ", "-"),
                domain.getTitle().toLowerCase().replace(" ", "-")
        );
    }

    private String part(ProfileType profile) {
        return profile.getTitle().toLowerCase()
                .replaceAll("[^a-z0-9]", "-")
                .replaceAll("[\\-]{2,}?", "-")
                .replaceAll("[\\-]$", "");
    }
}
