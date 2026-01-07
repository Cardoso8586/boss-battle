package com.boss_battle.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

import com.boss_battle.service.CustomUserDetailsService;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8080",
                "https://boss-battle.up.railway.app"
                
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Cache-Control",
                "Content-Type"
        ));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/static/**",
                        "/images/**",
                        "/icones/**",
                        "/audio/**",
                        "/templates",
                        "/css/**",
                        "/js/**",
                        "/api/boss/active",
                        "/api/boss/hit/**",
                        "/login/**",
                        "/cadastro/**",
                        "/aliados/**",
                        "/perfil/**",
                        "/index.html",
                        "/dashboard/**",
                        "/api/auth/cadastro/**",
                        "/api/auth/login/**",
                        "/api/saldoBossCoin/**",
                        "/api/boss/cooldown/**",
                        "/api/boss/guerreiros/ultimo-dano/**",
                        "/api/boss/ataques-recentes/**",
                        "/recarregar-energia/**",
                        "/api/atualizar/status/usuario/**",
                        "/comprar/guerreiro/**",
                        "/comprar/ataque/**",
                        "/comprar/energia/**",
                        "/comprar/ataque-especial/**",
                        "/comprar/espada/flanejante/**",
                        "/api/loja/**",
                        "/claim-referidos/**",
                        "/api/pocao-vigor/ativar/**",
                        "/api/atualizar/status/ajustes/**",
                        "/comprar/pocao-vigor/**",
                        "/api/rewards/preview/**",
                        "/equipar/guerreiro/**",
                        "/equipar/retaguarda/**",
                        "/retirar/ataque/**",
                        "/retirar/retaguarda/**",
                        "/retaguarda/reparo/**",
                        "/api/boss/ranking/dano/**",
                        "/api/espada-flanejante/ativar/**"
                     
                        
                        
                       
                        
                        
                        
                        
                       
                ).permitAll()
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard", true)
                    .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            )

            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
