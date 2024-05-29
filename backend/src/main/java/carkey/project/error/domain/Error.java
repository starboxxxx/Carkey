package carkey.project.error.domain;

import carkey.project.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "error")
public class Error {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String error;

    private String createdDate;

    public Error(String content) {
        this.error = content;
    }

    @PrePersist
    public void createdDate() {
        String customLocalDateTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.createdDate = customLocalDateTimeFormat;
    }
}
