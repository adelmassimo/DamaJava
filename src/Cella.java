import java.awt.*;
import javax.swing.*;


public class Cella extends JPanel {
		 private int x, y;
		 public static final Color bianco = new Color(234,232,215);
	     public static final Color nero = new Color(57,60,50);
     	 private boolean isCliked;
		 private Color bckColor;
		 private Pawn pawn;
		 public Cella( int x, int y) {
			 pawn = null;
			 setBck(x+y);
			 this.x = x;
			 this.y = y;
			 setOpaque( true );
			 bckColor = getBackground();
			 setBorder( BorderFactory.createLineBorder(Color.BLACK, 3) );}
		 public void setBck(int i){ setBackground( (i%2 == 0)?nero:bianco );}
		 public void switchColor() {
			 setBackground(isCliked ? bckColor : Color.GREEN);
			 isCliked = !isCliked;}
		 public void switchColor(Color c) {
			 setBackground(isCliked ? bckColor : c);
			 isCliked = !isCliked;}
		 public int getColor(){
			 return getBackground().getRGB();
		 }
		 public boolean isFree(){
			if(pawn == null) return true;
			else return false;
		}
		 public void setPawn(Pawn p){pawn = p;}
		 public Pawn getPawn(){return pawn;}
		 public int getIndex(){ return x*8 + y; }
	 }// Cella