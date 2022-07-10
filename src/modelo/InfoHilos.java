package modelo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import controlador.ControladorLog;

public class InfoHilos implements Runnable {

	private int conexiones;
	private int actuales;
	private int maximo;
	private Socket[] tabla;
	private ControladorLog cLog;
	private boolean abierto = true;

	public InfoHilos(Socket[] tabla) {
		conexiones = 0;
		actuales = 0;
		this.maximo = tabla.length;
		this.tabla = tabla;
	}

	public void setCLog(ControladorLog cLog) {
		this.cLog = cLog;
	}
	
	public void cerrar() {
		abierto = false;
	}
	
	private InputStreamReader isr;
	private OutputStreamWriter osw;
	
	@Override
	public void run() {
		Thread comprobarUsuarios = new Thread() {

			OutputStreamWriter oswTemp;
			
			@Override
			public void run() {
				while(true) {
					synchronized (this) {
						for (int r = 0; r < tabla.length; r++) {
							try {
								if (tabla[r] != null) {
									oswTemp = new OutputStreamWriter(tabla[r].getOutputStream());
									oswTemp.write(" ");
									oswTemp.flush();
								}
							} catch (IOException e2) {
								System.out.println("Un usuario ha salido.");
								
								String logOut = tabla[r].toString();
								
								tabla[r] = null;
								actuales--;
								
								enviarATodos("====SERVIDOR: HA SALIDO UN USUARIO====");
								cLog.añadirLog("SERVIDOR: HA SALIDO UN USUARIO:\n"+logOut+"\n\n");
								
								continue;
								
							}
						}
					}
					try {
						sleep(500);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		
		comprobarUsuarios.start();
		
		while (abierto) {
			for (int e = 0; e < tabla.length; e++) {
				try {
					if (tabla[e] != null) {

						synchronized (this) {
							isr = new InputStreamReader(tabla[e].getInputStream());

							if (isr.ready()) {
								char[] temp = new char[256];
								
								isr.read(temp);

								String mensaje = new String(temp);

								if (!mensaje.trim().equals("")) {
									
									cLog.añadirMensajes(mensaje+"\n");
									enviarATodos(mensaje, e);
								}
							}
						}
						
					}
				} catch (IOException e1) {
					System.err.println("Error del servidor al recibir mensaje");
				}
			}
		}
	}

	private synchronized void enviarATodos(String mensaje) {
		for (int r = 0; r < tabla.length; r++) {
			try {
				if (tabla[r] != null) {
					osw = new OutputStreamWriter(tabla[r].getOutputStream());
					osw.write(mensaje);
					osw.flush();
				}
			} catch (IOException e) {
				System.err.println("Error al enviar desde el servidor");
			}
		}
	}
	
	private synchronized void enviarATodos(String mensaje, int excepcion) {
		OutputStreamWriter osw;
		
		for (int r = 0; r < tabla.length; r++) {
			try {
				if (tabla[r] != null && r!=excepcion) {
					osw = new OutputStreamWriter(tabla[r].getOutputStream());
					osw.write(mensaje);
					osw.flush();
				}
			} catch (IOException e) {
				System.err.println("Error al enviar desde el servidor");
			}
		}
	}
	
	public synchronized void añadirUsu(Socket usu) {

		for (int u = 0; u < tabla.length; u++) {
			if (tabla[u] == null) {
				tabla[u] = usu;
				actuales++;
				conexiones++;
				System.out.println("Nuevo usuario");
				
				enviarATodos("====SERVIDOR: SE HA UNIDO UN USUARIO====", u);
				cLog.añadirLog("SERVIDOR: SE HA UNIDO UN USUARIO:\n"+usu.toString()+"\n\n");
				break;
			}
		}

		
	}

	public int getConexiones() {
		return conexiones;
	}

	public void setConexiones(int conexiones) {
		this.conexiones = conexiones;
	}

	public int getActuales() {
		return actuales;
	}

	public void setActuales(int actuales) {
		this.actuales = actuales;
	}

	public int getMaximo() {
		return maximo;
	}

	public void setMaximo(int maximo) {
		this.maximo = maximo;
	}

	public Socket[] getTabla() {
		return tabla;
	}

	public void setTabla(Socket[] tabla) {
		this.tabla = tabla;
	}

}
