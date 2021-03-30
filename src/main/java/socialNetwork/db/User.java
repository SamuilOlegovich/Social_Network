package socialNetwork.db;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
// User - название зарезервировано базой, потому меняем название таблицы
@Table(name = "usr")
// сериализовать класс не планируем потому все стандартные методы генирируем обычной анотацией
@Data
@EqualsAndHashCode(of = { "id" })
public class User implements Serializable {
    // ни какого автогенератора айди не указываем,
    // так как айдишники будут приходить из гугл аунтефикатора
    @Id
    @JsonView(Views.IdName.class)
    private String id;
    // поля которые будут приходит от гугла
    @JsonView(Views.IdName.class)
    private String name;
    @JsonView(Views.IdName.class)
    private String userpic;
    private String email;
    @JsonView(Views.FullProfile.class)
    private String gender;
    @JsonView(Views.FullProfile.class)
    private String locale;
    // указываем как у нас будет сохранятся дата при сериализации
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.FullProfile.class)
    private LocalDateTime lastVisit;



    // подписки пользователя (на кого подписан)
    @ManyToMany
    @JoinTable(
            // имя таблицы
            name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    @JsonView(Views.FullProfile.class)
    // кастомизирует анотацию @JsonIdentityInfo
    @JsonIdentityReference
    // чтоб избежать стековервло при сериализации
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private Set<User> subscriptions = new HashSet<>();



    // подписчики пользователя (кто подписан)
    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id")
    )
    @JsonView(Views.FullProfile.class)
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private Set<User> subscribers = new HashSet<>();

}
