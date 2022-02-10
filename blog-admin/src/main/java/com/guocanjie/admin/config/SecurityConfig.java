package com.guocanjie.admin.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


//    加密策略
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
//        测试加密策略 对guocanjie字符串进行加密输出
        String guocanjie = new BCryptPasswordEncoder().encode("guocanjie");
        System.out.println(guocanjie);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * 重点重写方法,用于拦截,类似于MVC的拦截
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() //开启登录认证
//                .antMatchers("/user/findAll").hasRole("admin") //访问接口需要admin的角色
                .antMatchers("/css/**").permitAll()  // 表示/css/下的所有路径不需要登陆认证
                .antMatchers("/img/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/plugins/**").permitAll()
                .antMatchers("/pages/**").authenticated()      // 表示/page/下的所有路径,认证通过后可以访问
                .antMatchers("/admin/**").access("@authService.auto(request,authentication)") //自定义service 来去实现实时的权限认证
                .and().formLogin()
                .loginPage("/login.html") //自定义的登录页面
                .loginProcessingUrl("/login") //登录处理接口
                .usernameParameter("username") //定义登录时的用户名的key 默认为username
                .passwordParameter("password") //定义登录时的密码key，默认是password
                .defaultSuccessUrl("/pages/main.html") //登陆成功后的默认页面
               .failureUrl("/login.html")
                .permitAll() //通过 不拦截，更加前面配的路径决定，这是指和登录表单相关的接口 都通过
                .and().logout() //退出登录配置
                .logoutUrl("/logout") //退出登录接口
                .logoutSuccessUrl("/login.html")
                .permitAll() //退出登录的接口放行
                .and()
                .httpBasic()
                .and()
                .csrf().disable() //csrf关闭 如果自定义登录 需要关闭
                .headers().frameOptions().sameOrigin();
    }
}
