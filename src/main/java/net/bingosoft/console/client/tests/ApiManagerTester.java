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

import javafx.event.Event;
import leap.core.annotation.Bean;
import leap.core.annotation.Inject;
import net.bingosoft.console.client.model.RestApi;
import net.bingosoft.console.client.service.ConsoleClient;
import net.bingosoft.console.client.support.Logger;

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
        log.info("创建API");
        RestApi api = client.createApi();
        log.info("创建API成功："+api.getId());
        log.info("删除API："+api.getId());
        client.deleteApi(api.getId());
        log.info("删除API成功："+api.getId());
    }
}
