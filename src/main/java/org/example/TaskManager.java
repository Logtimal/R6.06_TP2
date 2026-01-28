package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {

    private final List<Task> tasks = new ArrayList<>();

    public void addTask(String name, LocalDate dueDate) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom de la tâche ne peut pas être vide");
        }
        tasks.add(new Task(name, dueDate));
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getTasksDueBefore(LocalDate date) {
        return tasks.stream()
                .filter(task -> task.getDueDate().isBefore(date))
                .collect(Collectors.toList());
    }

    public boolean removeTask(String name) {
        return tasks.removeIf(task -> task.getName().equalsIgnoreCase(name));
    }

    public static class Task {
        private final String name;
        private final LocalDate dueDate;

        public Task(String name, LocalDate dueDate) {
            this.name = name;
            this.dueDate = dueDate;
        }

        public String getName() {
            return name;
        }

        public LocalDate getDueDate() {
            return dueDate;
        }
    }
}

