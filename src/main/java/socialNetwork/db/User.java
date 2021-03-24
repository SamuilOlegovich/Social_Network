package socialNetwork.db;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;



@Entity
// User - название зарезервировано базой, потому меняем название таблицы
@Table(name = "usr")
// сериализовать класс не планируем потому все стандартные методы генирируем обычной анотацией
@Data
public class User {
    // ни какого автогенератора айди не указываем,
    // так как айдишники будут приходить из гугл аунтефикатора
    @Id
    private String id;
    // поля которые будут приходит от гугла
    private String name;
    private String userpic;
    private String email;
    private String gender;
    private String locale;
    // дату последнего визита
    private LocalDateTime lastVisit;
}
