package controlador;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage ventana) throws Exception {
		FXMLLoader l = new FXMLLoader(getClass().getResource("/vista/VistaLogin.fxml"));
		
		AnchorPane root = l.load();
		
		Scene escena = new Scene(root);
		
		ControladorLogin cl = l.getController();
		cl.setVentana(ventana);
		
		ventana.setScene(escena);
		ventana.setTitle("Login");
		ventana.setResizable(false);
		ventana.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
