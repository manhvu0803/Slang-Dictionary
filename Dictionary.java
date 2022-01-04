import java.io.*;
import java.util.*;

public class Dictionary {
	public class SlangNotFoundException extends Exception {}
	public class SlangExistedException extends Exception {}

	Map<String, List<String>> data;
	
	@SuppressWarnings("unchecked")
	Dictionary() throws FileNotFoundException {
		data = new HashMap<>();

		var file = new File("slang.dat");
		if (!file.exists())
			loadDefault();
		
		try (var input = new ObjectInputStream(new FileInputStream(file))) {
			data = (HashMap<String, List<String>>)input.readObject();
		}
		catch (Exception e) {
			System.err.println("Error while reading slang.dat: " + e.getLocalizedMessage() + "\nReading slang.txt instead");
			loadDefault();
		}
	}

	void loadDefault() throws FileNotFoundException {
		try (var reader = new BufferedReader(new FileReader("Slang.txt"))) {
			reader.readLine();
			String line;
			String last = "";
			while ((line = reader.readLine()) != null) {
				String[] items = line.split("(`|\\| )");
				if (items.length > 1) {
					data.put(items[0], new ArrayList<String>());
					for (int i = 1; i < items.length; i++)
						data.get(items[0]).add(items[i]);
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

	public void save() throws IOException {
		try (var output = new ObjectOutputStream(new FileOutputStream("slang.dat"))) {
			output.writeObject(data);
		}
	}
	
	//#region static
	static Dictionary instance = null;

	public static Dictionary getInstance() throws FileNotFoundException {
		if (instance == null)
			instance = new Dictionary();
		return instance;
	}
	//#endregion
}