
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Главный класс, запускающий приложение телефонного справочника.
 */
public class PhoneDirectory {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки (не используются).
     */
    public static void main(String[] args) {

        Logger logger = Logger.getLogger(PhoneDirectoryManager.class.getName());
        PhoneDirectoryManager manager = new PhoneDirectoryManager();
        manager.initializeRecords();

        int M = manager.generateRandomM();
        int T = manager.generateRandomT();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable updateTask = new Runnable() {
            @Override
            public void run() {
                int N = manager.generateRandomN();
                int recordsToUpdate = manager.generateRandomCountOfRecordsToUpdate();
                try {
                    manager.updatePhoneNumbers(recordsToUpdate);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                executor.schedule(this, N, TimeUnit.MILLISECONDS);

                System.out.println("Обновление номеров с интервалом: " + N + " миллисекунд");

                logger.info("Updated " + recordsToUpdate + " phone numbers.");



            }
        };


        executor.schedule(updateTask, 0, TimeUnit.MILLISECONDS);


        executor.schedule(() -> {
            manager.printRecordsMSecondsAgo(M);
            executor.shutdown();
        }, T, TimeUnit.SECONDS);
    }
}