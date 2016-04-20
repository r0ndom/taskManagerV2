package ua.pb.task.manager.repository.mapper;


import ua.pb.task.manager.model.Archive;
import ua.pb.task.manager.model.TaskData;

import java.util.List;

/**
 * Created by Mednikov on 25.01.2016.
 */
public interface ArchiveMapper {
    void put(TaskData taskData);
    List<Archive> findAll();
}
