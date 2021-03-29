package socialNetwork.db;


// класс благодаря которому интерфейсами определяем чо показывать на выходе, а что нет
// применяемтся в анотациях - @JsonView(Views.FullMessage.class) - над определенным полем
public class Views {
    public interface Id {}

    public interface IdName extends Id {}

    public interface FullComment extends IdName {}

    public interface FullMessage extends IdName {}
}
