package no.difi.vefa.moribus.generator;

import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import no.difi.vefa.moribus.annotation.Order;
import no.difi.vefa.moribus.lang.MoribusException;

import java.io.IOException;

/**
 * @author erlend
 */
@Singleton
@Slf4j
@Order(2000)
public class HomepageGenerator extends AbstractXsltGenerator {

    @Override
    public void perform() throws IOException, MoribusException {
        log.info("Generate homepage");
        generate("/xslt/site-web.xslt", "generated/site-web.xml");
    }
}
