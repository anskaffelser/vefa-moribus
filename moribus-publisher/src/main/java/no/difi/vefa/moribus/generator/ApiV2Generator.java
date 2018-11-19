package no.difi.vefa.moribus.generator;

import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import no.difi.vefa.moribus.annotation.Order;
import no.difi.vefa.moribus.lang.MoribusException;

import java.io.IOException;

/**
 * @author erlend
 */
@Slf4j
@Singleton
@Order(1000)
public class ApiV2Generator extends AbstractXsltGenerator {

    @Override
    public void perform() throws IOException, MoribusException {
        log.info("Generate API v2");
        generate("/xslt/site-api-v2.xslt", "generated/site-api-v2.xml");
    }
}
