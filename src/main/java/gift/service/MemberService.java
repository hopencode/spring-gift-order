package gift.service;

import gift.auth.JwtAuth;
import gift.dto.*;
import gift.entity.Member;
import gift.exception.MemberExceptions;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtAuth jwtAuth;

    public MemberService(MemberRepository memberRepository, JwtAuth jwtAuth) {
        this.memberRepository = memberRepository;
        this.jwtAuth = jwtAuth;
    }

    public MemberResponseDto register(MemberRequestDto requestDto) {
        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new MemberExceptions.EmailAlreadyExistsException(requestDto.getEmail());
        }

        Member member = new Member(requestDto.getEmail(), requestDto.getPassword());
        memberRepository.save(member);
        String token = jwtAuth.createJwtToken(member);
        return new MemberResponseDto(token);
    }

    public MemberResponseDto login(MemberRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new MemberExceptions.MemberNotFoundException(requestDto.getEmail()));

        if (!member.getPassword().equals(requestDto.getPassword())) {
            throw new MemberExceptions.InvalidPasswordException();
        }

        String token = jwtAuth.createJwtToken(member);
        return new MemberResponseDto(token);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberExceptions.MemberNotFoundException(email));
    }

    public boolean isEmailExists(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    public String add(Member member) {
        memberRepository.save(member);
        return jwtAuth.createJwtToken(member);
    }
}
