package azerty.tguichaoua.mpb.model;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TableTypeAdapter extends TypeAdapter<Table> {

	final Gson gson;

	public TableTypeAdapter() {
		gson = new Gson();
	}

	@Override
	public void write(final JsonWriter jsonWriter, final Table table) throws IOException {
		jsonWriter.jsonValue(gson.toJson(table.getValues()));
	}

	@Override
	public Table read(final JsonReader jsonReader) throws IOException {
		final Map<String, Object> values = new HashMap<>();
		jsonReader.beginObject();
		String nextName = null;
		while (jsonReader.hasNext()) {
			final JsonToken token = jsonReader.peek();
			if (token == JsonToken.END_OBJECT) break;
			switch (token) {
				case NAME:
					nextName = jsonReader.nextName();
					break;
				case STRING:
					values.put(nextName, jsonReader.nextString());
					break;
				case NUMBER:
					values.put(nextName, jsonReader.nextDouble());
					break;
				case BOOLEAN:
					values.put(nextName, jsonReader.nextBoolean());
					break;
				case BEGIN_ARRAY:
				case BEGIN_OBJECT:
				case NULL:
					throw new IOException("Invalid value in Table");
			}
		}
		jsonReader.endObject();
		return new Table(values);
	}
}
