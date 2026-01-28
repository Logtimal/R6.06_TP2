package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Tests de la classe TaskManager")
class TaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new TaskManager();
    }

    @Nested
    @DisplayName("Tests d'ajout de tâches")
    class AddTaskTests {

        @Test
        @DisplayName("devrait ajouter une tâche valide correctement")
        void should_add_task_successfully() {
            // Given
            String taskName = "Faire les courses";
            LocalDate dueDate = LocalDate.of(2026, 2, 15);

            // When
            taskManager.addTask(taskName, dueDate);

            // Then
            List<TaskManager.Task> tasks = taskManager.getAllTasks();
            assertThat(tasks)
                    .hasSize(1)
                    .first()
                    .satisfies(task -> {
                        assertThat(task.getName()).isEqualTo(taskName);
                        assertThat(task.getDueDate()).isEqualTo(dueDate);
                    });
        }

        @Test
        @DisplayName("devrait vérifier que la tâche ajoutée possède le bon nom et la bonne date")
        void should_verify_task_has_correct_name_and_date() {
            // Given
            String taskName = "Réviser les tests unitaires";
            LocalDate dueDate = LocalDate.of(2026, 3, 1);

            // When
            taskManager.addTask(taskName, dueDate);

            // Then
            TaskManager.Task addedTask = taskManager.getAllTasks().get(0);
            assertThat(addedTask.getName()).isEqualTo(taskName);
            assertThat(addedTask.getDueDate()).isEqualTo(dueDate);
        }

        @Test
        @DisplayName("devrait lever une exception si le nom de la tâche est null")
        void should_throw_exception_when_task_name_is_null() {
            // Given
            String taskName = null;
            LocalDate dueDate = LocalDate.of(2026, 2, 15);

            // When & Then
            assertThatThrownBy(() -> taskManager.addTask(taskName, dueDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Le nom de la tâche ne peut pas être vide");
        }

        @Test
        @DisplayName("devrait lever une exception si le nom de la tâche est vide")
        void should_throw_exception_when_task_name_is_empty() {
            // Given
            String taskName = "";
            LocalDate dueDate = LocalDate.of(2026, 2, 15);

            // When & Then
            assertThatThrownBy(() -> taskManager.addTask(taskName, dueDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Le nom de la tâche ne peut pas être vide");
        }

        @Test
        @DisplayName("devrait lever une exception si le nom de la tâche est blank")
        void should_throw_exception_when_task_name_is_blank() {
            // Given
            String taskName = "   ";
            LocalDate dueDate = LocalDate.of(2026, 2, 15);

            // When & Then
            assertThatThrownBy(() -> taskManager.addTask(taskName, dueDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Le nom de la tâche ne peut pas être vide");
        }
    }

    @Nested
    @DisplayName("Tests de récupération de toutes les tâches")
    class GetAllTasksTests {

        @Test
        @DisplayName("devrait récupérer toutes les tâches ajoutées")
        void should_retrieve_all_added_tasks() {
            // Given
            taskManager.addTask("Tâche 1", LocalDate.of(2026, 2, 10));
            taskManager.addTask("Tâche 2", LocalDate.of(2026, 2, 20));
            taskManager.addTask("Tâche 3", LocalDate.of(2026, 3, 5));

            // When
            List<TaskManager.Task> tasks = taskManager.getAllTasks();

            // Then
            assertThat(tasks)
                    .hasSize(3)
                    .extracting(TaskManager.Task::getName)
                    .containsExactly("Tâche 1", "Tâche 2", "Tâche 3");
        }

        @Test
        @DisplayName("devrait retourner la liste avec le bon nombre d'éléments")
        void should_return_correct_number_of_tasks() {
            // Given
            taskManager.addTask("Tâche A", LocalDate.of(2026, 2, 10));
            taskManager.addTask("Tâche B", LocalDate.of(2026, 2, 20));

            // When
            List<TaskManager.Task> tasks = taskManager.getAllTasks();

            // Then
            assertThat(tasks).hasSize(2);
        }

        @Test
        @DisplayName("devrait retourner une liste vide quand aucune tâche n'a été ajoutée")
        void should_return_empty_list_when_no_tasks_added() {
            // When
            List<TaskManager.Task> tasks = taskManager.getAllTasks();

            // Then
            assertThat(tasks).isEmpty();
        }
    }

    @Nested
    @DisplayName("Tests de filtrage des tâches par date")
    class GetTasksDueBeforeTests {

        @Test
        @DisplayName("devrait retourner uniquement les tâches dont la date est strictement antérieure")
        void should_return_only_tasks_due_before_given_date() {
            // Given
            taskManager.addTask("Tâche 1", LocalDate.of(2026, 2, 10));
            taskManager.addTask("Tâche 2", LocalDate.of(2026, 2, 20));
            taskManager.addTask("Tâche 3", LocalDate.of(2026, 3, 5));
            LocalDate filterDate = LocalDate.of(2026, 2, 25);

            // When
            List<TaskManager.Task> tasksDueBefore = taskManager.getTasksDueBefore(filterDate);

            // Then
            assertThat(tasksDueBefore)
                    .hasSize(2)
                    .extracting(TaskManager.Task::getName)
                    .containsExactly("Tâche 1", "Tâche 2");
        }

        @Test
        @DisplayName("ne devrait pas inclure les tâches dont la date est égale à la date fournie")
        void should_not_include_tasks_with_equal_date() {
            // Given
            taskManager.addTask("Tâche 1", LocalDate.of(2026, 2, 10));
            taskManager.addTask("Tâche 2", LocalDate.of(2026, 2, 20));
            taskManager.addTask("Tâche 3", LocalDate.of(2026, 2, 20));
            LocalDate filterDate = LocalDate.of(2026, 2, 20);

            // When
            List<TaskManager.Task> tasksDueBefore = taskManager.getTasksDueBefore(filterDate);

            // Then
            assertThat(tasksDueBefore)
                    .hasSize(1)
                    .extracting(TaskManager.Task::getName)
                    .containsExactly("Tâche 1");
        }

        @Test
        @DisplayName("ne devrait pas inclure les tâches dont la date est postérieure à la date fournie")
        void should_not_include_tasks_with_later_date() {
            // Given
            taskManager.addTask("Tâche 1", LocalDate.of(2026, 2, 10));
            taskManager.addTask("Tâche 2", LocalDate.of(2026, 3, 20));
            taskManager.addTask("Tâche 3", LocalDate.of(2026, 4, 5));
            LocalDate filterDate = LocalDate.of(2026, 2, 15);

            // When
            List<TaskManager.Task> tasksDueBefore = taskManager.getTasksDueBefore(filterDate);

            // Then
            assertThat(tasksDueBefore)
                    .hasSize(1)
                    .extracting(TaskManager.Task::getName)
                    .containsExactly("Tâche 1");
        }

        @Test
        @DisplayName("devrait retourner une liste vide quand aucune tâche ne correspond au critère")
        void should_return_empty_list_when_no_tasks_match_criteria() {
            // Given
            taskManager.addTask("Tâche 1", LocalDate.of(2026, 3, 10));
            taskManager.addTask("Tâche 2", LocalDate.of(2026, 3, 20));
            LocalDate filterDate = LocalDate.of(2026, 2, 1);

            // When
            List<TaskManager.Task> tasksDueBefore = taskManager.getTasksDueBefore(filterDate);

            // Then
            assertThat(tasksDueBefore).isEmpty();
        }
    }

    @Nested
    @DisplayName("Tests de suppression de tâches")
    class RemoveTaskTests {

        @Test
        @DisplayName("devrait supprimer une tâche existante par son nom")
        void should_remove_existing_task_by_name() {
            // Given
            taskManager.addTask("Tâche à supprimer", LocalDate.of(2026, 2, 10));
            taskManager.addTask("Tâche à garder", LocalDate.of(2026, 2, 20));

            // When
            boolean result = taskManager.removeTask("Tâche à supprimer");

            // Then
            assertThat(result).isTrue();
            assertThat(taskManager.getAllTasks())
                    .hasSize(1)
                    .extracting(TaskManager.Task::getName)
                    .containsExactly("Tâche à garder");
        }

        @Test
        @DisplayName("devrait être insensible à la casse lors de la suppression")
        void should_be_case_insensitive_when_removing_task() {
            // Given
            taskManager.addTask("Tâche Test", LocalDate.of(2026, 2, 10));

            // When
            boolean result = taskManager.removeTask("tâche test");

            // Then
            assertThat(result).isTrue();
            assertThat(taskManager.getAllTasks()).isEmpty();
        }

        @Test
        @DisplayName("devrait retourner true lorsqu'une tâche est supprimée")
        void should_return_true_when_task_is_removed() {
            // Given
            taskManager.addTask("Tâche", LocalDate.of(2026, 2, 10));

            // When
            boolean result = taskManager.removeTask("Tâche");

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("devrait retourner false lorsqu'aucune tâche ne correspond au nom")
        void should_return_false_when_no_task_matches_name() {
            // Given
            taskManager.addTask("Tâche 1", LocalDate.of(2026, 2, 10));

            // When
            boolean result = taskManager.removeTask("Tâche inexistante");

            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("devrait mettre à jour correctement la liste après suppression")
        void should_update_list_correctly_after_removal() {
            // Given
            taskManager.addTask("Tâche 1", LocalDate.of(2026, 2, 10));
            taskManager.addTask("Tâche 2", LocalDate.of(2026, 2, 15));
            taskManager.addTask("Tâche 3", LocalDate.of(2026, 2, 20));

            // When
            taskManager.removeTask("Tâche 2");

            // Then
            List<TaskManager.Task> remainingTasks = taskManager.getAllTasks();
            assertThat(remainingTasks)
                    .hasSize(2)
                    .extracting(TaskManager.Task::getName)
                    .containsExactly("Tâche 1", "Tâche 3");
        }

        @Test
        @DisplayName("devrait retourner false lors de la suppression dans une liste vide")
        void should_return_false_when_removing_from_empty_list() {
            // When
            boolean result = taskManager.removeTask("Tâche inexistante");

            // Then
            assertThat(result).isFalse();
        }
    }
}

