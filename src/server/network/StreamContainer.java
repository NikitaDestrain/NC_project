package server.network;

import java.io.DataOutputStream;
import java.util.*;

/**
 * Container for client's streams
 */

public class StreamContainer {

    private static StreamContainer instance;
    private Map<Integer, DataOutputStream> clientNotificationOutputStreams;
    private Map<Integer, DataOutputStream> clientDataOutputStreams;

    private StreamContainer() {
        clientNotificationOutputStreams = new HashMap<>();
        clientDataOutputStreams = new HashMap<>();
    }

    public static StreamContainer getInstance() {
        if (instance == null) instance = new StreamContainer();
        return instance;
    }

    /**
     * Returns list of actual channels for notification to clients
     * @return UnmodifiableList
     */

    public List<DataOutputStream> getClientNotificationOutputStreams() {
        LinkedList<DataOutputStream> list = new LinkedList<>(clientNotificationOutputStreams.values());
        return Collections.unmodifiableList(list);
        //todo vlla чем вам коллекция то не угодила, зачем обязательно ее в List оборачивать? К тому же помним правило: отдаем коллекцию наружу - делаем ее unmodifiable
        //!!!Не делается unmodified, преобразуется в лист для более быстрой итерации, так как ключи уже не будут играть роли
        //todo vlla 2 unmodified сделать все таки нужно. Выигрыш, получаемый от более быстрой итерации нивелируется накладными расходами по клонированию коллекции. DONE
        // Ключи и не нужны - clientNotificationOutputStreams.values() уже возвращает только коллекцию значений
    }

    /**
     * Returns list of actual channels for communicate to clients
     * @return UnmodifiableList
     */

    public List<DataOutputStream> getClientDataOutputStreams() {
        LinkedList <DataOutputStream> list = new LinkedList<>(clientDataOutputStreams.values());
        return Collections.unmodifiableList(list);
    }

    protected void removeNotificationOutputStream(Integer key) {
        clientNotificationOutputStreams.remove(key);
        //todo vlla просто удалить стрим из мапы - не достаточно. Стримы всегда надо закрывать. DONE
        // закрыты в фасаде
    }

    protected void addNotificationOutputStream(Integer key, DataOutputStream dataOutputStream) {
        clientNotificationOutputStreams.put(key, dataOutputStream);
    }

    protected void addClientDataOutputStreams(Integer key, DataOutputStream dataOutputStream) {
        clientDataOutputStreams.put(key, dataOutputStream);
    }

    protected void removeClientDataOutputStreams(Integer key) {
        clientDataOutputStreams.remove(key);
    }

    public DataOutputStream getDataOutputStream(int key) {
        return clientDataOutputStreams.get(key);
    }
}
