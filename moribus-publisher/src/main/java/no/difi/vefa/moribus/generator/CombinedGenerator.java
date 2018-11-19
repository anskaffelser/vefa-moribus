package no.difi.vefa.moribus.generator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import no.difi.vefa.moribus.annotation.Order;
import no.difi.vefa.moribus.api.Generator;
import no.difi.vefa.moribus.jaxb.v2.GroupType;
import no.difi.vefa.moribus.jaxb.v2.IncludeType;
import no.difi.vefa.moribus.jaxb.v2.ObjectFactory;
import no.difi.vefa.moribus.lang.MoribusException;
import no.difi.vefa.moribus.util.Arguments;
import no.difi.vefa.moribus.util.SaxonUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author erlend
 */
@Slf4j
@Singleton
@Order(0)
public class CombinedGenerator implements Generator {

    private static final JAXBContext JAXB_CONTEXT;

    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    @Inject
    private Arguments arguments;

    @Inject
    private SaxonUtil saxon;

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(GroupType.class, IncludeType.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void perform() throws IOException, MoribusException {
        log.info("Combine source files");
        try (OutputStream outputStream = Files.newOutputStream(arguments.getTarget("all.xml"))) {
            saxon.perform("/xslt/combine.xslt", prepareIncludes(), outputStream);
        }
    }

    private InputStream prepareIncludes() throws IOException, MoribusException {
        List<IncludeType> includes = Files.walk(arguments.getSource())
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".xml"))
                .map(p -> {
                    IncludeType includeType = new IncludeType();
                    includeType.setValue(p.toString().replaceAll("\\\\", "/"));
                    return includeType;
                })
                .collect(Collectors.toList());

        GroupType groupType = new GroupType();
        groupType.getDomainOrSubDomainOrProcess().addAll(includes);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
            marshaller.marshal(OBJECT_FACTORY.createGroup(groupType), baos);

            return new ByteArrayInputStream(baos.toByteArray());
        } catch (JAXBException e) {
            throw new MoribusException("Unable to create virtual document with includes.", e);
        }
    }
}
