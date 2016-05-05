import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

/*
 * This algorithms work only if number of inputs equal 4 or 5 in fasta format.
 * If you try different inputs please update Alignment.FindSimilarPatternGraph() 
 * and Alignment.MultipleAlignment() functions.
 * 
 * While similarity matrix is writing on console,DecimalFormat("##.##") is used.
 * So you can not see all digits.
 * If you want to see all digits, please use Alignment.AlignmentAll() function.
 * 
 * 
 * Hasan Kýlýçarslan         
 * April,2016
 * 
 * */
public class Test {

	
	public static void main(String[] args) throws IOException {
		
		
		Alignment a=new Alignment("Input.txt");
		
		a.FindSimilarPatternGraph();
		a.MultipleAlignment();
		
		a.PrintList();
		
		
		
		
		
		
		
		
		
		
		
	}

}
