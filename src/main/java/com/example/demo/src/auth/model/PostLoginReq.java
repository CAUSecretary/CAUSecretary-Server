package com.example.demo.src.auth.model;




import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostLoginReq {
    private String email;
    private String password;
}