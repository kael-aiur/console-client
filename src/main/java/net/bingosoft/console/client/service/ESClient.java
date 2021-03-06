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

import leap.lang.Charsets;
import leap.lang.New;
import leap.lang.Strings;
import leap.lang.codec.Base64;
import leap.lang.http.HTTP;
import leap.lang.json.JSON;
import net.bingosoft.console.client.model.ESQuery;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author kael.
 */
public class ESClient{
    
    private final RestClient restClient;
    
    public static final String user = "elastic";
    public static final String password = "8rmc3ZJ9yr";
    
    public static final String defaultScheme = "http";
    public static final String defaultHost = "10.201.76.136";
    public static final String defaultPort = "9200";

    public ESClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public Map<String,Object> query(ESQuery query){
        try {
            Response resp = restClient.performRequest(HTTP.Method.GET.name(),query.getEndpoint(), New.hashMap(),query.getEntity());
            Map<String,Object> res;
            try (InputStream is = resp.getEntity().getContent();
                 InputStreamReader bis = new InputStreamReader(is, Charsets.UTF_8_NAME);
                 BufferedReader br = new BufferedReader(bis)){
                res = JSON.decode(br);
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void close(){
        try {
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static ESClient newClient(String host, int port, String scheme, String account, String password){
        String basic = Base64.encode(Strings.format("{0}:{1}",account,password));
        String header = Strings.format("Basic {0}",basic);
        ESClient client = new ESClient(
            RestClient.builder(new HttpHost(host, port, scheme))
            .setDefaultHeaders(new Header[]{new BasicHeader("Authorization",header)})
            .build()
        );
        return client;
    }
    
}
