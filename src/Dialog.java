import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

public class Dialog{   // Creo la finestra prima di giocare
	private JFrame begin;
	private JCheckBox time;
	private JList<String> timeValue;
	private JTextField user1; 
	private JTextField user2; 
	
	private class Clicker extends MouseAdapter {		 
		public void mouseClicked(MouseEvent me){
			int vTime;
			if(time.isSelected()){
				String value = (String) timeValue.getSelectedValue();
				value = value.substring(0, 2);
				vTime = Integer.parseInt(value);
			}
			else
				vTime = 0;
			
			Server.play(user1.getText(), user2.getText(), time.isSelected(), vTime);
			begin.setVisible(false);
	 }
	}// Clicker
	
	public Dialog(){ // Grafica della Finestra iniziale
		
		begin = new JFrame("New Game!");
		user1 = new JTextField();
		user2 = new JTextField();
		time = new JCheckBox("Sfida a Tempo");
		
		String voci[] = {"05 Sec", "10 Sec", "15 Sec"};
		timeValue = new JList<String>(voci);
		
		JLabel startImg = new JLabel("", new ImageIcon("images/start.png"), JLabel.CENTER);
		
		timeValue.setAutoscrolls(true);
		timeValue.setVisibleRowCount(4);  
		
		user1.setText("Ernie il pollo");
		user1.setSelectionStart(0);
		user1.setSelectionEnd(user1.getText().length());
		user1.setPreferredSize(new Dimension(200, 50));
		
		user2.setText("Peter Griffin");
		user2.setSelectionStart(0);
		user2.setSelectionEnd(user2.getText().length());
		user2.setPreferredSize(new Dimension(200, 50));
		
		begin.setPreferredSize(new Dimension(200, 300));
		begin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		begin.setResizable(false);
	
		JButton start = new JButton();
		start.addMouseListener(new Clicker());
		start.setBackground(Color.ORANGE);
		start.add(startImg);
		start.setPreferredSize(new Dimension(250, 200));
		
		Container pane = begin.getContentPane();
		pane.setLayout(new GridLayout(5,1));
		
		pane.add(user1);
		pane.add(user2);
		pane.add(time);
		pane.add(timeValue);
		pane.add(start);
		
		start.setFocusable(true);
		begin.setLocationRelativeTo(null);
		begin.pack();
		begin.setVisible(true);

	}
}