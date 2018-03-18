import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class HW3_103403519 extends JFrame {
	private static PokeSerializable pokemon = new PokeSerializable();
	private Image pic;
	private Panel panel;
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	JPanel p3 = new JPanel();
	private JTextField text;
	static private JButton candy, read, save, newsave;
	private JLabel candycount, fileinfo;
	private GridLayout gridlayout;
	private Color color;
	
	private static String line;
	private static String line1[], line2[];
	private BufferedReader br;
	
	int count;
	private String evolutionstatus, nickname;
	private String small = "", medium = "medium", large = "large";
	private int filestatus = 0; //0=file, 1=openfile
	private static String filename, filepath;
	
	public HW3_103403519(){
		super("神奇寶貝養成囉！");
		pic = new Image();
		panel = new Panel();
		add(pic, BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);
	}
	
	public class Image extends JPanel{
		private Color color;
		ImageIcon icon;
		public void paintComponent(Graphics g){
			setBackground(color.WHITE);
			if(evolutionstatus == "")
				icon = new ImageIcon(getClass().getResource("small.png"));
			else if(evolutionstatus == "medium")
				icon = new ImageIcon(getClass().getResource("medium.png"));
			else if(evolutionstatus == "large")
				icon = new ImageIcon(getClass().getResource("large.png"));
			g.drawImage(icon.getImage(), 200, 0, null);
		}
	}
	
	public class Panel extends JPanel{
		Panel(){
			evolutionstatus = pokemon.getMonster();
			nickname = pokemon.getNickname();
			count = pokemon.getCandy();
			try{
				br = new BufferedReader(new FileReader("/Users/Wilson60103/Desktop/School/進階JAVA/HW3/pokemon.txt"));
				int i=0;
				while ((line=br.readLine()) != null) {
					if(i == 0)
						line1 = line.split(" "); //line1[0]=small.png, line1[1]=25
					else if(i == 1)
						line2 = line.split(" "); //line2[0]=medium.png, line2[1]=100
					i++;
				}
				br.close();
			}
			catch(IOException ioexception){
				System.out.println(ioexception);
			}
			gridlayout = new GridLayout(4,1);
			setLayout(gridlayout);
			add(p1);
			add(p2);
			add(p3);
		
			text = new JTextField(nickname, 66);
			p1.setBackground(color.white);
			p1.add(text);
		
			candy = new JButton("為他吃糖！");
			p2.add(candy);
			candycount = new JLabel("0/25");
			p2.add(candycount);
			
			read = new JButton("讀取遊戲");
			p3.add(read);
			save = new JButton("存檔");
			p3.add(save);
			newsave = new JButton("另存新檔");
			p3.add(newsave);
			fileinfo = new JLabel("New File");
			add(fileinfo);
		
			ButtonHandler handler = new ButtonHandler();
			candy.addActionListener(handler);
			read.addActionListener(handler);
			save.addActionListener(handler);
			newsave.addActionListener(handler);
		}
	}
	public class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand() == "為他吃糖！"){
				count++;
				if(evolutionstatus.equals(small)){ //count == 25
					candycount.setText(String.format("%d/25", count));
					if(count == Integer.parseInt(line1[1])){
						JOptionPane.showMessageDialog(null, "要進化了！");
						count = 0;
						candycount.setText(String.format("%d/100", count));
						evolutionstatus = "medium";
						pic.repaint();
					}
				}
				else if(evolutionstatus.equals(medium)){
					candycount.setText(String.format("%d/100", count));
					if(count == Integer.parseInt(line2[1])){
						JOptionPane.showMessageDialog(null, "要進化了！");
						JOptionPane.showMessageDialog(null, "您的神奇寶貝已最終進化");
						evolutionstatus = "large";
						pic.repaint();	
					}
				}
			}
			else if(e.getActionCommand() == "存檔"){
				if(filestatus == 0){
					try{
						String savename = JOptionPane.showInputDialog(("輸入存檔檔名"));
						if(! savename.equals(null)){
							nickname = text.getText();
							pokemon.setCandy(count);
							pokemon.setMonster(evolutionstatus);
							pokemon.setNickname(nickname);
							JOptionPane.showMessageDialog(null, "您的存檔檔名是" + savename);
							FileOutputStream fs = new FileOutputStream(savename);
							ObjectOutputStream output = new ObjectOutputStream(fs);
							output.writeObject(pokemon);
							output.close();	
							filename = savename;
							filestatus = 1;
							filepath = "/Users/Wilson60103/Documents/workspace/HW3_103403519/"+savename;
							fileinfo.setText(filepath);
						}
					}	
					catch(Exception e3){}
				}
				else if(filestatus == 1){
					nickname = text.getText();
					pokemon.setCandy(count);
					pokemon.setMonster(evolutionstatus);
					pokemon.setNickname(nickname);
					try{
						FileOutputStream fs = new FileOutputStream(filename);
						ObjectOutputStream output = new ObjectOutputStream(fs);
						output.writeObject(pokemon);
						output.close();	
					}
					catch(Exception e1){e1.printStackTrace();}
				}
			}
			else if(e.getActionCommand() == "另存新檔"){
				try{
					String savename = JOptionPane.showInputDialog(("輸入存檔檔名"));
					if(! savename.equals(null)){
						nickname = text.getText();
						pokemon.setCandy(count);
						pokemon.setMonster(evolutionstatus);
						pokemon.setNickname(nickname);
						JOptionPane.showMessageDialog(null, "您的存檔檔名是" + savename);
						FileOutputStream fs = new FileOutputStream(savename);
						ObjectOutputStream output = new ObjectOutputStream(fs);
						output.writeObject(pokemon);
						output.close();	
						filestatus = 1;
						filepath = "/Users/Wilson60103/Documents/workspace/HW3_103403519/"+savename;
						fileinfo.setText(filepath);
					}
				}	
				catch(Exception e3){}
			}
			else if(e.getActionCommand() == "讀取遊戲"){
				try{
					File file = getFileOrDirectoryPath();
					if(file != null){
						FileInputStream fi = new FileInputStream(file);
						ObjectInputStream input = new ObjectInputStream(fi);
						pokemon = (PokeSerializable)input.readObject();
						input.close();
					
						count = pokemon.getCandy();
						evolutionstatus = pokemon.getMonster();
						if(evolutionstatus.equals(small))
							evolutionstatus = small;
						else if(evolutionstatus.equals(medium))
							evolutionstatus = medium;
						else if(evolutionstatus.equals(large))
							evolutionstatus = large;
						pic.repaint();	
						nickname = pokemon.getNickname();
						text.setText(nickname);	
					
						if(evolutionstatus.equals(small))
							candycount.setText(String.format("%d/25", count));
						else if(evolutionstatus.equals(medium))
							candycount.setText(String.format("%d/100", count));
						else if(evolutionstatus.equals(large))
							candycount.setText("100/100");
						
						filepath = file.getPath();
						fileinfo.setText(filepath);
						filename = file.getName();
						filestatus = 1;
					}
				}
				catch(Exception e2){e2.printStackTrace();}
			}
		}
	}

	private File getFileOrDirectoryPath() throws FileNotFoundException, StreamCorruptedException{
		JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    int result = fileChooser.showOpenDialog(this);
	    if (result == JFileChooser.CANCEL_OPTION)
	    	return null;
	    else
	    	return fileChooser.getSelectedFile();
		
	} 
	
	public static void main(String args[]) {
		HW3_103403519 game = new HW3_103403519();
		game.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		game.setSize( 800, 600 ); 
		game.setVisible( true ); 
	}
}
