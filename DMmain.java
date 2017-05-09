import java.io.*;
import java.security.KeyStore.Entry;
import java.util.*;

public class DMmain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		StringTokenizer tokendString;
		BufferedReader br = null; // 읽어온거 담을 버퍼공간
		String[] result = new String[4];
		ArrayList<String> cat = new ArrayList<>();
		TreeSet[] ts = new TreeSet[8];
		TreeSet sum = new TreeSet();
		String docId ="";
		int[] docCount = new int[8];
		
		for(int i=0;i<8;i++){ // treeSet 배열 초기화
			ts[i] = new TreeSet();
		}
		
		for (int k = 1; k <= 4; k++) {
			File file = new File("C:\\workspace\\DataMining\\src", "HKIB-20000_00"+k+".txt");
			try {
				br = new BufferedReader(new FileReader(file));
				String line = "";

				while ((line = br.readLine()) != null) { // readLine = 한 줄씩 읽어옴

					if(line.contains("<KW>")){ // <KW> 이후 @DOCUMENT 나올때까지 다 무시
						while((line = br.readLine()) != null){
							if(line.contains("@DOCUMENT") || line==null){
								break;
							}
						}
						continue;
					}
					tokendString = new StringTokenizer(line," |/||!|#|%|:|,|.|-|[|]|~|(|)|\"|\'|;|*|&|<|>|=|을|를|은|는|에|?");
					while (tokendString.hasMoreTokens()) {
						String word = tokendString.nextToken();
						
						if(word.equals("DocID")){ // 파일에서 기사하나 나왔을때
							if(tokendString.countTokens()==1){
								docId = tokendString.nextToken(); // 그때의 문서 id를 따로 저장
							}else{
								docId = tokendString.nextToken()+"-"+tokendString.nextToken(); // 그때의 문서 id를 따로 저장
							}
							
							line = br.readLine(); // 그 다음줄 불러옴
							tokendString = new StringTokenizer(line,"/");
							tokendString.nextToken();
							cat.add(tokendString.nextToken()); // 카테고리 분야
							
							String temp = cat.get(cat.size()-1); // cat의 마지막거 임시저장 
							if (temp.equals("건강과 의학")) // 각 cat 범주의 doc갯수 세주기 위해
								docCount[0]++;
							else if (temp.equals("경제"))
								docCount[1]++;
							else if (temp.equals("과학"))
								docCount[2]++;
							else if (temp.equals("교육"))
								docCount[3]++;
							else if (temp.equals("문화와 종교"))
								docCount[4]++;
							else if (temp.equals("사회"))
								docCount[5]++;
							else if (temp.equals("산업"))
								docCount[6]++;
							else if (temp.equals("여가생활"))
								docCount[7]++;
							
							br.readLine(); br.readLine(); br.readLine(); // #TEXT있는 다다다음줄로 이동시키고 끝내서 TEXT다음부터 다시 읽어오기 시작
							break;
						}
						if (cat.size() != 0) {
							if(word.equals("@DOCUMENT"))
								break;
							String lastCat = cat.get(cat.size() - 1);
							if (lastCat.equals("건강과 의학"))
								ts[0].add("0+"+docId+" "+word);
							else if (lastCat.equals("경제"))
								ts[1].add("1+"+docId+" "+word);
							else if (lastCat.equals("과학"))
								ts[2].add("2+"+docId+" "+word);
							else if (lastCat.equals("교육"))
								ts[3].add("3+"+docId+" "+word);
							else if (lastCat.equals("문화와 종교"))
								ts[4].add("4+"+docId+" "+word);
							else if (lastCat.equals("사회"))
								ts[5].add("5+"+docId+" "+word);
							else if (lastCat.equals("산업"))
								ts[6].add("6+"+docId+" "+word);
							else if (lastCat.equals("여가생활"))
								ts[7].add("7+"+docId+" "+word);
							
							sum.add(word);
						}
					}
				}
				//result[k-1] = "\n[문서"+k+"]까지  Featuer 갯수 : " + ts.size() + "\n";
				result[k-1] = "\n[txt"+k+"]까지  Featuer 갯수 : " + (ts[0].size()+ts[1].size()+ts[2].size()+ts[3].size()
						+ts[4].size()+ts[5].size()+ts[6].size()+ts[7].size()) + "\n";
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null)
					try {
						br.close();
					} // 끝까지 읽었으면 스트림 닫음
					catch (IOException e) {
					}
			}
			System.out.println("작업중..."+k*25+"%");
			
		}// for 4 times
		
		//****************************  카이 스퀘어    ******************************//
	    float A=0,B=0,C=0,D=0;
	    float[] square = new float[]{0,0,0,0,0,0,0,0};
	    String str1,str2;
	    
	    ArrayList<String> sumList = new ArrayList<>(sum);
	    ArrayList<String>[] tsList = new ArrayList[8];
	    ArrayList<String>[] tsListId = new ArrayList[8];
	    
	    StringTokenizer t;
	    for(int i=0;i<8;i++){
	    	tsList[i] = new ArrayList<>(ts[i]); // treeSet을 List로
	    	tsListId[i] = new ArrayList<>(); // docID list 초기화
	    }
		for (int i = 0; i < 8; i++) { // 앞에 docID 는 다 날린 상태로 다시 저장 -- 밑에서 indexOf로  해보려고....
			for (int k = 0; k < tsList[i].size(); k++) {
				t = new StringTokenizer(tsList[i].get(k), " ");
				tsListId[i].add(t.nextToken()); // docId
				tsList[i].set(k, t.nextToken()); // 수정
			}
		}

	    StringTokenizer tkn;
	    
	    //System.out.println("sum -> List "+sumList.size()+"\n");
	    
	    Map<Float,String> chiVal = new HashMap<Float,String>();
	    
	    try{
            BufferedWriter fw = new BufferedWriter(new FileWriter("D:\\train\\compile.txt",true));

	    
	    //-------------------- sumList의 피쳐 하나씩 꺼내서 tsList 0~7 과 일치하는거 찾기 -----------------//
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
			for (int i = 0; i < 8; i++) { // 최대 square 값 찾기
				for (int k = 0; k < 7; k++) {
					if (square[k] > square[k + 1]) {
						temp = square[k + 1];
						square[k + 1] = square[k];
						square[k] = temp;
					}
				}
			}

			max = square[7]; // 이 피쳐의 카이제곱 값
			
			
	        fw.write(max+" "+sumList.get(s)+"/");
	        fw.flush();
	        
			//chiVal.put(max,sumList.get(s)); // treeSet에 카이제곱 값 + 피쳐 형태로 최종 저장
	    } // sumList for문
	    fw.close();
	    } // 파일쓰기 try
	    catch(Exception e){
	    	e.printStackTrace();
	    }

	    ArrayList<String> Vector = new ArrayList<>();
	   
	    Vector.addAll(makeVector()); // 카이제곱, 피쳐 형태로 저장해놓은 파일을 읽어서 카이제곱 값으로 소팅, 최종 벡터를 리스트 타입으로 만든다
	    makeTrain(Vector); // train 파일을 만든다
	    LastTest(Vector); // test 파일을 만든다
	    
	} // main  
	
	
	public static ArrayList makeVector(){
		System.out.println("시작!");
		
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
				} // 끝까지 읽었으면 스트림 닫음
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
        //Collections.reverse(list); // 주석시 오름차순
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

				while ((line = br.readLine()) != null) { // readLine = 한 줄씩 읽어옴

					if(line.contains("<KW>")){ // <KW> 이후 @DOCUMENT 나올때까지 다 무시
						while((line = br.readLine()) != null){
							if(line.contains("@DOCUMENT") || line==null){
								break;
							}
						}
						continue;
					}
					tokendString = new StringTokenizer(line," |/||!|#|%|:|,|.|-|[|]|~|(|)|\"|\'|;|*|&|<|>|=|을|를|은|는|에|?");
					while (tokendString.hasMoreTokens()) {
						String word = tokendString.nextToken();
						
						if(word.equals("DocID")){ // 파일에서 기사하나 나왔을때
							
							line = br.readLine(); // 그 다음줄 불러옴 (#CAT)
							tokendString = new StringTokenizer(line,"/");
							tokendString.nextToken(); // 카테고리 종류
							
							cat.add(tokendString.nextToken()); // 현재 카테고리 저장
							String lastCat = cat.get(cat.size() - 1);
							if (lastCat.equals("건강과 의학"))
								categoryNum = "1";
							else if (lastCat.equals("경제"))
								categoryNum = "2";
							else if (lastCat.equals("과학"))
								categoryNum = "3";
							else if (lastCat.equals("교육"))
								categoryNum = "4";
							else if (lastCat.equals("문화와 종교"))
								categoryNum = "5";
							else if (lastCat.equals("사회"))
								categoryNum = "6";
							else if (lastCat.equals("산업"))
								categoryNum = "7";
							else if (lastCat.equals("여가생활"))
								categoryNum = "7";
						
							countRow++;
							
							if(TreeMap != null){ // 파일에 피쳐별 카이제곱 값으로 한줄 써주기
								
								System.out.println("행 = "+countRow);
								fw.write("\n"+categoryNum+" ");// 파일 첫칸에 현재 document의 카테고리 하나 써줌 
								
								
								
								for(int i=0;i<TreeMap.size();i++){
									Integer key = iteratorKey.next();
									key++;
									fw.write(key+":"+TreeMap.get(key-1)+" ");
								}
							}

							TreeMap = new TreeMap<>(); // 트리맵 새로 초기화
							
							br.readLine(); br.readLine(); br.readLine(); 
							break;
						}
						if(word.equals("@DOCUMENT"))
							break;
				
						// 인덱스를 오름차순으로 정렬하기위해
						int index; 
						if((index = FEAT.indexOf(word)) != -1){
							TreeMap.put(index,CHI.get(index) );
						}
						iteratorKey = TreeMap.keySet().iterator();
			
					} // 자른 단어 하나씩 꺼내는 while
				} // 가장 밖같 while
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null)
					try {
						br.close();
					} // 끝까지 읽었으면 스트림 닫음
					catch (IOException e) {
					}
			}
		}// for 4 times*/
		System.out.println("벡터 생성!");
		
		
		fw.close(); // writer 닫기
		}catch (IOException e) {} // file writer 의 예외처리
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

				while ((line = br.readLine()) != null) { // readLine = 한 줄씩 읽어옴

					if(line.contains("<KW>")){ // <KW> 이후 @DOCUMENT 나올때까지 다 무시
						while((line = br.readLine()) != null){
							if(line.contains("@DOCUMENT") || line==null){
								break;
							}
						}
						continue;
					}
					tokendString = new StringTokenizer(line," |/||!|#|%|:|,|.|-|[|]|~|(|)|\"|\'|;|*|&|<|>|=|을|를|은|는|에|?");
					while (tokendString.hasMoreTokens()) {
						String word = tokendString.nextToken();
						
						if(word.equals("DocID")){ // 파일에서 기사하나 나왔을때
							
							line = br.readLine(); // 그 다음줄 불러옴 (#CAT)
							tokendString = new StringTokenizer(line,"/");
							tokendString.nextToken(); // 카테고리 종류
							
							cat.add(tokendString.nextToken()); // 현재 카테고리 저장
							String lastCat = cat.get(cat.size() - 1);
							if (lastCat.equals("건강과 의학"))
								categoryNum = "1";
							else if (lastCat.equals("경제"))
								categoryNum = "2";
							else if (lastCat.equals("과학"))
								categoryNum = "3";
							else if (lastCat.equals("교육"))
								categoryNum = "4";
							else if (lastCat.equals("문화와 종교"))
								categoryNum = "5";
							else if (lastCat.equals("사회"))
								categoryNum = "6";
							else if (lastCat.equals("산업"))
								categoryNum = "7";
							else if (lastCat.equals("여가생활"))
								categoryNum = "8";
							
							countRow++;
							
							if(TreeMap != null){ // 파일에 피쳐별 카이제곱 값으로 한줄 써주기
								
								System.out.println("행 = "+countRow);
								fw.write("\n"+categoryNum+" ");// 파일 첫칸에 현재 document의 카테고리 하나 써줌 
								
								for(int i=0;i<TreeMap.size();i++){
									Integer key = iteratorKey.next();
									key++;
									fw.write(key+":"+TreeMap.get(key-1)+" ");
								}
							}

							TreeMap = new TreeMap<>(); // 트리맵 새로 초기화
							
							br.readLine(); br.readLine(); br.readLine(); // #TEXT있는 다다다음줄로 이동시키고 끝내서 TEXT다음부터 다시 읽어오기 시작
							break; 
						}
						if(word.equals("@DOCUMENT"))
							break;
			
						// 인덱스를 오름차순으로 정렬하기위해
						int index; 
						if((index = FEAT.indexOf(word)) != -1){
							TreeMap.put(index,CHI.get(index) );
						}
						iteratorKey = TreeMap.keySet().iterator();
				
					} // 자른 단어 하나씩 꺼내는 while
				} // 가장 밖같 while
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null)
					try {
						br.close();
					} // 끝까지 읽었으면 스트림 닫음
					catch (IOException e) {
					}
			}
		fw.close(); // writer 닫기
		}catch (IOException e) {} // file writer 의 예외처리
	}
}// class

