package vn.example.ch31clean.domain;

import javax.inject.Inject;

public class RefreshPostsUseCase {

    private final PostRepository repository;

    @Inject
    public RefreshPostsUseCase(PostRepository repository) {
        this.repository = repository;
    }

    public void execute(PostRepository.RefreshCallback callback) {
        repository.refresh(callback);
    }
}
