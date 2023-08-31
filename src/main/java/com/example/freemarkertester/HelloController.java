package com.example.freemarkertester;

import freemarker.core.ParseException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HelloController {

  private static final Logger log = LoggerFactory.getLogger(HelloController.class);

  @FXML
  public TextArea textAreaTemplate;

  @FXML
  public TextArea textAreaPayload;

  @FXML
  public TextArea logTextArea;

  @FXML
  public TextArea textAreaResult;

  @FXML
  public Label executeResultLabel;

  @FXML
  public Button buttonExecute;

  private Stage stage;
  private FreeMarkerTemplate freeMarkerTemplate;

  public HelloController() {
    this.freeMarkerTemplate = new FreeMarkerTemplate();
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  public void onButtonLoadTemplateFileClicked() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new ExtensionFilter("FreeMarker Template", "*.ftl"));
    loadFileAndSetText(fileChooser, textAreaTemplate);
  }

  private void loadFileAndSetText(FileChooser fileChooser, TextArea textArea) {
    File file = fileChooser.showOpenDialog(stage);
    if (file != null) {
      String template = null;
      try {
        template = Files.readString(file.toPath(), StandardCharsets.UTF_8);
      } catch (Exception e) {
        handleException(e);
      }
      textArea.setText(template);
      updateExecuteButtonDisabled();
    }
  }

  private void handleException(Exception e) {
    log.error("Exception occurred", e);
    logTextArea.setText(logTextArea.getText() + "\n" + ExceptionUtils.getStackTrace(e));
  }

  @FXML
  public void onButtonLoadPayloadFileClicked() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new ExtensionFilter("Payload File", "*.json"));
    loadFileAndSetText(fileChooser, textAreaPayload);
  }

  @FXML
  public void onButtonExecuteClicked() {
    try {
      StringWriter stringWriter = new StringWriter();
      Template template = freeMarkerTemplate.getTemplate(textAreaTemplate.getText());
      template.process(new JSONObject(textAreaPayload.getText()), stringWriter);
      String result = stringWriter.toString();
      textAreaResult.setText(result);
      validateJson(result);
    } catch (ParseException e) {
      handleTemplateError("There's an exception when parsing template", e.getColumnNumber(), e);
    } catch (TemplateException e) {
      handleTemplateError("There's an exception when processing template", e.getColumnNumber(), e);
    } catch (Exception e) {
      executeResultLabel.setText("There's an exception");
      executeResultLabel.setTextFill(Paint.valueOf("red"));
      handleException(e);
    }
  }

  private void handleTemplateError(String message, int columNumber, Exception e) {
    executeResultLabel.setText(message);
    executeResultLabel.setTextFill(Paint.valueOf("red"));
    handleException(e);
    textAreaTemplate.requestFocus();
    textAreaTemplate.positionCaret(columNumber);
  }

  private void validateJson(String json) {
    try {
      new JSONObject(json);
      executeResultLabel.setText("Json result is valid");
      executeResultLabel.setTextFill(Paint.valueOf("green"));
    } catch (JSONException e) {
      executeResultLabel.setText("Json result is not valid");
      executeResultLabel.setTextFill(Paint.valueOf("red"));
    }
  }

  @FXML
  public void onButtonCopyResultClicked() {
    setClipboardContent(textAreaResult.getText());
  }

  private void setClipboardContent(String textContent) {
    Clipboard clipboard = Clipboard.getSystemClipboard();
    ClipboardContent content = new ClipboardContent();
    content.putString(textContent);
    clipboard.setContent(content);
  }

  @FXML
  public void updateExecuteButtonDisabled() {
    boolean disable = StringUtils.isBlank(textAreaTemplate.getText()) ||
        StringUtils.isBlank(textAreaPayload.getText());
    buttonExecute.setDisable(disable);
  }

  @FXML
  public void minifyTemplate() {
    textAreaTemplate.setText(new Minify().minify(textAreaTemplate.getText()));
  }

  @FXML
  public void copyTemplateToClipboard() {
    setClipboardContent(textAreaTemplate.getText());
  }

  @FXML
  public void copyTemplateEscapedToClipboard() {
    setClipboardContent(StringEscapeUtils.escapeJava(textAreaTemplate.getText()));
  }

  @FXML
  public void clearLog() {
    logTextArea.clear();
  }
}