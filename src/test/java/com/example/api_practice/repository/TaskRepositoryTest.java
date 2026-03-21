package com.example.api_practice.repository;

import com.example.api_practice.config.SecurityConfig;
import com.example.api_practice.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("タスクを保存")
    void saveTask_success() {
        Task task = new Task("title", "description");

        Task saved = taskRepository.save(task);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("title");
        assertThat(saved.getDescription()).isEqualTo("description");
        assertThat(saved.isCompleted()).isFalse();
    }

    @Test
    @DisplayName("IDでタスクを取得")
    void findById_existingId() {
        Task task = new Task("title", "description");
        Task saved = taskRepository.save(task);

        Optional<Task> result = taskRepository.findById((saved.getId()));

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("存在しないIDの場合はemptyを返す")
    void findById_nonExistingId() {
        Optional<Task> result = taskRepository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("タスク一覧を取得")
    void findAll_success() {
        taskRepository.save(new Task("A", "a"));
        taskRepository.save(new Task("B", "b"));

        List<Task> result = taskRepository.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("タスクの削除")
    void deleteTask_success() {
        Task task = new Task("title", "description");
        Task saved = taskRepository.save(task);

        taskRepository.deleteById(saved.getId());

        Optional<Task> result = taskRepository.findById(saved.getId());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("タスクの更新")
    void updateTask_success() {
        Task task = new Task("title", "description");
        Task saved = taskRepository.save(task);

        saved.setTitle("updated title");
        Task updated = taskRepository.save(saved);

        assertThat(updated.getTitle()).isEqualTo("updated title");
    }

    @Test
    @DisplayName("ページングで取得")
    void findAll_withPaging() {
        for (int i = 0; i < 5; i++) {
            taskRepository.save(new Task("task" + i, "desc"));
        }

        Pageable pageable = PageRequest.of(0, 3);
        Page<Task> page = taskRepository.findAll(pageable);

        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
    }
}
