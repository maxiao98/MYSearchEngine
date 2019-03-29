package core.query;

import java.util.ArrayList;
import java.util.HashMap;
import core.preprocess.DictionarySegment;
import core.preprocess.WordsSegmenter;
import core.preprocess.invertedIndex.InvertedIndex;
import core.util.Result;
import core.util.ResultGenerator;

public class Response {

	// 倒排索引
	private HashMap<String, ArrayList<String>> invertedIndexMap;

	// 返回结果列表
	private ArrayList<Result> results;

	// 查询语句分词器
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

		// 关键词分词、剔除停用词，并对分词结果进行查找对应的结果
		wordsSegmenter.initialiseTagHashMap();

		// 合并各个分词的结果，返回初步的网页URL信息
		ArrayList<String> keyWords = wordsSegmenter.segmentSentence(request.toCharArray());

		System.out.println("查询关键字被分为:");
		for (String keyWord : keyWords)
			System.out.println(keyWord);

		System.out.println("分词结果显示结束");

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

				System.out.println("查询结果：");
				for (String url : resultUrl)
					System.out.println(url);

				// 根据URL通过数据库获得网页所在位置，从而在RAWs中获得网页内容

				for (String url : resultUrl) {

					Result result = resultGenerator.generateResult(url);
					if (result == null)
						System.out.println(url + "对应的result为空");
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
		// 如果第一次执行，那么resultUrl还是空的，直接返回resultTemp
		if (resultUrl.size() == 0)
			return tempResult;

		// 否则需要合并两者的公共部分
		ArrayList<String> totalResult = new ArrayList<String>();
		for (String tempUrl : tempResult) {
			if (resultUrl.contains(tempUrl))
				totalResult.add(tempUrl);
		}

		return totalResult;
	}

	public static void main(String[] args) {
		//输入一串语句测试
		Response response = new Response();
		ArrayList<Result> results = response.getResponse("电影");

		System.out.println("返回结果如下：");
		for (Result result : results) {
			System.out.println(result.getTitle());
			System.out.println(result.getContent());
			System.out.println(result.getUrl() + "  " + result.getDate());
		}

	}

}
