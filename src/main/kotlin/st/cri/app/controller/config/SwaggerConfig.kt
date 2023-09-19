package st.cri.app.controller.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .securityContexts(listOf(securityContext()))
            .securitySchemes(java.util.List.of<SecurityScheme>(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.basePackage("st.cri.app.controller.rest"))
            .paths(PathSelectors.any())
            .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
            "BaseApplication",
            "",
            "1.0",
            "Terms of service",
            Contact("Candoit", "https://candoit.com.ar", "soporte@candoit.com.ar"),
            "License of API",
            "API license URL", emptyList()
        )
    }

    private fun apiKey(): ApiKey {
        return ApiKey("JWT", "Authorization", "header")
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder().securityReferences(defaultAuth()).build()
    }

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return java.util.List.of(SecurityReference("JWT", authorizationScopes))
    }
}
