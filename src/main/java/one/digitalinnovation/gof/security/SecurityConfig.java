package one.digitalinnovation.gof.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.Customizer;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/api/**").hasRole("ADMIN") // Permite somente ADMIN
                                .anyRequest().authenticated() // Outras requisições precisam estar autenticadas
                )
                .formLogin(); // Habilita o login padrão
    }
}
