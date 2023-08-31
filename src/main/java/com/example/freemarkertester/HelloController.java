package com.example.freemarkertester;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class HelloController {

  @FXML
  public TextArea textAreaTemplate;

  @FXML
  public TextArea logTextArea;

  private Stage stage;

  public HelloController() {
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  public void onButtonLoadTemplateFileClicked() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new ExtensionFilter("FreeMarker Template", "*.ftl"));
    File file = fileChooser.showOpenDialog(stage);
    if (file != null) {
      String template = null;
      try {
        template = Files.readString(file.toPath(), StandardCharsets.UTF_8);
      } catch (IOException e) {
        logTextArea.setText(logTextArea.getText() + "\n" + ExceptionUtils.getStackTrace(e));
      }
      textAreaTemplate.setText(template);
    }
  }
}