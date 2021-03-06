package com.example.demo.src.user;


import com.example.demo.src.auth.model.User;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.createUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    public GetUserRes getUsersByEmail(String email){
        String getUsersByEmailQuery = "select userIdx, email, univ, department, belong  from User where email=?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.queryForObject(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("email"),
                        rs.getString("univ"),
                        rs.getString("department"),
                        rs.getString("belong"),
                getUsersByEmailParams));
    }

/*
    public GetUserRes getUsersByIdx(int userIdx){
        String getUsersByIdxQuery = "select userIdx, email, univ, department, belong  from User where userIdx=?";
        int getUsersByIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUsersByIdxQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("email"),
                        rs.getString("univ"),
                        rs.getString("department"),
                        rs.getString("belong"),
                getUsersByIdxParams));
    }

 */

    public GetUserRes getUsersByIdx(int userIdx){
        String getUsersByIdxQuery = "select * from User where userIdx=?";
        int getUsersByIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUsersByIdxQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("univ"),
                        rs.getString("department"),
                        rs.getString("belong")),
                getUsersByIdxParams);
    }





    //????????????
    public int createUser(createUser postUserReq){
        System.out.println("dao :" + postUserReq.toString());
        String createUserQuery = "insert into User (userIdx, name, phone, email, password, univ, department, belong, certifyImg, certified ) VALUES (?,?,?,?,?,?,?,?,?,?);";
        Object[] createUserParams = new Object[]{postUserReq.getUserIdx(), postUserReq.getName(), postUserReq.getPhone(),
                postUserReq.getEmail(), postUserReq.getPassword(), postUserReq.getUniv(), postUserReq.getDepartment(),
                postUserReq.getBelong(), postUserReq.getCertifyImg(), postUserReq.getCertified()};

        this.jdbcTemplate.update(createUserQuery, createUserParams);  // User table??? ???????????? ??????

        //String lastInserIdQuery = "select last_insert_id();"; // ?????? ????????? ?????? ????????? ?????????
        //int imsi = this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
        //System.out.println("imsi : "+imsi);
        String checkUserIdxQuery = "select exists(select userIdx from User where userIdx = ?)";
        int checkUserIdxParams = postUserReq.getUserIdx();
        int check = this.jdbcTemplate.queryForObject(checkUserIdxQuery,int.class,checkUserIdxParams);
        System.out.println("dao db??? ???????????????. = 1 ????????? 0 check : " + check);
        if (check == 1){ //db ??????. ????????? ????????????.
            return postUserReq.getUserIdx();
        }else{ //db??? ?????? ??????.
            return 0;
        }

    }


    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int checkUserExist(int userIdx){
        String checkUserExistQuery = "select exists(select userIdx from User where userIdx = ?)";
        int checkUserExistParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);

    }





    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set nickName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }




}
