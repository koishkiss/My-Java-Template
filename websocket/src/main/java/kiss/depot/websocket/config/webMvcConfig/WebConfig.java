package kiss.depot.websocket.config.webMvcConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
* 拦截器配置
* 静态资源映射配置
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/1
* */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        registry.addInterceptor(new Interceptor())
                .addPathPatterns("/**")  //填入要检查的请求
                .excludePathPatterns(
                        "/favicon.ico",
                        "/error"
                );  //填入免于检查的请求
    }

}
