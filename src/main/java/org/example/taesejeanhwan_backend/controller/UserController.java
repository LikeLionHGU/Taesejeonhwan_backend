import com.thc.back14th.dto.user.UserCreateRequest;
import com.thc.back14th.dto.user.UserResponse;
import com.thc.back14th.dto.user.UserUpdateRequest;
import com.thc.back14th.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // HTTP 요청 받는 컨트롤러
@RequiredArgsConstructor // final로 선언된 필드들만 모아서 생성자를 자동생성.
// new UserService() 안 하고, 스프링이 만들어준 걸 생성자로 꽂아줌!!
@RequestMapping("/users") // 공통 URL 앞부분 설정.예를들어 https://likelion.users.
public class UserController {

    private final UserService userService; // 컨트롤러는 요청/응답 처리만 하고 진짜 규칙은 service가 함.
    // controller는 입구고, service가 실제 처리하는곳.

    @PostMapping// 추가하기!!
    public Long create(@Valid @RequestBody UserCreateRequest req) {
        return userService.create(req);
    } // 요청 body(JSON) UserCreateRequest DTO에 매핑!
    // @Valid는 DTO에 검증 어노테이션을 실행!, 아까 DTO에 있던 @NotBlank, @Email등 검증 실패하면 400에러 남.

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll(); // 서비스가 DB에서 전부 조회하고 List<UserResponse>로 변환해서 돌려줌.
    }// Entity를 그대로 내보내면 나중에 구조 바뀔 때 API가 깨짐!! 그래서 안전하게 DTO 사용.

    @GetMapping("/{id}")// 경로에 id라는 변수가 들어갔음. 예를들어 도서관에서 한국사 책 하나를 가져오는 느낌.
    public UserResponse findOne(@PathVariable Long id) {
        return userService.findOne(id);
    }// URL {id} 값을 꺼내서 변수 id에 담아둔다. 그래서 PathVariable은 주소에 박힌 값을 뽑아 오는 것.

    @PutMapping("/{id}")// PUT은 수정! 수정은 '누구를(id) + 어떻게 바꿀지(body)'가 필요하다.
    public void update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest req) {
        userService.update(id, req);
    }
    /*
    @PathVariable Long id : PathVariable은 주소에 박힌 값을 뽑는 것.
    @RequestBody UserUpdateRequest req : 요청 body(JSON)를 DTO로 변환해서 받음.
    결론적으로 RequestBody는 JSON을 객체로 바꿔서 받는 것!!
    @Valid : DTO에 달려있는 검증 어노테이션을 실행!
    userService.update(id, req) : 저번에 배운것처럼 컨트롤러는 로직을 하지 않음. id와 req만 넘겨주는 역할을 한다.
    진짜 수정은 Service가 함! 컨트롤러는 전달만.
     */

    @DeleteMapping("/{id}")// 누구를 지울지만 알면됨 ({id})
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
    /*
    @PathVariable Long id : 삭제는 body가 없어도 됨. URL에서 id만 뽑으면 충분.
     userService.delete(id) : 실제 삭제는 서비스가 repository를 통해 수행!
     */

    /*
    컨트롤러 핵심!
    1. @PathVariable 붙여야함. 안붙이면 id가 안들어와서 400에러 뜸.
    2. Update에서는 @RequestBody를 붙여야한다. 수정 할때 request가 JSON 형식으로 들어온걸 바꿔서
    Update해야 하기 때문에 붙여야함.
    3. DTO에 검증 어노테이션을 붙였으면 @Valid 쓰기.

     */
}