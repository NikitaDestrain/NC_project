package server.exportdata;

public class ExportConstants {
    public static final String ONLY_OBJECT = "only object";
    public static final String OBJECT_WITH_PARENT = "object with parent";
    public static final String OBJECT_WITH_CHILDREN = "object with children";

    //todo vlla у вас получается что стратегия жестко завязана на тип данных. DONE
    // На самом деле, стратегии экспорта у нас в архитектуре всего три: экспортировать только объект, экспортировать объкт с родителем, экспортировать объект с потомком.
    // Итого всего три константы, с помощью которых можно указать на нужную стратегию.
    // А вот реализация будет однозначно отпределяться по совокупности двух параметров: типа данных и стратегии.
    //
    // У вас же на каждый тип данных - несколько стратегий и несколько констант. Предположим, типов данных 100 (в неткрекере их тысячи и десятки тысяч).
    // В этом случае, у вас будет 200-300 стратегий? Предположим, пользователь добавил новый тип данных. Ему нужно будет не только написать реализации для существующих стратегий,
    // Ему будет нужно еще создать для каждой новую константу.
    //
    // Еще минус вашего подхода - это ужасно сложночитаемый файл конфига. Конфиги служат для того, чтобы администратор системы мог легко поменять ее поведения, изменив хначения в конфиге.
    // Какой конфиг легче читается и модифицируется - тот, в котором используется три константы, или тот, в котором констант сотни?

    public static final String PATH_TO_PROPERTIES = "properties/exportconfig.properties";

    public static final String TASK_PROPERTY = "TASK";
    public static final String JOURNAL_PROPERTY = "JOURNAL";

    // EXCEPTION MESSAGES

    public static final String INCORRECT_PARAMETER = "Unable to perform the export due to the incorrect received parameter: ";
}
