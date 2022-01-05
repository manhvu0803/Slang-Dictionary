import java.io.*;
import java.util.*;

public class SlangDictionary implements Closeable {
	public class SlangException extends Exception {
		public SlangException(String argument) {
			super(argument);
		}
	}

	Map<String, List<String>> data;
	
	@SuppressWarnings("unchecked")
	SlangDictionary() throws IOException {
		data = new TreeMap<>();

		var file = new File(dataFile);
		if (!file.exists()) {
			System.err.println(dataFile + " not found\nReading " + defaultFile + " instead");
			loadDefault();
			return;
		}
		
		try (var input = new ObjectInputStream(new FileInputStream(file))) {
			data = (TreeMap<String, List<String>>)input.readObject();
		}
		catch (Exception e) {
			System.err.println("Error while reading " + dataFile + ": " + e.getLocalizedMessage());
			System.err.println("\nReading " + defaultFile + " instead");
			loadDefault();
		}
	}

	void loadDefault() throws IOException {
		try (var reader = new BufferedReader(new FileReader(defaultFile))) {
			reader.readLine();
			String line;
			String last = "";
			while ((line = reader.readLine()) != null) {
				String[] items = line.split("(`|\\| )");
				if (items.length > 1) {
					data.put(items[0], new ArrayList<String>(5));
					for (int i = 1; i < items.length; i++)
						data.get(items[0]).add(items[i]);
					last = items[0];
				}
				else 
					data.get(last).add(items[0]);
			}
		}
	}

	public List<String> getMeanings(String slang) {
		return data.get(slang);
	}

	public Collection<String> getAllSlangs() {
		return data.keySet();
	}

	public Collection<String> getRandomSlangs(int number) {
		var slangs = new HashSet<String>();
		var keys = getAllSlangs().toArray(new String[0]);
		var generator = new Random();
		number = Math.min(number, keys.length);
		while (slangs.size() < number && number <= keys.length) {
			int pos = generator.nextInt(keys.length);
			slangs.add(keys[pos]);
		}
		return slangs;
	}
	
	public Map<String, String> getRandomSlangMeaning(int number) {
		var slangs = getRandomSlangs(number);
		var generator = new Random();
		var slangMap = new HashMap<String, String>();
		for (var slang: slangs) {
			var mns = getMeanings(slang).toArray(new String[0]);
			slangMap.put(slang, mns[generator.nextInt(mns.length)]);
		}
		return slangMap;
	}

	public void addMeaning(String slang, String meaning) throws SlangException {
		var meanings = data.get(slang);
		if (meanings == null)
			throw new SlangException("Slang " + slang + " doesn't exsit");
		meanings.add(meaning);
	}

	public void deleteMeaning(String slang, int pos) throws SlangException {
		var meanings = data.get(slang);
		if (meanings == null)
			throw new SlangException("Slang " + slang + " doesn't exsit");
		meanings.remove(pos);
	}

	public void addSlang(String slang, Collection<String> meanings) throws SlangException {
		if (data.get(slang) != null)
			throw new SlangException("Slang " + slang + " has already existed");
		var newMeanings = new ArrayList<String>(meanings.size());
		for (var meaning: meanings)
			if (meaning.length() > 0)
				newMeanings.add(meaning);
		data.put(slang, newMeanings);
	}

	public void deleteSlang(String slang) throws SlangException {
		if (data.remove(slang) == null)
			throw new SlangException("Slang " + slang + " doesn't exsit");
	}

	public void reset() throws IOException {
		loadDefault();
	}

	@Override
	public void close() throws IOException {
		try (var output = new ObjectOutputStream(new FileOutputStream(dataFile))) {
			output.writeObject(data);
			System.out.println("Saved data to " + dataFile);
		}
	}

	//#region static
	static final String defaultFile = "slang_default.txt";
	static final String dataFile = "slang.dat";

	static SlangDictionary instance = null;

	public static SlangDictionary getInstance() throws IOException {
		if (instance == null)
			instance = new SlangDictionary();
		return instance;
	}
	//#endregion
}