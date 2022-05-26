package com.example.demo.src.auth;


import com.example.demo.config.BaseException;
import com.example.demo.src.auth.model.PostLoginReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.demo.src.auth.model.User;


import javax.sql.DataSource;
import java.util.List;

@Repository

public class AuthDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User getPwd(PostLoginReq postLoginReq) {
        //클라이언트에서 받은 객체에서 email을 받아서, 이 email을 가진 해당 유저를 리턴을 해줌.
        String getPwdQuery = "select userIdx, name, phone, email, password, univ, department, belong, certifyImg, certified from User where email = ?";
        String getPwdParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("univ"),
                        rs.getString("department"),
                        rs.getString("belong"),
                        rs.getString("certifyImg"),
                        rs.getString("certified")
                ),
                getPwdParams
        );
    }



    // 이메일 확인
    public int checkEmail(String email) {
        String checkEmailQuery =
                "select exists(select userIdx from User where email = ? and status IN ('active', 'dormant'))"; // 이메일 중복되는 지 확인
        String checkEmailParam = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParam); //결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환
    }











    /*


    // 해당 이메일을 가진 유저가 있을 때 상태 확인 -> 탈퇴 후 신규 가입 시 email이 중복되는 값이 되버림
    public String checkStatusOfUser(String email) {
        String checkStatusOfUserQuery = "select status from User where email = ?";
        String checkStatusOfUserParam = email;

        List<String> statusOfUsers =
                this.jdbcTemplate.queryForList(checkStatusOfUserQuery, String.class, checkStatusOfUserParam);

        // 만약 탈퇴회원이 다시 로그인한다 했을 때 active가 존재하면 로그인이고 아니면 회원가입 필요 메시지 날리도록
        for (String status : statusOfUsers) {
            if (status.equals("active") || status.equals("dormant")) {
                return status; // active dormant 검사를 먼저 해야함
            }
        } // 이 반복문을 통과하면 탈퇴회원임

        // 재가입 안 한 탈퇴회원이면 아래 리턴식으로 반환
        return this.jdbcTemplate.queryForObject(checkStatusOfUserQuery, String.class, checkStatusOfUserParam);
    }

    // 해당 userIdx을 가진 유저 상태 체크 -> 휴면이면 재활성화, 탈퇴면 예외 메시지
    public void checkStatusOfUser(int userIdx) throws BaseException {
        String statusOfUserQuery = "select status from User where userIdx = ?";

        if (this.jdbcTemplate.queryForObject(statusOfUserQuery, String.class, userIdx).equals("dormant")) // 휴면 상태면 재활성화
            jdbcTemplate.update("update User set status = 'active', recOthers = 1, recSimilarAge = 1 where userIdx = ?", userIdx);

        else if (this.jdbcTemplate.queryForObject(statusOfUserQuery, String.class, userIdx).equals("deleted")) // 탈퇴 상태면 throw exception
            throw new BaseException(INVALID_JWT);
    }

    // 해당 이메일을 가진 유저의 식별자 반환
    public int idxOfUserWithEmail(String email) {
        String idxOfUserWithEmailQuery = "select userIdx from User where email = ? and status IN ('active', 'dormant')";
        String idxOfUserWithEmailParam = email;
        int userIdx = this.jdbcTemplate.queryForObject(idxOfUserWithEmailQuery, int.class, idxOfUserWithEmailParam);
        // 휴면 상태면 재활성화
        jdbcTemplate.update("update User set status = 'active', recOthers = 1, recSimilarAge = 1 where userIdx = ? and status = 'dormant'", userIdx);
        return userIdx;
    }

    // 로그인 기록 갱신
    public int updateLastConnect(int userIdx) {
        String lastConnectQuery = "update User set lastConnect = CURRENT_TIMESTAMP where userIdx = ?";
        int lastConnectParam = userIdx;
        return this.jdbcTemplate.update(lastConnectQuery, lastConnectParam);
        // 대응시켜 매핑시켜 쿼리 요청(변경했으면 1, 실패했으면 0)
    }

    // 디바이스 토큰 갱신
    public int updateToken(String fcmToken, int userIdx) {
        String query = "update User set fcmToken = ? where userIdx = ?";
        Object[] params = new Object[]{fcmToken, userIdx};
        return this.jdbcTemplate.update(query, params);
        // 대응시켜 매핑시켜 쿼리 요청(변경했으면 1, 실패했으면 0)
    }

     */
}