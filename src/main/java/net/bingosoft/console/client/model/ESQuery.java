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

package net.bingosoft.console.client.model;

import leap.lang.Charsets;
import leap.lang.New;
import leap.lang.Strings;
import leap.lang.http.ContentTypes;
import leap.lang.json.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kael.
 */
public abstract class ESQuery {

    public abstract String getEndpoint();

    public abstract HttpEntity getEntity();
    
    public static ESQuery statisticAll(){
        return new StatisticAll();
    }
    
    public static class StatisticAll extends ESQuery{
        @Override
        public String getEndpoint() {
            return "/trace-log-*/_search";
        }

        @Override
        public HttpEntity getEntity() {
            Map<String,Object> body = new HashMap<>();
            body.put("query",query());
            body.put("aggs",aggs());
            return EntityBuilder.create().setContentEncoding(Charsets.UTF_8_NAME)
                    .setContentType(ContentType.create(ContentTypes.APPLICATION_JSON))
                    .setBinary(Strings.getBytes(JSON.encode(body), Charsets.UTF_8_NAME))
                    .build();
        }

        private Map<String, Object> query(){
            Map<String,Object> query = new HashMap<>();
            
            Map<String,Object> bool = new HashMap<>();
            Map<String,Object> filter = new HashMap<>();
            Map<String,Object> filterBool = new HashMap<>();
            List<Map<String,Object>> filterBoolMust = New.arrayList();
            
            query.put("bool",bool);
            bool.put("filter",filter);
            filter.put("bool",filterBool);
            filterBool.put("must",filterBoolMust);
            
            filterBoolMust.add(New.hashMap("term",New.hashMap("component","apigateway")));
            filterBoolMust.add(New.hashMap("term",New.hashMap("span.kind","server")));
            
            return query;
        }
        private Map<String, Object> aggs(){
            Map<String,Object> aggs = new HashMap<>();

            Map<String,Object> callCount = new HashMap<>();
            Map<String,Object> callCountTerms = new HashMap<>();
            Map<String,Object> callCountAggs = new HashMap<>();
            Map<String,Object> callCountAggsClientCount = new HashMap<>();
            Map<String,Object> callCountAggsClientCountTerms = new HashMap<>();
            
            aggs.put("call_count",callCount);
            
            callCount.put("terms",callCountTerms);
            callCount.put("aggs",callCountAggs);

            callCountAggs.put("aggs",callCountAggsClientCount);
            
            callCountTerms.put("field","tags.service.name");
            callCountTerms.put("size","10000");
            
            callCountAggsClientCount.put("terms",callCountAggsClientCountTerms);

            callCountAggsClientCountTerms.put("field","tags.auth.client_id");
            callCountAggsClientCountTerms.put("size","10000");
            
            return aggs;
        }
        
        
    }

}
