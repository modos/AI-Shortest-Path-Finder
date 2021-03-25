import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class App {

    static LinkedList<String> result = new LinkedList<String>();

    private static class Cell {
        int x;
        int y;
        int dist; // distance
        Cell prev; // parent cell in the path

        Cell(int x, int y, int dist, Cell prev) {
            this.x = x;
            this.y = y;
            this.dist = dist;
            this.prev = prev;
        }

        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    // BFS, Time O(n^2), Space O(n^2)
    public static void shortestPath(int[][] matrix, int[] start, int[] end) {
        int sx = start[0], sy = start[1];
        int dx = end[0], dy = end[1];

        // if start or end value is 0, return
        if (matrix[sx][sy] == 1 || matrix[dx][dy] == 1)
            return;

        int m = matrix.length;
        int n = matrix[0].length;
        Cell[][] cells = new Cell[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] != 1) {
                    cells[i][j] = new Cell(i, j, Integer.MAX_VALUE, null);
                }
            }
        }

        LinkedList<Cell> queue = new LinkedList<>();
        Cell src = cells[sx][sy];
        src.dist = 0;
        queue.add(src);
        Cell dest = null;
        Cell p;
        while ((p = queue.poll()) != null) {
            // find destination
            if (p.x == dx && p.y == dy) {
                dest = p;
                break;
            }

            // moving up
            visit(cells, queue, p.x - 1, p.y, p);

            // moving down
            visit(cells, queue, p.x + 1, p.y, p);

            // moving left
            visit(cells, queue, p.x, p.y - 1, p);

            // moving right
            visit(cells, queue, p.x, p.y + 1, p);
        }

        if (dest == null) {
            System.out.print("Not Found");
            return;
        } else {
            LinkedList<Cell> path = new LinkedList<>();
            p = dest;
            do {
                path.addFirst(p);
            } while ((p = p.prev) != null);

            LinkedList<Cell> corners = new LinkedList<>();

            corners.add(new Cell(0, 0));
            corners.add(new Cell(m -1, 0));
            corners.add(new Cell(m - 1, n - 1));
            corners.add(new Cell(0, n - 1));

            for (int i = 0; i < path.size() - 1; i++) {

                boolean isCorner = false;

                for (int j = 0; j < corners.size(); j++) {
                    if (corners.get(j).x == path.get(i).x && corners.get(j).y == path.get(i).y) {
                        isCorner = true;
                        break;
                    }
                }

                if (isCorner) {
                    if (path.get(i).x > path.get(i + 1).x)
                        App.result.add("Down");
                        //System.out.print("Down ");
                    else if (path.get(i).x < path.get(i + 1).x)
                        App.result.add("Up");
                        //System.out.print("Up ");
                    else if (path.get(i).y > path.get(i + 1).y)
                        App.result.add("Right");
                        //System.out.print("Right ");
                    else if (path.get(i).y < path.get(i + 1).y)
                        App.result.add("Left");
                        //System.out.print("Left ");
                } else {
                    if (path.get(i).x > path.get(i + 1).x)
                        App.result.add("Up");
                        //System.out.print("Up ");
                    else if (path.get(i).x < path.get(i + 1).x)
                        App.result.add("Down");
                        //System.out.print("Down ");
                    else if (path.get(i).y > path.get(i + 1).y)
                        App.result.add("Left");
                        //System.out.print("Left ");
                    else if (path.get(i).y < path.get(i + 1).y)
                        App.result.add("Right");
                        //System.out.print("Right ");
                }
            }

        }
    }

    // function to update cell visiting status
    static void visit(Cell[][] cells, LinkedList<Cell> queue, int x, int y, Cell parent) {

        int limX = cells.length - 1;
        int limY = cells[0].length - 1;

        if (x == 0 || y == 0 || x == limX || y == limY) {
            if ((y == 0 || y == limY) && x > limX)
                x = 0;
            if ((y == 0 || y == limY) && x < 0)
                x = limX;

            if ((x == 0 || x == limX) && y > limY)
                y = 0;
            if ((x == 0 || x == limX) && y < 0)
                y = limY;
        }

        if (x < 0 || x >= cells.length || y < 0 || y >= cells[0].length || cells[x][y] == null) {
            return;
        }

        int dist = parent.dist + 1;
        Cell p = cells[x][y];
        if (dist < p.dist) {
            p.dist = dist;
            p.prev = parent;
            queue.add(p);
        }
    }

    public static void main(String[] args) throws IOException {
        /* int[][] matrix = { { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 }, { 0, 1, 1, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 },
                { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 } }; */

                File file = new File("src/matrix.txt");
                BufferedReader br = new BufferedReader(new FileReader(file));
    
                int row = 0;
                int col = 0;
                String st;
    
                st = br.readLine();
                row++;
                col = st.split(" ").length;  
    
                while ((st = br.readLine()) != null){
                    row++;
                }
                int[][] matrix = new int[row][col];

                int[] start = new int[2];
                int [] end = new int[2];
    
                BufferedReader br1 = new BufferedReader(new FileReader(file));
    
                for (int i = 0; i < row; i++)
                {
                    String line = br1.readLine();
    
                    for (int j = 0; j < col; j++)
                    {
                        String[] chars = line.split(" ");

                        if (chars[j].equals("S")){
                            start[0] = i; start[1] = j; chars[j] = "0";
                        }
                        if (chars[j].equals("A")){
                            end[0] = i; end[1] = j;  chars[j] = "0";
                        }

                        matrix[i][j] = Character.getNumericValue(chars[j].charAt(0)) ;
                    }
                }

                br.close();
                br1.close();
                
        shortestPath(matrix, start, end);

        for (int i = 0; i < result.size() - 1; i++) {
			System.out.print(result.get(i) + " ");
		}

		System.out.print(result.get(result.size() - 1));
    }
}
