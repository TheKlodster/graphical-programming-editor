import java.util.HashMap;

public class Structure {

    private HashMap<String, String> assignment;

    public Structure() {
        this.assignment = new HashMap<>();
    }

    /**
     *
     * @param key - the key in the hashmap.
     * @return the key that is assigned to the value.
     */
    public String get(String key) {
        return this.assignment.get(key);
    }

    public void set(String key, String value) {
        this.assignment.put(key, value);
    }

    public HashMap getMap() {
        return assignment;
    }
}