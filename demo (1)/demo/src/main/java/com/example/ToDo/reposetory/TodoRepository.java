package com.example.ToDo.reposetory;

import com.example.ToDo.Models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    Optional<Todo> findById(int id);
}
