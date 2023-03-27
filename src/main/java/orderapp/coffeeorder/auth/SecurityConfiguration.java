package orderapp.coffeeorder.auth;

import orderapp.coffeeorder.auth.filter.JwtAuthenticationFilter;
import orderapp.coffeeorder.auth.filter.JwtVerificationFilter;
import orderapp.coffeeorder.auth.handler.MemberAccessDeniedHandler;
import orderapp.coffeeorder.auth.handler.MemberAuthenticationEntryHandler;
import orderapp.coffeeorder.auth.handler.MemberAuthenticationSuccessHandler;
import orderapp.coffeeorder.auth.jwt.JwtTokenizer;
import orderapp.coffeeorder.auth.util.CustomAuthorityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfiguration {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;

    public SecurityConfiguration(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin().and()
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryHandler())
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer()).and()
                .authorizeHttpRequests(getAuthorizeHttpRequestsCustomizer());

        return http.build();
    }

    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> getAuthorizeHttpRequestsCustomizer() {
        return authorize -> authorize
                .antMatchers(HttpMethod.POST, "/members").permitAll()
                .antMatchers(HttpMethod.PATCH, "/members/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/members").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/members/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/members/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/coffees/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/coffees/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/orders").hasRole("USER")
                .antMatchers(HttpMethod.PATCH, "/orders/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/orders").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/orders/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/orders/**").hasRole("USER")
                .antMatchers("/questions/*/answers/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/questions").hasRole("USER")
                .antMatchers(HttpMethod.PATCH, "/questions/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/questions").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/questions/**").hasRole("USER")
                .anyRequest().permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("POST", "GET", "PATCH", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler(jwtTokenizer));
            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);

            builder.addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
        }
    }
}
