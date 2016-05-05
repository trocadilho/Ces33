package ces33;

public class Logic {
	
	public String dectoBin(int dec){
		String bin = new String();
		bin = Integer.toBinaryString(dec);
		return bin;
	}
	
	public int numPag(int memVirt){
		String numPag = new String();
		String bin = dectoBin(memVirt);
		for(int i=bin.length(); i<32; i++){
			bin = "0" + bin;
		}
		numPag = bin.substring(16, 24);
		return(Integer.parseInt(numPag, 2));
	}
	
	public int numDes(int memVirt){
		String numDes = new String();
		String bin = dectoBin(memVirt);
		for(int i=bin.length(); i<32; i++){
			bin = "0" + bin;
		}
		numDes = bin.substring(24);
		return(Integer.parseInt(numDes, 2));
	}
	
	public int numQuadro(int[] tabPag, int numPag){
		return tabPag[numPag];
	}
	
	public int byteSinalizado(int[][] memFis, int[] tabPag, int memVirt){
		int numPag = numPag(memVirt);
		int numDes = numDes(memVirt);
		int numQuadro = numQuadro(tabPag, numPag);
		return (memFis[numQuadro][numDes]);
	}
}
