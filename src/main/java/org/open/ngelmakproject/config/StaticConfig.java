package org.open.ngelmakproject.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticConfig implements WebMvcConfigurer {

  @Value("${nk.file.upload-directory.location}")
  private String fileStorageLocation;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    /**
     * Any request that arrives at [endPoint]/public/** will be mapped to
     * /content/public/
     * Note that classpath here is by default src/main/resources.
     */
    String root = this.fileStorageLocation.replace("./", "");
    String pattern = String.format("/%s/**", root); // -> /<nk.file.upload-directory.location>/**
    String path = String.format("file:./%s/", root); // -> file:./<nk.file.upload-directory.location>/
    registry.addResourceHandler(pattern).addResourceLocations(path)
    .setCacheControl(CacheControl.maxAge(48, TimeUnit.HOURS));

  }
}