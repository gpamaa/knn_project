package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import utility.Keyboard;

/**
 * classe che rappresenta il terminale che richiede la predizione
 */
public class Client {

	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	private ArrayList<String> tipi = new ArrayList<>();

	/**
	 * costruttore di classe
	 * 
	 * @param address
	 *                rappresenta l indirizzo ip del server a cui il client si
	 *                collega
	 * @param port
	 *                rappresenta il numero di porta associato al servizio richiesto
	 *                sul server
	 * @throws IOException
	 *                                viene lanciata quando ci sono problemi di
	 *                                comunicazione con il server
	 * @throws ClassNotFoundException
	 *                                viene lanciata quando la classe non è stata
	 *                                trovata
	 */

	public Client(String address, int port) throws IOException, ClassNotFoundException {

		socket = new Socket(address, port);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}

	/**
	 * scrive un oggetto sul canale di comunicazione condiviso con il server
	 * 
	 * @param d
	 *          oggetto che viene scritto nel canale
	 * @throws IOException
	 *                                viene lanciata quando ci sono problemi di
	 *                                comunicazione con il server
	 * @throws ClassNotFoundException
	 *                                viene lanciata quando la classe non è stata
	 *                                trovata
	 */

	public void writeInput(Object d) throws IOException, ClassNotFoundException {

		out.writeObject(d);
	}

	/**
	 * verifica se il nome del file,tabella e credenzial dell utente nel DBMS siano
	 * valide
	 * 
	 * @return boolean
	 *         restituisce vero se il nome del file,tabella e credenziali esistono
	 *         altrimenti falso
	 */
	public boolean talking5() {
		try {
			String s = (String) in.readObject();
			if (s.equals("@ERROR")) {
				return false;
			}
		} catch (ClassNotFoundException | IOException e) {
		}
		return true;
	}

	/**
	 * indica il numero di colonne che dovra contenere il querypoint
	 * 
	 * @return String
	 *         contiene fine example per finire l inserimento del querypoint
	 * @throws IOException
	 *                                viene lanciata quando ci sono problemi di
	 *                                comunicazione con il server
	 * @throws ClassNotFoundException
	 *                                viene lanciata quando la classe non è stata
	 *                                trovata
	 */
	public String talking2() throws IOException, ClassNotFoundException {
		String risposta;
		risposta = (String) in.readObject();
		if (!risposta.equals("@ENDEXAMPLE"))
			tipi.add(risposta);
		return risposta;
	}

	/**
	 * verifica la validità del query point e k inseriti dall'utente
	 * 
	 * @param valori
	 *               contiene i valori digitati dall'utente
	 * @param k
	 *               rappresenta il numero di distanze diverse da prendere
	 *               in considerazione
	 * @return boolean
	 *         restituisce vero se gli input sono stati scritti correttamente falso
	 *         altrimenti
	 */
	public boolean talking3(ArrayList<String> valori, String k) {
		Iterator<String> iterator = tipi.iterator();
		int i = 0;
		int k1 = 0;
		try {
			k1 = Integer.valueOf(k);
			if (k1 < 1) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		while (iterator.hasNext()) {
			String tipo = iterator.next();
			if (tipo.equals("@READDOUBLE")) {
				try {
					Double.parseDouble(valori.get(i));
				} catch (NumberFormatException e) {
					return false;
				}
			} else {
				if (valori.get(i).length() == 1) {
					try {
						Double.parseDouble(valori.get(i));
						return false;
					} catch (NumberFormatException e) {
						if (valori.get(i).equals("")) {
							return false;
						}
					}
				} else {
					return false;
				}
			}
			i++;
		}
		iterator = tipi.iterator();
		i = 0;
		while (iterator.hasNext()) {
			String tipo = iterator.next();
			try {
				if (tipo.equals("@READDOUBLE")) {
					out.writeObject((Double.parseDouble(valori.get(i))));
				} else {
					out.writeObject(valori.get(i));
				}
			} catch (IOException e) {
			}
			i++;
		}
		try {
			out.writeObject(k1);
		} catch (IOException e) {
			return false;
		}
		i = 0;
		tipi.removeAll(tipi);
		return true;
	}

	/**
	 * consente di mostrare a video un istanza di questa classe
	 * 
	 * @return String
	 *         restituisce la stringa che rappresenta lo stato dell istanza
	 */
	public String toString() {
		return "socket" + socket + "output stream" + out + "input stream" + in;
	}

	/**
	 * restituisce la risposta del server sull inserimento del querypoint e k
	 * 
	 * @return valore della predizione
	 */
	public Object talking4() {
		try {
			Object o = in.readObject();
			return o;
		} catch (IOException | ClassNotFoundException e) {
			return 0.0;
		}
	}

	private void talking() throws IOException, ClassNotFoundException {
		String menu = "";
		int decision = 0;
		do {
			String risposta = "";
			do {
				out.writeObject(decision);
				String tableName = "";
				System.out.println("Table name (without estensione):");
				tableName = Keyboard.readString();
				out.writeObject(tableName);
				risposta = (String) in.readObject();

			} while (risposta.contains("@ERROR"));

			System.out.println("KNN loaded on the server");
			// predict
			String c;
			do {
				out.writeObject(4);
				boolean flag = true; // reading example
				do {
					risposta = (String) (in.readObject());
					if (!risposta.contains("@ENDEXAMPLE")) {
						// sto leggendo l'esempio
						String msg = (String) (in.readObject());
						if (risposta.equals("@READSTRING")) // leggo una stringa
						{
							System.out.println(msg);
							String s = Keyboard.readString();
							try {
								do {
									Double.valueOf(s);
									System.out.println(msg);
									s = Keyboard.readString();
								} while (true);
							} catch (NumberFormatException e) {
							}
							out.writeObject(s);
						} else // leggo un numero
						{
							double x = 0.0;
							do {
								System.out.println(msg);
								x = Keyboard.readDouble();
							} while (new Double(x).equals(Double.NaN));
							out.writeObject(x);
						}

					} else
						flag = false;
				} while (flag);

				// sto leggendo k
				risposta = (String) (in.readObject());
				int k = 0;
				do {
					System.out.print(risposta);
					k = Keyboard.readInt();
				} while (k < 1);
				out.writeObject(k);
				// aspetto la predizione

				System.out.println("Prediction:" + in.readObject());

				System.out.println("Vuoi ripetere predizione? Y/N");
				c = Keyboard.readString();
			} while (c.toLowerCase().equals("y"));
			out.writeObject(5);
			System.out.println("Vuoi ripetere una nuova esecuzione con un nuovo oggetto KNN? (Y/N)");
			menu = Keyboard.readString().toLowerCase();
			out.writeObject(menu);
		} while (menu.equals("y"));
	}

	/**
	 * rappresenta il metodo principale della classe
	 * 
	 * @param args
	 *             contiene indirizzo ip e numero di porta al quale il client si
	 *             connette
	 * @throws IOException
	 *                                viene lanciata quando ci sono problemi di
	 *                                comunicazione con il server
	 * @throws ClassNotFoundException
	 *                                viene lanciata quando la classe non è stata
	 *                                trovata
	 * @throws UnknownHostException
	 *                                viene lanciata quando il server non è stato
	 *                                trovato
	 * @throws NumberFormatException
	 *                                viene lanciata quando la porta non è un valore
	 *                                intero
	 */
	public static void main(String[] args)
			throws IOException, UnknownHostException, ClassNotFoundException, NumberFormatException {
		try {
			new Client(args[0], Integer.valueOf(args[1]));
		} catch (ConnectException e) {
			System.out.println("Server non avviato;");
		}
	}
}