package com.example.dictionaryApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import com.example.googleAPI.TranslatorAPIwithScripts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.stage.Stage;

public class Controller {


    String selectedWord = ""; // từ đang chọn hiện tại
    int selectedWordIndex = 0; // vi tri tu dang chon hien tai trong arraylist


    @FXML
    private ListView<String> searchListView; // listview chứa kết quả từ search bar

    @FXML
    private TextField searchBar; // search bar - ô để nhập từ để tra

    @FXML
    private Label target; // label hiển thị từ đang tra hiện tại

    @FXML
    private TextArea definition; // text area chứa định nghĩa của từ đang tra hiện tại

    @FXML
    private TextArea spelling; // text area chứa phat am của từ đang tra hiện tại

    @FXML
    private TextArea GGAPIText; // text area để nhập câu cần tra

    @FXML
    private TextArea GGAPITranslate; // text area để chứa câu sau khi translate

    @FXML
    private Button eraseButton; // nút xóa từ

    @FXML
    private Button speechButton; // nút phát âm

    @FXML
    private Button editButton; // nút sửa từ

    @FXML
    private Button SaveEditButton; // nút lưu từ khi sửa


    @FXML
    void editButton() { // khi ấn nút sửa từ
        editButton.setDisable(true); // disable nut sửa
        eraseButton.setDisable(true); // disable nút xóa
        if (definition.isEditable()) {
            definition.setEditable(false);
            definition.setBlendMode(BlendMode.DARKEN);
            spelling.setEditable(false);
            spelling.setBlendMode(BlendMode.DARKEN);
        } else {
            definition.setEditable(true);
            definition.setBlendMode(BlendMode.SRC_OVER);
            spelling.setEditable(true);
            spelling.setBlendMode(BlendMode.SRC_OVER);
        }
        SaveEditButton.setVisible(true);
    }

    @FXML
    void SaveEditWord(ActionEvent event) { // khi ấn nút lưu từ
        editButton.setDisable(false);
        SaveEditButton.setVisible(false);
        eraseButton.setDisable(false);

        definition.setEditable(false);
        definition.setBlendMode(BlendMode.DARKEN);
        spelling.setEditable(false);
        spelling.setBlendMode(BlendMode.DARKEN);

        Word word = new Word(selectedWord, spelling.getText(), definition.getText());
        if (DictionaryGUIManagement.editWord(word)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SUCCESS");
            alert.setHeaderText("Edit success : " + selectedWord);
            alert.setContentText("Please press UPDATE to save your change");
            alert.showAndWait();
        }

    }

    @FXML
    void TranslateAPIButton(ActionEvent event) throws IOException { // nut translate
        GGAPITranslate.setText(TranslatorAPIwithScripts.translate("", "vi", GGAPIText.getText()));
    }

    @FXML
    void sound(ActionEvent event) { // đọc từ
        Speech.speak(selectedWord);
    }

    @FXML
    void xuatfile() throws IOException { // cập nhật file txt
        DictionaryGUIManagement.exportDictGUIDataToFile();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update Success");
        alert.setHeaderText("Update Success To File ");
        alert.showAndWait();
    }

    /**
     * Vô hiệu hóa các nút và text area trong trường hợp rơi vào index không xóa được
     */
    private void disableButtonAndTextArea() {
        eraseButton.setDisable(true);
        speechButton.setDisable(true);
        editButton.setDisable(true);

        definition.setEditable(false);
        definition.setBlendMode(BlendMode.DARKEN);
        spelling.setEditable(false);
        spelling.setBlendMode(BlendMode.DARKEN);
    }

    /**
     * Đặt lại các Button and Textarea về mặc định
     */
    private void resetButtonAndTextAreaState() {
        eraseButton.setDisable(false); // enable nút erase
        speechButton.setDisable(false); // enable nút phát âm
        editButton.setDisable(false); // enable nút sửa

        SaveEditButton.setVisible(false); // ẩn nút lưu

        definition.setEditable(false); // definition area can't edit
        definition.setBlendMode(BlendMode.DARKEN);
        spelling.setEditable(false); // spelling area can't edit
        spelling.setBlendMode(BlendMode.DARKEN);
    }

