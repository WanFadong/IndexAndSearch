package character.init;

import java.util.Iterator;
import java.util.List;

import dao.DianGuDao;
import dao.DianGuInIndexDao;
import model.DianGuPO;
import search.SearchEngine;
import search.WordInfo;

/**
 * 搜索那些在倒排中出现的词语；
 * 
 * @author fadongwan
 *
 */
public class DianGuInIndex {
	private final int minLength = 2;
	private final int maxLength = 3;

	private void createDianGuInIndex() {
		int count = 0;
		DianGuDao dianGuDao = new DianGuDao();
		List<DianGuPO> dianGuList = dianGuDao.findAll();
		// 判断典故是否在倒排索引中
		Iterator<DianGuPO> dianGuItr = dianGuList.iterator();
		while (dianGuItr.hasNext()) {
			count++;
			if (count % 100 == 0) {
				System.out.println(count);
			}
			DianGuPO dianGu = dianGuItr.next();
			List<String> patternList = dianGu.getPatternList();
			Iterator<String> it = patternList.iterator();
			while (it.hasNext()) {
				String pattern = it.next();
				if (pattern.length() > maxLength || pattern.length() < minLength) {
					it.remove();
					continue;
				}
				List<WordInfo> wordInfoList = SearchEngine.searchFromMongo(pattern);
				if (wordInfoList.size() == 0) {
					it.remove();
				}
			}
			// 如果典故中所有典面都被删除，那么删除该典故
			if (patternList.size() == 0) {
				dianGuItr.remove();
			}
		}
		// 存储；
		DianGuInIndexDao dianGuInIndexDao = new DianGuInIndexDao();
		dianGuInIndexDao.insert(dianGuList);
	}

	public static void main(String[] args) {
		DianGuInIndex instance = new DianGuInIndex();
		instance.createDianGuInIndex();
	}

}
