package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "kakao_account", nullable = false)
    private boolean kakaoAccount;

    public Member() {}

    public Member(Long id, String email, String password, boolean kakaoAccount) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.kakaoAccount = kakaoAccount;
    }

    public Member(String email, String password, boolean kakaoAccount) {
        this(null, email, password, kakaoAccount);
   ;}

    public Member(String email, String password) {
        this(null, email, password, false);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isKakaoAccount() {
        return kakaoAccount;
    }
}
