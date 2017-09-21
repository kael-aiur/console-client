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

package net.bingosoft.console.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import net.bingosoft.console.client.support.ConsoleOutputStream;

import java.io.PrintStream;

/**
 * @author kael.
 */
public class DebuggerController {

    private @FXML TextArea consoleWriter;
    
    public void init(){
        ConsoleOutputStream cos = new ConsoleOutputStream(System.out,consoleWriter);
        System.setOut(new PrintStream(cos));
    }

    public void clearDebugger(ActionEvent actionEvent) {
        consoleWriter.clear();
    }
}
