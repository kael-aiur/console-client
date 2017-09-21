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
import net.bingosoft.console.client.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kael.
 */
@Bean
public class UserConverter extends StringConverter<User> implements PostCreateBean {

    protected Map<String,User> users = new HashMap<>();
    protected @Inject(id="users") List<Map<String,String>> usersList;
    
    @Override
    public String toString(User object) {
        return object.getName();
    }

    @Override
    public User fromString(String string) {
        return users.get(string);
    }

    @Override
    public void postCreate(BeanFactory factory) throws Throwable {
        usersList.forEach(map -> {
            String name = map.get("name");
            String loginId = map.get("loginId");
            String password = map.get("password");
            User u = new User();
            u.setName(name);
            u.setLoginId(loginId);
            u.setPassword(password);
            users.put(name,u);
        });
    }
    public User[] getAllUser(){
        return users.entrySet().stream().map(entry -> entry.getValue()).toArray(value -> new User[value]);
    }
}
