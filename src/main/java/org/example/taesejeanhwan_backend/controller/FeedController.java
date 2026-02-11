package org.example.taesejeanhwan_backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.dto.feed.response.*;
import org.example.taesejeanhwan_backend.dto.feed.request.*;
import org.example.taesejeanhwan_backend.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {

    public final ReviewService reviewService;
    public final UserService userService;
    public final WishService wishService;
    public final ContentService contentService;
    public final UserContentService userContentService;
    public final GenreService genreService;
    public final UserGenreService userGenreService;

    @PostMapping("/reviews")
    public FeedResponseResult addReview(@RequestBody FeedRequestAddReview feedAddReview) {
        return reviewService.addReview(feedAddReview);
    }

    //콘텐츠 찜하기
    @PostMapping("/wish")
    public FeedResponseResult addWish(@RequestBody FeedRequestAddWish feedRequestAddWish) {
        return wishService.addWish(feedRequestAddWish);
    }

    //찜한 콘텐츠 가져오기
    @GetMapping("/{user_id}/wish")
    public List<FeedResponseGetWish> getWishList(@PathVariable Long user_id) {
        return wishService.getWishList(user_id);
    }

    //콘텐츠 리뷰 확인
    @GetMapping("/{user_id}/{content_id}/review")
    public FeedResponseGetContent getContentReview(@PathVariable Long user_id, Long content_id) {
        return reviewService.getContentReview(user_id, content_id);
    }

    //콘텐츠의 전체 리뷰 확인
    @GetMapping("/{content_id}/reviews")
    public List<FeedResponseGetReview> getReviews(@PathVariable Long content_id) {
        return reviewService.getAllReviews(content_id);
    }

    //준혁
    @GetMapping("/{user_id}/contents")
    public List<FeedResponseGetUserContent> getUserContents(@PathVariable Long user_id) {
        return userContentService.getUserContents(user_id);
    }

    @GetMapping("/{mode}")
    public FeedResponseUserAndContent getUserAndContent(@PathVariable String mode, @RequestParam("page") Long page) {
        return contentService.getUserAndContent(mode, page);
    }

    @GetMapping("/search-content/{keyword}")
    public FeedResponseSearchContent searchContent(@PathVariable String keyword) {
        return contentService.searchContent(keyword);
    }

    @GetMapping("/search-user/{keyword}")
    public FeedResponseSearchUser searchUser(@PathVariable String keyword) {
        return contentService.searchUser(keyword);
    }

    //준혁
    @GetMapping("/genre")
    public List<FeedResponseGetGenre> getAllGenres() {
        return contentService.getAllGenres();
    }

    //준혁
    @PutMapping("/reviews")
    public FeedResponseReviewUpdate updateReview(@RequestBody FeedRequestReviewUpdate feedRequestReviewUpdate) {
        return reviewService.updateReview(feedRequestReviewUpdate);
    }

    //준혁
    @PutMapping("/genre")
    public FeedResponseResult updateKeyword(@RequestBody FeedRequestUpdateGenre feedRequestUpdateGenre) {
        return userService.updateKeyword(feedRequestUpdateGenre);
    }

    //준혁
    @DeleteMapping("/wish")
    public FeedResponseResult deleteWish(@RequestBody FeedRequestDeleteWish feedRequestDeleteWish) {
        return wishService.deleteWish(feedRequestDeleteWish);
    }



}
