package dev.piluvitu.todolist.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

  @Id
  @GeneratedValue(generator = "UUID")
  @Column(unique = true)
  private UUID id;

  private UUID idUser;
  private Boolean checked;

  @Column(length = 50)
  private String title;

  private String description;
  private String priority;

  // yyyy-MM-ddTHH:mm:ss
  private LocalDateTime startAt;
  private LocalDateTime endAt;

  @CreationTimestamp
  private LocalDateTime createdAt;

  public void setTitle(String title) throws Exception {
    if (title.length() > 50) {
      throw new Exception("O Title deve conter no max 50 caracteres.");
    }
    this.title = title;
  }
}
