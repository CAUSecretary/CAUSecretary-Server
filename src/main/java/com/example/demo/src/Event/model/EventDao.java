package com.example.demo.src.Event.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class EventDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkEventExist(int eventIdx, int userIdx){
        String checkEventExistQuery = "select exists(select * from Event where eventIdx = ? and userIdx = ? )";
        int checkEventIdxParams = eventIdx;
        int checkUserIdxParams = userIdx;
        int check = this.jdbcTemplate.queryForObject(checkEventExistQuery,
                int.class,checkEventIdxParams,checkUserIdxParams);
        System.out.println("check :"+check);

        return check;
    }

    public int checkEventExist(int eventIdx){
        String checkEventExistQuery = "select exists(select * from Event where eventIdx = ? )";
        int checkEventIdxParams = eventIdx;
        int check = this.jdbcTemplate.queryForObject(checkEventExistQuery,
                int.class,checkEventIdxParams);
        System.out.println("check :"+check);
        return check;
    }
    public int checkPhotoExist(int eventIdx){
        String checkEventExistQuery = "select exists(select * from Photo where eventIdx = ? )";
        int checkEventIdxParams = eventIdx;
        int check = this.jdbcTemplate.queryForObject(checkEventExistQuery,
                int.class,checkEventIdxParams);
        System.out.println("check :"+check);
        return check;
    }


}
