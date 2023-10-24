module com.asher.convexhulls {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.asher.convexhulls to javafx.fxml;
    exports com.asher.convexhulls;
}