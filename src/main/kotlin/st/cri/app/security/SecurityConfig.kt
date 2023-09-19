package st.cri.app.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import st.cri.app.security.jwt.JwtRequestFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(val jwtRequestFilter: JwtRequestFilter) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/swagger-resources/**",
                        "/swagger-ui.html",
                        "/swagger-ui/*",
                        "/v2/api-docs",
                        "/webjars/**",
                        "/public/**").permitAll()
                    .requestMatchers("/authenticate").permitAll()
                    .requestMatchers("/usuarios/activar").permitAll()
                    .requestMatchers("/usuarios/password").permitAll()
                    .requestMatchers("/usuarios/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = mutableListOf("http://localhost:4200", "https://admin-isophar.web.app")
        configuration.setAllowedMethods(mutableListOf("GET", "POST", "OPTIONS"))
        configuration.allowedHeaders = mutableListOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    companion object {
        private val AUTH_WHITELIST = arrayOf(
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/*",
            "/v2/api-docs",
            "/webjars/**",
            "/public/**"
        )
    }
}
