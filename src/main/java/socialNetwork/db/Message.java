package socialNetwork.db;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity                             // указываем что это сущность
@Table                              // указываем что эту сущность нужно искать в таблице
@ToString(of = {"id", "text"})      // для автоматической генирации метода toString()
@EqualsAndHashCode(of = {"id"})     // тоже для Equals
public class Message {
    @Id // обязательно библиотека javax.persistence.*;
    @GeneratedValue(strategy = GenerationType.AUTO)
    // используем для скрытия полей (для этого создаем дополнительный класс Views)
    @JsonView(Views.Id.class)
    private Long id;
    // используем для скрытия полей (для этого создаем дополнительный класс Views)
    @JsonView(Views.IdName.class)
    private String text;

    // указываем что поле не обновляемое
    @Column(updatable = false)
    // указываем как у нас будет сохранятся дата при сериализации
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    // используем для скрытия полей (для этого создаем дополнительный класс Views)
    @JsonView(Views.FullMessage.class)
    private LocalDateTime creationDate;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
