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
import leap.lang.json.JSON;
import net.bingosoft.console.client.model.ESQuery;
import net.bingosoft.console.client.service.ESClient;
import net.bingosoft.console.client.support.Logger;

import java.util.Map;

/**
 * @author kael.
 */
@Bean
public class CallStatistics implements Tester {
    
    protected @Inject Logger log;
    protected @Inject ESClient client;
    
    @Override
    public String getName() {
        return "API调用统计";
    }

    @Override
    public <T extends Event> void runTest(T e) {
        Map<String, Object> res = client.query(ESQuery.statisticAll());
        log.info(JSON.encode(res));
    }
}
