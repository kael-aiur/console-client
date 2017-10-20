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

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import leap.core.AppContext;
import net.bingosoft.console.client.model.Client;
import net.bingosoft.console.client.model.User;
import net.bingosoft.console.client.service.ConsoleClient;
import net.bingosoft.console.client.support.ClientConverter;
import net.bingosoft.console.client.support.Logger;
import net.bingosoft.console.client.support.UserConverter;
import net.bingosoft.console.client.tests.Tester;

import java.io.IOException;

/**
 * @author kael.
 */
public class MainController {
    
    protected AppContext app;
    private ConsoleClient console;
    private Stage debugger;
    
    private @FXML VBox mainVBox;
    private @FXML ChoiceBox<User> userSelector;
    private @FXML ChoiceBox<Client> clientSelector;
    private @FXML TextArea logConsole;
    
    private @FXML TextField atTextField;
    
    public void init(){
        console = app.getBeanFactory().getBean(ConsoleClient.class);
        userSelector.addEventHandler(ActionEvent.ACTION,console);
        clientSelector.addEventHandler(ActionEvent.ACTION,console);

        app.getBeanFactory().getBean(Logger.class).setAppendable(new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                logConsole.appendText(csq.toString());
                return this;
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                logConsole.appendText(csq.subSequence(start,end).toString());
                return this;
            }

            @Override
            public Appendable append(char c) throws IOException {
                logConsole.appendText(c+"");
                return this;
            }
        });
        
        UserConverter uc = app.getBeanFactory().getBean(UserConverter.class);
        ClientConverter cc = app.getBeanFactory().getBean(ClientConverter.class);
        User u = uc.getAllUser()[0];
        Client c = cc.getAllClient()[0];
        userSelector.setConverter(uc);
        userSelector.setItems(FXCollections.observableArrayList(uc.getAllUser()));

        clientSelector.setConverter(cc);
        clientSelector.setItems(FXCollections.observableArrayList(cc.getAllClient()));
        
        userSelector.setValue(u);
        clientSelector.setValue(c);

        app.getBeanFactory().getBeans(Tester.class).forEach(tester -> {
            Node n = tester.getNode();
            if(null != n){
                Separator separator = new Separator();
                separator.setMinHeight(10);
                mainVBox.getChildren().add(separator);
                mainVBox.getChildren().add(tester.getNode());
            }
        });
        
    }
    
    public void clearInfo(){
        logConsole.clear();
    }
    
    public void showDebugger(){
        debugger.show();
    }
    
    public AppContext getApp() {
        return app;
    }

    public void setApp(AppContext app) {
        this.app = app;
    }

    public ChoiceBox getUserSelector() {
        return userSelector;
    }

    public void setUserSelector(ChoiceBox userSelector) {
        this.userSelector = userSelector;
    }

    public void getAccessToken(ActionEvent actionEvent) {
        atTextField.setText(console.getAccessToken());
    }

    public Stage getDebugger() {
        return debugger;
    }

    public void setDebugger(Stage debugger) {
        this.debugger = debugger;
    }
}
