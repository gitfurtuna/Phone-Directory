import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PhoneDirectoryManagerTest {
    private PhoneDirectoryManager manager;

    @BeforeEach
    void setUp() {
        manager = new PhoneDirectoryManager();
        manager.initializeRecords();
        PhoneDirectoryManager.history.clear();
    }

    /**
     * Проверяет, что метод initializeRecords() правильно инициализирует записи.
     * Ожидается, что размер текущих записей будет равен 100000,
     * и что первая и последняя записи не равны null.
     */
    @Test
    void testInitializeRecords() {
        assertEquals(100000, PhoneDirectoryManager.currentRecords.size());
        assertNotNull(PhoneDirectoryManager.currentRecords.get(0));
        assertNotNull(PhoneDirectoryManager.currentRecords.get(99999));
    }

    /**
     * Проверяет, что метод updatePhoneNumbers() обновляет номер телефона.
     * Ожидается, что номер телефона первой записи изменится после вызова метода.
     */
    @Test
    void testUpdatePhoneNumbers() throws InterruptedException {
        String initialPhoneNumber = PhoneDirectoryManager.currentRecords.get(0).getPhoneNumber();
        manager.updatePhoneNumbers(1);
        assertNotEquals(initialPhoneNumber, PhoneDirectoryManager.currentRecords.get(0).getPhoneNumber());
    }

    /**
     * Проверяет, что метод storeHistory() сохраняет записи в историю.
     * Ожидается, что история не будет пустой после вызова метода.
     */
    @Test
    void testStoreHistory() {
        manager.storeHistory();
        assertFalse(PhoneDirectoryManager.history.isEmpty());
    }

    /**
     * Проверяет, что метод cleanupOldHistory() корректно очищает устаревшие записи из истории.
     * Ожидается, что после очистки останется одна запись, и старая запись будет удалена.
     */
    @Test
    void testCleanupOldHistory() {
        long currentTime = System.currentTimeMillis();
        PhoneDirectoryManager.history.put(currentTime - 31000, Map.of(1, new PhoneRecord("OldName", "555-1000")));
        PhoneDirectoryManager.history.put(currentTime - 10000, Map.of(2, new PhoneRecord("NewName", "555-2000")));

        assertFalse(PhoneDirectoryManager.history.isEmpty(), "History should not be empty before cleanup.");
        assertEquals(2, PhoneDirectoryManager.history.size(), "History should contain two records before cleanup.");

        manager.cleanupOldHistory(currentTime);

        assertEquals(1, PhoneDirectoryManager.history.size(), "History should contain one record after cleanup.");
        assertTrue(PhoneDirectoryManager.history.containsKey(currentTime - 10000), "History should contain the new record.");
        assertFalse(PhoneDirectoryManager.history.containsKey(currentTime - 31000), "History should not contain the old record.");
    }

    /**
     * Проверяет, что метод printRecordsMSecondsAgo(int seconds)
     * Корректно выводит записи для контакта на момент времени, соответствующий указанным секундам.
     *
     * В этом тесте создаются две записи для одного и того же контакта "Name888" с разными номерами телефонов:
     * - Первая запись с номером "555-1000" зафиксирована 10 секунд назад.
     * - Вторая запись с номером "555-2000" зафиксирована 5 секунд назад.
     *
     * Ожидается, что при запросе записей на момент 10 секунд назад будет выведен только номер "555-1000",
     * так как он был актуален на момент запроса.
     */
    @Test
    void testPrintRecordsMSecondsAgo() {
        long currentTime = System.currentTimeMillis();

        PhoneDirectoryManager.history.put(currentTime - 10000, Map.of(1, new PhoneRecord("Name888", "555-1000")));
        PhoneDirectoryManager.history.put(currentTime - 5000, Map.of(1, new PhoneRecord("Name888", "555-2000")));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        manager.printRecordsMSecondsAgo(10);

        String output = outContent.toString();

        assertTrue(output.contains("1 ContactA 555-1000"), "Output should contain record for ContactA with old number.");
        assertFalse(output.contains("1 ContactA 555-2000"), "Output should not contain record for ContactA with new number.");
    }

}
