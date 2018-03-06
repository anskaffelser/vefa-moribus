package no.difi.vefa.moribus.processor;

import com.google.inject.multibindings.Multibinder;
import no.difi.vefa.moribus.api.Processor;
import no.difi.vefa.moribus.guice.MoribusModule;
import org.kohsuke.MetaInfServices;

/**
 * @author erlend
 */
@MetaInfServices
public class ProcessorModule extends MoribusModule {

    @Override
    protected void configure() {
        Multibinder<Processor> processors = Multibinder.newSetBinder(binder(), Processor.class);
        processors.addBinding().to(DomainLookupProcessor.class);
        processors.addBinding().to(DownloadProcessor.class);
        processors.addBinding().to(ProfileLookupProcessor.class);
        processors.addBinding().to(ResourceProcessor.class);
        processors.addBinding().to(RoleLookupProcessor.class);
        processors.addBinding().to(SiteProcessor.class);
    }
}
