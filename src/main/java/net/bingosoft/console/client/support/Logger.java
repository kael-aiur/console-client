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

import javafx.application.Platform;
import leap.core.annotation.Bean;
import leap.lang.Strings;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author kael.
 */
@Bean
public class Logger {
    private Appendable appendable = System.out;
    
    public void debug(String log){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                StackTraceElement[] ses = Thread.currentThread().getStackTrace();
                String se = "";
                for(int i = 0; i < ses.length; i++ ){
                    if(ses[i].getClassName().equals(this.getClass().getName())){
                        StackTraceElement s = ses[i+1];
                        se = s.getClassName()+"."+s.getMethodName()+"(in line:"+s.getLineNumber()+")";
                        break;
                    }
                }
                String t = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String l = Strings.format("{0}-[{1}] DEBUG: {2}",t,se,log);
                System.out.println(l);
            }
        });
    }
    
    public void info(String log){
        debug(log);
        Platform.runLater(() -> {
            try {
                appendable.append(log);
                appendable.append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void setAppendable(Appendable appendable) {
        this.appendable = appendable;
    }
}
