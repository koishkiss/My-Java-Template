package kiss.depot.model.constant;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*
* 全局静态变量提供类
* 目前将yml文件里提取的值也静态注入到这个类中，当这个值可能在多个地方引用
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/3
* */

public class STATIC {

    private STATIC() {}


    //用于提供yml文件中的各种value（仅多处使用时添加）
    @Component
    public static class VALUE {
        @PostConstruct
        @SuppressWarnings("unused")
        public void init() {
            web_path = WEB_PATH;

            default_avatar = DEFAULT_AVATAR_NAME;
            default_photo = DEFAULT_PHOTO_NAME;

            img_web = IMAGE_WEB_PATH;
            img_local = IMAGE_LOCAL_PATH;

            video_web = VIDEO_WEB_PATH;
            video_local = VIDEO_LOCAL_PATH;

            audio_web = AUDIO_WEB_PATH;
            audio_local = AUDIO_LOCAL_PATH;

            page_size = DEFAULT_PAGE_SIZE;
            max_page_size = MAX_PAGE_SIZE;
            min_page_size = MIN_PAGE_SIZE;
            scan_page_size = DEFAULT_SCAN_PAGE_SIZE;
            min_scan_page_size = MIN_SCAN_PAGE_SIZE;
            max_scan_page_size = MAX_SCAN_PAGE_SIZE;

            expire = EXPIRE;
            secret = SECRET;
        }

        @Value("${path.web-path}")
        String WEB_PATH;
        public static String web_path;  //网址

        @Value("${path.default-avatar-name}")
        String DEFAULT_AVATAR_NAME;
        public static String default_avatar;  //默认头像名

        @Value("${path.default-photo-name}")
        String DEFAULT_PHOTO_NAME;
        public static String default_photo;  //默认图片名

        @Value("${path.image-web-path}")
        String IMAGE_WEB_PATH;
        public static String img_web;

        @Value("${path.image-local-path}")
        String IMAGE_LOCAL_PATH;
        public static String img_local;

        @Value("${path.video-web-path}")
        String VIDEO_WEB_PATH;
        public static String video_web;

        @Value("${path.video-local-path}")
        String VIDEO_LOCAL_PATH;
        public static String video_local;

        @Value("${path.audio-web-path}")
        String AUDIO_WEB_PATH;
        public static String audio_web;

        @Value("${path.audio-local-path}")
        String AUDIO_LOCAL_PATH;
        public static String audio_local;


        //默认页面大小
        @Value("${page.default-page-size:15}")
        int DEFAULT_PAGE_SIZE;
        public static int page_size;

        //最大页面大小
        @Value("${page.max-page-size:30}")
        int MAX_PAGE_SIZE;
        public static int max_page_size;

        //最小页面大小
        @Value("${page.min-page-size:5}")
        int MIN_PAGE_SIZE;
        public static int min_page_size;

        //默认扫描页面数
        @Value("${page.default-scan-page-size:5}")
        int DEFAULT_SCAN_PAGE_SIZE;
        public static int scan_page_size;

        //最大扫描页面数
        @Value("${page.max-scan-page-size:10}")
        int MAX_SCAN_PAGE_SIZE;
        public static int max_scan_page_size;

        //最小扫描页面数
        @Value("${page.min-scan-page-size:2}")
        int MIN_SCAN_PAGE_SIZE;
        public static int min_scan_page_size;


        //jwt过期时间
        @Value("${jwt.expire:604800000}")
        long EXPIRE;
        public static long expire;

        //jwt密钥
        @Value("${jwt.secret:the-default-secret}")
        String SECRET;
        public static String secret;
    }
}
