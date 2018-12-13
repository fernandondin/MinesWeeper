package minesweeper;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;
public class Mine implements Serializable{
    public class Cell implements Serializable{
	private int cx;
	private int cy;
	private int element;
	public boolean marked;
	private LinkedList<Cell> neighbors;
	public Cell(int cx,int cy,int element){
	    this.cx=cx;
	    this.cy=cy;
	    this.element=element;
	    neighbors = new LinkedList<>();
	}
        public LinkedList<Cell> getNeighbors(){
            return this.neighbors;
        }
        public int getX(){
            return cx;
        }
        public int getY(){
            return cy;
        }
        public int getElement(){
            return this.element;
        }
	public void setMarked(boolean b){
	    this.marked=marked;
	}
	public boolean isMarked(){
	    return this.marked;
	}
	@Override public boolean equals(Object o){
	    @SuppressWarnings("unchecked")
		Cell cell = (Cell)o;
	    return cell.cx == this.cx && cell.cy == this.cy;
	}
	@Override public String toString(){
	    return "("+cx+","+cy+"):"+element;
	}
	private void addN(){
	    if(cx == 0 && cy == 0){
		neighbors.add(new Cell(0,1,0));
		neighbors.add(new Cell(1,0,0));
		neighbors.add(new Cell(1,1,0));
	    }
	    else if(cx == x-1 && cy == 0){
		neighbors.add(new Cell(cx-1,0,0));
		neighbors.add(new Cell(cx-1,1,0));
		neighbors.add(new Cell(cx,1,0));
	    }
	    else if(cx == 0 && cy == y-1){
		neighbors.add(new Cell(0,cy-1,0));
		neighbors.add(new Cell(1,cy-1,0));
		neighbors.add(new Cell(1,cy,0));
	    }
	    else if(cx == x-1 && cy == y-1){
		neighbors.add(new Cell(cx-1,cy,0));
		neighbors.add(new Cell(cx-1,cy-1,0));
		neighbors.add(new Cell(cx,cy-1,0));
	    }
	    else if(cx == 0){
		neighbors.add(new Cell(cx,cy-1,0));
		neighbors.add(new Cell(cx,cy+1,0));
		neighbors.add(new Cell(cx+1,cy-1,0));
		neighbors.add(new Cell(cx+1,cy,0));
		neighbors.add(new Cell(cx+1,cy+1,0));
	    }
	    else if(cy == 0){
		neighbors.add(new Cell(cx-1,cy,0));
		neighbors.add(new Cell(cx-1,cy+1,0));
		neighbors.add(new Cell(cx,cy+1,0));
		neighbors.add(new Cell(cx+1,cy,0));
		neighbors.add(new Cell(cx+1,cy+1,0));
	    }
	    else if(cx == x-1){
		neighbors.add(new Cell(cx,cy-1,0));
		neighbors.add(new Cell(cx,cy+1,0));
		neighbors.add(new Cell(cx-1,cy-1,0));
		neighbors.add(new Cell(cx-1,cy,0));
		neighbors.add(new Cell(cx-1,cy+1,0));
	    }
	    else if(cy == y-1){
		neighbors.add(new Cell(cx-1,cy,0));
		neighbors.add(new Cell(cx-1,cy-1,0));
		neighbors.add(new Cell(cx,cy-1,0));
		neighbors.add(new Cell(cx+1,cy,0));
		neighbors.add(new Cell(cx+1,cy-1,0));
	    }
	    else{
		neighbors.add(new Cell(cx-1,cy-1,0));
		neighbors.add(new Cell(cx-1,cy,0));
		neighbors.add(new Cell(cx-1,cy+1,0));
		neighbors.add(new Cell(cx,cy-1,0));
		neighbors.add(new Cell(cx,cy+1,0));
		neighbors.add(new Cell(cx+1,cy-1,0));
		neighbors.add(new Cell(cx+1,cy,0));
		neighbors.add(new Cell(cx+1,cy+1,0));
	    }
	}
    }
    private int x;
    private int y;
    private int minas;
    private Cell[][] cell;
    private String nombre;
    public void set(String n){
        this.nombre=nombre;
    }
    public String getNombre(){
        return this.nombre;
    }
    public Mine(int x,int y,int minas,String nombre){
	this.x=x;
	this.y=y;
	this.minas=minas;
	cell = new Cell[x][y];
        llenaCell();
        this.nombre=nombre;
    }
    public int getMinas(){
        return minas;
    }
    public Cell[][] getCell(){  
        return this.cell;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public void setMarked(Cell c,boolean b){
        cell[c.cx][c.cy].marked = true;
    }
    private void llenaCell(){
	int elemento = 0;
	int c = minas;
	Random r = new Random();
	int l = 400;
	while(c > 0){
	    for(int i=0;i<cell.length;i++){
		for(int j=0;j<cell[i].length;j++){
		    if(cell[i][j] == null){
			cell[i][j] = new Cell(i,j,0);
			cell[i][j].addN();
		    }
		    int rand = r.nextInt(l);
		    if(cell[i][j].element != -1 && rand == l-1 && c > 0){
			c--;
			cell[i][j].element = -1;
		    }
		}
	    }
	}
	updateCell();
	updateNeighbors();
	for(int i=0;i<cell.length;i++){
		for(int j=0;j<cell[i].length;j++){
		    for(Cell ne:cell[i][j].neighbors){
		}
	    }
}
    }
    private void updateCell(){
	for(int i=0;i<cell.length;i++){
		for(int j=0;j<cell[i].length;j++){
		    int c =0;
		    for(Cell ne:cell[i][j].neighbors){
			ne = cell[ne.cx][ne.cy];
			if(ne.element==-1)
			    c++;
		    }
		    cell[i][j].element = cell[i][j].element == -1 ? cell[i][j].element:c;
		}
	    }
    }
	private void updateNeighbors(){
        for(int i=0;i<cell.length;i++){
            for(int j=0;j<cell[i].length;j++){
                 for(Cell vecino:cell[i][j].neighbors){
                     vecino.element = cell[vecino.cx][vecino.cy].element;
                 }   
            }       
        }
    }
    @Override public String toString(){
	String s = "";
	for(int i=0;i<cell.length;i++){
	    for(int j=0;j<cell[i].length;j++){
		if(cell[i][j].element == -1 )
		    s+="*  ";
		else	      
		    s+= cell[i][j].element+"  ";
	    }
	    s+="\n";
	}
	return s;
    }
}
