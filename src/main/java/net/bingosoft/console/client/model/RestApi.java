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

import java.util.Map;

/**
 * @author kael.
 */
public class RestApi {
    
    private final Map<String, Object> map;

    public RestApi(Map<String, Object> map) {
        this.map = map;
    }

    public String getId() {
        return (String) map.get("id");
    }

    public String getTitle(){
        return (String)map.get("title");
    }

    public String getName() {
        return (String) map.get("name");
    }
}
