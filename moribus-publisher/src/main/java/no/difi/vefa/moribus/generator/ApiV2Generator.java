package no.difi.vefa.moribus.generator;

import com.google.inject.Singleton;
import no.difi.vefa.moribus.annotation.Step;
import no.difi.vefa.moribus.annotation.Order;
import no.difi.vefa.moribus.lang.MoribusException;

import java.io.IOException;

/**
 * @author erlend
 */
@Singleton
@Order(1000)
@Step("api-v2")
public class ApiV2Generator extends AbstractXsltGenerator {

    @Override
    public void perform() throws IOException, MoribusException {
        generate("/xslt/site-api-v2.xslt", "generated/site-api-v2.xml");
    }
}
