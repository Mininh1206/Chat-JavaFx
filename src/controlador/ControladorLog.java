package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ControladorLog {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea tbLog;

    @FXML
    private TextArea tbMensajes;
    
    public void a�adirLog(String mensaje) {
    	tbLog.appendText(mensaje);
    }
    
    public void a�adirMensajes(String mensaje) {
    	tbMensajes.appendText(mensaje);
    }

    @FXML
    void initialize() {
        assert tbLog != null : "fx:id=\"tbLog\" was not injected: check your FXML file 'VistaLog.fxml'.";
        assert tbMensajes != null : "fx:id=\"tbMensajes\" was not injected: check your FXML file 'VistaLog.fxml'.";

    }

}
