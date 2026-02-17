package org.example.taesejeanhwan_backend.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.dto.feed.response.*;
import org.example.taesejeanhwan_backend.dto.feed.request.*;
import org.example.taesejeanhwan_backend.dto.user.response.UserResponseSearchUser;
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

    //리뷰추가
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
    public FeedResponseGetContent getContentReview(@PathVariable Long user_id, @PathVariable Long content_id) {
        return reviewService.getContentReview(user_id, content_id);
    }

    //콘텐츠의 전체 리뷰 확인
    @GetMapping("/{content_id}/reviews")
    public List<FeedResponseGetReview> getReviews(@PathVariable Long content_id) {
        return reviewService.getAllReviews(content_id);
    }

    //준혁 (영화관 피드 목록 조회)(끝)
    @GetMapping("/{user_id}/contents")
    public List<FeedResponseGetUserContent> getUserContents(@PathVariable Long user_id) {
        return userContentService.getUserContents(user_id);
    }

    //mode에 따른 유저와 콘텐츠 보기
    @GetMapping("/{mode}/{user_id}")
    public FeedResponseUserAndContent getUserAndContent(@PathVariable String mode, @PathVariable Long user_id, @RequestParam("page") int page) {
        return contentService.getUserAndContent(mode, user_id, page);
    }

    //콘텐츠 검색
    @GetMapping("/search-content/{keyword}")
    public FeedResponseSearchContent searchContent(@PathVariable String keyword) {
        return contentService.searchContent(keyword);
    }

    //유저 검색
    @GetMapping("/search-user/{keyword}")
    public UserResponseSearchUser searchUser(@PathVariable String keyword) {
        return userService.searchUser(keyword);
    }

    //준혁(모든 장르 불러오기)(끝)
    @GetMapping("/genre")
    public List<FeedResponseGetGenre> getAllGenres() {

        return contentService.getAllGenres();
    }

    //준혁(리뷰 수정하기)(끝)
    @PutMapping("/reviews")
    public FeedResponseReviewUpdate updateReview(@RequestBody FeedRequestReviewUpdate feedRequestReviewUpdate) {
        return reviewService.updateReview(feedRequestReviewUpdate);
    }

    //준혁(장르 수정하기)(끝)
    @PutMapping("/{user_id}/genre")
    public FeedResponseResult updateKeyword(
            @PathVariable Long user_id,
            @RequestBody FeedRequestUpdateGenre req
    ) {
        return userService.updateKeyword(user_id, req);
    }


    //준혁(찜하기 제거)(끝)
    @DeleteMapping("/wish")
    public FeedResponseResult deleteWish(@RequestBody FeedRequestDeleteWish feedRequestDeleteWish) {
        return wishService.deleteWish(feedRequestDeleteWish);
    }

}
