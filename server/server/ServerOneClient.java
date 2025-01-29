package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.Data;
import data.TrainingDataException;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.InsufficientColumnNumberException;
import database.NoValueException;
import java.sql.SQLException;
import mining.KNN;
import java.io.IOException;

/** classe utile per soddisfare le richieste del singolo client */
public class ServerOneClient extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    /**
     * costruttore utile per impostare il socket e la comunicazione col client
     * 
     * @param s socket su cui comunicare col client
     * @throws IOException eccezione lanciata in caso di errori nella comunicazione
     *                     col client
     */
    public ServerOneClient(Socket s) throws IOException {
        socket = s;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        start();
    }

    /** metodo per l'esecuzione delle richieste del singolo client */
    public void run() {
        int decision;
        int d;
        int decision2;
        Data data = null;
        DbAccess db = null;
        KNN knn = null;
        try {
            do {
                decision = (int) in.readObject();
                decision2 = 0;
                do {
                    d = 0;
                    if (decision == 1) {
                        try {
                            String fileName = (String) in.readObject();

                            try {
                                data = new Data(fileName + ".dat");
                                out.writeObject("caricamento riuscito");
                                d = (int) in.readObject();
                            } catch (TrainingDataException | ClassNotFoundException | IOException e) {
                                out.writeObject("@ERROR");
                            }
                        } catch (IOException | ClassNotFoundException e) {
                        }
                    } else if (decision == 2) {
                        try {
                            String fileName = (String) in.readObject();
                            knn = KNN.carica(fileName + ".dat.dmp");
                            out.writeObject("caricamento riuscito");
                            d = (int) in.readObject();
                        } catch (ClassNotFoundException | IOException e) {
                            out.writeObject("@ERROR");
                        }
                    } else if (decision == 3) {
                        try {
                            if (decision2 == 0) {
                                do {
                                    String user = (String) in.readObject();
                                    String password = (String) in.readObject();
                                    try {
                                        db = new DbAccess(user, password);
                                        out.writeObject("caricamento riuscito");
                                        decision2 = (int) in.readObject();
                                    } catch (DatabaseConnectionException e) {
                                        out.writeObject("@ERROR");
                                    }
                                } while (decision2 != 4);
                            }
                            String tableName = (String) in.readObject();
                            data = new Data(db, tableName);
                            out.writeObject("caricamento riuscito");
                            d = (int) in.readObject();
                        } catch (IOException | ClassNotFoundException | InsufficientColumnNumberException
                                | NoValueException | SQLException e) {
                            out.writeObject("@ERROR");
                        }
                    }
                } while (d != 4);
                int d1;
                do {
                    if (decision != 2)
                        knn = new KNN(data);
                    out.writeObject(knn.predict(out, in));
                    d1 = (int) in.readObject();
                } while (d1 != 5);
            } while (((String) in.readObject()).equals("y"));
        } catch (IOException |

                ClassNotFoundException e) {
        }
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}