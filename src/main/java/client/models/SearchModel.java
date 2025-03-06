package client.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.ChatRoom;

public class SearchModel {
   private static SearchModel instance;
   private ObservableList<ChatRoom> resultSet;

   public static SearchModel getInstance() {
      if (instance == null) {
         instance = new SearchModel();
      }
      return instance;
   }

   private SearchModel() {
      this.resultSet = FXCollections.observableArrayList();
   }

   public ObservableList<ChatRoom> getResultSet() {
      return resultSet;
   }

   public void setResultSet(ObservableList<ChatRoom> resultSet) {
      this.resultSet.setAll(resultSet);
   }

   public void clearModel() {
      this.resultSet.clear();
   }
}
