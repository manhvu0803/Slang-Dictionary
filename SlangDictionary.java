import java.io.*;
import java.util.*;

public class SlangDictionary implements Closeable {
	public class SlangException extends Exception {
		public SlangException(String argument) {
			super(argument);
		}
	}

	Map<String, List<String>> data;
	Map<String, List<String>> fullText;
	
	@SuppressWarnings("unchecked")
	SlangDictionary() throws IOException {
		data = new TreeMap<String, List<String>>();
		fullText = new HashMap<String, List<String>>();

		var file = new File(dataFile);
		if (!file.exists()) {
			System.err.println(dataFile + " not found\nReading " + defaultFile + " instead");
			loadDefault();
			return;
		}
		
		try (var input = new ObjectInputStream(new FileInputStream(file))) {
			data = (TreeMap<String, List<String>>)input.readObject();
			fullText = (HashMap<String, List<String>>)input.readObject();
		}
		catch (Exception e) {
			System.err.println("Error while reading " + dataFile + ": " + e.getLocalizedMessage());
			e.printStackTrace();
			System.err.println("\nReading " + defaultFile + " instead");
			loadDefault();
		}
	}

	public void loadDefault() throws IOException {
		data = new TreeMap<String, List<String>>();
		try (var reader = new BufferedReader(new FileReader(defaultFile))) {
			reader.readLine();
			String line;
			String last = "";
			while ((line = reader.readLine()) != null) {
				String[] items = line.split("(`|\\| )");
				if (items.length > 1) {
					var meanings = new ArrayList<String>(3);
					for (int i = 1; i < items.length; i++) {
						meanings.add(items[i]);
						addKeywords(items[0], items[i]);
					}
					data.put(items[0], meanings);

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

	public List<String> getSlangs(String keyword) {
		return fullText.get(keyword);
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
	
	public List<String[]> getRandomSlangMeaning(int number) {
		var slangs = getRandomSlangs(number);
		var generator = new Random();
		var slangMap = new ArrayList<String[]>();
		for (var slang: slangs) {
			var mns = getMeanings(slang).toArray(new String[0]);
			slangMap.add(new String[]{slang, mns[generator.nextInt(mns.length)]});
		}
		return slangMap;
	}

	public void addKeywords(String slang, String meaning) {
		var c = meaning.split("[^A-z0-9]");
		for (var keyword: c) {
			keyword = keyword.toUpperCase();
			if (fullText.get(keyword) == null)
				fullText.put(keyword, new ArrayList<String>());
			fullText.get(keyword).add(slang);
		}
	}

	public void addKeywords(String slang, List<String> meanings) {
		for (var meaning: meanings)
			addKeywords(slang, meaning);
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
		addKeywords(slang, newMeanings);
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
			output.writeObject(fullText);
		 	output.flush();
		 	System.out.println("Saved data to " + dataFile);
		}
	}

	void writeText() throws IOException {
		try (var output = new BufferedWriter(new FileWriter("slang.txt"))) {
			output.write("Slang`Meaning");
			for (var key: data.keySet()) {
				output.write("\n" + key + "`");
				var meanings = data.get(key);
				for (int i = 0; i < meanings.size(); i++) {
					if (i > 0)
						output.write("| ");
					output.write(meanings.get(i));
				}
			}
			output.flush();
			System.out.println("Saved data to " + "slang.txt");
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