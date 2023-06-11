package hr.mlinx.pinterestclone.service;

import hr.mlinx.pinterestclone.model.Post;
import hr.mlinx.pinterestclone.model.User;

import java.util.List;
import java.util.Set;

public interface PostService {

    List<Post> getPosts();
    List<Post> getPostsByUserId(Long userId);
    Post savePost(Post post);
    Post createPostByUserId(Post post, Long userId);
    void deletePostById(Long postId);

    void deleteYourPostById(Long postId, Long userId);

    Set<Post> getLikedPostsOfUser(User user);

    Set<User> getUserLikesOfPost(Post post);

    void likePost(Long postId, Long userId);
    void unlikePost(Long postId, Long userId);

}
