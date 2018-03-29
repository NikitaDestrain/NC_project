package server.importdata;

import server.importdata.journalstrategy.ExceptionThrowJournalStrategy;
import server.importdata.journalstrategy.IgnoreJournalStrategy;
import server.importdata.journalstrategy.ReplaceJournalStrategy;
import server.importdata.taskstrategy.ExceptionThrowTaskStrategy;
import server.importdata.taskstrategy.IgnoreTaskStrategy;
import server.importdata.taskstrategy.ReplaceTaskStrategy;
import server.model.Journal;
import server.model.Task;

import java.util.HashMap;
import java.util.Map;

public class StoreStrategyHelper {
    private static StoreStrategyHelper ourInstance = new StoreStrategyHelper();
    private Map<Integer, StoreStrategy> strategies;

    public static StoreStrategyHelper getInstance() {
        return ourInstance;
    }

    private StoreStrategyHelper() {
        strategies = new HashMap<>();
        strategies.put(StoreConstants.REPLACE_JOURNAL_STRATEGY, new ReplaceJournalStrategy<Journal>());
        strategies.put(StoreConstants.IGNORE_JOURNAL_STRATEGY, new IgnoreJournalStrategy<Journal>());
        strategies.put(StoreConstants.EXCEPTION_THROW_JOURNAL_STRATEGY, new ExceptionThrowJournalStrategy<Journal>());
        strategies.put(StoreConstants.REPLACE_TASK_STRATEGY, new ReplaceTaskStrategy<Task>());
        strategies.put(StoreConstants.IGNORE_TASK_STRATEGY, new IgnoreTaskStrategy<Task>());
        strategies.put(StoreConstants.EXCEPTION_THROW_TASK_STRATEGY, new ExceptionThrowTaskStrategy<Task>());
    }

    public StoreStrategy resolveStrategy(int strategy) throws StoreException {
        switch (strategy) {
            case StoreConstants.REPLACE_JOURNAL_STRATEGY:
                return strategies.get(StoreConstants.REPLACE_JOURNAL_STRATEGY);
            case StoreConstants.IGNORE_JOURNAL_STRATEGY:
                return strategies.get(StoreConstants.IGNORE_JOURNAL_STRATEGY);
            case StoreConstants.EXCEPTION_THROW_JOURNAL_STRATEGY:
                return strategies.get(StoreConstants.EXCEPTION_THROW_JOURNAL_STRATEGY);
            case StoreConstants.REPLACE_TASK_STRATEGY:
                return strategies.get(StoreConstants.REPLACE_TASK_STRATEGY);
            case StoreConstants.IGNORE_TASK_STRATEGY:
                return strategies.get(StoreConstants.IGNORE_TASK_STRATEGY);
            case StoreConstants.EXCEPTION_THROW_TASK_STRATEGY:
                return strategies.get(StoreConstants.EXCEPTION_THROW_TASK_STRATEGY);
            default:
                return null;
        }
    }
}
