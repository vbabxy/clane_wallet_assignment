package com.clane.walletservice.config;

import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.TypeNameProviderPlugin;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Optional;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.clane.walletservice"))
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Wallet Service")
                .version("1.0.0")
                .build();
    }

    @Component
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
    public static class FullyQualifiedTypeNameProvider implements TypeNameProviderPlugin {

        @Override
        public String nameFor(Class<?> type) {
            return Optional.ofNullable(AnnotationUtils.findAnnotation(type, ApiModel.class))
                    .map(ApiModel::value)
                    .map(Strings::emptyToNull)
                    .orElseGet(type::getName);
        }

        @Override
        public boolean supports(DocumentationType delimiter) {
            return true;
        }
    }
}
