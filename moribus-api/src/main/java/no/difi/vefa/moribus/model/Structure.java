package no.difi.vefa.moribus.model;

import lombok.Getter;
import no.difi.vefa.moribus.jaxb.domain_1.DomainType;
import no.difi.vefa.moribus.jaxb.profile_1.ProfileType;
import no.difi.vefa.moribus.jaxb.profile_1.RoleType;
import no.difi.vefa.moribus.jaxb.transportprofile_1.TransportProfileType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author erlend
 */
@Getter
public class Structure {

    private List<DomainType> domains;

    private List<ProfileType> profiles;

    private List<TransportProfileType> transportProfiles;

    private Map<String, DomainType> domainMap;

    private Map<String, ProfileType> profileMap;

    private Map<String, RoleType> roleMap;

    private Map<String, TransportProfileType> transportProfileTap;

    public Structure(List<DomainType> domains, List<ProfileType> profiles,
                     List<TransportProfileType> transportProfiles) {
        this.domains = domains;
        this.profiles = profiles;
        this.transportProfiles = transportProfiles;

        domainMap = domains.stream()
                .collect(Collectors.toMap(DomainType::getId, Function.identity()));

        profileMap = profiles.stream()
                .collect(Collectors.toMap(ProfileType::getId, Function.identity()));

        roleMap = profiles.stream()
                .map(ProfileType::getRole)
                .flatMap(List::stream)
                .collect(Collectors.toMap(RoleType::getId, Function.identity()));

        transportProfileTap = transportProfiles.stream()
                .collect(Collectors.toMap(TransportProfileType::getId, Function.identity()));
    }
}
