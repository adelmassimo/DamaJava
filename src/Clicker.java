import java.awt.event.*;

public class Clicker extends MouseAdapter {
		 
		 private int count, id;
		 private Cella source, dest, ate;
		 private Rules rules;
		 private Griglia g;
		 private boolean secondTime;
		 
		 public Clicker(Griglia g, Rules r){
			 super();
			 id = g.getId();	 //id del giocatore
			 this.g = g;		 // g è la scacchiera
			 rules = r;			 // oggetto che contiene le regole
			 secondTime = false; // vero se ho già mangiato un pedone e posso rimangiare
		 }
		 public void mouseClicked(MouseEvent me){
			 Cella targetCell = (Cella) me.getSource();
			 
			 if(targetCell.getPawn() != null){ //----------PRIMO CLICK----------------1

				 rules.resetCells();
				 if((count == 0 && rules.firstClick(targetCell, id)) || 
					(count == 1 && targetCell.getPawn().isMine())){//--------------------2
					 if(count == 1){// SE RIPigio su un altro mio pedone
						 rules.cleanEatCells();
						 if(secondTime){		//se sono durante una serie di mangiate e ripigio su me stesso mi interrompo
							 secondTime = false;
							 count = 0;
							 g.sendMessage(3);
							 return;
						 }
						 count=0;
						 if(targetCell != source) rules.firstClick(targetCell, id);
						 else{ source = null; return;}
					 }
					 source = targetCell;
					 count=1;
					 //funzione che suggerisce le mosse
					 return;
				 }//-----------------------------------------------------------2
			 }//--------------------------------------------------1
			 if(count == 1 && rules.secondClick(targetCell) && source.getPawn() != null){ //------SECONDO CLICK
			
				 if(rules.haveEat(targetCell)){
					 ate = rules.atePawn(targetCell, g); //ate è la cella che contiene il pedone mangiato
				 } 
				 
				 rules.resetCells();
				 rules.cleanEatCells();
				 dest = targetCell;
				 count = 0;
				
				 g.sendPosition(source, dest, ate);
				 
				 if(source.getPawn() != null){
					 boolean dama = source.getPawn().moveTo(dest); //moveTo torna true se il pedone è diventato dama
					 if(dama && id == 0)
						 rules.incBlackDama();
					 else if(dama && id == 1)
						 rules.incWhiteDama();
						
				 } //getPawn != null
				 
				 if(ate == null){	 	 // ha mangiato
					 g.sendMessage(3);
					 return;}
					 
				 if(rules.eatableRounds(targetCell, id)){ //posso rimangiare?
					 source = targetCell;
					 secondTime = true;//si
					 count =1;}
				 else{			
					 secondTime = false;//no
					 ate = null;
					 source = null;
					 dest = null;
					 count = 0;
					 g.sendMessage(3); 
				 }
			 }
			 if(targetCell.getColor() == Cella.bianco.getRGB()) return;
		 }
	 }
	 