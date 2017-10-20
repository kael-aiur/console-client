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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import leap.core.annotation.Bean;
import leap.core.annotation.Inject;
import leap.lang.New;
import leap.lang.Strings;
import leap.lang.json.JSON;
import net.bingosoft.console.client.model.ESQuery;
import net.bingosoft.console.client.service.ESClient;
import net.bingosoft.console.client.support.Logger;
import net.bingosoft.console.client.support.PrimaryStateSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kael.
 */
@Bean
public class CallStatistics implements Tester {

    protected @Inject Logger log;
    protected @Inject ESClient client;
    protected @Inject PrimaryStateSupplier supplier;
    
    protected TextField account;
    protected TextField password;
    protected TextField host;
    protected TextField port;
    protected ChoiceBox<String> scheme;


    @Override
    public String getName() {
        return "API调用统计";
    }

    @Override
    public <T extends Event> void runTest(T e) {
        ESClient es = null;
        try {
            String a = account.getText();
            String p = password.getText();
            if (Strings.isEmpty(a) && Strings.isEmpty(p)) {
                a = ESClient.user;
                p = ESClient.password;
            }
            String s = scheme.getValue();
            String h = host.getText();
            int o = Integer.parseInt(port.getText());
            es = client.newClient(h, o, s, a, p);
            Map<String, Object> res = es.query(ESQuery.statisticAll());
            log.debug("========json start========");
            log.debug(JSON.encode(res));
            log.debug("========json end========");
            List<Data> datas = convert(res);
            refreshCharts(datas);
        } catch (Exception exception) {
            exception.printStackTrace();
            log.info(exception.getMessage());
        } finally {
            if (null != es) {
                es.close();
            }
        }

    }

    private void refreshCharts(List<Data> datas) {
        Stage chartsStage = new Stage();
        chartsStage.setHeight(600);
        chartsStage.setWidth(800);
        chartsStage.initOwner(supplier.get());
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        BarChart<Number, String> barChart = new BarChart<>(y, x);

        XYChart.Series<Number, String> series = new XYChart.Series<>();
        barChart.getData().add(series);

        Scene scene = new Scene(barChart);
        chartsStage.setScene(scene);
        datas.stream().map(data -> {
            XYChart.Data<Number, String> d = new XYChart.Data<>();
            d.setXValue(data.getCount());
            d.setYValue(data.getKey());
            d.setExtraValue(data);
            series.getData().add(d);
            
            StackPane node = (StackPane) d.getNode();
            node.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                System.out.println(node.getHeight());
                System.out.println(node.getWidth());
            });

            node.parentProperty().addListener((ov, oldParent, parent) -> {
                Group parentGroup = (Group) parent;
                parentGroup.getChildren().add(new Text(d.getXValue() + ""));
            });
//            BorderPane bp = new BorderPane();
//            bp.setRight(new Text(d.getXValue() + ""));
//            node.getChildren().add(bp);
            return d;
        }).collect(Collectors.toList());
        chartsStage.show();
    }

    private List<Data> convert(Map<String, Object> res) {
        Map<String, Object> aggs = (Map<String, Object>) res.get("aggregations");
        Map<String, Object> cc = (Map<String, Object>) aggs.get("call_count");
        List<Map<String, Object>> buckets = (List<Map<String, Object>>) cc.get("buckets");
        //log.info("==============================================================");
        //String tmpl = "{0}\t\t\t|{1}\t\t\t|{2}\t\t\t|{3}\t\t\t|";
        //log.info(Strings.format(tmpl,"API编码","客户端ID","调用次数","API总被调用次数")) ;
        List<Data> datas = new ArrayList<>(buckets.size());
        buckets.forEach(m -> {
            String apiName = (String) m.get("key");
            String count = String.valueOf(m.get("doc_count"));
            Data data = new Data();
            data.setKey(apiName);
            data.setCount(Integer.parseInt(count));
            Map<String, Object> caggs = (Map<String, Object>) m.get("aggs");
            List<Map<String, Object>> cb = (List<Map<String, Object>>) caggs.get("buckets");
            if (cb.size() > 0) {
                data.setChildren(new ArrayList<>());
                cb.forEach(cbm -> {
                    String clientId = (String) cbm.get("key");
                    String ccount = String.valueOf(cbm.get("doc_count"));

                    Data child = new Data();
                    child.setKey(clientId);
                    child.setCount(Integer.parseInt(ccount));
                    data.getChildren().add(child);
                    //log.info(Strings.format(tmpl,apiName,clientId,ccount,count));
                });
            } else {
                //log.info(Strings.format(tmpl,apiName,"--","--",count));
            }
            datas.add(data);
        });
        return datas;
    }

    @Override
    public Node getNode() {
        VBox box = new VBox();
        Label label = new Label(getName());
        box.getChildren().add(label);


        FlowPane fp1 = new FlowPane();
        fp1.setHgap(20);
        Label l1 = new Label("日志账号：");
        Label l2 = new Label("日志密码：");
        account = new TextField();
        password = new TextField();

        fp1.getChildren().add(l1);
        fp1.getChildren().add(account);
        fp1.getChildren().add(l2);
        fp1.getChildren().add(password);
        // box.getChildren().add(fp1);

        FlowPane fp2 = new FlowPane();
        fp2.setHgap(20);
        //fp2.setPadding(new Insets(10,5,5,5));

        scheme = new ChoiceBox<>(FXCollections.observableArrayList(New.arrayList("http", "https")));
        scheme.setValue(ESClient.defaultScheme);

        host = new TextField(ESClient.defaultHost);
        port = new TextField(ESClient.defaultPort);

        fp2.getChildren().add(scheme);
        fp2.getChildren().add(host);
        fp2.getChildren().add(port);

        box.getChildren().add(fp2);

        Button button = new Button("统计结果");
        button.addEventHandler(ActionEvent.ACTION, event -> runTest(event));

        box.getChildren().add(button);


        return box;
    }

    private class Data {
        protected String key;
        protected int count;
        protected List<Data> children;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<Data> getChildren() {
            return children;
        }

        public void setChildren(List<Data> children) {
            this.children = children;
        }
    }
}
