package ua.pb.task.manager.model;

import java.util.Comparator;

/**
 * Created by Mednikov on 17.02.2016.
 */
public enum Order {
    ASC,
    DESC,
    NATURAL;

    public Comparator<TaskData> getTaskDataComparator() {
        Comparator<TaskData> comparator = new Comparator<TaskData>() {
            @Override
            public int compare(TaskData task1, TaskData task2) {
                Integer priority1 = Integer.valueOf(getPriority(task1));
                Integer priority2 = Integer.valueOf(getPriority(task2));
                if (priority1 < priority2)
                    return 1;
                if (priority1 > priority2)
                    return -1;
                return 0;
            }
        };
        return comparator;
    }

    private String getStrFromTask(TaskData taskData, String str) {
        return taskData.getParams().get(str);
    }

    private String getPriority(TaskData taskData) {
        return getStrFromTask(taskData, "priority");
    }


}
