package modelo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.scene.control.TextArea;

public class Cliente implements Runnable {
	Socket s;
	String usuario;
	String ip;
	int puerto;
	TextArea chat;

	public Cliente(String usuario, String ip, int puerto) {
		s = new Socket();
		this.usuario = usuario;
		this.ip = ip;
		this.puerto = puerto;
	}
	
	public void conectarCliente() throws IOException {
		s.connect(new InetSocketAddress(ip, puerto));
	}

	public Socket getS() {
		return s;
	}

	public void setS(Socket s) {
		this.s = s;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setChat(TextArea chat) {
		this.chat = chat;
	}

	private InputStreamReader isr;

	@Override
	public void run() {
		try {
			isr = new InputStreamReader(s.getInputStream());
		} catch (IOException e1) {
		}

		while (true) {
			try {
				if (!s.isClosed()) {
					char[] temp = new char[256];
					isr.read(temp);

					String mensaje = new String(temp);

					if (!mensaje.trim().equalsIgnoreCase(""))
						chat.appendText(mensaje + "\n");
				}

			} catch (IOException e) {
				System.err.println("Error cliente");
				chat.appendText("====SERVIDOR: SERVIDOR CERRADO====");
				break;
			}
		}

	}

}
