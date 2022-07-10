package controlador;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import modelo.Cliente;

public class ControladorChat {

	private Cliente c;
	private Socket s;
	private OutputStreamWriter osw;
	
	public void setCliente(Cliente c) {
		this.c = c;
		s = c.getS();
		
		try {
			osw = new OutputStreamWriter(s.getOutputStream());
		} catch (IOException e) {
			System.err.println("Error");
		}
		
		c.setChat(tbMensajes);
		
		Thread hiloCliente = new Thread(c);
		hiloCliente.start();
	}

	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btEnviar;

    @FXML
    private Button btSalir;

    @FXML
    private TextField tbMensaje;

    @FXML
    private TextArea tbMensajes;

    @FXML
    void enviar(ActionEvent event) {
    	if (tbMensaje.getLength()>0) {
    		try {
    			
    			String mensaje = c.getUsuario()+": "+tbMensaje.getText();
    			
    			tbMensajes.appendText("Tú: "+tbMensaje.getText()+"\n");
    			
    			osw.write(mensaje);
    			osw.flush();
    			
    			System.out.println("enviado");
    			
        		tbMensaje.clear();
        		
    		} catch (IOException e) {
    			System.err.println("Error al enviar");
			}
    	}
    }

    @FXML
    void salir(ActionEvent event) {
    	try {
			s.close();
			System.out.println("Cerrado");
		} catch (IOException e1) {
			System.err.println("Error");
		}
    	System.exit(0);
    }

    @FXML
    void initialize() {
        assert btEnviar != null : "fx:id=\"btEnviar\" was not injected: check your FXML file 'VistaChat.fxml'.";
        assert btSalir != null : "fx:id=\"btSalir\" was not injected: check your FXML file 'VistaChat.fxml'.";
        assert tbMensaje != null : "fx:id=\"tbMensaje\" was not injected: check your FXML file 'VistaChat.fxml'.";
        assert tbMensajes != null : "fx:id=\"tbMensajes\" was not injected: check your FXML file 'VistaChat.fxml'.";
        
    }

}
