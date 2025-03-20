
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Класс, управляющий логикой работы со справочником телефонных номеров.
 */
public class PhoneDirectoryManager {
    private static final int MAX_RECORDS = 100000;
    static final Map<Integer, PhoneRecord> currentRecords = new ConcurrentHashMap<>();
    static final NavigableMap<Long, Map<Integer, PhoneRecord>> history = new ConcurrentSkipListMap<>();
    private static final Random random = new Random();

    /**
     * Заполняет справочник начальными записями.
     */
    public void initializeRecords() {
        for (int i = 0; i < MAX_RECORDS; i++) {
            currentRecords.put(i, new PhoneRecord("Name" + i, generateRandomPhoneNumber()));
        }
        storeHistory();
    }

    /**
     * Обновляет номера телефонов в справочнике.
     *
     * @param count количество записей для обновления.
     */
    public void updatePhoneNumbers(int count) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            int id = random.nextInt(MAX_RECORDS);
            currentRecords.get(id).setPhoneNumber(generateRandomPhoneNumber());
            storeHistory();
        }
    }

    /**
     * Сохраняет текущее состояние справочника в историю.
     */
    public void storeHistory() {
        long currentTime = System.currentTimeMillis();
        Map<Integer, PhoneRecord> historyRecords = new HashMap<>();

        for (Map.Entry<Integer, PhoneRecord> entry : currentRecords.entrySet()) {
            historyRecords.put(entry.getKey(), new PhoneRecord(entry.getValue().getName(), entry.getValue().getPhoneNumber()));
        }
        history.put(currentTime, historyRecords);
        cleanupOldHistory(currentTime);

    }

    /**
     * Очищает старые записи из истории.
     *
     * @param currentTime текущее время в миллисекундах.
     */
    void cleanupOldHistory(long currentTime) {
        history.headMap(currentTime - 30000).clear();
    }

    /**
     * Выводит записи, которые были актуальны на момент времени, запрашиваемый пользователем.
     *
     * @param seconds количество минут назад, для которых нужно получить записи.
     */
    public void printRecordsMSecondsAgo(int seconds) {
        long targetTime = System.currentTimeMillis() - (seconds * 1000L);
        Map<Integer, PhoneRecord> recordsAtTime = history.floorEntry(targetTime).getValue();

        recordsAtTime.forEach((id, record) -> {
            System.out.println(id + " " + record);
        });


    }

    /**
     * Генерирует случайный номер телефона в формате 555-XXXX.
     *
     * @return случайный номер телефона.
     */
    private String generateRandomPhoneNumber() {
        return "555-" + (1000 + random.nextInt(9000));
    }

    /**
     * Генерирует случайное значение в диапазоне от 500 до 5000 миллисекунд.
     *
     * @return случайное значение времени в миллисекундах.
     */
    public int generateRandomN() {
        return 500 + random.nextInt(4500);
    }

    /**
     * Генерирует случайное значение в диапазоне от 0 до 30 секунд.
     *
     * @return случайное значение времени в секундах.
     */
    public int generateRandomM() {
        return random.nextInt(30);
    }

    /**
     * Генерирует случайное значение в диапазоне от 60 до 300 секунд.
     *
     * @return случайное значение времени в секундах.
     */
    public int generateRandomT() {
        return 60 + random.nextInt(240);
    }

    /**
     * Генерирует случайное количество записей для обновления.
     * Значение будет в диапазоне от 1 до MAX_RECORDS.
     *
     * @return случайное количество записей для обновления.
     */
    public int generateRandomCountOfRecordsToUpdate() {
        return random.nextInt(MAX_RECORDS) + 1;
    }
}
