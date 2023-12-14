package com.superngb.taskservice.repository;

import com.superngb.taskservice.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<List<Task>> findAllByUsersId(Long id);

    Optional<List<Task>> findAllByCardId(Long id);
}
