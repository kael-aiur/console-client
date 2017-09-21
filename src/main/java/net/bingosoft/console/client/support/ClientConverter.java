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

package net.bingosoft.console.client.support;

import javafx.util.StringConverter;
import leap.core.BeanFactory;
import leap.core.annotation.Bean;
import leap.core.annotation.Inject;
import leap.core.ioc.PostCreateBean;
import net.bingosoft.console.client.model.Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kael.
 */
@Bean
public class ClientConverter extends StringConverter<Client> implements PostCreateBean {
    
    protected Map<String,Client> clients = new HashMap<>();
    protected @Inject(id="clients") List<Map<String,String>> clientsList;
    
    @Override
    public String toString(Client object) {
        return object.getName();
    }

    @Override
    public Client fromString(String string) {
        return clients.get(string);
    }

    @Override
    public void postCreate(BeanFactory factory) throws Throwable {
        clientsList.forEach(map -> {
            String name = map.get("name");
            String clientId = map.get("clientId");
            String secret = map.get("secret");
            Client c = new Client();
            c.setName(name);
            c.setClientId(clientId);
            c.setSecret(secret);
            clients.put(name,c);
        });
    }
    public Client[] getAllClient(){
        return clients.entrySet().stream().map(entry -> entry.getValue()).toArray(value -> new Client[value]);
    }
}
