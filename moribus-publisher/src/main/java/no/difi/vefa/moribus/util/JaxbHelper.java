package no.difi.vefa.moribus.util;

import no.difi.vefa.moribus.jaxb.domain_1.DomainType;
import no.difi.vefa.moribus.jaxb.domain_1.DomainsType;
import no.difi.vefa.moribus.jaxb.lookup_1.*;
import no.difi.vefa.moribus.jaxb.profile_1.ProfileType;
import no.difi.vefa.moribus.jaxb.profile_1.ProfilesType;
import no.difi.vefa.moribus.lang.MoribusException;

import javax.inject.Singleton;
import javax.xml.bind.*;

/**
 * Class making available some much used XML resources.
 *
 * @author erlend
 */
@Singleton
public class JaxbHelper {

    private static final JAXBContext JAXB_CONTEXT;

    public static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(DomainLookupType.class, DownloadType.class,
                    ProfileLookupType.class, RoleLookupType.class, DomainType.class, DomainsType.class,
                    ProfileType.class, ProfilesType.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static Unmarshaller createUnmarshaller() throws JAXBException {
        return JAXB_CONTEXT.createUnmarshaller();
    }

    public static Marshaller createMarshaller() throws MoribusException, JAXBException {
        try {
            Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
            /*
            marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new NamespacePrefixMapper() {
                @Override
                public String getPreferredPrefix(String s, String s1, boolean b) {
                    switch (s) {
                        case "urn:fdc:difi.no:2018:vefa:moribus:Domain-1":
                            return "d";
                        case "urn:fdc:difi.no:2018:vefa:moribus:Lookup-1":
                            return "";
                        case "urn:fdc:difi.no:2018:vefa:moribus:Profile-1":
                            return "p";
                        default:
                            return s1;
                    }
                }
            });
            */
            return marshaller;
        } catch (PropertyException e) {
            throw new MoribusException(e.getMessage(), e);
        }
    }
}
