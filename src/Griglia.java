import java.awt.*;

import javax.swing.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

 public class Griglia implements Runnable{
	 
//---------------GRIGLIA PROPERTIES----------------- 
	 private Socket socket;
	 private DataInputStream dIn;
	 private DataOutputStream dout;
	 private int id;
	// private CellaCliccata lastClik;
	 private Clicker clicker;
	 private Cella[] celle;
	 private Rules rules;
	 private Pawn[] opponentP; //array di pedoni dell'avversario
	 private Pawn[] ownP; //array di pedoni propri
	 private JFrame f;
	 private String player;
	 private JProgressBar progressBar;
	 private int timeValue;
	 boolean moved, timedMatch;
	 //private final Pawn deathPawn = new Pawn(null, "none");
	 public Griglia(String host, int port, int i, String p, boolean time, int timeValue) {
		 moved = false;
		 timedMatch = time;
		 id = i;
		 player = p;
		 this.timeValue = timeValue;
		 progressBar = new JProgressBar(0, timeValue*1000);
		 	progressBar.setValue(0);
		 initGraphic(8, 8);
		 try{	//mi connetto al server
				socket = new Socket(host, port);
				System.out.println( "connected to " + socket);
		        
				//din = new DataInputStream(socket.getInputStream());
				dout = new DataOutputStream(socket.getOutputStream());

				//faccio partire un thread per ricevere messaggi
				new Thread(this).start();
				
				} catch(IOException e){
					e.printStackTrace();
				}
	 }// Griglia Constructor
	 
	 public void sendPosition(Cella source, Cella dest, Cella ate){
		 
		 int message;
		 if (ate != null)	message = ate.getIndex()*1000000 + source.getIndex()*1000 + dest.getIndex();
		 else				message = 64*1000000 + source.getIndex()*1000 + dest.getIndex();
		 try{
			 dout.writeInt(message);
		 	}catch(IOException ie){}
		 }		 
	 public void sendMessage(int mex){
		 try{
			dout.writeInt(mex);
		}catch(IOException ie){}
		if(mex == 3){ 
			moved = true;
			progressBar.setValue(0);
		}
		}	
	 
	 public void writeWinner(int winner){
		 try{
		 dout.writeInt(winner);
		 } catch(IOException e){
			 e.printStackTrace();
		 }
	 }
	 public int getId(){return id;}
	 public void initGraphic(int rows, int cols){
		//creo un JPanel per la progressBar
		JPanel contProgressBar = new JPanel();
		contProgressBar.setLayout(new GridLayout(1, 1));
		contProgressBar.add(progressBar);	
			
		JPanel scacchiera = new JPanel();
		scacchiera.setLayout( new GridLayout(rows, cols) );
		celle = new Cella[rows * cols];
		rules = new Rules(celle);
		clicker = new Clicker(this, rules);
		opponentP = new Pawn[12];
		ownP = new Pawn[12];
		f = new JFrame();	
		//creo la scacchiera
		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){
				celle[i*rows+j] = new Cella(i, j);
				celle[i*rows+j].addMouseListener( clicker );
				scacchiera.add( celle[i*rows+j] );
				celle[i*rows+j].getColor();
			}
		}
		 
		initOwnPawns();
		initOpponentPawns();
		//lastClik = new CellaCliccata(celle[0]);
		progressBar.setForeground(Color.GREEN);
		 
		Container c = f.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(scacchiera, BorderLayout.CENTER);
		if(timedMatch) c.add(contProgressBar,BorderLayout.SOUTH);
	
				 //creo la finestra	
		f.setMinimumSize(new Dimension(400, 400));
		f.setResizable(false);
		f.setSize(rows*80, cols*80);
		String title = Integer.toString(id);
		f.setTitle("Client " + title);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocation(120, 130);
		f.setTitle(player);
		if(id != 1)
			f.setLocation(970, 130);
		
	 } //initGraphic

	 public void initOwnPawns(){
		 	String color = "white";
		 	if (id == 1) color = "black";
			ownP[0] = new Pawn(celle[63], color, id);
			ownP[0].addToCell();
			ownP[1] = new Pawn(celle[61], color, id);
			ownP[1].addToCell();
			ownP[2] = new Pawn(celle[59], color, id);
			ownP[2].addToCell();
			ownP[3] = new Pawn(celle[57], color, id);
			ownP[3].addToCell();
			ownP[4] = new Pawn(celle[54], color, id);
			ownP[4].addToCell();
			ownP[5] = new Pawn(celle[52], color, id);
			ownP[5].addToCell();
			ownP[6] = new Pawn(celle[50], color, id);
			ownP[6].addToCell();
			ownP[7] = new Pawn(celle[48], color, id);
			ownP[7].addToCell();
			ownP[8] = new Pawn(celle[47], color, id);
			ownP[8].addToCell();
			ownP[9] = new Pawn(celle[45], color, id);
			ownP[9].addToCell();
			ownP[10] = new Pawn(celle[43], color, id);
			ownP[10].addToCell();
			ownP[11] = new Pawn(celle[41], color, id);
			ownP[11].addToCell();
	
	 }//initPawns
	 
	 public void initOpponentPawns(){
		 	String color = "black";
		 	if (id == 1) color = "white";
		 	opponentP[0] = new Pawn(celle[0], color, id);
		 	opponentP[0].addToCell();
		 	opponentP[1] = new Pawn(celle[2], color, id);
		 	opponentP[1].addToCell();
		 	opponentP[2] = new Pawn(celle[4], color, id);
		 	opponentP[2].addToCell();
		 	opponentP[3] = new Pawn(celle[6], color, id);
		 	opponentP[3].addToCell();
		 	opponentP[4] = new Pawn(celle[9], color, id);
		 	opponentP[4].addToCell();
			opponentP[5] = new Pawn(celle[11], color, id);
			opponentP[5].addToCell();
			opponentP[6] = new Pawn(celle[13], color, id);
			opponentP[6].addToCell();
			opponentP[7] = new Pawn(celle[15], color, id);
			opponentP[7].addToCell();
			opponentP[8] = new Pawn(celle[16], color, id);
			opponentP[8].addToCell();
			opponentP[9] = new Pawn(celle[18], color, id);
			opponentP[9].addToCell();
			opponentP[10] = new Pawn(celle[20], color, id);
			opponentP[10].addToCell();
			opponentP[11] = new Pawn(celle[22], color, id);
			opponentP[11].addToCell();
		 
	 }
	 
	 public void run() {
	try {
		int message;
	// Receive messages one-by-one, forever 
		while (true) {
	// Get the next message 
			do{
				dIn = new DataInputStream(socket.getInputStream());
				message = dIn.readInt();
				moved = false;
	// "Decripto" il messaggio
				if(message != 3){
					int dest = message%1000;
					//System.out.println("dest:" + dest);
					int source = ((message - dest)%1000000)/1000; 
					//System.out.println("source:" + source);
					int ate = (message - source)/1000000;
			
			
					if (ate < 64 /*&& celle[63-ate].getPawn() != null*/)
						celle[63-ate].getPawn().removeFromCell();
					ate = 64;
					Cella targetCell = celle[63-source];
					targetCell.getPawn().moveTo(celle[63-dest]);
					}
				}while(message != 3);


			 if(timedMatch){
				 //bar.setVisible(true);
				 int num = 0;
				 //faccio scorrere il tempo
				 while (num <= timeValue*1000 && !moved) {
					 progressBar.setValue((moved)?0:num);
					 setProgressColor(progressBar);
					 try {
						 Thread.sleep(1);
					 } catch (InterruptedException e) {
					 }
					 num += 1;
				 } //while
			 } //id timedMatch
			 
			if(!moved && timedMatch){
				 rules.resetCells();
				 rules.cleanEatCells();
					dout.writeInt(3);
				}
	// Print it to our text window 
		}
	} 
	catch( IOException ie ) {} 
}
	 public void closeWindow(){
		 f.disable();
		 closePawns();
		 //clicker.setTurn(false);
		 
		 
	 }
	 public void openWindow(){
		 f.enable();
		 openPawns();
		 //clicker.setTurn(true);
	 }

	 private void closePawns(){
		 for(int i = 0; i<64; i++)
			 if(celle[i].getPawn() != null)
				 celle[i].getPawn().setEnabled(false);
	 }

	 private void openPawns(){
		 for(int i = 0; i<64; i++)
			 if(celle[i].getPawn() != null)
				 celle[i].getPawn().setEnabled(true);
	 }
	 public Rules getRules(){ return rules; }
	 
	 public void setProgressColor(JProgressBar p){
		int mid = 5*timeValue/10;
		int ending = 8*timeValue/10;
		if(p.getValue()/1000 <= mid) p.setForeground(Color.GREEN);
		else if(p.getValue()/1000> ending) p.setForeground(Color.RED);
		else p.setForeground(Color.ORANGE);
	 }
	 
 } // class
 
 
 
 
