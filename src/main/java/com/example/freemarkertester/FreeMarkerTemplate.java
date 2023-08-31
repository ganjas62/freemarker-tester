package com.example.freemarkertester;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;

public class FreeMarkerTemplate {

  private Configuration configuration;

  public FreeMarkerTemplate() {
    Configuration configuration = new Configuration(
        freemarker.template.Configuration.VERSION_2_3_28);
    configuration.setDefaultEncoding("UTF-8");
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    configuration.setLogTemplateExceptions(false);
    configuration.setWrapUncheckedExceptions(true);
    DefaultObjectWrapper defaultObjectWrapper = new DefaultObjectWrapper(
        freemarker.template.Configuration.VERSION_2_3_28);
    defaultObjectWrapper.setIterableSupport(true);
    configuration.setObjectWrapper(defaultObjectWrapper);
    this.configuration = configuration;
  }

  public Template getTemplate(String templateBody) throws IOException {
    return new Template(null, templateBody, configuration);
  }
}
