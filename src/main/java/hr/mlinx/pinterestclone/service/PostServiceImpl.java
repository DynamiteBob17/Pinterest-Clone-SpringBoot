package hr.mlinx.pinterestclone.service;

import hr.mlinx.pinterestclone.exception.BadRequestException;
import hr.mlinx.pinterestclone.exception.ResourceNotFoundException;
import hr.mlinx.pinterestclone.model.Post;
import hr.mlinx.pinterestclone.model.User;
import hr.mlinx.pinterestclone.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findAllByUser(userService.getUserById(userId));
    }

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post createPostByUserId(Post post, Long userId) {
        post.setUser(userService.getUserById(userId));
        return savePost(post);
    }

    @Override
    public void deletePostById(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public void deleteYourPostById(Long postId, Long userId) {
        Post post = getPostById(postId);

        if (!Objects.equals(post.getUser().getId(), userId)) {
            throw new BadRequestException("You are not allowed to delete post by id " + postId);
        }

        postRepository.delete(post);
    }

    @Override
    public Set<Post> getLikedPostsOfUser(User user) {
        Hibernate.initialize(user.getLikedPosts());
        return user.getLikedPosts();
    }

    @Override
    public Set<User> getUserLikesOfPost(Post post) {
        Hibernate.initialize(post.getLikedByUsers());
        return post.getLikedByUsers();
    }

    private enum PostAction {
        LIKE, UNLIKE
    }

    @Override
    public void likePost(Long userId, Long postId) {
        modifyPostLikes(postId, userId, PostAction.LIKE);
    }

    @Override
    public void unlikePost(Long userId, Long postId) {
        modifyPostLikes(postId, userId, PostAction.UNLIKE);
    }

    private void modifyPostLikes(Long userId, Long postId, PostAction postAction) {
        User user = userService.getUserById(userId);
        Post post = getPostById(postId);
        Set<Post> likedPosts = getLikedPostsOfUser(user);
        Set<User> likedByUsers = getUserLikesOfPost(post);

        if (postAction == PostAction.LIKE) {
            likedPosts.add(post);
            likedByUsers.add(user);
        } else if (postAction == PostAction.UNLIKE) {
            likedPosts.remove(post);
            likedByUsers.remove(user);
        }

        userService.saveUser(user);
        savePost(post);
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post", "post_id", postId));
    }

}
