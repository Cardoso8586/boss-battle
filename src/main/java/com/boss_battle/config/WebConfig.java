package com.boss_battle.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Banners
        registry.addResourceHandler("/uploads/banners/**")
                .addResourceLocations("file:uploads/banners/");

        // Ícones estáticos
        registry.addResourceHandler("/icones/**")
                .addResourceLocations("classpath:/static/icones/");

        // ✅ Placas RTX
        registry.addResourceHandler("/uploads/placas/rtx/**")
                .addResourceLocations("file:uploads/placas/rtx/");
    }
}
