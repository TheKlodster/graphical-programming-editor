import java.util.HashMap;

public class Structure {

    private HashMap<String, String> assignment;

    public Structure() {
        this.assignment = new HashMap<>();
    }

    /**
     * The getter method to obtain the value assigned to the key.
     *
     * @param key - the key in the hashmap.
     * @return the key that is assigned to the value.
     */
    public String get(String key) {
        return this.assignment.get(key);
    }

    /**
     * The setter to set the key and value inside the hashmap.
     *
     * @param key - the key to be stored as the key in the hashmap.
     * @param value - as for the key, the value to be stored against that key.
     */
    public void set(String key, String value) {
        this.assignment.put(key, value);
    }

    /**
     * Retrieves the hashmap to be used in different classes, normally for printing out
     * the contents.
     *
     * @return the hashmap.
     */
    public HashMap getMap() {
        return assignment;
    }
}