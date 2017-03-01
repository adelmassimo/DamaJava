import java.util.concurrent.Semaphore;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;


public class Server extends Thread{
	
	private static final int port = 5000;
	private Socket client1;
	private Socket client2;
	private static Griglia[] g;

	Semaphore s;
	
	public static void main(String args[]){
		new Dialog();
	}
	
	public void startTurn(int id){
		try{
			s.acquire();
			id = (id == 0)?1:0;
			g[id].closeWindow();
		}catch(InterruptedException ie) {}			
	}
	
	public void endTurn(int id){
		s.release();
		id = (id == 0)?1:0;
		g[id].openWindow();
	}
	public Server(Semaphore s) throws IOException{
		this.s = s;
		start();
	}//constructor
	
	public void run(){
		//initMatch();
		try{listen();}
		catch(IOException ie){}
	}
	
	private void listen() throws IOException{
		ServerSocket ss = new ServerSocket(port); // oggetto per rimanere in ascolto sulla porta selezionata	
		System.out.println( "Listening on "+ss ); // quando si richiama la accept l'oggetto ss crea un nuovo socket 
												  // che rappresenta la nuova connessione arrivata
		while(true){
			client1 = ss.accept(); //connessione agganciata dal socket
			client2 = ss.accept();
			System.out.println("Richiesta di connessione da:" + client1);
			System.out.println("Richiesta di connessione da:" + client2);
			//DataOutputStream dout1 = new DataOutputStream(client1.getOutputStream());
			//DataOutputStream dout2 = new DataOutputStream(client2.getOutputStream());
			//creo 2 thread che hanno il compito di interagire con le nuove connessioni
			new ServerThread(this, client2, 1, s);
			new ServerThread(this, client1, 0, s);			
		} //while
				
	} //listen
	
	public void sendTo(int i, int message, Socket sock) throws IOException{
		if(i == 1){
			DataOutputStream dOut = new DataOutputStream(client1.getOutputStream());
			dOut.writeInt(message);
		}
		else{
			DataOutputStream outSocket = new DataOutputStream(client2.getOutputStream());
			outSocket.writeInt(message);
		}
		
	}//sendToAll
	
	public void finishedMatch(int winner){
		System.out.println("Match finished..!!");
		g[0].getRules().endTime = System.currentTimeMillis(); 
		g[0].closeWindow();
		g[1].closeWindow();
		//faccio le statistiche
		//giocatore 1 -> id = 0 -> neri
		int atePawnsBlack = g[0].getRules().getBlackDead();
		int blackDama = g[0].getRules().getblackDama();
		
		//giocatore 2 -> id = 1 -> bianchi
		int atePawnsWhite = g[1].getRules().getWhiteDead();
		int whiteDama = g[1].getRules().getwhiteDama();
		
		//tempo totale
		
		//creo la finestra con tutte le informazioni sulla partita
		JFrame notice = new JFrame("Match Finished..!!");
		notice.setPreferredSize(new Dimension(400, 400));
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container pane = notice.getContentPane();
		//per fare il colspan
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.weighty = 0.5;
		
		pane.setLayout(new GridBagLayout());
		JLabel giocatore1, giocatore2;
		
		
		if(winner == 1){
			giocatore1 = new JLabel("", new ImageIcon("images/whiteWinner.png"), JLabel.CENTER);
			giocatore2 = new JLabel("", new ImageIcon("images/blackLooser.png"), JLabel.CENTER);
			}
		else{
			giocatore1 = new JLabel("", new ImageIcon("images/whiteLooser.png"), JLabel.CENTER);
			giocatore2 = new JLabel("", new ImageIcon("images/blackWinner.png"), JLabel.CENTER);}

		giocatore1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
		giocatore2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(giocatore1, c);

		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 0;
		pane.add(giocatore2, c);
		
		JLabel firstData1 = new JLabel("Pedoni mangiati: " + atePawnsBlack, JLabel.CENTER); //primo dato gioc 1
		//firstData1.setPreferredSize(new Dimension(100, 100));
		firstData1.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.black));
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(firstData1, c);
		
		JLabel firstData2 = new JLabel("Pedoni mangiati: " + atePawnsWhite, JLabel.CENTER); //primo dato gioc 2
		//firstData2.setPreferredSize(new Dimension(100, 100));
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;
		pane.add(firstData2, c);
		
		JLabel secondData1 = new JLabel("Dame: " + blackDama, JLabel.CENTER); //primo dato gioc 2
		//secondData1.setPreferredSize(new Dimension(100, 100));
		secondData1.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.black));
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(secondData1, c);
		
		JLabel secondData2 = new JLabel("Dame: " + whiteDama, JLabel.CENTER); //secondo dato gioc 2
		//secondData2.setPreferredSize(new Dimension(100, 100));
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 2;
		pane.add(secondData2, c);
		
		JLabel dateData = new JLabel("Tempo tot. di gioco: " + (g[0].getRules().endTime - g[0].getRules().startTime)/1000 + "s", JLabel.CENTER); //secondo dato gioc 2
		//dateData.setPreferredSize(new Dimension(200, 100));
		dateData.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
		//c.ipady = 100;      //make this component tall
		//c.weightx = 0.0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(dateData, c);
		
		notice.setLocationRelativeTo(null);
		notice.pack();
		notice.setVisible(true);
	}
	
	public void removeConnection(Socket s){
		try{
		s.close();
		}catch(IOException e){e.printStackTrace();}
	}//removeConnection
	
	public static void play(String g1Name, String g2Name, boolean isTimedMatch, int timeValue){
		 Semaphore s = new Semaphore(1);
		try{
			new Server(s);
		}catch(IOException ioe){}
		g = new Griglia[2];
		g[0] = new Griglia("127.0.0.1", port, 0, g1Name, isTimedMatch, timeValue);
		g[1] = new Griglia("127.0.0.1", port, 1, g2Name, isTimedMatch, timeValue);
	}
}