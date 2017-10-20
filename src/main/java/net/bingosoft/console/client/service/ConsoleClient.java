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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import leap.core.annotation.Bean;
import leap.core.annotation.Inject;
import leap.lang.Strings;
import leap.lang.convert.Converts;
import leap.lang.http.ContentTypes;
import leap.lang.http.HTTP;
import leap.lang.http.Headers;
import leap.lang.http.client.HttpClient;
import leap.lang.http.client.HttpRequest;
import leap.lang.http.client.HttpResponse;
import leap.lang.json.JSON;
import leap.lang.json.JsonObject;
import net.bingosoft.console.client.model.Client;
import net.bingosoft.console.client.model.RestApi;
import net.bingosoft.console.client.model.ServerConfig;
import net.bingosoft.console.client.model.User;
import net.bingosoft.console.client.support.Logger;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author kael.
 */
@Bean
public class ConsoleClient implements EventHandler<ActionEvent> {

    private @Inject Logger log;
    
    public String getAccessToken(){
        return getAccessToken(selectedUser,selectedClient);
    }
    
    public String getAccessToken(User u, Client c){
        String url = config.getSso()+"/oauth2/token";
        HttpRequest request = httpClient.request(url);
        request.addFormParam("grant_type","password");
        request.addFormParam("username", u.getLoginId());
        request.addFormParam("password", u.getPassword());
        request.addFormParam("client_id", c.getClientId());
        request.addFormParam("client_secret", c.getSecret());
        
        String send = Strings.format("\nPOST {0} \nparams:grant_type={1}&username={2}&password={3}&client_id={4}&client_secret={5}",
                url,"password",u.getLoginId(),u.getPassword(),c.getClientId(),c.getSecret());
        log.debug(send);
        HttpResponse response = request.post();
        int status = response.getStatus();
        String content = request.post().getString();
        String resp = Strings.format("\nstatus:{0}\ncontent:{1}",status,content);
        log.debug(resp);
        Map<String, Object> map = JSON.decodeMap(content);
        String token = Objects.toString(map.get("access_token"));
        return token;
    }
    
    public RestApi createApi(String name){
        String json = "{" +
                "\"enableExplorer\":true," +
                "\"visibility\":\"Public\"," +
                "\"secLevel\":\"Public\"," +
                "\"name\":\"qweqweqe\"," +
                "\"title\":\"sfdsfdsdf\"," +
                "\"orgId\":null," +
                "\"personLiable\":null," +
                "\"regionId\":\"default_region\"," +
                "\"type\":\"Rest\"," +
                "\"webserviceApi\":null," +
                "\"tagTitles\":null," +
                "\"contacts\":null," +
                "\"phoneNum\":null," +
                "\"email\":null," +
                "\"provider\":null," +
                "\"version\":\"1.0.0\"" +
                "}";
        Map<String, Object> api = JSON.decodeMap(json);
        api.put("title","UI客户端创建API");
        api.put("name", name);
        HttpResponse response = consoleReq("/api")
                .setContentType(ContentTypes.APPLICATION_JSON_UTF8)
                .setBody(JSON.encode(api)).post();
        if(!response.is2xx()){
            throw new RuntimeException(response.getStatus() + ":" + response.getString());
        }
        Map<String, Object> map = JSON.decodeMap(response.getString());
        return new RestApi(map);
    }
    
    public void deleteApi(String apiId){
        HttpResponse response = consoleReq("/api/"+apiId).setMethod(HTTP.Method.DELETE).send();
        if(!response.is2xx()){
            throw new RuntimeException(response.getStatus() + ":" + response.getString());
        }
    }
    
    public List<RestApi> queryApi(String filters){
        try {
            HttpResponse response = consoleReq("/api")
                    .setMethod(HTTP.Method.GET).addQueryParam("filters",filters).send();
            return JSON.parse(response.getString()).asJsonArray().asList().stream()
                    .map(o -> new RestApi((Map<String, Object>) o)).collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            log.debug(e.getMessage());
            throw e;
        }
    }
    
    public void importApi(String body){
        HttpResponse response = consoleReq("/apiimport").setMethod(HTTP.Method.POST)
                .addHeader(Headers.CONTENT_TYPE,ContentTypes.APPLICATION_JSON_UTF8)
                .setBody(body).send();
        log.debug(response.getStatus() + ":" + response.getString());
    }
    
    private @Inject ServerConfig config;
    private @Inject HttpClient httpClient;
    private User selectedUser;
    private Client selectedClient;
    
    private HttpRequest consoleReq(String uri){
        return request(config.getConsole()+uri);
    }
    
    private HttpRequest request(String url){
        String at = getAccessToken();
        return httpClient.request(url).addHeader("Authorization","Bearer "+at);
    }
    
    @Override
    public void handle(ActionEvent event) {
        if(event.getSource() instanceof ChoiceBox){
            Object v = ((ChoiceBox) event.getSource()).getValue();
            if(v instanceof User){
                selectedUser = (User) v;
            }else if(v instanceof Client){
                selectedClient = (Client)v;
            }
        }
    }
}
