
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.Dimension;
import javax.swing.SpringLayout;
import javax.swing.JTextArea;

public class MainFrame {
	public static final int WIDTH = 1000, HEIGHT=600;
	private JFrame frame;
	private final JSpinner numNodesSpinner = new JSpinner();
	private boolean genetics=true;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("TSPGEN");
		frame.setBounds(100, 100, 100+WIDTH, 100+HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		DisplayPanel myPanel = new DisplayPanel();
		springLayout.putConstraint(SpringLayout.NORTH, myPanel, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, myPanel, 194, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, myPanel, 671, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, myPanel, 1093, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(myPanel);
		myPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panel, 671, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.WEST, myPanel);
		frame.getContentPane().add(panel);
		SpringLayout sl_panel = new SpringLayout();
		sl_panel.putConstraint(SpringLayout.NORTH, numNodesSpinner, 129, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, numNodesSpinner, 109, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, numNodesSpinner, -10, SpringLayout.EAST, panel);
		panel.setLayout(sl_panel);
		
		
		JButton startAlgorithmButton = new JButton("Start algorithm");
		sl_panel.putConstraint(SpringLayout.WEST, startAlgorithmButton, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, startAlgorithmButton, -10, SpringLayout.EAST, panel);
		panel.add(startAlgorithmButton);
		

		myPanel.setTriggerButton(startAlgorithmButton);
		JButton button = new JButton("Generate nodes");
		sl_panel.putConstraint(SpringLayout.SOUTH, startAlgorithmButton, -9, SpringLayout.NORTH, button);
		sl_panel.putConstraint(SpringLayout.EAST, button, -10, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.WEST, button, 0, SpringLayout.WEST, startAlgorithmButton);
		panel.add(button);
		button.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				int currentValue = (int)numNodesSpinner.getValue();
				startAlgorithmButton.setText("Start algorithm");
				myPanel.stopAlgorithm();
			    myPanel.generateNodes(currentValue);
			  } 
			} );
		
		JRadioButton genAlgButton = new JRadioButton("Genetic algorithm");
		sl_panel.putConstraint(SpringLayout.SOUTH, button, -6, SpringLayout.NORTH, genAlgButton);
		sl_panel.putConstraint(SpringLayout.EAST, genAlgButton, -10, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.WEST, genAlgButton, 0, SpringLayout.WEST, startAlgorithmButton);
		panel.add(genAlgButton);
		genAlgButton.setSelected(true);
		
		ButtonGroup group = new ButtonGroup();
		group.add(genAlgButton);
		
		JRadioButton allSolButton = new JRadioButton("Check all solutions");
		sl_panel.putConstraint(SpringLayout.SOUTH, allSolButton, -6, SpringLayout.NORTH, numNodesSpinner);
		sl_panel.putConstraint(SpringLayout.SOUTH, genAlgButton, -6, SpringLayout.NORTH, allSolButton);
		sl_panel.putConstraint(SpringLayout.EAST, allSolButton, -10, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.WEST, allSolButton, 0, SpringLayout.WEST, startAlgorithmButton);
		panel.add(allSolButton);
		group.add(allSolButton);
		
		JLabel lblNewLabel = new JLabel("Nodes:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblNewLabel, 2, SpringLayout.NORTH, numNodesSpinner);
		sl_panel.putConstraint(SpringLayout.WEST, lblNewLabel, 0, SpringLayout.WEST, startAlgorithmButton);
		panel.add(lblNewLabel);
		numNodesSpinner.setMinimumSize(new Dimension(29, 18));
		panel.add(numNodesSpinner);
		numNodesSpinner.setValue(new Integer(10));
		
		JLabel lblGenerations = new JLabel("Generations:");
		sl_panel.putConstraint(SpringLayout.WEST, lblGenerations, 10, SpringLayout.WEST, panel);
		panel.add(lblGenerations);
		
		JSpinner numGensSpinner = new JSpinner();
		sl_panel.putConstraint(SpringLayout.NORTH, lblGenerations, 3, SpringLayout.NORTH, numGensSpinner);
		sl_panel.putConstraint(SpringLayout.SOUTH, numNodesSpinner, -7, SpringLayout.NORTH, numGensSpinner);
		sl_panel.putConstraint(SpringLayout.NORTH, numGensSpinner, 154, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, numGensSpinner, 0, SpringLayout.WEST, numNodesSpinner);
		sl_panel.putConstraint(SpringLayout.EAST, numGensSpinner, -10, SpringLayout.EAST, panel);
		panel.add(numGensSpinner);
		numGensSpinner.setValue(new Integer(1000));
		
		JLabel lblPopulationSize = new JLabel("Population size:");
		sl_panel.putConstraint(SpringLayout.WEST, lblPopulationSize, 0, SpringLayout.WEST, startAlgorithmButton);
		panel.add(lblPopulationSize);
		
		JSpinner popSizeSpinner = new JSpinner();
		sl_panel.putConstraint(SpringLayout.WEST, popSizeSpinner, 0, SpringLayout.WEST, numNodesSpinner);
		sl_panel.putConstraint(SpringLayout.EAST, popSizeSpinner, -10, SpringLayout.EAST, panel);
		panel.add(popSizeSpinner);
		popSizeSpinner.setValue(new Integer(150));
		
		JLabel lblEliteSize = new JLabel("Elite size:");
		sl_panel.putConstraint(SpringLayout.SOUTH, lblEliteSize, -446, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblPopulationSize, -11, SpringLayout.NORTH, lblEliteSize);
		sl_panel.putConstraint(SpringLayout.WEST, lblEliteSize, 0, SpringLayout.WEST, startAlgorithmButton);
		panel.add(lblEliteSize);
		
		JSpinner eliteSizeSpinner = new JSpinner();
		sl_panel.putConstraint(SpringLayout.SOUTH, popSizeSpinner, -6, SpringLayout.NORTH, eliteSizeSpinner);
		sl_panel.putConstraint(SpringLayout.WEST, eliteSizeSpinner, 0, SpringLayout.WEST, numNodesSpinner);
		sl_panel.putConstraint(SpringLayout.EAST, eliteSizeSpinner, -10, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.NORTH, eliteSizeSpinner, -3, SpringLayout.NORTH, lblEliteSize);
		panel.add(eliteSizeSpinner);
		eliteSizeSpinner.setValue(new Integer(20));
		
        float min = 0.000f;
        float value = 0.05f;
        float max = 1.000f;
        float stepSize = 0.001f;
        SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, stepSize);
		JSpinner mutRatioSpinner = new JSpinner(model);
		sl_panel.putConstraint(SpringLayout.NORTH, mutRatioSpinner, 6, SpringLayout.SOUTH, eliteSizeSpinner);
		sl_panel.putConstraint(SpringLayout.WEST, mutRatioSpinner, 0, SpringLayout.WEST, numNodesSpinner);
		sl_panel.putConstraint(SpringLayout.EAST, mutRatioSpinner, -10, SpringLayout.EAST, panel);
		panel.add(mutRatioSpinner);
		
		JLabel lblMutationRatio = new JLabel("Mutation ratio:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblMutationRatio, 12, SpringLayout.SOUTH, lblEliteSize);
		sl_panel.putConstraint(SpringLayout.WEST, lblMutationRatio, 0, SpringLayout.WEST, startAlgorithmButton);
		panel.add(lblMutationRatio);
		
		JTextArea output = new JTextArea();
		sl_panel.putConstraint(SpringLayout.WEST, output, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, output, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, output, 0, SpringLayout.EAST, startAlgorithmButton);
		panel.add(output);
		
		allSolButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            if(allSolButton.isSelected()) {
	            	numGensSpinner.setEnabled(false);
	            	eliteSizeSpinner.setEnabled(false);
	            	popSizeSpinner.setEnabled(false);
	            	mutRatioSpinner.setEnabled(false);
	            	genetics=false;
	            }

	        }
	    });
		
		genAlgButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            if(genAlgButton.isSelected()) {
	            	numGensSpinner.setEnabled(true);
	            	eliteSizeSpinner.setEnabled(true);
	            	popSizeSpinner.setEnabled(true);
	            	mutRatioSpinner.setEnabled(true);
	            	genetics=true;
	            }

	        }
	    });
		
		startAlgorithmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(startAlgorithmButton.getText().toString().equals("Start algorithm")) {
				int maxGen=(int)numGensSpinner.getValue(), popSize=(int)popSizeSpinner.getValue(), eliteSize=(int)eliteSizeSpinner.getValue();
				double mutationRatio = (double)mutRatioSpinner.getValue();
				myPanel.startAlgorithm(maxGen,popSize,eliteSize,mutationRatio,genetics);
				startAlgorithmButton.setText("Stop Algorithm");
				}else {
					startAlgorithmButton.setText("Start algorithm");
					myPanel.stopAlgorithm();
				}
			}
		});
		output.setEditable(false);
		myPanel.setOutputLabel(output);
		
		JLabel lblOutput = new JLabel("Output:");
		sl_panel.putConstraint(SpringLayout.NORTH, output, 6, SpringLayout.SOUTH, lblOutput);
		sl_panel.putConstraint(SpringLayout.NORTH, lblOutput, 6, SpringLayout.SOUTH, lblMutationRatio);
		sl_panel.putConstraint(SpringLayout.WEST, lblOutput, 0, SpringLayout.WEST, startAlgorithmButton);
		panel.add(lblOutput);
		
	}
}
