package core.preprocess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

//�ִ��ļ���ȡ��
public class DictionaryReader {

	// �����зִʷ���һ��Hashset�ļ��϶�����

	public DictionaryReader() {
	}

	public HashSet<String> scanDictionary(String dictFile) {
		HashSet<String> dictionary = new HashSet<String>();
		try {
			FileReader fileReader = new FileReader(dictFile);
			BufferedReader bfReader = new BufferedReader(fileReader);

			String word;
			while ((word = bfReader.readLine()) != null) {
				dictionary.add(word);
			}
			bfReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("�ֵ䳤��Ϊ�� " + dictionary.size());

		return dictionary;
	}
}
