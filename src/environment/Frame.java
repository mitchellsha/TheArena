package environment;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.threed.jpct.util.KeyMapper;

public class Frame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	public Frame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("The Arena");
		setLayout(new BorderLayout());
		setSize(1024, 768);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	private void startGame() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		
		PlayspacePanel play = new PlayspacePanel(new KeyMapper(Frame.this));
		play.setBorder(new EmptyBorder(5,5,5,5));
		
		contentPane.add(play, BorderLayout.CENTER);
		
		add(contentPane);
	}
	
	public static void main(String[] args0) {
		Frame frame = new Frame();
		frame.startGame();
		frame.setVisible(true);
	}
}
