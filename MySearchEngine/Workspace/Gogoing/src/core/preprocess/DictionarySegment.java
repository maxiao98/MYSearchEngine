package core.preprocess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

import configure.Configuration;

import core.util.HtmlParser;

//分词类
public class DictionarySegment {

	private HashSet<String> dictionary; // 词典
	private HashSet<String> stopWordDictionary; // 停用词词典
	private DictionaryReader dictionaryReader = new DictionaryReader();
	private static final int maxLength = 4;
	private static String dictionaryFile = "";
	private static String stopDictionaryFile = "";
	private Configuration configuration;

	public DictionarySegment() {
		// 获取文件路径
		configuration = new Configuration();
		dictionaryFile = configuration.getValue("DICTIONARYPATH") + "\\Dictionary\\wordlist.txt";
		stopDictionaryFile = configuration.getValue("DICTIONARYPATH") + "\\Dictionary\\stopWord.txt";

		dictionary = dictionaryReader.scanDictionary(dictionaryFile);
		stopWordDictionary = dictionaryReader.scanDictionary(stopDictionaryFile);
	}

	public ArrayList<String> SegmentFile(String htmlDoc) {
		// 把html的文件用正则表达式处理，去掉标签等无用信息，保留文本进行操作
		HtmlParser htmlparser = new HtmlParser();
		String htmlText = htmlparser.htmlToText(htmlDoc);

		// 断句cutIntoSentance，把句子传到cutIntoWord，然后获得返回值
		ArrayList<String> sentances = cutIntoSentance(htmlText);
		ArrayList<String> segResult = new ArrayList<String>();
		for (int i = 0; i < sentances.size(); i++) {
			segResult.addAll(cutIntoWord(sentances.get(i)));
		}

		return segResult;
	}

	public ArrayList<String> cutIntoSentance(String htmlDoc) {

		ArrayList<String> sentance = new ArrayList<String>();
		// 以空格符、","、"."及"!"作为定界符
		String token = "。，、；：？！“”‘’《》（）-";
		// 创建StringTokenizer类的对象stringTokenizer,并构造字符串stringTokenizer的分析器
		StringTokenizer stringTokenizer = new StringTokenizer(htmlDoc, token);

		// 获取字符串str1中语言符号的个数
		int number = stringTokenizer.countTokens();

		// 利用循环来获取字符串str1中下一个语言符号,并输出
		while (stringTokenizer.hasMoreTokens())
			sentance.add(stringTokenizer.nextToken());

		return sentance;
	}

	// 过滤停用词，过滤单字
	public ArrayList<String> cutIntoWord(String sentance) {
		int currentLength = 0;
		String waitTocut = sentance;
		ArrayList<String> sentanceSegResult = new ArrayList<String>();

		while (waitTocut.length() != 0) {
			String temp;
			if (waitTocut.length() >= maxLength)
				currentLength = maxLength;
			else
				currentLength = waitTocut.length();

			temp = waitTocut.substring(0, currentLength);

			while (!dictionary.contains(temp) && currentLength > 1) {
				currentLength--;
				temp = temp.substring(0, currentLength);
			}

			// temp是分好的词，判断temp是否在停用词表中，如果不是，则放入list中
			if (!stopWordDictionary.contains(temp) && temp.length() != 1)
				sentanceSegResult.add(temp);

			// 句子去除temp长度的字符串，继续执行
			waitTocut = waitTocut.substring(currentLength);
		}

		// System.out.println(result);
		return sentanceSegResult;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DictionarySegment dictSeg = new DictionarySegment();
	}

}
