package socialNetwork.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import socialNetwork.db.Comment;
import socialNetwork.db.Message;
import socialNetwork.db.User;
import socialNetwork.db.Views;
import socialNetwork.dto.EventType;
import socialNetwork.dto.ObjectType;
import socialNetwork.repo.CommentRepo;
import socialNetwork.util.WsSender;

import java.util.function.BiConsumer;


@Service
public class CommentService {
    private final BiConsumer<EventType, Comment> wsSender;
    private final CommentRepo commentRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo, WsSender wsSender) {
        this.wsSender = wsSender.getSender(ObjectType.COMMENT, Views.FullComment.class);
        this.commentRepo = commentRepo;
    }

    public Comment create(Comment comment, User user) {
        // берем коммент и проставляем ему автора и сохраняем его
        comment.setAuthor(user);
        Comment commentFromDB = commentRepo.save(comment);

        // для отправки комментов по сокету
        wsSender.accept(EventType.CREATE, commentFromDB);

        return commentFromDB;
    }
}
