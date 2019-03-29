package core.preprocess.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import core.util.MD5;
import core.util.Page;

//RawsAnalyzer类实现了从原始网页集合Raws的分析操作，
//建立网页URL、网页内容摘要、网页在Raws中偏移的映射、所属Raws的映射 
//需要遍历其中的众多文件

public class RawsAnalyzer {

	private MD5 md5 = new MD5();
	private int offset;
	private Page page;
	private String rootDirectory;

	public RawsAnalyzer(String rootName) {
		this.rootDirectory = rootName;
		page = new Page();
	}

	// 文件夹读取
	public void createPageIndex() {
		ArrayList<String> fileNames = getSubFile(rootDirectory);
		for (String fileName : fileNames)
			createPage(fileName);
	}

	public void createPage(String fileName) {
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bfReader = new BufferedReader(fileReader);

			String word;
			offset = 0;
			int oldOffset = 0;

			while ((word = bfReader.readLine()) != null) {
				oldOffset = offset;
				
				offset += word.length() + 1;
				
				String url = readRawHead(bfReader);
				
				String content = readRawContent(bfReader);

				System.out.println(fileName +"偏移"+ offset);
				
				String contentMD5 = md5.getMD5ofString(content);//MD5生成
				
				page.setPage(url, oldOffset, contentMD5, fileName);
				
				page.store();
			}

			bfReader.close();

			System.out.println("该初始网页库解析完成");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//与getRawHead类似的操作
	private String readRawHead(BufferedReader bfReader) {
		String urlLine = null;
		try {

			urlLine = bfReader.readLine();
			offset = offset + urlLine.length() + 1;
			if (urlLine != null)
				urlLine = urlLine.substring(urlLine.indexOf(":") + 1, urlLine.length());

			String temp;
			while (!(temp = bfReader.readLine()).trim().isEmpty()) {
				offset = offset + temp.length() + 1;
			}
			offset += 1;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return urlLine;
	}
	//与getRawContent类似的操作
	private String readRawContent(BufferedReader bfReader) {
		StringBuffer strBuffer = new StringBuffer();

		try {
			String word;
			while ((word = bfReader.readLine()) != null) {
				offset = offset + word.length() + 1;
				if (word.trim().isEmpty())
					break;
				else
					strBuffer.append(word + "\n");
			}
			offset += 2;//跳了两行
		} catch (IOException e) {
			e.printStackTrace();
		}

		return strBuffer.toString();
	}
	//获得文件夹下所有文件名
	public static ArrayList<String> getSubFile(String fileName) {

		ArrayList<String> fileNames = new ArrayList<String>();

		File rootFile = new File(fileName);

		if (!rootFile.exists()) {
			System.out.println("文件夹不存在");
			return null;
		}
		if (rootFile.isFile()) {
			System.out.println("这是个文件");
			fileNames.add(rootFile.getAbsolutePath());
			return fileNames;
		}

		System.out.println(fileName + "是个文件夹");
		String[] subFiles = rootFile.list();
		for (int i = 0; i < subFiles.length; i++) {
			fileNames.add(fileName + "\\" + subFiles[i]);
		}

		return fileNames;
	}

	public static void main(String[] args) {
		//测试效果
		RawsAnalyzer analyzer = new RawsAnalyzer("Raws");
		analyzer.createPageIndex();

	}

}
