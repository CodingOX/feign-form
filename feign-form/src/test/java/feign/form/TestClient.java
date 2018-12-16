/*
 * Copyright 2018 Artem Labazin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package feign.form;

import feign.*;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Artem Labazin
 */
public interface TestClient {

    //POST方式下 Feign通过 urlencoded的方式传递参数
    @RequestLine("POST /form")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Response form(@Param("key1") String key1, @Param("key2") String key2);

    //上传文件的同时，传递id和其他参数
    @RequestLine("POST /upload/{id}")
    @Headers("Content-Type: multipart/form-data")
    String upload(@Param("id") Integer id, @Param("public") Boolean isPublic, @Param("file") File file);

    //传递文件
    @RequestLine("POST /upload")
    @Headers("Content-Type: multipart/form-data")
    String upload(@Param("file") File file);

    //本来就支持？
    @RequestLine("POST /json")
    @Headers("Content-Type: application/json")
    String json(Dto dto);

    //POST支持map
    @RequestLine("POST /query_map")
    String queryMap(@QueryMap Map<String, Object> value);

    //多文件上传1
    @RequestLine("POST /upload/files")
    @Headers("Content-Type: multipart/form-data")
    String uploadWithArray(@Param("files") File[] files);

    //多文件上传2
    @RequestLine("POST /upload/files")
    @Headers("Content-Type: multipart/form-data")
    String uploadWithList(@Param("files") List<File> files);

    //多文件上传3
    @RequestLine("POST /upload/files")
    @Headers("Content-Type: multipart/form-data")
    String uploadWithManyFiles(@Param("files") File file1, @Param("files") File file2);

    //文件上传+JSON
    @RequestLine("POST /upload/with_json")
    @Headers("Content-Type: multipart/form-data")
    Response uploadWithJson(@Param("dto") Dto dto, @Param("file") File file);

    //
    @RequestLine("POST /upload/unknown_type")
    @Headers("Content-Type: multipart/form-data")
    String uploadUnknownType(@Param("file") File file);

    //FormData格式的数据，可以是自定义
    @RequestLine("POST /upload/form_data")
    @Headers("Content-Type: multipart/form-data")
    String uploadFormData(@Param("file") FormData formData);
}
