package concert_hall;

import concert_hall.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Repository<T extends BaseEntity<ID>, ID> {

    private final static Logger logger = LoggerFactory.getLogger(Repository.class);
    private final String entityName;

    private File fileDatabase;
    private Map<ID, T> localDatabase;

    public Repository(String entityName) {
        this.entityName = entityName;
        logger.info("Initializing data from " + entityName + " database ...");
        fileDatabase = new File("concert-hall-server\\database\\" + entityName + ".dat");

        loadFromFile();
    }

    private void loadFromFile() {
        localDatabase = new HashMap<>();
        try {
            //noinspection ResultOfMethodCallIgnored
            fileDatabase.createNewFile();
            try {

                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(fileDatabase));
                //noinspection InfiniteLoopStatement
                while (true) {
                    @SuppressWarnings("unchecked")
                    T element = (T) stream.readObject();
                    localDatabase.put(element.getId(), element);
                }
            } catch (EOFException ignored) {
                logger.info(localDatabase.size() + " items of type " + entityName + " loaded");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fileDatabase, false));
            localDatabase.forEach((id, element) -> {
                try {
                    stream.writeObject(element);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            logger.info(entityName + " database updated");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void save(T t) {
        if (!localDatabase.containsKey(t.getId())) {
            localDatabase.put(t.getId(), t);
            logger.info(t + " saved");
            saveToFile();
        }
        logger.info(t + " not saved, already existing entity with id " + t.getId());
    }

    public T findById(ID id) {
        return localDatabase.get(id);
    }

//    public boolean existsById(ID id) {
//        return localDatabase.get(id) != null;
//    }

    public Collection<T> findAll() {
        return localDatabase.values();
    }

    public Integer count() {
        return localDatabase.size();
    }

    public void deleteById(ID id) {
        localDatabase.remove(id);
        saveToFile();
    }

//    public void update(T t) {
//        if (existsById(t.getId())) {
//            deleteById(t.getId());
//            save(t);
//            logger.info(entityName + " with id " + t.getId() + " updated to " + t);
//        }
//        logger.info(entityName + " with id " + t.getId() + " doesn't exist. Cannot update to " + t);
//
//    }

}
