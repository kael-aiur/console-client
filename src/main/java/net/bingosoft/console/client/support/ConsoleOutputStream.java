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

import javafx.scene.control.TextArea;
import leap.lang.Arrays2;
import leap.lang.Strings;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kael.
 */
public class ConsoleOutputStream extends OutputStream {
    
    private final PrintStream ps;
    private final TextArea c;
    private final List<Byte> sw = new LinkedList<>();
    
    public ConsoleOutputStream(PrintStream ps,TextArea c) {
        this.ps = ps;
        this.c =c;
    }

    @Override
    public void write(int b) throws IOException {
        ps.write(b);
        synchronized (sw){
            sw.add((byte) b);
            if(b == '\n'){
                byte[] bs = new byte[sw.size()];
                for(int i = 0; i < bs.length; i++){
                    bs[i] = sw.get(i);
                }
                c.appendText(Strings.newStringUtf8(bs));
                sw.clear();
            }
        }
    }
}
