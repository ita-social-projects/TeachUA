package com.softserve.teachua.service;

import com.softserve.commons.util.marker.Archiver;
import com.softserve.commons.exception.NotExistException;
import com.softserve.teachua.dto.task.CreateTask;
import com.softserve.teachua.dto.task.SuccessCreatedTask;
import com.softserve.teachua.dto.task.SuccessUpdatedTask;
import com.softserve.teachua.dto.task.TaskPreview;
import com.softserve.teachua.dto.task.TaskProfile;
import com.softserve.teachua.dto.task.UpdateTask;
import com.softserve.teachua.model.Task;
import java.util.List;

/**
 * This interface contains all needed methods to manage tasks.
 */

public interface TaskService extends Archiver<Long> {
    /**
     * The method deletes entity {@code Task} returns dto {@code TaskProfile} of deleted task by id.
     *
     * @param id
     *            - id of Task to delete.
     *
     * @return new {@code TaskProfile}.
     *
     * @throws NotExistException
     *             if task not exists.
     */
    TaskProfile deleteTask(Long id);

    /**
     * The method returns entity {@code Task} of task by id.
     *
     * @param id
     *            - put Task id.
     *
     * @return new {@code Task}.
     *
     * @throws NotExistException
     *             if task not exists.
     */
    Task getTaskById(Long id);

    /**
     * The method returns list {@code List<TaskPreview>} of task dto by challenge id.
     *
     * @param id
     *            - put Challenge id.
     *
     * @return new {@code List<TaskPreview>}.
     *
     * @throws NotExistException
     *             if challenge not exists.
     */
    List<TaskPreview> getTasksByChallengeId(Long id);

    /**
     * The method returns list {@code List<TaskPreview>} of task with startDate earlier from now, by challenge id.
     *
     * @param id
     *            - put Challenge id.
     *
     * @return new {@code List<TaskPreview>}.
     *
     * @throws NotExistException
     *             if challenge not exists.
     */
    List<TaskPreview> getCurrentTasksByChallengeId(Long id);

    /**
     * The method returns list of dto {@code List<TaskPreview>} of all tasks.
     *
     * @return new {@code List<TaskPreview>}.
     */
    List<TaskPreview> getListOfTasks();

    /**
     * The method returns dto {@code TaskProfile} of task by id.
     *
     * @param id
     *            - put Task id.
     *
     * @return new {@code TaskProfile}.
     *
     * @throws NotExistException
     *             if task not exists.
     */
    TaskProfile getTask(Long id);

    /**
     * The method returns dto {@code SuccessCreatedTask} if task successfully added.
     *
     * @param createTask
     *            - place body of dto {@code CreateTask}.
     *
     * @return new {@code SuccessCreatedTask}.
     */
    SuccessCreatedTask createTask(Long id, CreateTask createTask);

    /**
     * The method returns dto {@code SuccessUpdatedTask} of updated task.
     *
     * @param id
     *            - put Task id.
     * @param updateTask
     *            - place body of dto {@code UpdateTask}.
     *
     * @return new {@code SuccessUpdatedTask}.
     */
    SuccessUpdatedTask updateTask(Long id, UpdateTask updateTask);
}
