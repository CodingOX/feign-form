
package feign.form.feign.spring;

import feign.codec.Decoder;
import feign.form.spring.converter.SpringManyMultipartFilesReader;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 多个文件下载的接口
 */
@FeignClient(
        name = "multipart-download-support-service",
        url = "http://localhost:8080",
        configuration = DownloadClient.ClientConfiguration.class
)
public interface DownloadClient {

    @RequestMapping(
            value = "/multipart/download/{fileId}",
            method = GET
    )
    MultipartFile[] download(@PathVariable("fileId") String fileId);

    /**
     * 多文件下载feign的配置
     */
    class ClientConfiguration {
        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        public Decoder feignDecoder() {
            //获取原本的 Convert
            List<HttpMessageConverter<?>> springConverters = messageConverters.getObject().getConverters();
            //准备构建新的Convert
            List<HttpMessageConverter<?>> decoderConverters = new ArrayList<>(springConverters.size() + 1);
            decoderConverters.addAll(springConverters);
            //添加新的 多文件下载 解析器
            decoderConverters.add(new SpringManyMultipartFilesReader(4096));
            //通过构造函数传入
            HttpMessageConverters httpMessageConverters = new HttpMessageConverters(decoderConverters);
            //Bingo
            return new SpringDecoder(new ObjectFactory<HttpMessageConverters>() {
                @Override
                public HttpMessageConverters getObject() {
                    return httpMessageConverters;
                }
            });
        }
    }
}
