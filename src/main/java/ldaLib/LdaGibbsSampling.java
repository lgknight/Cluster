package ldaLib;

import cluster.ResourceType;
import model.LDAResult;
import util.FileUtil;
import util.PropertyHandler;

import java.io.File;
import java.io.IOException;


/**Liu Yang's implementation of Gibbs Sampling of LDA
 * @author yangliu
 * @blog http://blog.csdn.net/yangliuy
 * @mail yangliuyx@gmail.com
 */

public class LdaGibbsSampling {

	public static class modelparameters {
		float alpha = 0.5f; //usual value is 50 / K
		float beta = 0.1f;//usual value is 0.1
		int topicNum = 100;
		int iteration = 100;
		int saveStep = 10;
		int beginSaveIters = 50;
		int ldaTypicalWordsNum = 10;
	}

	public static void loadParameters(modelparameters ldaparameters){
        ldaparameters.alpha = Float.valueOf(PropertyHandler.getProperty("ldaAlpha"));
        ldaparameters.beta = Float.valueOf(PropertyHandler.getProperty("ldaBeta"));
        ldaparameters.topicNum = Integer.valueOf(PropertyHandler.getProperty("ldaTopicNum"));
        ldaparameters.iteration = Integer.valueOf(PropertyHandler.getProperty("ldaIteration"));
        ldaparameters.saveStep = Integer.valueOf(PropertyHandler.getProperty("ldaSaveStep"));
        ldaparameters.beginSaveIters = Integer.valueOf(PropertyHandler.getProperty("ldaBeginSaveIters"));
		ldaparameters.ldaTypicalWordsNum = Integer.valueOf(PropertyHandler.getProperty("ldaTypicalWordsNum"));
    }




	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String resultPath = PropertyHandler.getProperty("ldaResultsPath");

		modelparameters ldaparameters = new modelparameters();
        loadParameters(ldaparameters);
		Documents docSet = new Documents(ResourceType.News);
		System.out.println("wordMap size " + docSet.termToIndexMap.size());
		FileUtil.mkdir(new File(resultPath));
		LdaModel model = new LdaModel(ldaparameters);
//		System.out.println("1 Initialize the model ...");
		model.initializeModel(docSet);
//		System.out.println("2 Learning and Saving the model ...");
		model.inferenceModel();
//		System.out.println("3 Output the final model ...");

		for(LDAResult r : model.doc2topic()){
			System.out.println(r.toString());
		}

//		model.saveIteratedModel(ldaparameters.iteration, docSet);
		System.out.println("Done!");

	}
}
