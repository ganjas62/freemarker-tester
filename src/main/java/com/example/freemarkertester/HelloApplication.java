package com.example.freemarkertester;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    URL resource = HelloApplication.class.getResource("hello-view.fxml");
    System.out.println(resource);
    FXMLLoader fxmlLoader = new FXMLLoader(resource);
    Scene scene = new Scene(fxmlLoader.load(), 600, 500);
    HelloController helloController = fxmlLoader.getController();
    helloController.setStage(stage);
    stage.setTitle("Freemarker Tester");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}