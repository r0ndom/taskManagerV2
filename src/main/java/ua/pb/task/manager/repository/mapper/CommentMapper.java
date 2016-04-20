package ua.pb.task.manager.repository.mapper;


import ua.pb.task.manager.model.Comment;

import java.util.List;

/**
 * Created by Mednikov on 10.02.2016.
 */
public interface CommentMapper {
    void create(Comment comment);
    void update(Comment comment);
    void delete(Long id);
    List<Comment> findByTaskId(String id);
}
