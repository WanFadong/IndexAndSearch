package character;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mongoDBUtil.FileHelper;

public class Output {

	public static void main(String[] args) {
		Map<String, Class<?>> columnMap = new HashMap<String, Class<?>>();
		columnMap.put("_id", Integer.class);
		columnMap.put("value", Integer.class);
		String[] fileNames = { "频数.txt", "典面长度.txt", "诗中位置.txt", "句中位置.txt" };
		String[] collectionNames = { "frequency", "length", "poetry_index", "verse_index" };
		try {
			for (int i = 0; i < fileNames.length; i++) {
				FileHelper.toFile(fileNames[i], collectionNames[i], columnMap);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
