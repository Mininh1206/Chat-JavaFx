package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import modelo.Cliente;
import modelo.Servidor;

public class ControladorLogin {

	private Stage ventana;

	public void setVentana(Stage ventana) {
		this.ventana = ventana;
	}

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btConectar;

	@FXML
	private TextField tbIp;

	@FXML
	private TextField tbPuerto;

	@FXML
	private TextField tbUsuario;

	@FXML
	private Button btCrear;

	@FXML
	void conectar(ActionEvent event) {

		if (tbUsuario.getLength() > 0 && tbIp.getLength() > 0 && tbPuerto.getLength() > 0) {
			try {
				int puerto = Integer.parseInt(tbPuerto.getText());

				Cliente c = new Cliente(tbUsuario.getText(), tbIp.getText(), puerto);

				c.conectarCliente();

				FXMLLoader l = new FXMLLoader(getClass().getResource("/vista/VistaChat.fxml"));

				AnchorPane root = l.load();

				Scene escena = new Scene(root);

				ventana.setTitle("Chat de texto - " + c.getUsuario());
				ventana.setScene(escena);

				ventana.setOnCloseRequest(e -> {
					try {
						c.getS().close();
						System.out.println("Cerrado");
					} catch (IOException e1) {
						System.err.println("Error");
					}
					System.exit(0);
				});

				ControladorChat cc = l.getController();
				cc.setCliente(c);

			} catch (IOException e) {
				System.err.println("Error al conectar");
			}

		}
	}

	Servidor servidor = null;

	@FXML
	void crear(ActionEvent event) {

		if (tbUsuario.getLength() > 0 && tbIp.getLength() > 0 && tbPuerto.getLength() > 0) {

			try {

				TextInputDialog tid = new TextInputDialog();
				tid.setContentText("Introduce el máximo de usuarios");

				tid.showAndWait();

				int maximo = Integer.parseInt(tid.getResult());

				int puerto = Integer.parseInt(tbPuerto.getText());

				servidor = new Servidor(puerto, maximo);

				Thread hiloServidor = new Thread(servidor);
				hiloServidor.start();

				Cliente c = new Cliente("(host) " + tbUsuario.getText(), tbIp.getText(), puerto);

				FXMLLoader log = new FXMLLoader(getClass().getResource("/vista/VistaLog.fxml"));

				AnchorPane rootLog = log.load();

				Scene escenaLog = new Scene(rootLog);

				Stage ventanaLog = new Stage();
				ventanaLog.setTitle("Log");
				ventanaLog.setScene(escenaLog);
				ventanaLog.setResizable(false);

				ventanaLog.setOnCloseRequest(e -> {
					servidor.cerrar();
					System.out.println("Cerrado");
				});

				ControladorLog cLog = log.getController();
				servidor.setCLog(cLog);

				servidor.iniciarInfoHilos();
				c.conectarCliente();

				ventanaLog.show();

				FXMLLoader l = new FXMLLoader(getClass().getResource("/vista/VistaChat.fxml"));

				AnchorPane root = l.load();

				Scene escena = new Scene(root);

				ventana.setTitle("Chat de texto - " + c.getUsuario());
				ventana.setScene(escena);

				ventana.setOnCloseRequest(e -> {
					try {
						c.getS().close();
						System.out.println("Cerrado");
					} catch (IOException e1) {
						System.err.println("Error");
					}
					System.exit(0);
				});

				ControladorChat cc = l.getController();
				cc.setCliente(c);

				Thread hiloCliente = new Thread(c);
				hiloCliente.start();

			} catch (NumberFormatException e) {
				System.err.println("Introduce un número");
			} catch (IOException e) {
				System.err.println("Error al conectar");
				if (servidor != null) {
					servidor.cerrar();
				}
			}

		}
	}

	@FXML
	void initialize() {
		assert tbUsuario != null : "fx:id=\"tbUsuario\" was not injected: check your FXML file 'VistaLogin.fxml'.";
		assert tbIp != null : "fx:id=\"tbIp\" was not injected: check your FXML file 'VistaLogin.fxml'.";
		assert tbPuerto != null : "fx:id=\"tbPuerto\" was not injected: check your FXML file 'VistaLogin.fxml'.";
		assert btConectar != null : "fx:id=\"btConectar\" was not injected: check your FXML file 'VistaLogin.fxml'.";
		assert btCrear != null : "fx:id=\"btCrear\" was not injected: check your FXML file 'VistaLogin.fxml'.";

	}

}
