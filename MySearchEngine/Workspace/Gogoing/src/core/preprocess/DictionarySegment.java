package core.preprocess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

import configure.Configuration;

import core.util.HtmlParser;

//�ִ���
public class DictionarySegment {

	private HashSet<String> dictionary; // �ʵ�
	private HashSet<String> stopWordDictionary; // ͣ�ôʴʵ�
	private DictionaryReader dictionaryReader = new DictionaryReader();
	private static final int maxLength = 4;
	private static String dictionaryFile = "";
	private static String stopDictionaryFile = "";
	private Configuration configuration;

	public DictionarySegment() {
		// ��ȡ�ļ�·��
		configuration = new Configuration();
		dictionaryFile = configuration.getValue("DICTIONARYPATH") + "\\Dictionary\\wordlist.txt";
		stopDictionaryFile = configuration.getValue("DICTIONARYPATH") + "\\Dictionary\\stopWord.txt";

		dictionary = dictionaryReader.scanDictionary(dictionaryFile);
		stopWordDictionary = dictionaryReader.scanDictionary(stopDictionaryFile);
	}

	public ArrayList<String> SegmentFile(String htmlDoc) {
		// ��html���ļ���������ʽ����ȥ����ǩ��������Ϣ�������ı����в���
		HtmlParser htmlparser = new HtmlParser();
		String htmlText = htmlparser.htmlToText(htmlDoc);

		// �Ͼ�cutIntoSentance���Ѿ��Ӵ���cutIntoWord��Ȼ���÷���ֵ
		ArrayList<String> sentances = cutIntoSentance(htmlText);
		ArrayList<String> segResult = new ArrayList<String>();
		for (int i = 0; i < sentances.size(); i++) {
			segResult.addAll(cutIntoWord(sentances.get(i)));
		}

		return segResult;
	}

	public ArrayList<String> cutIntoSentance(String htmlDoc) {

		ArrayList<String> sentance = new ArrayList<String>();
		// �Կո����","��"."��"!"��Ϊ�����
		String token = "������������������������������-";
		// ����StringTokenizer��Ķ���stringTokenizer,�������ַ���stringTokenizer�ķ�����
		StringTokenizer stringTokenizer = new StringTokenizer(htmlDoc, token);

		// ��ȡ�ַ���str1�����Է��ŵĸ���
		int number = stringTokenizer.countTokens();

		// ����ѭ������ȡ�ַ���str1����һ�����Է���,�����
		while (stringTokenizer.hasMoreTokens())
			sentance.add(stringTokenizer.nextToken());

		return sentance;
	}

	// ����ͣ�ôʣ����˵���
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

			// temp�ǷֺõĴʣ��ж�temp�Ƿ���ͣ�ôʱ��У�������ǣ������list��
			if (!stopWordDictionary.contains(temp) && temp.length() != 1)
				sentanceSegResult.add(temp);

			// ����ȥ��temp���ȵ��ַ���������ִ��
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
