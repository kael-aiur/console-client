/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.bingosoft.console.client.tests;

import javafx.application.Platform;
import javafx.event.Event;
import leap.core.annotation.Bean;
import leap.core.annotation.Inject;
import net.bingosoft.console.client.model.RestApi;
import net.bingosoft.console.client.service.ConsoleClient;
import net.bingosoft.console.client.support.Logger;

import java.util.List;
import java.util.UUID;

/**
 * @author kael.
 */
@Bean
public class ApiManagerTester implements Tester {
    
    protected @Inject ConsoleClient client;
    protected @Inject Logger log;
    
    @Override
    public String getName() {
        return "测试API增删改查";
    }

    @Override
    public <T extends Event> void runTest(T e) {
        //test();
        deleteAll();
    }
    
    private void test(){
        log.info("创建API");
        RestApi api = client.createApi(UUID.randomUUID().toString());
        log.info("创建API成功："+api.getId());
        log.info("删除API："+api.getId());
        client.deleteApi(api.getId());
        log.info("删除API成功："+api.getId());
    }
    
    private void deleteAll(){
        List<RestApi> apis = client.queryApi("name like '%%'");
        log.info("一共"+apis.size()+"个API");
        apis.forEach(api -> {
            try {
                log.info("开始删除API："+api.getName());
                client.deleteApi(api.getId());
                log.info("删除API："+api.getName()+"成功");
            }catch (Exception e1){
                e1.printStackTrace();
                log.info("删除API："+api.getName()+"出错："+e1.getMessage());
            }
        });
    }
}