    @FXML
    void minus(ActionEvent event) {  //xóa từ

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Erase Word");
        alert.setHeaderText("Do you want to erase this word ?");
        alert.setContentText(target.getText());
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        //  ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton) {
            int previousSearchListViewIndex = searchListView.getSelectionModel().getSelectedIndex();
            DictionaryGUIManagement.removeWord(selectedWordIndex); // xóa từ
            updateListView(); // update lại list view dựa trên searchbar
            searchListView.getSelectionModel().select(previousSearchListViewIndex); // select vị trí gần vị trí vừa xóa nhất
            if (searchListView.getItems().size() == 0) {
                target.setText("");
                definition.setText("");
                spelling.setText("");
                disableButtonAndTextArea();
            }
        } else if (result.get() == noButton) {
            // no thing happen, things go on
            event.consume();
        }
    }

    @FXML
    void plus(ActionEvent event) throws IOException {  // thêm từ
        Parent root = FXMLLoader.load(getClass().getResource("plusScene.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root));
        primaryStage.showAndWait();
        updateListView();
    }


    /**
     * Tạo một ArrayList chứa kết quả khi người dùng search một từ nào đó trên searchBox.
     *
     * @param searchWords từ cần search
     * @param dict        search từ đây
     * @return trả về ArrayList chứa kết quả search
     */
    private ArrayList<String> searchList(String searchWords, ArrayList<Word> dict) {
        ArrayList<String> searchResultArray = new ArrayList<>();
        for (Word word : dict) {
            if (word.getWord_target().indexOf(searchWords) == 0) { // nếu từ đang duyệt có chứa từ cần tìm bắt đầu từ 0 thì add
                searchResultArray.add(word.getWord_target());
            }
        }
        return searchResultArray;
    }

    /**
     * Truyền data vào ListView từ ArrayList chứa String.
     *
     * @param dict source data.
     */
    private void createListView(ArrayList<String> dict) {
        for (String s : dict) {
            searchListView.getItems().add(s);
        }
    }

    /**
     * Update ListView theo Search Bar hiện tại
     */
    public void updateListView() {
        //update listview
        searchListView.getItems().clear();
        createListView(searchList(searchBar.getText(), DictionaryGUI.dict));
    }


    @FXML
    void initialize() throws IOException {
        assert searchBar != null : "fx:id=\"searchBar\" was not injected: check your FXML file 'gammatest.fxml'.";
        assert target != null : "fx:id=\"copy\" was not injected: check your FXML file 'gammatest.fxml'.";
        assert definition != null : "fx:id=\"defiinition\" was not injected: check your FXML file 'gammatest.fxml'.";

        // Đọc dữ liệu từ file
        DictionaryGUIManagement.insertDictGUIDataFromFile();

        // Truyền data vào ListView trong lần chạy đầu tiên, tất cả từ
        for (int i = 0; i < DictionaryGUI.dict.size(); i++) {
            searchListView.getItems().add(DictionaryGUI.dict.get(i).getWord_target());
        }

        // Khi search bar thay đổi
        searchBar.textProperty().addListener(((observableValue, s, t1) -> {
            updateListView();
        }));

        // Mỗi khi lựa chọn trong listview thay đổi thì
        // + reset button , textarea
        // + rơi vào trường hợp index = 0 thì disable hết nút
        searchListView.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, s, t1) -> {

                    //System.out.println("LISTENER + " + t1);

                    if (t1 != null) {
                        // lấy từ hiện tại đang có trên list view ( t1 )
                        selectedWord = searchListView.getSelectionModel().getSelectedItem();

                        // set target thành t1
                        target.setText(selectedWord);

                        // tìm kiếm vị trí của từ đó trong dict
                        selectedWordIndex = DictionaryGUIManagement.DictionaryGUILookup(selectedWord);

                        //System.out.println(selectedWordIndex);

                        target.setText(DictionaryGUI.dict.get(selectedWordIndex).getWord_target());
                        spelling.setText(DictionaryGUI.dict.get(selectedWordIndex).getWord_spelling());
                        definition.setText(DictionaryGUI.dict.get(selectedWordIndex).getWord_explain());
                        resetButtonAndTextAreaState();

                        if (selectedWordIndex == 0) {
                            disableButtonAndTextArea();
                        }
                    }
                });

        searchListView.getSelectionModel().select(0);
    }
}



