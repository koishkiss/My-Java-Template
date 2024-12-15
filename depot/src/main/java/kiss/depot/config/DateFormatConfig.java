package kiss.depot.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/*
* 日期反序列化配置类
* author: 网上找的忘了是谁
* launch: 2024/11/1
* last update: 2024/12/5
* */

@JsonComponent
public class DateFormatConfig {

    //从yml文件中spring.jackson.date-format的位置获取值
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    protected String pattern;

    @Value("${spring.jackson.time-zone:GMT+8}")
    protected String time_zone;

    // Date 类型全局时间格式化
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilder() {

        return builder -> {
            // 时间格式设置
            DateFormat df = new SimpleDateFormat(pattern);
            // 时区设置
            df.setTimeZone(TimeZone.getTimeZone(time_zone));
            builder.failOnEmptyBeans(false)
                    .failOnUnknownProperties(false)
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .dateFormat(df);
        };
    }

    // LocalDate 类型全局时间格式化
    @Bean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
    }
}
