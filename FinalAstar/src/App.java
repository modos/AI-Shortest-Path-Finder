import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class App {

	private static final String input1 = "src/matrix.txt";

    static LinkedList<String> result = new LinkedList<String>();

	public static void main(String[] args) {
		Matrix m = FileUtils.read2DMatrixFromFile(input1);
		AStar.runSearch(m);

        for (int i = 0; i < result.size() - 1; i++) {
			System.out.print(result.get(i) + " ");
		}

		System.out.print(result.get(result.size() - 1));
	}
}

class AStar {
	public static void runSearch(Matrix data) {
		int[][] mark = new int[data.getN()][data.getM()];
		Cell[][] par = new Cell[data.getN()][data.getM()];

		mark[data.getiSource()][data.getjSource()] = 1;
		Queue<Cell> q = new PriorityQueue<>(new Comparator<Cell>() {
			@Override
			public int compare(Cell o1, Cell o2) {
				return o1.h_value - o2.h_value;
			}
		});

		q.offer(data.getCell(data.getiSource(), data.getjSource(), 1));
		par[data.getiSource()][data.getjSource()] = null;

		while (!q.isEmpty()) {
			Cell curr = q.poll();
			for (int i = 0; i < 4; ++i) {
				int newX = curr.x + utils.rr[i];
				int newY = curr.y + utils.cc[i];

				int limX = 9;
				int limY = 9;
	
			if (curr.x == 0 || curr.y == 0 || curr.y == limX || curr.y == limY) {
				
				if ((newY == 0 || newY == limY) && newX > limX){
			
					newX = 0;
				}
				
				if ((newY == 0 || newY == limY) && newX < 0){
				
					newX = limX;
				}
				   
	
				if ((newX == 0 || newX == limX) && newY > limY){
		
					newY = 0;
				}
				   
				if ((newX == 0 || newX == limX) && newY < 0){
		
					newY = limY;
				}
				   
			}

				if (!data.isValidToGo(newX, newY)) continue;
				if (mark[newX][newY] != 0) continue;

				mark[newX][newY] = mark[curr.x][curr.y] + 1;
				par[newX][newY] = curr;
				q.offer(data.getCell(newX, newY, mark[newX][newY]));

				if (data.isDestination(newX, newY)) break;
			}
		}

		if (mark[data.getiDes()][data.getjDes()] == 0) {
			System.out.println("Not Found");
		} else {
			utils.tracking(data, par);
		}
	}
}


class Matrix {
	private int N;
	private int M;

	private int iSource, jSource;
	private int iDes, jDes;

	private char[][] data;

	public Matrix(int n, int m) {
		this.N = n;
		this.M = m;
		data = new char[N][M];
	}

	public void update(int i, int j, char val) {

		if (!isInside(i,j)) return;
		data[i][j] = val;
		if (val == 'S') {
			iSource = i;
			jSource = j;
		}
		if (val == 'A') {
			iDes = i;
			jDes = j;
		}
	}

	public int getN() {
		return N;
	}

	public int getM() {
		return M;
	}

	public int getiSource() {
		return iSource;
	}

	public int getjSource() {
		return jSource;
	}

	public int getiDes() {
		return iDes;
	}

	public int getjDes() {
		return jDes;
	}

	public boolean isValidToGo(int i, int j) {
		
		return isInside(i, j) && data[i][j] != '1';
	}

	public boolean isInside(int i, int j) {

		if (i < 0 || i >= N) return false;
		if (j < 0 || j >= M) return false;
		return true;
	}

	public Cell getCell(int i, int j, int cost) {
		Cell c = new Cell(i, j, data[i][j], getHeuristicFrom(i, j) + cost);
		return c;
	}

	public Cell getCell(int i, int j) {
		Cell c = new Cell(i, j, data[i][j], getHeuristicFrom(i, j));
		return c;
	}

