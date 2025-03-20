/**
 * Класс, представляющий запись с именем и номером телефона.
 */
public class PhoneRecord {
    private final String name;
    private String phoneNumber;

    /**
     * Конструктор для создания новой записи.
     *
     * @param name        имя владельца номера телефона.
     * @param phoneNumber номер телефона.
     */
    public PhoneRecord(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Устанавливает новый номер телефона.
     *
     * @param phoneNumber новый номер телефона.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "PhoneRecord{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    /**
     *  Возвращает имя контакта.
     *
     * @return имя контакта.
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает номер телефона контакта.
     *
     * @return номер телефона контакта.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }


}

