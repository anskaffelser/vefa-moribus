package no.difi.vefa.moribus.util;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
public class Thymeleaf {

    private static final TemplateEngine ENGINE;

    static {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");

        ENGINE = new TemplateEngine();
        ENGINE.setTemplateResolver(resolver);
        ENGINE.addDialect(new LayoutDialect());
    }

    public static void publish(String template, Path path, Context context) throws IOException {
        if (!Files.exists(path.getParent()))
            Files.createDirectories(path.getParent());

        try (Writer writer = Files.newBufferedWriter(path)) {
            ENGINE.process(template, context, writer);
        }
    }
}
