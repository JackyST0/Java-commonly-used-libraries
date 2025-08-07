package com.example.testspringboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/2/10 16:15
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${custom.swagger.basePackage:}")
    private String basePackage;
    @Value("${custom.swagger.title:}")
    private String title;
    @Value("${custom.swagger.description:}")
    private String description;
    @Value("${custom.swagger.serviceUrl:}")
    private String serviceUrl;
    @Value("${custom.swagger.version:}")
    private String version;

    @Bean
    public Docket createRestApi() {
        return (new Docket(DocumentationType.SWAGGER_2))
                .apiInfo(this.apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage(this.basePackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return (new ApiInfoBuilder())
                .title(this.title)
                .description(this.description)
                .termsOfServiceUrl(this.serviceUrl)
                .version(this.version).build();
    }
}
