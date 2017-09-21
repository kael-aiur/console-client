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

/**
 * @author kael.
 */
public interface Tester {
    
    String getName();
    
    <T extends Event> void runTest(T e);
    
    default Node getNode(){
        FlowPane fp = new FlowPane();
        Label label = new Label(getName());
        fp.setHgap(20);
        Button button = new Button("运行测试");
        button.addEventHandler(ActionEvent.ACTION, event -> runTest(event));
        fp.getChildren().add(label);
        fp.getChildren().add(button);
        return fp;
    }
    
}
