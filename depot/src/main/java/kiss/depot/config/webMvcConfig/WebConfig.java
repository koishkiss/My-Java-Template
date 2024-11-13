package kiss.depot.config.webMvcConfig;

import kiss.depot.model.constant.STATIC.VALUE;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
                        "/error",
                        VALUE.img_web+"**",
                        VALUE.video_web+"**",
                        VALUE.audio_web+"**"
                );  //填入免于检查的请求
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //配置资源映射：设置虚拟路径，访问绝对路径下资源：访问 http://localhost:8080/avatar/xxx.png访问D:/javaDesign/photo/avatar下的资源
        registry.addResourceHandler(VALUE.img_web+"**")
                .addResourceLocations("file:" + VALUE.img_local);

        registry.addResourceHandler(VALUE.video_web+"**")
                .addResourceLocations("file:"+VALUE.video_local);

        registry.addResourceHandler(VALUE.audio_web+"**")
                .addResourceLocations("file:"+VALUE.audio_local);
    }

}
