import javax.swing.*;


public class Pawn extends JLabel{
	 private boolean mine;
	 private Cella c;		//cella a cui appartiene il pedoneone
	 private boolean alive;	// è stato mangiato??
	 private int id; 
	 private boolean isDama;
	 private String color;
	 
	 
	 public Pawn(Cella c, String color, int id){
		 super("", new ImageIcon("images/" + color + ".png"), JLabel.CENTER);
		 this.id = id;
		 this.c = c;
		 alive = true;
		 mine = ( (color.equals("white") && id==0) || (color.equals("black") && id==1) )?true:false;
		 isDama = false;
		 this.color = color;
		 
	 }
	 
	 public void addToCell(){
		c.add(this);
		c.setPawn(this);
	 }
	 public boolean isMine(){return mine;}
	 public void removeFromCell(){
		 c.remove(this);
		 c.switchColor(c.bianco);
		 c.switchColor(c.nero);
		 c.setPawn(null);
		 c = null;
	 }
	 public boolean moveTo(Cella dest){ // faccio ritornare true se il pedone si è trasformato in dama
		 removeFromCell();
		 c=dest;
		 addToCell();
		 int y = c.getIndex()%8;
		 int x = (c.getIndex()-y)/8;		 
		 if(x == 0 || x == 7){
			 chenageInDama();
			 return true;
		 }
		 
		// c.switchColor(c.bianco);
		 //c.switchColor(c.nero);
		 c.updateUI();
		 return false;
	 }
	 
	 public boolean checkIsDama(){ return isDama; }
	 public void chenageInDama(){
		 if(isDama)
			 isDama = false;
		 else isDama = true;
		 
		 ImageIcon newDama = new ImageIcon("images/Dama" + color + ".png");
		 this.setIcon(newDama);
		 c.updateUI();
	 }
	 public String getColor(){ return color; }
	 
	 
	 
	 
	 
	 
 }//Pawn