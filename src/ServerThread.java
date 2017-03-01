import java.io.DataInputStream;
import java.util.concurrent.Semaphore;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

	
public class ServerThread extends Thread {
	private Server server;
	private Socket socket;
	private int id;
	Semaphore s;
	
	public ServerThread(Server serv, Socket sock, int i, Semaphore s){
		this.s = s;
		id = i;
		server = serv;
		socket = sock;
		start();
	}
	
	public void run(){
		try{
			//crea un oggetto DataInputStream per comunicare
				DataInputStream din = new DataInputStream(socket.getInputStream()); 
				int message;
				//mi metto in ascolto per nuovi messaggi
				while(true){
					
					server.startTurn(id);
					/* è un po alla rovescia la cosa: se id è 1 significa che solo 1 può ricevere
					 * indi per cui 0 deve inviare.
					 * segue che se startTurn(0) viene eseguito è 1 che deve giocare e viceversa.
					*/
					do{
						message = din.readInt();// Aspetta, cicla, fino all'arrivo di un messaggio.
						
						if(message == 1){ //hanno vinto i bianchi
							server.finishedMatch(message);
						}
						else if(message == 0){ // 0 -> hanno vinto i neri
							server.finishedMatch(message);
						}
						//dico al server di inviarlo a tutti gli utenti connessi
						else if(message != 1 && message != 0) //altrimenti manda il messaggio sbagliato a tutti
							server.sendTo(id, message, socket);
					}while(message != 3); // il messaggio 3 indica che il giocatore avversario ha finito il turno. muovo io. senno sto in ascolto e aggiorno le mosse
					server.endTurn(id);
					Thread.sleep(50);
				} //while
		} catch(EOFException ie){
			System.out.print("Ciao");
			ie.printStackTrace();
		} catch (IOException e) {
			System.out.print("bona");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			//interrompo la connessione per qualsiasi tipo di problema
			server.removeConnection(socket);
		}
	} //run
	
} //class
