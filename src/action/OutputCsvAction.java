package action;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutputCsvAction {

	final private int TESTNUMBER = 40; //問題数
	InputCsvAction ica = new InputCsvAction();
	/**
	 * 正答率用CSVファイルの出力
	 * @param Map
	 */
	public void outRateCsv(Map<String,Object> Map) {

		try {
			//フォルダの確認（ない場合は作成）
			File folder = new File("c:\\Test");
			if(folder.exists() == false) {
				folder.mkdir();
			}

			//出力CSVファイルの確認
			boolean existFlg;
			File file = new File("C:\\Test\\TestResult.csv");
			if(file.exists()) {
				existFlg = true;
			}
			else {
				existFlg = false;
			}

			// 出力ファイルの作成
			FileOutputStream fos;
			if(existFlg == false) {
				fos = new FileOutputStream("C:\\Test\\TestResult.csv",false);
			}
			else {
				fos = new FileOutputStream("C:\\Test\\TestResult.csv",true);
			}
			OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("Shift_JIS"));
        	PrintWriter pw = new PrintWriter(osw);

        	if(existFlg == false) {
        		//新規作成するときのみ実行
        		pw.print(",,,,,,,テスト１（文章把握）,,,テスト２（四則演算）,,,テスト３（言語推論）,,,テスト４（数列）");
        		pw.println(",,,言語（テスト１＋３）,,,数理（テスト２＋４）,,,総合（テスト１-４）,,,言語正答数,,数理正答数,,総合解答数,,総合正答数,, ");
        		pw.print("タイムスタンプ,受験者ID,氏名,性別,生年月日,最終学歴,現在の状態,");
        		for(int i=0;i<7;i++) {
        			pw.print("解答数,正答数,正答率,");
        		}
        		for(int j=0;j<4;j++) {
        			pw.print("粗点,換算点,");
        		}
        		pw.println("総合正答率");
        	}
        	else {
        		//作成済みのCSVファイルの書き込み位置の設定
        		FileInputStream fi = new FileInputStream("C:\\\\Test\\\\TestResult.csv");
        		InputStreamReader is = new InputStreamReader(fi);
        		BufferedReader br = new BufferedReader(is);

        		//ファイル読み込み
        	    String line = br.readLine();

        	    while( line != null )
                {
        	    	pw.println(line);

    				// readLine メソッドを使ってもう1行読み込む。
                    line = br.readLine();
                }
        	}

        	//タイムスタンプ
        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		String time = sdf.format(timestamp);
        	pw.print(time+",");

        	//受験者ID
        	sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        	String userId = sdf.format(timestamp);
        	pw.print(userId+",");

        	//名前
        	pw.print(Map.get("name")+",");
        	//性別
        	String gender = (String) Map.get("gender");
        	if(gender.contentEquals("man")) {
        		pw.print("男,");
        	}
        	else {
        		pw.print("女,");
        	}
        	//生年月日
        	pw.print(Map.get("birth")+",");
        	//最終学歴
        	pw.print(Map.get("edu")+",");
        	//現在の状態
        	String status = (String) Map.get("status");
        	if(status.equals("sonota")) {
        		pw.print((String)Map.get("sonota")+",");
        	}
        	else {
        		switch(status) {
        		case "student":
        			pw.print("在学中,");
        			break;
        		case "second":
        			pw.print("第二新卒,");
        			break;
        		case "worker":
        			pw.print("会社員,");
        			break;
        		}
        	}

        	//各テストの解答数等の出力
        	int i; //テストの番号(1～4)
        	//言語用変数
        	int langACount=0;
        	int langCCount=0;
        	double langCRate;
        	//数理用変数
        	int mathACount=0;
        	int mathCCount=0;
        	double mathCRate;

        	for(i=1;i<=4;i++) {
        		List<String> testList = (List<String>) Map.get("test"+i);

        		//テストの答えを取得
        		List<String> answerList = getTestAnswer(i);

        		List<Double> calcList = calcTest(testList,answerList);
        		double aCount = calcList.get(0); //解答数
        		double cCount = calcList.get(1); //正答数
        		double cRate =  Math.round(calcList.get(2) * 100.0); //正答率
        		pw.print((int)aCount+",");
        		pw.print((int)cCount+",");
        		pw.print((int)cRate+"%,");

        		if(i%2==1) {
        			langACount += (int)aCount;
        			langCCount += (int)cCount;
        		}
        		else {
        			mathACount += (int)aCount;
        			mathCCount += (int)cCount;
        		}
        	}

        	//言語出力
        	pw.print(langACount+",");
        	pw.print(langCCount+",");
        	langCRate =Math.round(((double)langCCount / (double)langACount) * 100.0);
        	pw.print((int)langCRate+"%,");
        	//数理出力
        	pw.print(mathACount+",");
        	pw.print(mathCCount+",");
        	mathCRate =Math.round(((double)mathCCount / (double)mathACount) * 100.0);
        	pw.print((int)mathCRate+"%,");
        	//総合出力
        	int totalACount = langACount+mathACount;
        	int totalCCount = langCCount + mathCCount;
        	pw.print(totalACount+",");
        	pw.print(totalCCount+",");
        	double totalCRate =Math.round(((double)totalCCount / (double)totalACount) * 100.0);
        	pw.print((int)totalCRate+"%,");

        	int convert = 0; //換算点
        	//言語 粗点・換算点
        	pw.print(langCCount+",");
        	convert = ica.getConvertScore(langACount,"lang");
        	pw.print(convert+",");
        	//数理 粗点・換算点
        	pw.print(mathCCount+",");
        	convert = ica.getConvertScore(mathACount,"math");
        	pw.print(convert+",");
        	//総合解答数 粗点・換算点
        	pw.print(totalACount+",");
        	convert = ica.getConvertScore(totalACount,"answer");
        	pw.print(convert+",");
        	//総合正答数 粗点・換算点
        	pw.print(totalCCount+",");
        	convert = ica.getConvertScore(totalCCount,"correct");
        	pw.print(convert+",");

        	//総合正答率
        	pw.print((int)totalCRate+"%");

        	pw.close();


		} catch (IOException ex) {
            ex.printStackTrace();
        }
	}

	/**
	 * テストの答えを取得する
	 * @param number
	 * @return
	 */
	private List<String> getTestAnswer(int number){
		List<String> answerList = new ArrayList<>();
		List<String> allAnswerList = ica.getAnswer();

		switch(number) {
		case 1:
			for(int i=TESTNUMBER*0;i<TESTNUMBER*1;i++) {
				answerList.add(allAnswerList.get(i));
			}
			break;
		case 2:
			for(int i=TESTNUMBER*1;i<TESTNUMBER*2;i++) {
				answerList.add(allAnswerList.get(i));
			}
			break;
		case 3:
			for(int i=TESTNUMBER*2;i<TESTNUMBER*3;i++) {
				answerList.add(allAnswerList.get(i));
			}
			break;
		case 4:
			for(int i=TESTNUMBER*3;i<TESTNUMBER*4;i++) {
				answerList.add(allAnswerList.get(i));
			}
			break;
		}

		return answerList;
	}

	/**
	 * 解答数、正答数、正答率を計算
	 * @param testList
	 * @param answerList
	 * @return
	 */
	private List<Double> calcTest(List<String> testList,List<String> answerList){
		List<Double> calcList = new ArrayList<>(); //0:解答数 1:正答数 2:正答率

		double aCount = 0; //解答数
		double cCount = 0; //正答数
		double cRate;      //正答率
		for(int i=0;i<TESTNUMBER;i++) {
			if(testList.get(i) != null) {
				aCount++;
				if(testList.get(i).equals(answerList.get(i))) {
					cCount++;
				}
			}
		}
		cRate=cCount / aCount;

		calcList.add(aCount);
		calcList.add(cCount);
		calcList.add(cRate);

		return calcList;
	}

}