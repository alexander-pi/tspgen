import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;


public class DisplayPanel extends JPanel {
	private int  nodeD=6,numNodes,currentGen=0,maxGen=50,popSize=100,eliteSize=20, bestGenSol;
	private float bestDistance=Float.MAX_VALUE,lastBestSolTime,currentBestDistance;
	private double mutationRatio=0.0d;
	private Path bestPath, currentBestPath;
	private int lexOrder[];
	private Random random;
	private Node nodes[];
	private ArrayList<Path> population;
	private ArrayList<String> outputLines;
	private boolean nodesOnScreen,runningAlgorithm, freezeResults,iterationFinished,genetics;
	private JButton triggerButton;
	private long startTime;
	private JTextArea outputLabel;

	
	public DisplayPanel() {
		random = new Random();
		setBackground(Color.BLACK);
		outputLines = new ArrayList<String>();
		for(int i=0;i<6;i++)
			outputLines.add("");
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(iterationFinished && runningAlgorithm) {
            		iterationFinished=false;
            		update();
            		repaint();
            	}
            }
        });
        timer.start();
    }
	public void setOutputLabel(JTextArea label) {outputLabel = label;}
	public void setTriggerButton(JButton button) {triggerButton=button;}
	public void startAlgorithm(int maxGen,int popSize, int eliteSize,double mutationRatio,boolean genetics) {
		if(nodesOnScreen) {
		this.maxGen = maxGen;this.popSize=popSize;this.eliteSize=eliteSize;this.mutationRatio=mutationRatio;this.genetics=genetics;
		if(genetics) {
			population = new ArrayList<Path>();
			currentGen=0;
		}else {
			lexOrder = new int[nodes.length];
			for(int i=0;i<nodes.length;i++)
				lexOrder[i]=i;
			bestPath = new Path(lexOrder);
			bestDistance = bestPath.distance;
			currentBestPath = new Path(lexOrder);
		}
		freezeResults=false;
		runningAlgorithm=true;
		for(int i=0;i<6;i++)
			outputLines.set(i,"");
		outputLabel.setText("");
		startTime=System.currentTimeMillis();
		}else JOptionPane.showMessageDialog(this, "Generate some points first");
	}
	public void stopAlgorithm() {
		runningAlgorithm=false;
		freezeResults=false;
	}
	public void generateNodes(int num) {
		numNodes=num;
		nodesOnScreen=true;
		outputLabel.setText("");
		nodes = new Node[num];
		for(int i = 0; i < numNodes;i++) {
			nodes[i]=new Node(random.nextInt(getWidth()),random.nextInt(getHeight()));
		}
		repaint();
	}
    public void update() {
    	if(genetics)
    		updatePopulation(population);
    	else 
    		updateNextSolution();
    	updateOutput();
    }
    private void updateOutput() {
    	float elapsedTime=(float)(System.currentTimeMillis()-startTime)/1000f;
    	
    	outputLines.set(0,"Best distance: "+String.format("%.02f", bestDistance)+",");
    	outputLines.set(1,"found in: "+lastBestSolTime+" s"+(genetics?",":""));
    	
    	if(genetics) {
    		outputLines.set(2, "in "+bestGenSol+"th generation");
    		outputLines.set(3, currentGen+"th generation");
    	}
    	
    	outputLines.set(genetics?4:2,"Execution time: "+String.format("%.02f", elapsedTime)+" s");
    	outputLabel.setText(getOutputText());
    }
    private String getOutputText() {
    	String output="";
    	for(String line:outputLines)
    		output+=line+"\n";
    	return output;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if(nodesOnScreen) {
            g2.setColor(Color.WHITE);
            for(Node node: nodes) {
            	Ellipse2D.Float ellipse = new Ellipse2D.Float(node.x, node.y,nodeD, nodeD);
                g2.fill(ellipse);
            }
        }
        if(runningAlgorithm || freezeResults) {
        	if(runningAlgorithm) {g2.setColor(Color.WHITE);
        	paintRoute(g2,currentBestPath);}
            g2.setColor(Color.RED);
            paintRoute(g2,bestPath);
            }
        iterationFinished=true;
    }
    private void paintRoute(Graphics2D g2, Path path) {
        GeneralPath gPath = new GeneralPath();
        gPath.moveTo(nodes[path.route[0]].x, nodes[path.route[0]].y);
        for(int i=1;i<path.route.length;i++) {
        	gPath.lineTo(nodes[path.route[i]].x, nodes[path.route[i]].y);
        }
        gPath.lineTo(nodes[path.route[0]].x, nodes[path.route[0]].y);
        g2.draw(gPath);

    }

    private void updateNextSolution() {
    	int largestI = -1;
    	if(lexOrder[0]>lexOrder[lexOrder.length-1]) {
      		triggerButton.setText("Start algorithm");
      		runningAlgorithm=false;
      		freezeResults = true;
      		return;
    	}
    	  for (int i = 0; i < lexOrder.length - 1; i++) {
    	    if (lexOrder[i] < lexOrder[i + 1]) {
    	      largestI = i;
    	    }
    	  }
    	  if (largestI == -1) {
    	    //noLoop();
      		triggerButton.setText("Start algorithm");
      		runningAlgorithm=false;
      		freezeResults = true;
      		return;
    	  }

    	  // STEP 2
    	  int largestJ = -1;
    	  for (int j = 0; j < lexOrder.length; j++) {
    	    if (lexOrder[largestI] < lexOrder[j]) {
    	      largestJ = j;
    	    }
    	  }

    	  // STEP 3
    	  swap(lexOrder, largestI, largestJ);

    	  // STEP 4: reverse from largestI + 1 to the end
    	  int endArray[] = Arrays.copyOfRange(lexOrder, largestI+1,lexOrder.length);
    	  for(int i = 0; i < endArray.length / 2; i++)
    	  {
    	      int temp = endArray[i];
    	      endArray[i] = endArray[endArray.length - i - 1];
    	      endArray[endArray.length - i - 1] = temp;
    	  }
    	  System.arraycopy( endArray, 0, lexOrder, lexOrder.length-endArray.length, endArray.length );
    	  
    	  Path newPath = new Path(lexOrder);
    	  if(newPath.distance<bestDistance) {
  			bestDistance = newPath.distance;
  			bestPath = newPath;
  			lastBestSolTime = (System.currentTimeMillis()-startTime)/1000f;
    	  }
    	  currentBestPath=newPath;
	}
    private void swap(int a[],int i,int j) {
    	  int temp = a[i];
    	  a[i] = a[j];
    	  a[j] = temp;
    	}
	//so our panel is the corerct size when pack() is called on Jframe
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }
    private void updatePopulation(ArrayList<Path> currentGeneration){
    	if(currentGen<=0){
    		ArrayList<Integer> numbers = new ArrayList<>();
    		for(int i=0;i<numNodes;i++)
    			numbers.add(i);
    		
    		for(int i=0;i<popSize;i++){
    			Collections.shuffle(numbers);
    			int[] indexes = new int[numbers.size()];
    			for (int j=0;j<numbers.size();j++)
    				indexes[j]=numbers.get(j);
    			population.add(new Path(indexes));
    		}
    		Collections.sort(currentGeneration);
    		bestDistance = currentGeneration.get(0).distance;
    		bestPath = currentGeneration.get(0);
    		currentBestPath = currentGeneration.get(0);
    		}else{
    		Collections.sort(currentGeneration);
    		float totalFitness = calcFitness(currentGeneration);
    		ArrayList<Integer> bestIndexes = getBestIndexes(currentGeneration,totalFitness);
    		ArrayList<Path> children = nextGeneration(currentGeneration,bestIndexes);
    		currentGeneration.clear();
    		currentGeneration.addAll(children);
    		}

    	currentGen++;
    	
    	if(currentGen>=maxGen) {
    		triggerButton.setText("Start algorithm");
    		runningAlgorithm=false;
    		freezeResults = true;
    	}
    	}
    
    private ArrayList<Path> nextGeneration(ArrayList<Path> currentGeneration,ArrayList<Integer> bestIndexes){
    	ArrayList<Path> children = new ArrayList<Path>();
    	for(int i=0;i<eliteSize;i++)
            children.add(currentGeneration.get(i));
    		
        Collections.shuffle(bestIndexes);
    	int  leng = bestIndexes.size()-eliteSize;
    	for(int i=0;i<leng;i++){
            Path child = breed(currentGeneration.get(bestIndexes.get(i)), currentGeneration.get(bestIndexes.get(leng-i-1)));
            children.add(child);
    	}
    	mutatePopulation(children);
    
        return children;
    }
    
    private void mutatePopulation(ArrayList<Path> children){
    	for(Path child:children)
    		mutateIndividual(child);

    		
    }
    private void mutateIndividual(Path individual){
    	for(int i=0; i < individual.route.length;i++){
    		if(random.nextFloat()*100<mutationRatio){
    			int genB = random.nextInt(individual.route.length);
    			int temp = individual.route[i];
    			individual.route[i] = individual.route[genB];
    			individual.route[genB] = temp;
    			}
    		}
    	}
    
    private Path breed(Path parent1, Path parent2){
    	int child[], childP1[], childP2[];
        
        int geneA = random.nextInt(parent1.route.length);
        int geneB = random.nextInt(parent2.route.length);
        
        int startGene = Math.min(geneA, geneB);
        int endGene = Math.max(geneA, geneB);
        
        childP1 = new int[endGene-startGene+1];
        childP2 = new int[parent2.route.length-childP1.length];
        for (int i=startGene,j=0;i<= endGene;i++,j++)
            childP1[j]=parent1.route[i];
        
        int a =0;
    	for(int i=0;i<parent2.route.length;i++){
    		boolean foo = false;
    		for(int j=0;j<childP1.length;j++){
    			if(parent2.route[i]==childP1[j]){
    				foo=true;
    				break;}
    		}
    		if(!foo) {
    			childP2[a]=parent2.route[i];
    			a++;
    		}	
    	}
    	
    	child = new int[childP1.length+childP2.length];
    	System.arraycopy(childP1, 0, child, 0, childP1.length);
    	System.arraycopy(childP2, 0, child, childP1.length, childP2.length);
    	//System.out.println("Parent1: "+Arrays.toString(parent1.route)+"\nParent2: "+Arrays.toString(parent2.route)+"\nChild: "+Arrays.toString(child));
        return new Path(child);
    }

    
    private float calcFitness(ArrayList<Path> currentGeneration) {
    	float fitness_sum=0,currentWorstDistance=0;
    	currentBestDistance = Float.MAX_VALUE;
    	for(Path path:currentGeneration){
    		if(path.distance < bestDistance){
    			bestDistance = path.distance;
    			bestPath = new Path(path.route);
    			lastBestSolTime = (System.currentTimeMillis()-startTime)/1000f;
    			bestGenSol = currentGen;
    			}

    		if(path.distance < currentBestDistance){
    			currentBestDistance = path.distance;
    			currentBestPath = new Path(path.route);
    			}
    		if(path.distance>currentWorstDistance){
    			currentWorstDistance=path.distance;
    			}
    		}
    	for(Path path:currentGeneration){
    		float fitness =currentWorstDistance-path.distance;
    		path.fitness=fitness;
    		fitness_sum+=fitness;}
    	return fitness_sum;
    }
    
    public ArrayList<Integer> getBestIndexes(ArrayList<Path> currentGeneration,float totalFitness){
    	float cum_sum[] = new float[currentGeneration.size()];
    	ArrayList<Integer> bestIndexes= new ArrayList<Integer>();
    	for(int i =0;i<currentGeneration.size();i++){
    		Path path = currentGeneration.get(i);
    		float relativeFitness = path.fitness*100/totalFitness;
    		float cumRelativeFitness = relativeFitness;
    		if(i>0)
    			cumRelativeFitness = cum_sum[i-1] + relativeFitness;
    		cum_sum[i] = cumRelativeFitness;
    		}
    	for(int i=0; i<eliteSize;i++)
    		bestIndexes.add(i);
    	for(int i=0; i<currentGeneration.size()-eliteSize;i++){
    		float pick = 100*random.nextFloat();
            for (i=0;i<currentGeneration.size();i++){
                if (pick <= cum_sum[i]){
                	bestIndexes.add(i);
                    break;
    				}
    			}
    		}
    	return bestIndexes;
    }
	private class Node{
		public int x,y;
		public Node(int x, int y) {
			this.x = x; this.y = y;
		}
	}
	private class Path  implements Comparable<Path> {
		public int route[];
		public float fitness,distance;
		public Path(int route[]) {
			this.route = new int[route.length];
			System.arraycopy( route, 0, this.route, 0, route.length );
			this.distance = calcDistance(route);
		}
		private float calcDistance(int route[]) {
			  float sum = 0;
			  for (int i = 0; i < route.length; i++) {
			    Node cityA = nodes[route[i]];
			    Node cityB = null;
				if(i>=route.length-1)
					cityB = nodes[route[0]];
				else cityB = nodes[route[i + 1]];
				float xDelta = cityA.x - cityB.x, yDelta = cityA.y - cityB.y;
			    float d = (float) Math.sqrt(xDelta*xDelta+yDelta*yDelta);
			    sum += d;
			  }
			  return sum;
		}
		@Override
		public int compareTo(Path o) {
			// TODO Auto-generated method stub
			return (int) (this.distance - o.distance);
		}

	}
}