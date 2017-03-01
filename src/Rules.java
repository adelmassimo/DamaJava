import java.awt.*;

public class Rules{ // non è statico per alcuni motivi
		private Cella[] celle;
		private Cella[] eatCell; // Array su cui salvo temporaneamente le celle in cui posso saltare se mangio una pedina
		private int left,right,top,down;
		private Cella morte;  //Cimitero dei pawn. 
		//conto i morti ogni volta che muore un pedone
		private int blackDead;	
		private int whiteDead;	
		private int blackDama;
		private int whiteDama;
		public long startTime;
		public long endTime = System.currentTimeMillis(); 
		
		
		public Rules(Cella[] c){
			celle = c;
			eatCell = new Cella[4];
			blackDead = 0;
			whiteDead = 0;
			blackDama = 0;
			whiteDama = 0;
			for(int i =0;i<4;i++)
				eatCell[i] = null;
			 startTime = System.currentTimeMillis(); 
		}
		private void setRoundLimits(int i){
			 int y = i%8;
			 int x = (i-y)/8;
			 
			 left = (y-1 < 0)?y:(y-1);
			 right = (y+1 > 7)?y:(y+1);
			 down = (x > 7)?x:(x+1);
			 top = (x-1 < 0)?x:(x-1);
		}
		private boolean isNearToBordLine(int index){
			int y = index%8;
			int x = (index-y)/8;

			if(7-y < 1) return true;
			if(y == 0) return true;
			if(7-x < 1) return true;
			if(x == 0) return true;
			
			return false;
		}
		public boolean legalCell(Cella c, int id, boolean dama){
			 if(c.getColor() == Cella.bianco.getRGB()) return false;
			 if(c.getPawn() != null){
				 if(c.getPawn().isMine()){
					 return false;// ho i bianchi e clicco un nero. RAZZISTA!! Da qualche parte scrivere che id=0 sono i bianchi..
				 }
				 else{
					 //TODO trovo un pedone da mangiare
					 // eatCell salva la cella nella quale potrei andare a digerire dopo mangiato
					 int y = c.getIndex()%8;
					 int x = (c.getIndex()-y)/8;
					 x = down - x; //se è = 0 so che il pedone da mangiare mi sta sotto
					 y = right - y; // se è = 0 so che """""""""""""""""""""""""" a destra

					 if(c.getPawn().checkIsDama() && !dama) return false;
					 if( x == 0 && y == 0 && c.getIndex()+9 < 64 && !isNearToBordLine(c.getIndex())) // in basso a destra
					 	{eatCell[0]=celle[c.getIndex()+9];
						 if(eatCell[0].isFree())
							 eatCell[0].switchColor(Color.GREEN);
						 return true;}
					 if( x == 0 && y != 0 && c.getIndex()+7 < 64 && !isNearToBordLine(c.getIndex()) ) // in basso a sinistra
						{eatCell[1]=celle[c.getIndex()+7];
						 if(eatCell[1].isFree())
							 eatCell[1].switchColor(Color.GREEN);
						 return true;}
					 if( x != 0 && y == 0 && c.getIndex()-7 > 0 && !isNearToBordLine(c.getIndex()) ) // in alto a destra
						{eatCell[2]=celle[c.getIndex()-7];
						 if(eatCell[2].isFree())
							 eatCell[2].switchColor(Color.GREEN);
						 return true;}
					 if( x != 0 && y != 0 && c.getIndex()-9 > 0 && !isNearToBordLine(c.getIndex()) ) // in alto a sinistra
					 	{eatCell[3]=celle[c.getIndex()-9];
						 if(eatCell[3].isFree())
							 eatCell[3].switchColor(Color.GREEN);
						 return true;}
				 }
			 }
			 if(!c.isFree()) return false;
			 c.switchColor(Color.GREEN);
			 return true;
		 }
		public boolean firstClick(Cella c, int id){
			 if(c.getPawn().isMine()){		 
				 return legalRounds(c,id);}
			 else
				 return false;			 
		}
		public boolean legalRounds(Cella c, int id){
			 setRoundLimits(c.getIndex());
			 boolean near = false;
			 boolean canMove = false;
			 boolean eaterIsDama = c.getPawn().checkIsDama();
			 
			 if(c.getPawn().checkIsDama() == false)
				 down--;
			  //controllo la legalità delle celle nell'circolare di c di raggio 1
			 for(int i = top; i <= down; i++)
				 for(int j = left; j <= right; j++){
					near = legalCell(celle[i*8+j], id, eaterIsDama);
					canMove = (canMove || near )?true:false;
					// Se posso fare almeno una mossa
			}
			//c.setBackground( (canMove)?Color.YELLOW:Color.RED);		 
			return canMove;
		}
		public boolean eatableRounds(Cella c, int id){
			 setRoundLimits(c.getIndex());
			 boolean near = false;
			 boolean canMove = false;
			 boolean eaterIsDama = c.getPawn().checkIsDama();
			 
			 if(c.getPawn().checkIsDama() == false)
				 down--;
			  //controllo la legalità delle celle nell'circolare di c di raggio 1
			 for(int i = top; i <= down; i++)
				 for(int j = left; j <= right; j++){
					near = canEat(celle[i*8+j], id, eaterIsDama);
					canMove = (canMove || near )?true:false;
					// Se posso fare almeno una mossa
			}
			//c.setBackground( (canMove)?Color.YELLOW:Color.RED);		 
			return canMove;
		}
		public boolean canEat(Cella c, int id, boolean dama){
			 if(c.getColor() == Cella.bianco.getRGB()) return false;
			 if(c.getPawn() == null) return false;
			 
			 if(c.getPawn().isMine()){
				return false;// ho i bianchi e clicco un nero. RAZZISTA!! Da qualche parte scrivere che id=0 sono i bianchi..
			 }
			 else{
				//TODO trovo un pedone da mangiare
				// eatCell salva la cella nella quale potrei andare a digerire dopo mangiato
				int y = c.getIndex()%8;
				int x = (c.getIndex()-y)/8;
				x = down - x; //se è = 0 so che il pedone da mangiare mi sta sotto
				y = right - y; // se è = 0 so che """""""""""""""""""""""""" a destra
					 
				if(c.getPawn().checkIsDama() && !dama) return false;
				if( x == 0 && y == 0 && c.getIndex()+9 < 64 && !isNearToBordLine(c.getIndex())) // in basso a destra
					{eatCell[0]=celle[c.getIndex()+9];
					 if(eatCell[0].isFree()){
						eatCell[0].switchColor(Color.GREEN);
						return true;}
					}
				
				if( x == 0 && y != 0 && c.getIndex()+7 < 64 && !isNearToBordLine(c.getIndex()) ) // in basso a sinistra
					{eatCell[1]=celle[c.getIndex()+7];
					 if(eatCell[1].isFree()){
						 eatCell[1].switchColor(Color.GREEN);
						 return true;}
					 }
				if( x != 0 && y == 0 && c.getIndex()-7 > 0 && !isNearToBordLine(c.getIndex()) ) // in alto a destra
					{eatCell[2]=celle[c.getIndex()-7];
					 if(eatCell[2].isFree()){
						eatCell[2].switchColor(Color.GREEN);
						 return true;}
					}
				if( x != 0 && y != 0 && c.getIndex()-9 > 0 && !isNearToBordLine(c.getIndex()) ) // in alto a sinistra
					{eatCell[3]=celle[c.getIndex()-9];
					 if(eatCell[3].isFree()){
						 eatCell[3].switchColor(Color.GREEN);
						 return true;}
					}
				 }
			 return false;
		 }
		public void resetCells(){
			for(int i = 0; i < 64; i++){
				if(celle[i].getColor() == Color.GREEN.getRGB()){
					celle[i].switchColor();
					if(i < 4)
						 eatCell[i] = null;}
				
			}
		}
		public Cella atePawn(Cella c, Griglia g){
			for(int i = 0; i<4; i++){
				 if(eatCell[i] != null)
				 	if(c.getIndex() == eatCell[i].getIndex()){
				 		if(i==0)
				 			morte=celle[c.getIndex()-9];
				 		if(i==1)
				 			morte=celle[c.getIndex()-7];
				 		if(i==2)
				 			morte=celle[c.getIndex()+7];
				 		if(i==3)
				 			morte=celle[c.getIndex()+9];
						
						
						if(morte.getPawn() != null){
							
							if(morte.getPawn().getColor().equals("black"))
								 incBlackDead();
							 if(morte.getPawn().getColor().equals("white"))
								 incWhiteDead();
							 //controllo se la partita è finita
							 if(blackDead == 12){
								 //parita finita, hanno vinto i bianchi -> 1
								g.writeWinner(1);
							 }
							 if(whiteDead == 12){
								 //partita finita, hanno vinto i neri -> 0
									 g.writeWinner(0);
							}
							 
							morte.getPawn().removeFromCell();
							
							
						} //getPawnn != null
						
						
						
				 		return morte;
				 	}
			}
			
			return null;
		}
		public boolean secondClick(Cella c){
			if(c.getBackground().getRGB() == Color.GREEN.getRGB()) 
				return true;
			 return false;
		}
		public boolean haveEat(Cella c){
			for(int i = 0; i < 4; i++)
				if(eatCell[i]!=null){
						return true;}
			return false;
		}
		public void cleanEatCells(){
			for(int i = 0; i < 4; i++)
				eatCell[i] = null;
		}	
		public int getWhiteDead(){ return whiteDead; }
		public void incWhiteDead(){ 
			whiteDead++;
			System.out.println("whiteDead: " + whiteDead);
		}
		public void incBlackDead(){ 
			blackDead++;
			System.out.println("blackDead: " + blackDead);
		}
		public int getBlackDead(){ return blackDead; }
		public int getwhiteDama(){ return whiteDama; }
		public int getblackDama(){ return blackDama; }
		public void incBlackDama(){ blackDama++; }
		public void incWhiteDama(){ whiteDama++; }
		
}//Rules