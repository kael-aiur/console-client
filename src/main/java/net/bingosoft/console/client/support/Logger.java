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

import leap.core.annotation.Bean;

import java.io.IOException;

/**
 * @author kael.
 */
@Bean
public class Logger {
    private Appendable appendable = System.out;
    public void log(String log){
        try {
            appendable.append(log);
            appendable.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setAppendable(Appendable appendable) {
        this.appendable = appendable;
    }
}