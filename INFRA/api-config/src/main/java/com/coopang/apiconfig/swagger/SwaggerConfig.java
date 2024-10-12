package com.coopang.apiconfig.swagger;

import static com.coopang.apiconfig.constants.HeaderConstants.HEADER_USER_ID;
import static com.coopang.apiconfig.constants.HeaderConstants.HEADER_USER_ROLE;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecuritySchemes({
        @SecurityScheme(
                name = "bearerAuth",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT"
        )
})
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Springdoc 테스트")
                .description("Springdoc을 사용한 Swagger UI 테스트")
                .version("1.0.0");
    }

    @Bean
    public OpenApiCustomizer addHeadersToSwagger() {
        return openApi -> openApi.getPaths().forEach((path, pathItem) -> {
            pathItem.readOperations().forEach(operation -> {
                operation.addParametersItem(new Parameter()
                        .in("header")
                        .name(HEADER_USER_ID)
                        .required(false)
                        .description("User ID Header"));

                operation.addParametersItem(new Parameter()
                        .in("header")
                        .name(HEADER_USER_ROLE)
                        .required(false)
                        .description("User Role Header"));
            });
        });
    }
}
