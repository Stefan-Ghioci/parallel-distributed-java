package concert_hall;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Repository<T extends BaseEntity<ID>, ID> {

    File fileStorage;
    Map<ID, T> localStorage;

    public Repository(String pathname) {
        System.out.println("Initializing repository from file " + pathname + " ...");
        fileStorage = new File(pathname);

        loadFromFile();
    }

    private void loadFromFile() {
        localStorage = new HashMap<>();
        try {
            fileStorage.createNewFile();
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(fileStorage));
            try {
                while (true) {
                    T element = (T) stream.readObject();
                    localStorage.put(element.getId(), element);
                }
            } catch (EOFException ignored) {
                localStorage.forEach((id, t) -> System.out.println("{Key=" + t.getId() + ", Value=" + t + "}"));
                System.out.println(localStorage.size() + " items loaded");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fileStorage, false));
            localStorage.forEach((id, element) -> {
                try {
                    stream.writeObject(element);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("Storage updated");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void save(T t) {
        if (!localStorage.containsKey(t.getId())) {
            localStorage.put(t.getId(), t);
            saveToFile();
        }
    }

    public T findById(ID id) {
        return localStorage.get(id);
    }

    public boolean existsById(ID id) {
        return localStorage.get(id) != null;
    }

    public Iterable<T> findAll() {
        return localStorage.values();
    }

    public long count() {
        return localStorage.size();
    }

    public void deleteById(ID id) {
        localStorage.remove(id);
        saveToFile();
    }

    public void update(T t) {
        deleteById(t.getId());
        save(t);
    }
}
