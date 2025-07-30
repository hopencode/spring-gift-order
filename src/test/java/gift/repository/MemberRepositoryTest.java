package gift.repository;

import gift.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원_계정_정상_저장() {
        Member member = new Member(null, "test@email.com", "12345678", false);

        var result = memberRepository.save(member);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getEmail()).isEqualTo(member.getEmail())
        );
    }

    @Test
    void findByEmail_정상_테스트() {
        Member member = new Member(null, "test@email.com", "12345678", false);
        memberRepository.save(member);

        Optional<Member> found = memberRepository.findByEmail("test@email.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@email.com");
    }

    @Test
    public void findByEmail_없는_회원_계정_검색_테스트() {
        Optional<Member> found = memberRepository.findByEmail("not_exist@email.com");

        assertThat(found).isNotPresent();
    }
}
