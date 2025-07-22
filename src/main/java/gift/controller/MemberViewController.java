package gift.controller;

import gift.annotation.AuthenticatedUser;
import gift.auth.JwtAuth;
import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.exception.MemberExceptions;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/members")
public class MemberViewController {

    private final MemberService memberService;
    private final JwtAuth jwtAuth;

    public MemberViewController(MemberService memberService, JwtAuth jwtAuth) {
        this.memberService = memberService;
        this.jwtAuth = jwtAuth;
    }

    @GetMapping("/membership")
    public String showRegisterPage() {
        return "member/register";
    }

    @PostMapping("/membership")
    public ResponseEntity<?> register(@RequestBody @Valid MemberRequestDto memberRequestDto, BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        if (memberService.isEmailExists(memberRequestDto.getEmail())) {
            errors.add("이미 등록된 이메일입니다.");
        }
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        MemberResponseDto responseDto = memberService.register(memberRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "member/login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid MemberRequestDto memberRequestDto, BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        }
        try {
            MemberResponseDto responseDto = memberService.login(memberRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (MemberExceptions.MemberNotFoundException e) {
            errors.add("등록되지 않은 이메일입니다.");
        } catch (MemberExceptions.InvalidPasswordException e) {
            errors.add("틀린 비밀번호입니다.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @GetMapping("/myInfo")
    public String showMyInfo() {
        return "member/myInfo";
    }

    @GetMapping("/wishlist")
    public ResponseEntity<?> getWishlist(@AuthenticatedUser String token) {

        List<String> wishlist = new ArrayList<>();
        return ResponseEntity.status(HttpStatus.OK).body(wishlist);
    }
}
