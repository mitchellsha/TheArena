package environment;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Frame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel score;
	private static MakeSound sound;

	
	public Frame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("The Arena");
		setLayout(new BorderLayout());
		setSize(1024, 768);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	private void createScoreDisplay(PlayspacePanel playSpacePanel){
		score = new JLabel("SCORE: " + playSpacePanel.getScore());
		score.setFont(new Font(score.getFont().getName(), Font.PLAIN, 20));
		add(score, BorderLayout.SOUTH);
	}
	
	private void startGame() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		sound = new MakeSound();

		PlayspacePanel play = new PlayspacePanel(Frame.this);
		play.setBorder(new EmptyBorder(5,5,5,5));
		
		contentPane.add(play, BorderLayout.CENTER);
		
		add(contentPane);
		createScoreDisplay(play);
	}
	
	public void setScoreToUser(int point){
		score.setText("SCORE: " + point);
	}
	
	public static void main(String[] args0) {
		Frame frame = new Frame();
		frame.startGame();
		frame.setVisible(true);
		sound.playSound("src/environment/SpaceJam.au");

	}
}
