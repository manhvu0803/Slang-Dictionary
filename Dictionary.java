import java.io.*;
import java.util.*;

public class Dictionary {
	Map<String, ArrayList<String>> data;

	public Dictionary() throws FileNotFoundException, IOException {
		data = new HashMap<>();

		try (var reader = new BufferedReader(new FileReader("Slang.txt"))) {
			reader.readLine();
			String line;
			String last = "";
			while ((line = reader.readLine()) != null) {
				String[] items = line.split("`");
				if (items.length > 1) {
					data.put(items[0], new ArrayList<String>());
					data.get(items[0]).add(items[1]);
					last = items[0];
				}
				else 
					data.get(last).add(items[0]);
			}
		}
	}

	public static void main(String args[]) throws Exception {
		var dict = new Dictionary();
		var keys = dict.data.keySet().toArray();
		
		//int start = 0, end = Math.min(10, keys.length);
		for (int i = 0; i < keys.length; i++) {
			var data = dict.data.get(keys[i]);
			if (data.size() > 1) {
				System.out.print(keys[i] + ": ");
				for (String str: data)
					System.out.print(str + ", ");
				System.out.println();
			}
		}
	}
}