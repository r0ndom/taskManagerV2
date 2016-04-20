package ua.pb.task.manager.repository.mapper;


import ua.pb.task.manager.model.TaskDto;
import ua.pb.task.manager.model.filter.TaskSearchFilter;

import java.util.List;

/**
 * Created by Mike on 3/1/2016.
 */
public interface FilterMapper {
    List<TaskDto> search(TaskSearchFilter filter);
}