	public Cell getDestinationCell() {
		return getCell(iDes, jDes);
	}
	public boolean isDestination(int i, int j) {

		return isInside(i, j) && data[i][j] == 'A';
	}

	public int getHeuristicFrom(int i, int j) {
		return Math.abs(getiDes() - i) + Math.abs(getjDes() - j);
	}
}
class Cell {
	public int x;
	public int y;
	public char val;
	public int h_value;

	public Cell(int i, int j, char val, int h_v) {
		this.x = i;
		this.y = j;
		this.val = val;
		this.h_value = h_v;
	}

	public Cell(int i, int j) {
		this.x = i;
		this.y = j;
	}
}
class utils {
	public static final int[] rr = new int[]{-1, 1, 0, 0};
	public static final int[] cc = new int[]{0, 0, -1, 1};


	public static void tracking(Matrix data, Cell[][] par) {
        
		LinkedList<Cell> corners = new LinkedList<>(); 
		corners.add(new Cell(0, 0));
		corners.add(new Cell(data.getN() -1, 0));
		corners.add(new Cell(data.getN() - 1, data.getM() - 1));
		corners.add(new Cell(0, data.getM() - 1));
		
		Cell curr = data.getDestinationCell();
		ArrayList<P> trace = new ArrayList<>();
		while (curr != null) {
			trace.add(new P(curr.x, curr.y));
			curr = par[curr.x][curr.y];
		}
		for (int i = trace.size() - 1; i >= 1; --i) {

			boolean isCorner = false;

			for (int j = 0; j < corners.size(); j++) {
				if (corners.get(j).x == trace.get(i).x && corners.get(j).y == trace.get(i).y) {
					isCorner = true;
					break;
				}
			}


			if (isCorner) {
				if (trace.get(i).x > trace.get(i - 1).x)
                    App.result.add("Down");
					//System.out.print("Down ");
				else if (trace.get(i).x < trace.get(i - 1).x)
                    App.result.add("Up");
					//System.out.print("Up ");
				else if (trace.get(i).y > trace.get(i - 1).y)
                    App.result.add("Right");
					//System.out.print("Right ");
				else if (trace.get(i).y < trace.get(i - 1).y)
                    App.result.add("Left");
					//System.out.print("Left ");
			} else {
				if (trace.get(i).x > trace.get(i - 1).x)
                    App.result.add("Up");
					//System.out.print("Up ");
				else if (trace.get(i).x < trace.get(i - 1).x)
                    App.result.add("Down");
					//System.out.print("Down ");
				else if (trace.get(i).y > trace.get(i - 1).y)
                    App.result.add("Left");
					//System.out.print("Left ");
				else if (trace.get(i).y < trace.get(i - 1).y)
                    App.result.add("Right");
					//System.out.print("Right ");
			}
			
		}
	}

	public static void copyData(Cell[][] from, Cell[][] to) {
		for (int i = 0; i < from.length; ++i)
			for (int j = 0; j < from[0].length; ++j)
				to[i][j] = from[i][j];
	}
}
class P {
		int x;
		int y;
		
		P(int x, int y){
			this.x = x;
			this.y = y;
		}
}

class FileUtils {
	public static Matrix read2DMatrixFromFile(String filename) {
        int row = 0;
        int col = 0;

        try {
            BufferedReader br1 = new BufferedReader(new FileReader(filename));
                
                String st;
                st = br1.readLine();
                row++;
                col = st.split(" ").length;  
    
                while ((st = br1.readLine()) != null){
                    row++;
                }

                br1.close();
        } catch (Exception e) {
            System.out.println("ERROR FILE READING!!!");
        }
        
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

			Matrix data = new Matrix(row, col);
			for (int i = 0; i < row; ++i) {
				String line = br.readLine();
				String[] cells = line.split(" ");
				for (int j = 0; j < col; ++j) {
					data.update(i, j, cells[j].charAt(0));
				}
			}

			return data;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR FILE READING!!!");
		}
		return new Matrix(0, 0);
	}
}
