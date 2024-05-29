package carkey.project.analysis.domain;

import carkey.project.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor
@Data
@Table(name = "analysis")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id")
    private Long id;

    private String totalPrice;

    private String originalImg;

    private String scratchImg;

    private String crushedImg;

    private String analyzeDate;

    private String naturalImg;

    private String analyzeDatetime;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Analysis(User user, String totalPrice, String originalImg, String naturalImg, String scratchImg, String crushedImg) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.originalImg = originalImg;
        this.naturalImg = naturalImg;
        this.scratchImg = scratchImg;
        this.crushedImg = crushedImg;
    }

    @PrePersist
    public void postDate() {
        String analyzeDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String analyzeDatetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.analyzeDate = analyzeDate;
        this.analyzeDatetime = analyzeDatetime;
    }


}
