package socialNetwork.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import socialNetwork.db.Comment;
import socialNetwork.db.User;
import socialNetwork.repo.CommentRepo;


@Service
public class CommentService {
    private final CommentRepo commentRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public Comment create(Comment comment, User user) {
        // берем коммент и проставляем ему автора и сохраняем его
        comment.setAuthor(user);
        commentRepo.save(comment);

        return comment;
    }
}
