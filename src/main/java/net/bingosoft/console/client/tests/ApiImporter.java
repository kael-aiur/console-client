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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import leap.core.annotation.Inject;
import net.bingosoft.console.client.support.Logger;
import net.bingosoft.console.client.support.PrimaryStateSupplier;

import java.io.File;
import java.io.IOException;

/**
 * @author kael.
 */
public class ApiImporter implements Tester {
    
    protected @Inject Logger log;
    
    private @Inject PrimaryStateSupplier supplier;
    
    private @FXML Button chooseFile;
    private @FXML TextField chooseFileText;
    
    private final FileChooser chooser = new FileChooser();
    
    @Override
    public String getName() {
        return "API导入";
    }

    @Override
    public <T extends Event> void runTest(T e) {
        
    }

    @Override
    public Node getNode() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/ui/importer.fxml"));
        try {
            loader.setController(this);
            VBox p = loader.load();
            chooser.setTitle("选择需要导入的文件");
            chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("All Files(*)","*.xls","*.xlsx","*.csv"));
            
            chooseFile.addEventHandler(ActionEvent.ACTION,event -> {
                chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
                File f = chooser.showOpenDialog(supplier.get());
                if (null != f){
                    log.info("选中文件:"+f.getAbsolutePath());
                    chooseFileText.setText(f.getAbsolutePath());
                }
            });
            return p;
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return null;
    }
}
