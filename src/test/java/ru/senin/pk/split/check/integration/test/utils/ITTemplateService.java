package ru.senin.pk.split.check.integration.test.utils;

import freemarker.template.*;
import lombok.SneakyThrows;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

public class ITTemplateService {

    private Configuration configuration;

    public ITTemplateService() {
        this.configuration = new Configuration(new Version(2, 3, 31));
        this.configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        this.configuration.setLocale(Locale.US);
        this.configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        this.configuration.setClassForTemplateLoading(getClass(), "/integration_test/request_templates/");
    }

    @SneakyThrows
    public String resolve(String templateName, Map<String, Object> input) {
        StringWriter stringWriter = new StringWriter();
        Template template = this.configuration.getTemplate(templateName);
        template.process(input, stringWriter);
        return stringWriter.toString();
    }
}
