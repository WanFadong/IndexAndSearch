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
		try {
			FileHelper.toFile("频数.txt", "frequency", columnMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
