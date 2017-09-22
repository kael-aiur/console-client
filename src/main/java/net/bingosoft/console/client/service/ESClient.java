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

package net.bingosoft.console.client.service;

import leap.core.BeanFactory;
import leap.core.ioc.PostCreateBean;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

/**
 * @author kael.
 */
public class ESClient implements PostCreateBean{
    
    private RestClient restClient;

    @Override
    public void postCreate(BeanFactory factory) throws Throwable {
        
        
        
        restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
    }
}
