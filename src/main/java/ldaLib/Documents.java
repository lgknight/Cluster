package ldaLib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cluster.ResourceType;
import model.Resource;
import model.dao.ResourceDAO;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import util.Stopwords;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * @Author : Lknight
 * @Date : 2017/5/22
 * @Description : 文档实体类
 * @Version : v1.0
 */

public class Documents {

	ArrayList<Document> docs = new ArrayList<Document>();
	Map<String,Integer> termToIndexMap = new HashMap<String, Integer>();
	ArrayList<String> indexToTermMap = new ArrayList<String>();
	Map<String,Integer> termCountMap = new HashMap<String, Integer>();

	public Map<String, Integer> getTermToIndexMap() {
		return termToIndexMap;
	}

	public ArrayList<String> getIndexToTermMap() {
		return indexToTermMap;
	}

	public Map<String, Integer> getTermCountMap() {
		return termCountMap;
	}

	public Documents(ResourceType type){
		readDocs(type);
	}
	public Documents(ResourceType type, int day){
		readDocs(type, day);
	}

	/**
	 * @Description： 读取数据库中文本，构造Document实体
	 * @Parameters: type-[新闻/博客/论坛]
	 * */
	public void readDocs(ResourceType type){
		for(Resource resource : ResourceDAO.getResource(type)){
			Document doc = new Document(resource, termToIndexMap, indexToTermMap, termCountMap);
			docs.add(doc);
		}
	}

	/**
	 * @Description： 读取数据库中day内的文本，构造Document实体
	 * @Parameters: type-[新闻/博客/论坛]
	 * */
	public void readDocs(ResourceType type, int day){
		for(Resource resource : ResourceDAO.getResource(type, day)){
			Document doc = new Document(resource, termToIndexMap, indexToTermMap, termCountMap);
			docs.add(doc);
		}
	}

	public static class Document {
		private String docId;
		int[] docWords;

		public String getDocId() {
			return docId;
		}

		public int[] getDocWords() {
			return docWords;
		}

		public Document(Resource resource, Map<String, Integer> termToIndexMap, ArrayList<String> indexToTermMap, Map<String, Integer> termCountMap){
			this.docId = resource.getId();
			//Read file and initialize word index array
			Result r = ToAnalysis.parse(resource.getContent());
			ArrayList<String> words = new ArrayList<String>();
			for(Term t : r){
				if(!Stopwords.isStopword(t.getName())) {
					words.add(t.getName());
				}
			}

			//Transfer word to index
			this.docWords = new int[words.size()];
			for(int i = 0; i < words.size(); i++){
				String word = words.get(i);
				if(!termToIndexMap.containsKey(word)){
					int newIndex = termToIndexMap.size();
					termToIndexMap.put(word, newIndex);
					indexToTermMap.add(word);
					termCountMap.put(word, new Integer(1));
					docWords[i] = newIndex;
				} else {
					docWords[i] = termToIndexMap.get(word);
					termCountMap.put(word, termCountMap.get(word) + 1);
				}
			}
			words.clear();
		}


	}
}
