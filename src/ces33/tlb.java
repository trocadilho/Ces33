package ces33;

public class tlb {
	private int numPag;
	private int numQuad;
	
	public tlb (int Pag, int Quad){
		this.numPag = Pag;
		this.numQuad = Quad;
	}
	
	public void setnumPag(int Pag){
		this.numPag = Pag;
	}
	
	public void setnumQuad(int Quad){
		this.numQuad = Quad;
	}
	
	public int getnumPag(){
		return this.numPag;
	}
	
	public int getnumQuad(){
		return this.numQuad;
	}
}
