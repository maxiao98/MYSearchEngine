package core.query;

import java.util.ArrayList;
import java.util.HashMap;
import core.preprocess.DictionarySegment;
import core.preprocess.WordsSegmenter;
import core.preprocess.invertedIndex.InvertedIndex;
import core.util.Result;
import core.util.ResultGenerator;

public class Response {

	// ��������
	private HashMap<String, ArrayList<String>> invertedIndexMap;

	// ���ؽ���б�
	private ArrayList<Result> results;

	// ��ѯ���ִ���
	private WordsSegmenter wordsSegmenter;

	private ResultGenerator resultGenerator;

	public Response() {
		InvertedIndex invertedIndex = new InvertedIndex();
		invertedIndexMap = invertedIndex.createInvertedIndex();
		wordsSegmenter = new WordsSegmenter(true);
		resultGenerator = new ResultGenerator();
	}

	public ArrayList<Result> getResponse(String request) {
		query(request);
		return results;
	}

	private void query(String request) {

		results = new ArrayList<Result>();

		// �ؼ��ʷִʡ��޳�ͣ�ôʣ����Էִʽ�����в��Ҷ�Ӧ�Ľ��
		wordsSegmenter.initialiseTagHashMap();

		// �ϲ������ִʵĽ�������س�������ҳURL��Ϣ
		ArrayList<String> keyWords = wordsSegmenter.segmentSentence(request.toCharArray());

		System.out.println("��ѯ�ؼ��ֱ���Ϊ:");
		for (String keyWord : keyWords)
			System.out.println(keyWord);

		System.out.println("�ִʽ����ʾ����");

		ArrayList<String> resultUrl = new ArrayList<String>();

		ArrayList<String> tempResult = new ArrayList<String>();
		for (String keyWord : keyWords) {
			tempResult = invertedIndexMap.get(keyWord);
			if (tempResult != null) {
				resultUrl = mergeResultURL(resultUrl, tempResult);
			}
		}

		try {

			if (resultUrl.size() != 0) {

				System.out.println("��ѯ�����");
				for (String url : resultUrl)
					System.out.println(url);

				// ����URLͨ�����ݿ�����ҳ����λ�ã��Ӷ���RAWs�л����ҳ����

				for (String url : resultUrl) {

					Result result = resultGenerator.generateResult(url);
					if (result == null)
						System.out.println(url + "��Ӧ��resultΪ��");
					else {
						results.add(result);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private ArrayList<String> mergeResultURL(ArrayList<String> resultUrl, ArrayList<String> tempResult) {
		// �����һ��ִ�У���ôresultUrl���ǿյģ�ֱ�ӷ���resultTemp
		if (resultUrl.size() == 0)
			return tempResult;

		// ������Ҫ�ϲ����ߵĹ�������
		ArrayList<String> totalResult = new ArrayList<String>();
		for (String tempUrl : tempResult) {
			if (resultUrl.contains(tempUrl))
				totalResult.add(tempUrl);
		}

		return totalResult;
	}

	public static void main(String[] args) {
		//����һ��������
		Response response = new Response();
		ArrayList<Result> results = response.getResponse("��Ӱ");

		System.out.println("���ؽ�����£�");
		for (Result result : results) {
			System.out.println(result.getTitle());
			System.out.println(result.getContent());
			System.out.println(result.getUrl() + "  " + result.getDate());
		}

	}

}
