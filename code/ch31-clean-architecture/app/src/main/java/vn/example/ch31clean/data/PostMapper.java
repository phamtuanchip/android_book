package vn.example.ch31clean.data;

import java.util.ArrayList;
import java.util.List;

import vn.example.ch31clean.domain.Post;

/** Ranh giới duy nhất nơi PostEntity (tầng data) được chuyển thành Post (tầng domain). */
final class PostMapper {

    static Post toDomain(PostEntity entity) {
        return new Post(entity.id, entity.authorId, entity.title, entity.body);
    }

    static List<Post> toDomainList(List<PostEntity> entities) {
        List<Post> result = new ArrayList<>();
        for (PostEntity entity : entities) {
            result.add(toDomain(entity));
        }
        return result;
    }

    private PostMapper() {
    }
}
