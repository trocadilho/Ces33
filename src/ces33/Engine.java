package ces33;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Engine {
	
	private static final String INPUT_FILE_NAME = "C:\\Users\\Usuario\\Documents\\ITA\\4º ano\\CES-33\\BACKING_STORE.bin";
	
	public static void main(String[] args){
		Engine test = new Engine();
	    //read in the bytes
	    byte[] fileContents = test.read(INPUT_FILE_NAME);
	    //test.readAlternateImpl(INPUT_FILE_NAME);
	    //write it back out to a different file name
		Logic Logica = new Logic();
		ArrayList<tlb> TLB = new ArrayList<tlb>();
		int[] tabPag = new int[256];
		int[][] memFis = new int [256][256];
		int numQuadro, endFisico, byteSinalizado;
		int translatedAddresses = 0;
		int PageFaults = 0;
		int TLBHits = 0;
		Arrays.fill(tabPag, -1);
		for(int i=0; i<256; i++){
			Arrays.fill(memFis[i], -1);
		}
		Charset charset = Charset.forName("US-ASCII");
		Path addresses_path = Paths.get("C:/Users/Usuario/Documents/ITA/4º ano/CES-33", "addresses.txt");
		Path output_path = Paths.get("C:/Users/Usuario/Documents/ITA/4º ano/CES-33", "saida.txt");
		try (BufferedReader reader = Files.newBufferedReader(addresses_path, charset)) {
			try (BufferedWriter writer = Files.newBufferedWriter(output_path, charset)){
			    String line = null;
			    while ((line = reader.readLine()) != null) {
			    	translatedAddresses++;
			    	int memVirt = Integer.parseInt(line);
			    	numQuadro = Logica.searchTlb(Logica.numPag(memVirt), TLB);
			    	//Página não se encontra na TLB:
			    	if (numQuadro==-1){
				    	numQuadro = Logica.numQuadro(tabPag, Logica.numPag(memVirt));
				    	//PageFault:
				    	if(numQuadro==-1){
				    		PageFaults++;
				    		int j;
				    		for(j=0; memFis[j][0]!=-1 && j<256; j++);
				    		numQuadro = j;
				    		tabPag[Logica.numPag(memVirt)] = numQuadro;
				    		for(int i=0; i<256; i++){
				    			memFis[j][i] = fileContents[Logica.numPag(memVirt)*256 + i];
				    		}
				    	}
				    	tlb tlbEntry = new tlb(Logica.numPag(memVirt), numQuadro);
			    		//Usando lógica FIFO de substituição de páginas:
			    		Logica.FIFO(tlbEntry, TLB);
			    		//Usando lógica FIFO de substituição de páginas:
			    		//Logica.LRU(TLB, tlbEntry);
			    	}
			    	else{
			    		TLBHits++;
			    	}
			    	endFisico = numQuadro*256+Logica.numDes(memVirt);
			    	byteSinalizado = Logica.byteSinalizado(memFis, tabPag, memVirt);
			    	String s = "Virtual address: " + memVirt + " Physical address: " + endFisico + " Value: " + byteSinalizado + "\n";
			    	writer.write(s, 0, s.length());
			    }
			    writer.write("Number of Translated Addresses = " + translatedAddresses + "\n");
			    writer.write("Page Faults = " + PageFaults + "\n");
			    writer.write("Page Fault Rate = " + (float)PageFaults/translatedAddresses + "\n");
			    writer.write("TLB Hits = " + TLBHits + "\n");
			    writer.write("TLB Hit Rate = " + (float)TLBHits/translatedAddresses);
		    } catch (IOException x) {
	    		System.err.format("IOException: %s%n", x);
		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	/** Read the given binary file, and return its contents as a byte array.*/ 
	public byte[] read(String aInputFileName){
		System.out.println("Reading in binary file named : " + aInputFileName);
		File file = new File(aInputFileName);
		System.out.println("File size: " + file.length());
		byte[] result = new byte[(int)file.length()];
		try {
		  InputStream input = null;
		  try {
		    int totalBytesRead = 0;
		    input = new BufferedInputStream(new FileInputStream(file));
		    while(totalBytesRead < result.length){
		      int bytesRemaining = result.length - totalBytesRead;
		      //input.read() returns -1, 0, or more :
		      int bytesRead = input.read(result, totalBytesRead, bytesRemaining); 
		      if (bytesRead > 0){
		        totalBytesRead = totalBytesRead + bytesRead;
		      }
		    }
		    /*
		     the above style is a bit tricky: it places bytes into the 'result' array; 
		     'result' is an output parameter;
		     the while loop usually has a single iteration only.
		    */
		    System.out.println("Num bytes read: " + totalBytesRead);
		  }
		  finally {
		    System.out.println("Closing input stream.");
		    input.close();
		  }
		}
		catch (FileNotFoundException ex) {
		  System.out.println("File not found.");
		}
		catch (IOException ex) {
		  System.out.println(ex);
		}
		return result;
	}
}
