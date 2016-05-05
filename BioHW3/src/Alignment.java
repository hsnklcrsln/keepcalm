import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Alignment {
	public int gap_penalty = -5;
	public ArrayList<Protein> list = new ArrayList<Protein>();
	public double[][] SimilarityMatrix;
	public HashMap<String, Double> hm = new HashMap<String, Double>();
	public int[] align_order;


	public Alignment(String fileName) throws IOException {

		FillList(fileName);

		Scanner scn = new Scanner(System.in);
		System.out.print("enter gap penalty:");
		gap_penalty = scn.nextInt();
		SimilarityMatrix = SimilarityMatrix();
		align_order = new int[5];

	}

	public void FillList(String filename) throws IOException {
		BufferedReader bfr = new BufferedReader(new FileReader(new File(
				filename)));
		String row = "";

		while ((row = bfr.readLine()) != null) {
			Protein prot = new Protein();
			prot.organism = row.substring(1);
			prot.sequence = bfr.readLine();
			list.add(prot);
		}

		bfr.close();
	}

	public void PrintList() {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(((Protein) list.toArray()[i]).organism + "\t"+ ((Protein) list.toArray()[i]).sequence);
		}
	}

	public double SimilarityScore(String seq1, String seq2) {
		double score = 0;

		for (int i = 0; i < seq1.length(); i++) {
			if (seq1.charAt(i) == seq2.charAt(i)) {
				score = score + 1.0;
			}
		}
		score = score / seq1.length();

		return score;
	}

	public double[][] SimilarityMatrix() throws IOException {
		double[][] sm = new double[list.size()][list.size()];
		String[] align;
		Object[] arr = list.toArray();

		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				// System.out.println(((Protein)arr[i]).organism+" vs "+((Protein)arr[j]).organism);
				align = CreateAlignment(((Protein) arr[i]).sequence,((Protein) arr[j]).sequence);
				sm[i][j] = SimilarityScore(align[0], align[1]);

				String deneme=((Protein) arr[i]).organism.toString()+ "-" + ((Protein) arr[j]).organism.toString();
				String deneme1=((Protein) arr[i]).organism.toString()+ "-" + ((Protein) arr[j]).organism.toString();
				
				hm.put(deneme, sm[i][j]);
				hm.put(deneme1, sm[i][j]);

			}
		}

		return sm;

	}

	public void FindSimilarPatternGraph() throws IOException {
		PrintSimilarityMatrix();
		ArrayList<String> deneme_list=new ArrayList<String>();
		
		String[] deneme_array=new String[list.size()];
		for (int i = 0; i < deneme_array.length; i++) {
			deneme_array[i]=((Protein) list.toArray()[i]).organism;
		}

		double max = 0;
		
		
		for (int i = 0; i < hm.values().size(); i++) {
			if ((Double)hm.values().toArray()[i]>max) {
				max=(Double)hm.values().toArray()[i];
			}
		}
		boolean control=true;
		for (int i = 0; i < deneme_array.length; i++) {
			if(!control)
				break;
			for (int j = i+1; j < deneme_array.length; j++) {
				if (hm.get(deneme_array[i]+"-"+deneme_array[j])==max) {
					deneme_list.add(deneme_array[i]);
					deneme_list.add(deneme_array[j]);
					control=false;
					break;
					
				}
			}
		}
		
		
		
		while (deneme_list.size() != deneme_array.length - 2) {
			max = 0;
			double distance = 0;
			int r = 0;
			for (int i = 0; i < deneme_array.length; i++) {
				
				if (!deneme_list.contains(deneme_array[i] + "")) {
					for (int j = 0; j < deneme_list.size(); j++) {
						distance = distance+ hm.get(deneme_list.toArray()[j]+ "-"+ deneme_array[i]);
					}
					if (distance > max) {
						max = distance;
						r = i;
					}
					if (i == deneme_array.length - 1) {
						deneme_list.add(deneme_array[r]);
					}
				}
			}

		}

		for (int i = 0; i < deneme_array.length; i++)
			if (!deneme_list.contains(deneme_array[i] + "")) {
				deneme_list.add(deneme_array[i]);
			}

		Object[] t=deneme_list.toArray();
		
		if(t.length==5){
			System.out.print(t[0] +"\t"+ "--|__\n");
			System.out.print(t[1] +"\t"+ "--|  |__\n");
			System.out.print(t[2] +"\t"+ "-----|  |__\n");
			System.out.print(t[3] +"\t"+ "--------|  |\n");
			System.out.print(t[4] +"\t"+ "-----------|\n");
		}
		else if (t.length==4) {
			System.out.print(t[0] +"\t" + "--|__\n");
			System.out.print(t[1] +"\t" + "--|  |__\n");
			System.out.print(t[2] +"\t" + "-----|  |\n");
			System.out.print(t[3] +"\t" + "--------|\n");
		}
		
		System.out.println();
		
		

		
		for (int i = 0; i < deneme_array.length; i++) {
			for (int j = 0; j < deneme_array.length; j++) {
				if (deneme_list.toArray()[i] == deneme_array[j]) {
					align_order[i] = j;
				}
			}

		}
		

	}

	public void PrintSimilarityMatrix() throws IOException {
		double[][] sm = SimilarityMatrix;
		Object[] arr = list.toArray();
		for (int i = 0; i < arr.length; i++) {
			System.out.println(((Protein)arr[i]).organism+":"+"\t"+"("+i+")");
		}
		System.out.println();
		System.out.print("\t");
		for (int i = 0; i < arr.length; i++) {

			System.out.print(((Protein) arr[i]).organism.charAt(0) + "\t");

		}
		System.out.println();
		for (int i = 0; i < arr.length; i++) {

			for (int j = 0; j < arr.length; j++) {
				if (j == 0) {
					System.out.print(((Protein) arr[i]).organism.charAt(0)
							+ "\t");
				}
				System.out.print(new DecimalFormat("##.##").format(sm[i][j])
						+ "\t");
			}
			System.out.println();
		}
		System.out.println();
	}

	public int[][] MultipleDynamicProgMatrix(String[] seq_arr, String seq) throws IOException {

		Blosum b = new Blosum();
		int[][] dm = new int[seq_arr[0].length() + 1][seq.length() + 1];

		for (int i = 0; i <= seq_arr[0].length(); i++) {
			dm[i][0] = i * gap_penalty;
		}
		for (int j = 0; j <= seq.length(); j++) {
			dm[0][j] = j * gap_penalty;
		}

		int delete = 0;
		int insert = 0;
		int match = 0;

		int max = 0;

		for (int i = 1; i <= seq_arr[0].length(); i++) {
			for (int j = 1; j <= seq.length(); j++) {

				delete = dm[i - 1][j] + gap_penalty;
				insert = dm[i][j - 1] + gap_penalty;
				match=0;
				for (int k = 0; k < seq_arr.length; k++) {
					match = match+ b.MatchScore(seq_arr[k].charAt(i - 1),seq.charAt(j - 1));
				}
				match = match + dm[i - 1][j - 1];
				max = Math.max(insert, delete);
				max = Math.max(max, match);
				dm[i][j] = max;
			}
		}

		return dm;

	}

	public int[][] DynamicProgMatrix(String seq1, String seq2)
			throws IOException {

		Blosum b = new Blosum();
		int[][] dm = new int[seq1.length() + 1][seq2.length() + 1];

		for (int i = 0; i <= seq1.length(); i++) {
			dm[i][0] = i * gap_penalty;
		}
		for (int j = 0; j <= seq2.length(); j++) {
			dm[0][j] = j * gap_penalty;
		}

		int delete = 0;
		int insert = 0;
		int match = 0;
		int max = 0;
		for (int i = 1; i <= seq1.length(); i++) {
			for (int j = 1; j <= seq2.length(); j++) {

				delete = dm[i - 1][j] + gap_penalty;
				insert = dm[i][j - 1] + gap_penalty;
				match = dm[i - 1][j - 1]+ b.MatchScore(seq1.charAt(i - 1), seq2.charAt(j - 1));
				max = Math.max(insert, delete);
				max = Math.max(max, match);
				dm[i][j] = max;
			}
		}

		return dm;
	}

	public void PrintDynMatrix(String seq1, String seq2) throws IOException {
		int[][] dm = DynamicProgMatrix(seq1, seq2);

		for (int i = 0; i <= seq1.length(); i++) {

			for (int j = 0; j <= seq2.length(); j++) {

				System.out.print(dm[i][j] + "\t");
			}
			System.out.println();
		}

	}

	public void AlignmentAll() throws IOException {
		Object[] arr = list.toArray();
		String[] align;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				System.out.println(((Protein) arr[i]).organism + " vs "
						+ ((Protein) arr[j]).organism);
				align = CreateAlignment(((Protein) arr[i]).sequence,
						((Protein) arr[j]).sequence);
				System.out.println(align[0]);
				System.out.println(align[1]);
				System.out.println("similarity:"
						+ SimilarityScore(align[0], align[1]));
				System.out.println("\n");

			}
		}
	}

	public void MultipleAlignment() throws IOException {

		
		Protein[] inputs=new Protein[list.size()];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i]=new Protein();
			inputs[i]=(Protein)list.toArray()[i];
		}
		
		//input deðiþirse duruma göre burda düzenleme yapýlabilir.
		if(inputs.length==5){
			String[] arr=CreateAlignment(inputs[align_order[0]].sequence, inputs[align_order[1]].sequence);
			String[] arr1=CreateAlignmentMultiple(arr, inputs[align_order[2]].sequence);
			String[] arr2=CreateAlignmentMultiple(arr1, inputs[align_order[3]].sequence);
			String[] arr3=CreateAlignmentMultiple(arr2, inputs[align_order[4]].sequence);
			for (int i = 0; i < arr3.length; i++) {
				//System.out.print(inputs[align_order[i]].organism+"\t");
				inputs[align_order[i]].sequence=arr3[i];
				//System.out.println(arr3[i]);
			}
		}
		else if(inputs.length==4){
			String[] arr=CreateAlignment(inputs[align_order[0]].sequence, inputs[align_order[1]].sequence);
			String[] arr1=CreateAlignmentMultiple(arr, inputs[align_order[2]].sequence);
			String[] arr2=CreateAlignmentMultiple(arr1, inputs[align_order[3]].sequence);
			for (int i = 0; i < arr2.length; i++) {
				//System.out.print(inputs[align_order[i]].organism+"\t");
				inputs[align_order[i]].sequence=arr2[i];
				//System.out.println(arr2[i]);
			}
		}
		System.out.println();
		list.clear();
		for (int i = 0; i < inputs.length; i++) {
			list.add(inputs[i]);
		}
		

	}

	public String[] CreateAlignmentMultiple(String[] seq_arr, String seq) throws IOException {

		
			String[] alignment=new String[seq_arr.length];
			for (int i = 0; i < alignment.length; i++) {
				alignment[i]="";
			}
			String AlignmentA = "";
			
			Blosum b = new Blosum();
			int[][] T = MultipleDynamicProgMatrix(seq_arr, seq);
			int i = seq_arr[0].length(), j = seq.length();
			int match=0;
			while (i > 0 || j > 0) {
				
				if (i > 0 && T[i][j] == T[i - 1][j] + gap_penalty) {
					for (int k = 0; k < seq_arr.length; k++) {
						alignment[k] = seq_arr[k].charAt(i - 1) + alignment[k];
					}
					AlignmentA = "-" + AlignmentA;
					i--;

				} else if (j > 0 && T[i][j] == T[i][j - 1] + gap_penalty) {
					AlignmentA = seq.charAt(j - 1) + AlignmentA;
					for (int k = 0; k < seq_arr.length; k++) {
						alignment[k] = "-" + alignment[k];
					}
					j--;

				}
				else {
					match=0;
					for (int k = 0; k < seq_arr.length; k++) {
						match = match+ b.MatchScore(seq_arr[k].charAt(i - 1),seq.charAt(j - 1));
					}
					if (i > 0&& j > 0&& T[i][j] == T[i - 1][j - 1]+ match) {
						
						for (int k = 0; k < seq_arr.length; k++) {
							alignment[k] = seq_arr[k].charAt(i - 1) + alignment[k];
						}
						AlignmentA = seq.charAt(j - 1) + AlignmentA;
						i--;
						j--;
					}
				}
			}
			
			String[] temp=new String[alignment.length+1];
			
			for (int k = 0; k < temp.length-1; k++) {
				temp[k]=alignment[k];
			}
			temp[temp.length-1]=AlignmentA;
			
			
			return temp;
		


	}
	public String[] CreateAlignment(String seq1, String seq2)throws IOException {

		String AlignmentA = "";
		String AlignmentB = "";
		Blosum b = new Blosum();
		int[][] T = DynamicProgMatrix(seq1, seq2);
		int i = seq1.length(), j = seq2.length();
		while (i > 0 || j > 0) {
			if (i > 0&& j > 0&& T[i][j] == T[i - 1][j - 1]+ b.MatchScore(seq1.charAt(i - 1),seq2.charAt(j - 1))) {
				AlignmentA = seq1.charAt(i - 1) + AlignmentA;
				AlignmentB = seq2.charAt(j - 1) + AlignmentB;
				i--;
				j--;
			} else if (i > 0 && T[i][j] == T[i - 1][j] + gap_penalty) {
				AlignmentA = seq1.charAt(i - 1) + AlignmentA;
				AlignmentB = "-" + AlignmentB;
				i--;

			} else if (j > 0 && T[i][j] == T[i][j - 1] + gap_penalty) {
				AlignmentB = seq2.charAt(j - 1) + AlignmentB;
				AlignmentA = "-" + AlignmentA;
				j--;

			}
		}

		return new String[] { AlignmentA, AlignmentB };

	}

}















