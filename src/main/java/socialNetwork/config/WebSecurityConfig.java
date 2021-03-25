package socialNetwork.config;


import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import socialNetwork.db.User;
import socialNetwork.repo.UserDetailsRepo;

import java.time.LocalDateTime;

// указываем что это конфигурации и расширяем WebSecurityConfigurerAdapter
@Configuration
// нужны для аутетификации
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // указываем куда можно ходить без авторизации
        http
//                .authorizeRequests()
//                .mvcMatchers("/").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .csrf().disable();
             // для работы с визуальной частью
             .antMatcher("/**")
                    .authorizeRequests()
                    .antMatchers("/",
                            "/login**",
                            "/js/**",
                            "/error**").permitAll()
                    .anyRequest().authenticated()
                    // для выхода (логаут)
                    .and().logout().logoutSuccessUrl("/").permitAll()
                    .and()
                    .csrf().disable();
    }

    // для сохранения авторизованного пользователя в базу данных
    @Bean
    public PrincipalExtractor principalExtractor(UserDetailsRepo userDetailsRepo) {
        return map -> {
            // запрашиваем айди у гугла
            String id = (String) map.get("sub");
            // ищем пользователя и если не находим - то .orElseGet
            User user = userDetailsRepo.findById(id).orElseGet(() -> {
                // если его нет то возвращем нового юзера
                User newUser = new User();
                // заполняем поля юзера данными пришедшими с гугла
                newUser.setId(id);
                newUser.setName((String) map.get("name"));
                newUser.setEmail((String) map.get("email"));
                newUser.setGender((String) map.get("gender"));
                newUser.setLocale((String) map.get("locale"));
                newUser.setUserpic((String) map.get("picture"));
                // и возвращаем нового юзера
                return newUser;
            });
            // устанавливаем время последнего визита пользователя
            user.setLastVisit(LocalDateTime.now());
            // сохраняем
            return userDetailsRepo.save(user);
        };
    }
}
