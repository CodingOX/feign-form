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

package feign.form.feign.spring;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static feign.form.util.CharsetUtil.UTF_8;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

/**
 * 测试Spring Web项目非常方便啊,通过该方式在Test的时候启动web项目进行测试<br>
 * 侧面思考下：是否可以通过feign来模拟客户端做测试了？
 *
 * @author Tomasz Juchniewicz <tjuchniewicz@gmail.com>
 * @since 22.08.2016
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = DEFINED_PORT,
        classes = Server.class,
        properties = {
                "server.port=8080",
                "feign.hystrix.enabled=false"
        }
)
public class SpringFormEncoderTest {

    @Autowired
    private Client client;

    @Test
    public void upload1Test() throws Exception {
        val folder = "test_folder";
        val file = new MockMultipartFile("file", "test".getBytes(UTF_8));
        val message = "message test";

        val response = client.upload1(folder, file, message);

        Assert.assertEquals(new String(file.getBytes()) + ':' + message + ':' + folder, response);
    }

    @Test
    public void upload2Test() throws Exception {
        val folder = "test_folder";
        val file = new MockMultipartFile("file", "test".getBytes(UTF_8));
        val message = "message test";

        String response = client.upload2(file, folder, message);

        Assert.assertEquals(new String(file.getBytes()) + ':' + message + ':' + folder, response);
    }

    @Test
    public void uploadFileNameAndContentTypeTest() throws Exception {
        val folder = "test_folder";
        val file = new MockMultipartFile(
                "file",
                "hello.dat",
                "application/octet-stream",
                "test".getBytes(UTF_8)
        );
        val message = "message test";

        val response = client.upload3(file, folder, message);

        Assert.assertEquals(file.getOriginalFilename() + ':' + file.getContentType() + ':' + folder, response);
    }

    @Test
    public void upload4Test() throws Exception {
        val map = new HashMap<Object, Object>();
        map.put("one", 1);
        map.put("two", 2);

        val userName = "popa";
        val id = "42";

        val response = client.upload4(id, map, userName);

        Assert.assertEquals(userName + ':' + id + ':' + map.size(), response);
    }
}
