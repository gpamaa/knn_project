package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** classe necessaria per creare una nuova connessione ad un client */
public class MultiServer {
    private int port = 2025;

    /**
     * costruttore che definisce il numero di porta su cui connettersi
     * 
     * @param port numero di porta su cui connettersi
     */
    public MultiServer(int port) {
        System.out.println("server started");
        this.port = port;
        try {
            run();
        } catch (IOException e) {
            System.out.println("errore");
        }
    }

    private void run() throws IOException {
        ServerSocket serversocket = new ServerSocket(1010);
        try {
            while (true) {
                Socket s = serversocket.accept();
                try {
                    new ServerOneClient(s);
                } catch (IOException e) {
                    s.close();
                }
            }
        } catch (IOException e) {
            System.out.println("error");
            serversocket.close();
        }
    }

    /**
     * funzione principale della classe MultiServer
     * 
     * @param args
     *             contiene i comandi del cmd digitati dall'utente
     */
    public static void main(String[] args) {
        new MultiServer(1010);
    }
}