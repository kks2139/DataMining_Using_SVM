import java.io.*;
import java.security.KeyStore.Entry;
import java.util.*;

public class DMmain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		StringTokenizer tokendString;
		BufferedReader br = null; // �о�°� ���� ���۰���
		String[] result = new String[4];
		ArrayList<String> cat = new ArrayList<>();
		TreeSet[] ts = new TreeSet[8];
		TreeSet sum = new TreeSet();
		String docId ="";
		int[] docCount = new int[8];
		
		for(int i=0;i<8;i++){ // treeSet �迭 �ʱ�ȭ
			ts[i] = new TreeSet();
		}
		
		for (int k = 1; k <= 4; k++) {
			File file = new File("C:\\workspace\\DataMining\\src", "HKIB-20000_00"+k+".txt");
			try {
				br = new BufferedReader(new FileReader(file));
				String line = "";

				while ((line = br.readLine()) != null) { // readLine = �� �پ� �о��

					if(line.contains("<KW>")){ // <KW> ���� @DOCUMENT ���ö����� �� ����
						while((line = br.readLine()) != null){
							if(line.contains("@DOCUMENT") || line==null){
								break;
							}
						}
						continue;
					}
					tokendString = new StringTokenizer(line," |/||!|#|%|:|,|.|-|[|]|~|(|)|\"|\'|;|*|&|<|>|=|��|��|��|��|��|?");
					while (tokendString.hasMoreTokens()) {
						String word = tokendString.nextToken();
						
						if(word.equals("DocID")){ // ���Ͽ��� ����ϳ� ��������
							if(tokendString.countTokens()==1){
								docId = tokendString.nextToken(); // �׶��� ���� id�� ���� ����
							}else{
								docId = tokendString.nextToken()+"-"+tokendString.nextToken(); // �׶��� ���� id�� ���� ����
							}
							
							line = br.readLine(); // �� ������ �ҷ���
							tokendString = new StringTokenizer(line,"/");
							tokendString.nextToken();
							cat.add(tokendString.nextToken()); // ī�װ� �о�
							
							String temp = cat.get(cat.size()-1); // cat�� �������� �ӽ����� 
							if (temp.equals("�ǰ��� ����")) // �� cat ������ doc���� ���ֱ� ����
								docCount[0]++;
							else if (temp.equals("����"))
								docCount[1]++;
							else if (temp.equals("����"))
								docCount[2]++;
							else if (temp.equals("����"))
								docCount[3]++;
							else if (temp.equals("��ȭ�� ����"))
								docCount[4]++;
							else if (temp.equals("��ȸ"))
								docCount[5]++;
							else if (temp.equals("���"))
								docCount[6]++;
							else if (temp.equals("������Ȱ"))
								docCount[7]++;
							
							br.readLine(); br.readLine(); br.readLine(); // #TEXT�ִ� �ٴٴ����ٷ� �̵���Ű�� ������ TEXT�������� �ٽ� �о���� ����
							break;
						}
						if (cat.size() != 0) {
							if(word.equals("@DOCUMENT"))
								break;
							String lastCat = cat.get(cat.size() - 1);
							if (lastCat.equals("�ǰ��� ����"))
								ts[0].add("0+"+docId+" "+word);
							else if (lastCat.equals("����"))
								ts[1].add("1+"+docId+" "+word);
							else if (lastCat.equals("����"))
								ts[2].add("2+"+docId+" "+word);
							else if (lastCat.equals("����"))
								ts[3].add("3+"+docId+" "+word);
							else if (lastCat.equals("��ȭ�� ����"))
								ts[4].add("4+"+docId+" "+word);
							else if (lastCat.equals("��ȸ"))
								ts[5].add("5+"+docId+" "+word);
							else if (lastCat.equals("���"))
								ts[6].add("6+"+docId+" "+word);
							else if (lastCat.equals("������Ȱ"))
								ts[7].add("7+"+docId+" "+word);
							
							sum.add(word);
						}
					}
				}
				//result[k-1] = "\n[����"+k+"]����  Featuer ���� : " + ts.size() + "\n";
				result[k-1] = "\n[txt"+k+"]����  Featuer ���� : " + (ts[0].size()+ts[1].size()+ts[2].size()+ts[3].size()
						+ts[4].size()+ts[5].size()+ts[6].size()+ts[7].size()) + "\n";
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null)
					try {
						br.close();
					} // ������ �о����� ��Ʈ�� ����
					catch (IOException e) {
					}
			}
			System.out.println("�۾���..."+k*25+"%");
			
		}// for 4 times
		
		//****************************  ī�� ������    ******************************//
	    float A=0,B=0,C=0,D=0;
	    float[] square = new float[]{0,0,0,0,0,0,0,0};
	    String str1,str2;
	    
	    ArrayList<String> sumList = new ArrayList<>(sum);
	    ArrayList<String>[] tsList = new ArrayList[8];
	    ArrayList<String>[] tsListId = new ArrayList[8];
	    
	    StringTokenizer t;
	    for(int i=0;i<8;i++){
	    	tsList[i] = new ArrayList<>(ts[i]); // treeSet�� List��
	    	tsListId[i] = new ArrayList<>(); // docID list �ʱ�ȭ
	    }
		for (int i = 0; i < 8; i++) { // �տ� docID �� �� ���� ���·� �ٽ� ���� -- �ؿ��� indexOf��  �غ�����....
			for (int k = 0; k < tsList[i].size(); k++) {
				t = new StringTokenizer(tsList[i].get(k), " ");
				tsListId[i].add(t.nextToken()); // docId
				tsList[i].set(k, t.nextToken()); // ����
			}
		}

	    StringTokenizer tkn;
	    
	    //System.out.println("sum -> List "+sumList.size()+"\n");
	    
	    Map<Float,String> chiVal = new HashMap<Float,String>();
	    
	    try{
            BufferedWriter fw = new BufferedWriter(new FileWriter("D:\\train\\compile.txt",true));

	    
	    //-------------------- sumList�� ���� �ϳ��� ������ tsList 0~7 �� ��ġ�ϴ°� ã�� -----------------//
	    for(int s= 0;s<sumList.size();s++){
			int[] i_cnt = new int[]{0,0,0,0,0,0,0,0};
		    int i_sum=0, docSum=0;
			int num, ctg=0;

			for (int i = 0; i < 8; i++) {
				while((num = tsList[i].indexOf(sumList.get(s))) != -1){
					tsList[i].remove(num);
					i_cnt[i]++;
				}
				
			}
			for (int a = 0; a < 8; a++){
				i_sum += i_cnt[a];
				docSum += docCount[a];
			}
			for (int b = 0; b < 8; b++) {
				A = i_cnt[b];
				B = docCount[b] - A;
				C = i_sum - A;
				D = docSum - (A + B) - C;
				square[b] = (docSum * (A * D - C * B) * (A * D - C * B)) / ((A + C) * (B + D) * (A + B) * (C + D));
			}
			
			float temp, max, biggest;
			for (int i = 0; i < 8; i++) { // �ִ� square �� ã��
				for (int k = 0; k < 7; k++) {
					if (square[k] > square[k + 1]) {
						temp = square[k + 1];
						square[k + 1] = square[k];
						square[k] = temp;
					}
				}
			}

			max = square[7]; // �� ������ ī������ ��
			
			
	        fw.write(max+" "+sumList.get(s)+"/");
	        fw.flush();
	        
			//chiVal.put(max,sumList.get(s)); // treeSet�� ī������ �� + ���� ���·� ���� ����
	    } // sumList for��
	    fw.close();
	    } // ���Ͼ��� try
	    catch(Exception e){
	    	e.printStackTrace();
	    }

	    ArrayList<String> Vector = new ArrayList<>();
	   
	    Vector.addAll(makeVector()); // ī������, ���� ���·� �����س��� ������ �о ī������ ������ ����, ���� ���͸� ����Ʈ Ÿ������ �����
	    makeTrain(Vector); // train ������ �����
	    LastTest(Vector); // test ������ �����
	    
	} // main  
	
	
	public static ArrayList makeVector(){
		System.out.println("����!");
		
		StringTokenizer tokendString;
		BufferedReader br = null;
	    Map<String,Float> map = new HashMap<String,Float>();
	    ArrayList<String> features = new ArrayList<>();
	    
	    String feat;
	    float chi;
	    int count=0;
	    File file = new File("D:\\train\\compile.txt");
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";

			while ((line = br.readLine()) != null) {
				tokendString = new StringTokenizer(line,"/| ");
				while (tokendString.hasMoreTokens()) {
					chi = Float.parseFloat(tokendString.nextToken());
					feat = tokendString.nextToken();
					map.put(feat, chi);
				}
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} // ������ �о����� ��Ʈ�� ����
				catch (IOException e) {
				}
		}

		Iterator vector = sortByValue(map).iterator( );
		while (vector.hasNext()) {
			count++;
			String temp = (String)vector.next();
			features.add(temp+" "+map.get(temp));
		}

	    return features;
	}
	
	
	public static List sortByValue(final Map map) {
        List<String> list = new ArrayList();
        list.addAll(map.keySet());
         
        Collections.sort(list,new Comparator() {
             
            public int compare(Object o1,Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                 
                return ((Comparable) v2).compareTo(v1);
            }
             
        });
        //Collections.reverse(list); // �ּ��� ��������
        return list;
    }
	
	public static void makeTrain(ArrayList<String> list){
		StringTokenizer tokendString;
		BufferedReader br = null;
		ArrayList<String> cat = new ArrayList<>();
		String docId ="";
		String categoryNum="";
		StringTokenizer st;
		int countRow=0;
		TreeMap<Integer,String> TreeMap = null;
		Iterator<Integer> iteratorKey=null;
		
		
		ArrayList<String> CHI = new ArrayList<>();
		ArrayList<String> FEAT = new ArrayList<>();
		
		for(int n=0;n<list.size();n++){
			st = new StringTokenizer(list.get(n)," ");
			FEAT.add(st.nextToken());
			CHI.add(st.nextToken());
			
		}
		try{
        BufferedWriter fw = new BufferedWriter(new FileWriter("D:\\train\\train.txt"));
		for (int k = 1; k <= 4; k++) {
			File file = new File("C:\\workspace\\DataMining\\src", "HKIB-20000_00"+k+".txt");
			try {
				br = new BufferedReader(new FileReader(file));
				String line = "";

				while ((line = br.readLine()) != null) { // readLine = �� �پ� �о��

					if(line.contains("<KW>")){ // <KW> ���� @DOCUMENT ���ö����� �� ����
						while((line = br.readLine()) != null){
							if(line.contains("@DOCUMENT") || line==null){
								break;
							}
						}
						continue;
					}
					tokendString = new StringTokenizer(line," |/||!|#|%|:|,|.|-|[|]|~|(|)|\"|\'|;|*|&|<|>|=|��|��|��|��|��|?");
					while (tokendString.hasMoreTokens()) {
						String word = tokendString.nextToken();
						
						if(word.equals("DocID")){ // ���Ͽ��� ����ϳ� ��������
							
							line = br.readLine(); // �� ������ �ҷ��� (#CAT)
							tokendString = new StringTokenizer(line,"/");
							tokendString.nextToken(); // ī�װ� ����
							
							cat.add(tokendString.nextToken()); // ���� ī�װ� ����
							String lastCat = cat.get(cat.size() - 1);
							if (lastCat.equals("�ǰ��� ����"))
								categoryNum = "1";
							else if (lastCat.equals("����"))
								categoryNum = "2";
							else if (lastCat.equals("����"))
								categoryNum = "3";
							else if (lastCat.equals("����"))
								categoryNum = "4";
							else if (lastCat.equals("��ȭ�� ����"))
								categoryNum = "5";
							else if (lastCat.equals("��ȸ"))
								categoryNum = "6";
							else if (lastCat.equals("���"))
								categoryNum = "7";
							else if (lastCat.equals("������Ȱ"))
								categoryNum = "7";
						
							countRow++;
							
							if(TreeMap != null){ // ���Ͽ� ���ĺ� ī������ ������ ���� ���ֱ�
								
								System.out.println("�� = "+countRow);
								fw.write("\n"+categoryNum+" ");// ���� ùĭ�� ���� document�� ī�װ� �ϳ� ���� 
								
								
								
								for(int i=0;i<TreeMap.size();i++){
									Integer key = iteratorKey.next();
									key++;
									fw.write(key+":"+TreeMap.get(key-1)+" ");
								}
							}

							TreeMap = new TreeMap<>(); // Ʈ���� ���� �ʱ�ȭ
							
							br.readLine(); br.readLine(); br.readLine(); 
							break;
						}
						if(word.equals("@DOCUMENT"))
							break;
				
						// �ε����� ������������ �����ϱ�����
						int index; 
						if((index = FEAT.indexOf(word)) != -1){
							TreeMap.put(index,CHI.get(index) );
						}
						iteratorKey = TreeMap.keySet().iterator();
			
					} // �ڸ� �ܾ� �ϳ��� ������ while
				} // ���� �۰� while
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null)
					try {
						br.close();
					} // ������ �о����� ��Ʈ�� ����
					catch (IOException e) {
					}
			}
		}// for 4 times*/
		System.out.println("���� ����!");
		
		
		fw.close(); // writer �ݱ�
		}catch (IOException e) {} // file writer �� ����ó��
	}
	
	public static void LastTest(ArrayList<String> list){
		
		StringTokenizer tokendString;
		BufferedReader br = null;
		ArrayList<String> cat = new ArrayList<>();
		String docId ="";
		String categoryNum="";
		StringTokenizer st;
		int countRow=0;
		TreeMap<Integer,String> TreeMap = null;
		Iterator<Integer> iteratorKey=null;
		
		ArrayList<String> CHI = new ArrayList<>();
		ArrayList<String> FEAT = new ArrayList<>();
		
		for(int n=0;n<list.size();n++){
			st = new StringTokenizer(list.get(n)," ");
			FEAT.add(st.nextToken());
			CHI.add(st.nextToken());
			
		}
		
		try{
        BufferedWriter fw = new BufferedWriter(new FileWriter("D:\\train\\ttt.txt"));
		
			File file = new File("C:\\workspace\\DataMining\\src", "HKIB-20000_005.txt");
			try {
				br = new BufferedReader(new FileReader(file));
				String line = "";

				while ((line = br.readLine()) != null) { // readLine = �� �پ� �о��

					if(line.contains("<KW>")){ // <KW> ���� @DOCUMENT ���ö����� �� ����
						while((line = br.readLine()) != null){
							if(line.contains("@DOCUMENT") || line==null){
								break;
							}
						}
						continue;
					}
					tokendString = new StringTokenizer(line," |/||!|#|%|:|,|.|-|[|]|~|(|)|\"|\'|;|*|&|<|>|=|��|��|��|��|��|?");
					while (tokendString.hasMoreTokens()) {
						String word = tokendString.nextToken();
						
						if(word.equals("DocID")){ // ���Ͽ��� ����ϳ� ��������
							
							line = br.readLine(); // �� ������ �ҷ��� (#CAT)
							tokendString = new StringTokenizer(line,"/");
							tokendString.nextToken(); // ī�װ� ����
							
							cat.add(tokendString.nextToken()); // ���� ī�װ� ����
							String lastCat = cat.get(cat.size() - 1);
							if (lastCat.equals("�ǰ��� ����"))
								categoryNum = "1";
							else if (lastCat.equals("����"))
								categoryNum = "2";
							else if (lastCat.equals("����"))
								categoryNum = "3";
							else if (lastCat.equals("����"))
								categoryNum = "4";
							else if (lastCat.equals("��ȭ�� ����"))
								categoryNum = "5";
							else if (lastCat.equals("��ȸ"))
								categoryNum = "6";
							else if (lastCat.equals("���"))
								categoryNum = "7";
							else if (lastCat.equals("������Ȱ"))
								categoryNum = "8";
							
							countRow++;
							
							if(TreeMap != null){ // ���Ͽ� ���ĺ� ī������ ������ ���� ���ֱ�
								
								System.out.println("�� = "+countRow);
								fw.write("\n"+categoryNum+" ");// ���� ùĭ�� ���� document�� ī�װ� �ϳ� ���� 
								
								for(int i=0;i<TreeMap.size();i++){
									Integer key = iteratorKey.next();
									key++;
									fw.write(key+":"+TreeMap.get(key-1)+" ");
								}
							}

							TreeMap = new TreeMap<>(); // Ʈ���� ���� �ʱ�ȭ
							
							br.readLine(); br.readLine(); br.readLine(); // #TEXT�ִ� �ٴٴ����ٷ� �̵���Ű�� ������ TEXT�������� �ٽ� �о���� ����
							break; 
						}
						if(word.equals("@DOCUMENT"))
							break;
			
						// �ε����� ������������ �����ϱ�����
						int index; 
						if((index = FEAT.indexOf(word)) != -1){
							TreeMap.put(index,CHI.get(index) );
						}
						iteratorKey = TreeMap.keySet().iterator();
				
					} // �ڸ� �ܾ� �ϳ��� ������ while
				} // ���� �۰� while
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null)
					try {
						br.close();
					} // ������ �о����� ��Ʈ�� ����
					catch (IOException e) {
					}
			}
		fw.close(); // writer �ݱ�
		}catch (IOException e) {} // file writer �� ����ó��
	}
}// class

