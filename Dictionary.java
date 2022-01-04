import java.io.*;
import java.util.*;

public class Dictionary {
	public class SlangNotFoundException extends Exception {}
	public class SlangExistedException extends Exception {}

	Map<String, ArrayList<String>> data;

	Dictionary() throws FileNotFoundException {
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
		catch (IOException e) {
			System.err.println("Error while parsing dictionary: " + e.getLocalizedMessage());
		}
	}

	public List<String> getMeaning(String slang) {
		return data.get(slang);
	}

	public void addMeaning(String slang, String meaning) throws SlangNotFoundException {
		var meanings = data.get(slang);
		if (meanings == null) 
			throw new SlangNotFoundException();
		meanings.add(meaning);
	}

	public void deleteMeaning(String slang, int pos) throws SlangNotFoundException {
		var meanings = data.get(slang);
		if (meanings == null)
			throw new SlangNotFoundException();
		meanings.remove(pos);
	}

	public void addSlang(String slang, String[] meanings) throws SlangExistedException {
		if (data.get(slang) != null)
			throw new SlangExistedException();
		data.put(slang, new ArrayList<>(Arrays.asList(meanings)));
	}

	static Dictionary instance = null;

	public static Dictionary getInstance() throws FileNotFoundException {
		if (instance != null)
			instance = new Dictionary();
		return instance;
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