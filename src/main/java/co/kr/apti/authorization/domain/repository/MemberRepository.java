package co.kr.apti.authorization.domain.repository;

import co.kr.apti.authorization.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String memberId);

    @Query("SELECT m FROM Member m WHERE m.memberId = :memberId AND m.password = :password")
    Optional<Member> findByMemberIdAndPassword(String memberId, String password);

    @Query("SELECT m.memberId FROM Member m WHERE m.refreshToken = :refreshToken")
    Optional<Member> isAuthenticated (String refreshToken);

//    @Modifying // 기본 delete 쿼리를 수정하기 위해 사용
//    @Query("DELETE FROM Member m WHERE m.memberId = :memberId AND m.password = :password")
//    Integer deleteMember(String memberId, String password);

}
