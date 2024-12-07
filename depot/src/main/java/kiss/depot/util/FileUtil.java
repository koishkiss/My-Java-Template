package kiss.depot.util;

/*
* 上传文件工具类
* 未来可能考虑加上压缩存储
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/1
* */

import kiss.depot.config.exceptionConfig.exceptions.CommonErrException;
import kiss.depot.model.constant.STATIC.VALUE;
import kiss.depot.model.enums.CommonErr;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FileUtil {

    private static final String fileOperatorLock = "/operator/file/";

    //获取文件URL
    public static String getFileURL(String fileName, FileType fileType) {
        return VALUE.web_path + fileType.web_path + fileName;
    }

    //获取文件URL并附带时间戳
    public static String getFileURLWithTimestamp(String fileName, FileType fileType, String timestampName) {
        return VALUE.web_path + fileType.web_path + fileName + "?" + timestampName + "=" + System.currentTimeMillis();
    }

    //从URL中获取文件名
    public static String getFileNameFromURL(String fileURL) {
        int startIndex = fileURL.lastIndexOf("/");
        int endIndex = fileURL.indexOf("?");
        return fileURL.substring(
                startIndex == -1 ? 0 : startIndex + 1,
                endIndex == -1 ? fileURL.length() : endIndex
        );
    }

    //获取文件LocalPath
    public static String getFileLocalPath(String fileName, FileType fileType) {
        return fileType.local_path + fileName;
    }

    //根据原文件后缀生成唯一不重复文件名(使用UUID)
    private static String generateNewFileName(String originalFileName) {
        return UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    //上传文件，返回文件名称
    public static String uploadFile(MultipartFile uploadingFile, FileType fileType) {
        //判断文件不为null
        if (uploadingFile == null) {
            throw CommonErrException.raise(CommonErr.FILE_FORMAT_ERROR.setMsg("不可上传空文件!"));
        }

        //限制文件大小
        if (uploadingFile.getSize() > fileType.max_size) {
            throw CommonErrException.raise(CommonErr.FILE_OUT_OF_LIMIT);
        }

        //获取原文件名
        String originalFileName = uploadingFile.getOriginalFilename();

        //判断原文件格式正确性
        if (originalFileName != null && Arrays.stream(fileType.suffix).anyMatch(originalFileName::endsWith)) {

            //生成新文件名(随机uuid+原文件后缀)
            String newFileName = generateNewFileName(originalFileName);

            //生成新文件路径
            File targetFile = new File(fileType.local_path + newFileName);

            //判断文件父目录是否存在
            if (!targetFile.getParentFile().exists() && !targetFile.getParentFile().mkdirs()) {
                throw new RuntimeException("服务器好像开小差了，请再试试!");
            }

            //保存文件
            try {
                uploadingFile.transferTo(targetFile);
            } catch (IllegalStateException | IOException e) {
                throw new RuntimeException("服务器好像开小差了，请再试试!");
            }

            //返回文件名称
            return newFileName;
        }
        else throw CommonErrException.raise(CommonErr.FILE_FORMAT_ERROR.setMsg("文件格式错误!"));
    }

    //删除文件操作
    public static void removeFile(String fileName, FileType fileType) {
        synchronized ((fileOperatorLock + fileName).intern()) {
            File targetFile = new File(getFileLocalPath(fileName, fileType));

            if (!targetFile.exists()) {
                throw CommonErrException.raise(CommonErr.FILE_OPERATOR_ERR.setMsg("文件不存在!"));
            }

            if (!targetFile.delete()) {
                throw CommonErrException.raise(CommonErr.FILE_OPERATOR_ERR.setMsg("文件删除失败!"));
            }
        }
    }

    //文件类型枚举
    @AllArgsConstructor
    public enum FileType {
        IMAGE(new String[]{
                ".png",
                ".PNG",
                ".jpg",
                ".JPG",
                ".jpeg",
                ".JPEG",
                ".gif",
                ".GIF",
                ".bmp",
                ".BMP"
        },
                5242880L, //图片大小最大限制(5MB)
                VALUE.img_local,
                VALUE.img_web
        ),
        VIDEO(new String[]{
                ".avi",
                ".AVI",
                ".mp4",
                ".MP4",
                ".mkv",
                ".MKV",
                ".wmv",
                ".WMV"
        },
                104857600L, //视频大小最大限制(100MB)
                VALUE.video_local,
                VALUE.video_web
        ),
        AUDIO(new String[]{
                ".mp3",
                ".MP3",
                ".wav",
                ".WAV",
                ".m4a",
                ".M4A",
                ".flac",
                ".FLAC",
                ".Ogg",
                ".ogg"
        },
                10485760L, //音频大小最大限制(10MB)
                VALUE.audio_local,
                VALUE.audio_web
        );

        public final String[] suffix;
        public final long max_size;
        public final String local_path;
        public final String web_path;

    }

}
