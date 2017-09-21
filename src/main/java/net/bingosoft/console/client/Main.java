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

package net.bingosoft.console.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import leap.core.AppContext;
import net.bingosoft.console.client.controller.DebuggerController;
import net.bingosoft.console.client.controller.MainController;

/**
 * @author kael.
 */
public class Main extends Application {

    protected static AppContext app;
    
    public static void main(String[] args) {
        app = AppContext.initStandalone();
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/ui/main.fxml"));
        FXMLLoader debuggerLoader = new FXMLLoader(getClass().getResource("/ui/debugger.fxml"));
        Parent main = mainLoader.load();
        Parent debugger = debuggerLoader.load();
        MainController controller = mainLoader.getController();
        DebuggerController debuggerController = debuggerLoader.getController();
        controller.setApp(app);
        controller.init();
        debuggerController.init();
        
        Scene dscene = new Scene(debugger);
        Stage debugStage = new Stage();
        debugStage.setScene(dscene);
        debugStage.setTitle("调试控制台");
        controller.setDebugger(debugStage);
        
        Scene scene = new Scene(main);
        stage.setTitle("Console客户端测试工具");
        stage.setScene(scene);
        stage.show();
    }
}
