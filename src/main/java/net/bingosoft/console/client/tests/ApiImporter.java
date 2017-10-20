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

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import leap.core.annotation.Bean;
import leap.core.annotation.Inject;
import leap.lang.Charsets;
import leap.lang.json.JSON;
import leap.lang.json.JsonObject;
import net.bingosoft.console.client.model.RestApi;
import net.bingosoft.console.client.service.ConsoleClient;
import net.bingosoft.console.client.support.Logger;
import net.bingosoft.console.client.support.PrimaryStateSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author kael.
 */
@Bean
public class ApiImporter implements Tester {
    
    protected @Inject Logger log;
    
    private @Inject PrimaryStateSupplier supplier;
    private @Inject ConsoleClient console;
    
    @Override
    public String getName() {
        return "API导入";
    }

    @Override
    public <T extends Event> void runTest(T e) {
        JsonObject record = readImportFile();
        String json = JSON.encode(record.raw());
        console.importApi(json);
    }

    public <T extends Event> void runDel(T e){
        JsonObject record = readImportFile();
        List<Map<String, Object>> importApis = record.getList("apis");
        importApis.forEach(map -> {
            String filters = "name eq "+map.get("name");
            log.info("find api:" + filters);
            List<RestApi> apis = console.queryApi(filters);
            log.info("find " + apis.size() + " api.");
            apis.forEach(api -> console.deleteApi(api.getId()));
            log.info("delete " + apis.size() + " api.");
        });
    }
    
    public <T extends Event> void importScope(T e){
        String apiName = "qweqweqe";
        String filters = "name eq "+apiName;
        log.info("find api:" + filters);
        List<RestApi> qweApi = console.queryApi(filters);
        log.info("find " + qweApi.size() + " api.");
        qweApi.forEach(api -> console.deleteApi(api.getId()));
        log.info("delete " + qweApi.size() + " api.");
        log.info("create api " + console.createApi(apiName).getName() + " api.");
        JsonObject scopes = readScopeFile();
        String json = JSON.encode(scopes.raw());
        console.importApi(json);
    }
    
    private JsonObject readImportFile(){
        try (InputStream apiIs = this.getClass().getResourceAsStream("/json/api.json");
             InputStream swaggerIs = this.getClass().getResourceAsStream("/json/swagger.json");
             InputStreamReader apiIsr = new InputStreamReader(apiIs, Charsets.UTF_8_NAME);
             InputStreamReader swaggerIsr = new InputStreamReader(swaggerIs,Charsets.UTF_8_NAME)){
            JsonObject spec = JSON.parse(apiIsr).asJsonObject();
            List<Map<String, Object>> swaggers = spec.getList("swaggers");
            Object swagger = JSON.decode(swaggerIsr);
            swaggers.forEach(m -> m.put("swagger",swagger));
            return spec;
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return null;
        }
    }
    
    private JsonObject readScopeFile(){
        try (InputStream apiIs = this.getClass().getResourceAsStream("/json/scopes.json");
             InputStreamReader apiIsr = new InputStreamReader(apiIs, Charsets.UTF_8_NAME)){
            JsonObject scopes = JSON.parse(apiIsr).asJsonObject();
            return scopes;
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return null;
        }
    }
    
    @Override
    public Node getNode() {
        FlowPane fp = new FlowPane();
        Label label = new Label(getName());
        fp.setHgap(20);
        Button run = new Button("导入API");
        Button del = new Button("删除导入的API");
        Button scope = new Button("导入授权");
        run.addEventHandler(ActionEvent.ACTION, this::runTest);
        del.addEventHandler(ActionEvent.ACTION, this::runDel);
        scope.addEventHandler(ActionEvent.ACTION, this::importScope);
        fp.getChildren().add(label);
        fp.getChildren().add(run);
        fp.getChildren().add(del);
        fp.getChildren().add(scope);
        return fp;
    }
    
    
    
}
