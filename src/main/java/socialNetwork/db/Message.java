package socialNetwork.db;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
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
    // для картинок, видео, сайты и т д  - линк
    @JsonView(Views.FullMessage.class)
    private String link;
    // для ссылок ведущих не на ютуб и не на картинку на прямую
    @JsonView(Views.FullMessage.class)
    private String linkTitle;
    @JsonView(Views.FullMessage.class)
    private String linkDescription;
    @JsonView(Views.FullMessage.class)
    private String linkCover;


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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkDescription() {
        return linkDescription;
    }

    public void setLinkDescription(String linkDescription) {
        this.linkDescription = linkDescription;
    }

    public String getLinkCover() {
        return linkCover;
    }

    public void setLinkCover(String linkCover) {
        this.linkCover = linkCover;
    }
}
