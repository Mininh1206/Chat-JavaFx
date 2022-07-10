package modelo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import controlador.ControladorLog;

public class Servidor implements Runnable {
	
	private ServerSocket ss;
	private Socket[] tabla;
	private InfoHilos ih;
	private boolean abierto = true;
	
	public Servidor(int puerto, int maximo) {
		try {
			ss = new ServerSocket(puerto);
			tabla = new Socket[maximo];
			ih = new InfoHilos(tabla);
			
		} catch (IOException e) {
			System.err.println("Error al crear el servidor");
		}
	}
	
	public void setCLog(ControladorLog cLog) {
		ih.setCLog(cLog);
	}
	
	public void iniciarInfoHilos() {
		Thread hiloControl = new Thread(ih);
		hiloControl.start();
	}
	
	@Override
	public void run() {
		while(abierto){
			try {
				synchronized (this) {
					if (ih.getActuales()<=ih.getMaximo()) {
						Socket s = ss.accept();
						ih.añadirUsu(s);
					}
				}
				
				Thread.sleep(500);
				
			} catch (IOException e) {
				System.err.println("Error");
			} catch (InterruptedException e) {
			}
		}
		
		ih.cerrar();
	}
	
	public void cerrar() {
		abierto = false;
	}

}
