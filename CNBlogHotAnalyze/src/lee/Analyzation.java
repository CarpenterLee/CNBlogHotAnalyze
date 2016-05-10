package lee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 对抓取的博客数据进行基本的统计分析。
 * @author CarpenterLee
 */
public class Analyzation {
	public static void main(String[] args) throws Exception{
		new Analyzation().driver();
	}
	private void driver() throws Exception{
//		distrubitionHourOfDay(new File("./out/database4000_3.txt"));
		distrubitionDayOfWeek(new File("./out/database4000_3.txt"));
	}
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/**
	 * 从inFile指定的文件中读取博客数据，讲述据博客数据按天分类，统计每天的博客量和博客热度。
	 * @param inFile
	 * @throws Exception
	 */
	private void distrubitionDayOfWeek(File inFile) throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		String line;
		int itemCountPerDay[] = new int[7];
		int scoreSumPerDay[] = new int[7];
		double avgScorePerDay[] = new double[7];
		int totalItems = 0;
		while((line = reader.readLine()) != null){
			String[]  lineTokens = line.split("\t");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(simpleDateFormat.parse(lineTokens[0]));
			int recommend = Integer.parseInt(lineTokens[1]);
			int comment = Integer.parseInt(lineTokens[2]);
			int view = Integer.parseInt(lineTokens[3]);
			int score = recommend*10+comment*5+view;
			itemCountPerDay[calendar.get(Calendar.DAY_OF_WEEK)-1]++;
			//为避免离群点，分数高于1600就只取1600
			scoreSumPerDay[calendar.get(Calendar.DAY_OF_WEEK)-1] += (score < 1600 ? score : 1600);
			totalItems++;
		}
		reader.close();
		//0表示星期天，1表示星期一。
		for(int i = 0; i < avgScorePerDay.length; i++){
			avgScorePerDay[i] = 1.0*scoreSumPerDay[i]/itemCountPerDay[i];
			System.out.println(i + "\t" + scoreSumPerDay[i] + 
					"\t" + itemCountPerDay[i] + "\t" + avgScorePerDay[i]);
		}
		System.out.println("totalItems=" + totalItems);
	}
	/**
	 * 从inFile指定的文件中读取博客数据，讲述据博客数据按小时分类，统计每小时的博客量和博客热度。
	 * @param inFile
	 * @throws Exception
	 */
	private void distrubitionHourOfDay(File inFile) throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		String line;
		int itemCountPerHour[] = new int[24];
		int scoreSumPerHour[] = new int[24];
		double avgScorePerHour[] = new double[24];
		int totalItems = 0;
		while((line = reader.readLine()) != null){
			String[]  lineTokens = line.split("\t");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(simpleDateFormat.parse(lineTokens[0]));
			int recommend = Integer.parseInt(lineTokens[1]);
			int comment = Integer.parseInt(lineTokens[2]);
			int view = Integer.parseInt(lineTokens[3]);
			int score = recommend*10+comment*5+view;
			itemCountPerHour[calendar.get(Calendar.HOUR_OF_DAY)]++;
//			为避免离群点，分数高于1600就只取1600
			scoreSumPerHour[calendar.get(Calendar.HOUR_OF_DAY)] += (score < 1600 ? score : 1600);
			totalItems++;
		}
		reader.close();
		for(int i = 0; i < 24; i++){
			avgScorePerHour[i] = 1.0*scoreSumPerHour[i]/itemCountPerHour[i];
			System.out.println(i + "\t" + scoreSumPerHour[i] + 
					"\t" + itemCountPerHour[i] + "\t" + avgScorePerHour[i]);
		}
		System.out.println("totalItems=" + totalItems);
	}

}
