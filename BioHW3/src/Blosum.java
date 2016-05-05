import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Blosum {
	
	public int x=0;
	public int y=0;
	public int[][] scores=new int[24][24];
	
	public Blosum() throws IOException {
		ReadFromFile("Blosum50.txt");
	}
	public void ReadFromFile(String filename) throws IOException{
		
		String row="";
		BufferedReader bfr=new BufferedReader(new FileReader(new File(filename)));
		bfr.readLine();
		
		while ((row=bfr.readLine())!=null) {
			for (int i = 0; i < scores.length; i++) {
				scores[x][i]=Integer.parseInt(row.split("\t")[i+1]);
			}
			x++;
		}
		bfr.close();
		
	}
	public void PrintBlosumTable(){
		
		for (int i = 0; i < scores.length; i++) {
			for (int j = 0; j < scores.length; j++) {
				System.out.print(scores[i][j]+"\t");
			}
			System.out.println();
		}
	}
	public int MatchScore(char x,char y){
		return scores[Coord(x)][Coord(y)];
	}
	public int Coord(char x){
		//A	 B	C	D	E	F	G	H	I	K	L	M	N	P	Q	R	S	T	V	W	X	Y	Z	-
		//0	 1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	16	17	18	19	20	21	22
		int var=0;
		switch (x) {
		case 'A':
			var=0;
			break;
		case 'B':
			var=1;
			break;
		case 'C':
			var=2;
			break;
		case 'D':
			var=3;
			break;
		case 'E':
			var=4;
			break;
		case 'F':
			var=5;
			break;
		case 'G':
			var=6;
			break;
		case 'H':
			var=7;
			break;
		case 'I':
			var=8;
			break;
		case 'K':
			var=9;
			break;
		case 'L':
			var=10;
			break;
		case 'M':
			var=11;
			break;
		case 'N':
			var=12;
			break;
		case 'P':
			var=13;
			break;
		case 'Q':
			var=14;
			break;
		case 'R':
			var=15;
			break;
		case 'S':
			var=16;
			break;
		case 'T':
			var=17;
			break;
		case 'V':
			var=18;
			break;
		case 'W':
			var=19;
			break;
		case 'X':
			var=20;
			break;
		case 'Y':
			var=21;
			break;
		case 'Z':
			var=22;
			break;
		case '-':
			var=23;
			break;

		}
		
		return var;
	}
	
	
}
