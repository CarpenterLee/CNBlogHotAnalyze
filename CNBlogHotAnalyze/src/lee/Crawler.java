package lee;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 抓取http://www.cnblogs.com/sitehome/p/XXXX下的博客，并抽取有效数据。
 * eg: http://www.cnblogs.com/sitehome/p/3
 * @author CarpenterLee
 */
public class Crawler {
	private int itemCount = 0;
	public static void main(String[] args) throws Exception{
		long startTime = System.currentTimeMillis();
		
		new Crawler().driver(2, new File("out/database.txt"));
		
		long endTime = System.currentTimeMillis();
		int minutes = (int)(endTime-startTime)/1000/60;
		int seconds = (int)(endTime-startTime)/1000%60;
		System.out.println("time cost: " + minutes + " minutes " + seconds + " seconds");
	}
	private void driver(int pages, File outFile) throws Exception{
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFile));
		String urlBase = "http://www.cnblogs.com/sitehome/p/";//博客园首页文章分页地址
		for(int i = 1; i <= pages; ++i){
			crawler(urlBase+i, bufferedWriter);
		}
		bufferedWriter.close();
		System.out.println("----抓取页数: " + pages + ", 抓取博文数量: " + itemCount);
	}
	/**
	 * 抓取url指定的地址，解析出数据后写入到writer指定的文件。
	 * @param url
	 * @param writer
	 * @throws Exception
	 */
	private void crawler(String url, Writer writer) throws Exception{
		Document doc = Jsoup.connect(url).get();
		Elements elements = doc.getElementsByAttributeValue("class", "post_item");
		for(Element e : elements){
			String recommendCount = e.getElementsByAttributeValue("class", "diggnum").first().text();
			String titleStr = e.getElementsByAttributeValue("class", "titlelnk").first().text();
			String titlelnk = e.getElementsByAttributeValue("class", "titlelnk").first().attr("href");
			String summaryStr = e.getElementsByAttributeValue("class", "post_item_summary").first().text();
			String author = e.getElementsByAttributeValue("class", "lightblue").first().text();
			String authorURL = e.getElementsByAttributeValue("class", "lightblue").first().attr("href");
			String article_comment = e.getElementsByAttributeValue("class", "article_comment").first().text();
			Matcher matcher = Pattern.compile("[0-9]+").matcher(article_comment);
			matcher.find();
			String commentCount = matcher.group();
			String article_view = e.getElementsByAttributeValue("class", "article_view").first().text();
			matcher = Pattern.compile("[0-9]+").matcher(article_view);
			matcher.find();
			String viewCount = matcher.group();
			String post_item_foot = e.getElementsByAttributeValue("class", "post_item_foot").first().text();
			matcher = Pattern.compile("[0-9]+-[0-9]+-[0-9]+ [0-9]+:[0-9]+").matcher(post_item_foot);
			matcher.find();
			String releaseTime = matcher.group();
			System.out.println(author);
			String delimiter = "\t";
			writer.append(releaseTime);
			writer.append(delimiter);
			writer.append(recommendCount);
			writer.append(delimiter);
			writer.append(commentCount);
			writer.append(delimiter);
			writer.append(viewCount);
			writer.append(delimiter);
			writer.append(author);
			writer.append(delimiter);
			writer.append(authorURL);
			writer.append(delimiter);
			writer.append(titleStr);
			writer.append(delimiter);
			writer.append(titlelnk);
			writer.append(delimiter);
			writer.append(summaryStr);
			writer.append(delimiter);
			writer.append("\n");
			writer.flush();
			itemCount++;
		}
	}
}
